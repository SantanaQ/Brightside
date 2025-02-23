package org.saveload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.controller.MatchController;
import org.model.match.Match;

import java.io.*;

/**
 * <p>SlotSave wird zum Speichern und Laden von Spielständen auf den gegebenen Speicherslots verwendet.</p>
 *
 * <p>Diese Klasse ist Teil des <strong>Dependency Injection Entwurfsmusters</strong>: Agiert als Injektor für {@link Persistence} Interface Parameter
 * in Speicher-, und Lade-Methoden vom {@link MatchController}.</p>
 */
public class SlotSave implements Persistence {

    public static final String SAVE_SLOT_AUTO = "autosave.json";
    public static final String SAVE_SLOT_1 = "saveSlot1.json";
    public static final String SAVE_SLOT_2 = "saveSlot2.json";
    public static final String SAVE_SLOT_3 = "saveSlot3.json";
    public static final String SAVE_SLOT_4 = "saveSlot4.json";
    public static final String SAVE_SLOT_5 = "saveSlot5.json";
    public static final String SAVE_SLOT_DEV = "saveSlotDev.json";
    public static final String SAVE_FOLDER = "src/savefiles";
    private final Gson gson;

    public SlotSave()
    {
        gson = getGson();
    }

    public String serialize(Match match)
    {
        String json = gson.toJson(match, Match.class);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        SHA256 sha256 = new SHA256();
        jsonObject.addProperty("hash", sha256.encodeText(json));
        return gson.toJson(jsonObject);
    }

    public Match deserialize(JsonElement jsonElement)
    {
        return gson.fromJson(jsonElement, Match.class);
    }


    /**
     * Sichert JSON-Format eines Matches in einen Speicherslot.
     * @param slot ausgewählter Speicherslot
     * @param match Match Zustand, der gesichert werden soll.
     * @return true, wenn Match erfolgreich gespeichert wurde.
     */
    @Override
    public boolean save(String slot, Match match)
    {
        if(!match.isRunning())
        {
            System.err.println("Match is not running. Cant save finished match.");
            return false;
        }/*
        try
        {
            File file = new File(SAVE_FOLDER, slot);
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write(loadAutoSaveJson());
            }
            return true;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }*/
        String matchJson = serialize(match);
        try
        {
            File directory = new File(SlotSave.SAVE_FOLDER);
            if (!directory.exists() && !directory.mkdirs())
            {
                throw new FileNotFoundException("Could not create folder: " + SlotSave.SAVE_FOLDER);
            }
            File file = new File(SlotSave.SAVE_FOLDER, slot);
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write(loadAutoSaveJson());
                return true;
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lädt Match aus einem Speicherslot.
     * @param slot ausgewählter Speicherslot.
     * @return Match, konvertiert aus JSON Format.
     */
    @Override
    public Match load(String slot)
    {
        Match match;
        try
        {
            File file = new File(SAVE_FOLDER, slot);

            SaveFileValidator validator = new SaveFileValidator();
            if(!validator.validate(file)){
                throw new RuntimeException("Savefile might be corrupted");
            }
            try (JsonReader reader = new JsonReader(new FileReader(file)))
            {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                match = deserialize(jsonElement);
                MatchValidator matchValidator = new MatchValidator();
                if(!matchValidator.validate(match)){
                    throw new RuntimeException("Savefile might be corrupted");
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return match;
    }


    /**
     * Löscht den Inhalt eines Speicherslots.
     * @param slot ausgewählter Speicherslot.
     * @return true, wenn Speicherinhalt erfolgreich gelöscht wurde.
     */
    public boolean delete(String slot)
    {
        try
        {
            File directory = new File(SAVE_FOLDER);
            if (!directory.exists())
            {
                throw new FileNotFoundException();
            }
            File file = new File(SAVE_FOLDER, slot);
            return file.delete();
        } catch (IOException e)
        {
           return false;
        }
    }

    String loadAutoSaveJson()
    {
        try
        {
            File autoSave = new File(SlotSave.SAVE_FOLDER, SlotSave.SAVE_SLOT_AUTO);
            if(!autoSave.exists())
            {
                throw new FileNotFoundException("Auto save file not found.");
            }
            File directory = new File(SlotSave.SAVE_FOLDER);
            if (!directory.exists())
            {
                throw new IOException("directory does not exist.");
            }
            try (JsonReader reader = new JsonReader(new FileReader(autoSave)))
            {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                Match matchAuto = deserialize(jsonElement);
                return serialize(matchAuto);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
