package org.saveload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.model.match.Match;
import org.controller.MatchController;

import java.io.*;


/**
 * <p>FileSave wird zum Speichern und Laden von Matches aus externen Speicherdateien verwendet.</p>
 *
 * <p>Diese Klasse ist Teil des <strong>Dependency Injection Entwurfsmusters</strong>: Agiert als Injektor für {@link Persistence} Interface Parameter
 * in Speicher-, und Lade-Methoden vom {@link MatchController}.</p>
 */
public class FileSave  implements Persistence{

    private final Gson gson;

    public FileSave()
    {
        this.gson = getGson();
    }

    public JsonElement serialize(Match match)
    {
        return gson.toJsonTree(match);
    }

    public Match deserialize(JsonElement jsonElement)
    {
        return gson.fromJson(jsonElement, Match.class);
    }

    /**
     * Sichert JSON-Format eines Matches in eine externe Datei.
     * @param path Absoluter Pfad, an dem die Datei gespeichert werden soll.
     * @param match Match, das gesichert werden soll.
     * @return true, wenn das Match erfolgreich gespeichert wurde.
     */
    @Override
    public boolean save(String path, Match match)
    {
        if(!match.isRunning())
        {
            System.err.println("Match is not running. Cant save finished match.");
            return false;
        }
        if (!path.toLowerCase().endsWith(".json"))
        {
            path += ".json";
        }
        File file = new File(path);
        try (FileWriter writer = new FileWriter(file))
        {
            SlotSave slotSave = new SlotSave();
            writer.write(slotSave.loadAutoSaveJson());
            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lädt Match aus einer Speicherdatei.
     * @param path Absoluter Pfad der Datei.
     * @return Match, konvertiert aus JSON-Format.
     */
    @Override
    public Match load(String path)
    {
        Match match;
        try
        {
            File file = new File(path);
            SaveFileValidator validator = new SaveFileValidator();
            if(!validator.validate(file))
            {
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


}
