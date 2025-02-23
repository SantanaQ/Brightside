package org.saveload;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class SaveFileValidator {

    public boolean validate(File saveData) {
        if (!isValidFile(saveData) || isEmpty(saveData))
        {
            return false;
        }

        JsonObject jsonObj = parseJsonFile(saveData);
        if (jsonObj == null)
        {
            System.out.println("File is not valid JSON format");
            return false;
        }

        if (!isValidMatch(jsonObj))
        {
            System.out.println("Invalid match data");
            return false;
        }

        return true;
    }

    private boolean isValidFile(File file) {
        if (file == null || !file.exists() || !file.canRead() || !file.getName().endsWith(".json")) {
            System.out.println("Invalid file: null, does not exist, unreadable, or not json format.");
            return false;
        }
        return true;
    }

    private boolean isEmpty(File file)
    {
        if(file.length() == 0)
        {
            System.out.println("File content is empty1");
            return true;
        }
        try
        {
            String content = new String(Files.readAllBytes(file.toPath())).trim(); // Inhalt lesen und Leerzeichen entfernen
            if (content.equals("\"\"") || content.equals("{}") || content.equals("[]"))
            {
                System.out.println("File content is empty2");
                return true;
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return false;
    }

    private JsonObject parseJsonFile(File file) {
        try (JsonReader reader = new JsonReader(new FileReader(file)))
        {
            JsonElement element = JsonParser.parseReader(reader);
            return element.isJsonObject() ? element.getAsJsonObject() : null;
        } catch (IOException | JsonSyntaxException e)
        {
            return null;
        }
    }

    private boolean isValidMatch(JsonObject jsonObj)
    {
        JsonElement hash = jsonObj.remove("hash");
        if (hash == null || !hash.isJsonPrimitive())
        {
            return false;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileJson = gson.toJson(jsonObj);

        SHA256 sha256 = new SHA256();
        return sha256.verifyHash(fileJson, hash.getAsString());
    }





}
