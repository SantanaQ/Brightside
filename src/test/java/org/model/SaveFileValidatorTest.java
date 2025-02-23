package org.model;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.model.match.Match;
import org.saveload.SaveFileValidator;
import org.model.match.Setup;
import org.saveload.SlotSave;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class SaveFileValidatorTest {

    SaveFileValidator saveFileValidator;
    File file;

    @BeforeEach
    void SetUp() throws IOException {
        this.saveFileValidator = new SaveFileValidator();
        this.file = File.createTempFile("test", ".json");
    }

    @AfterEach
    void TearDown()
    {
        this.saveFileValidator = null;
        SlotSave slotSave = new SlotSave();
        slotSave.delete(SlotSave.SAVE_SLOT_DEV);
    }

    @Test
    @DisplayName("Loading non existing file should not work")
    void loadingfilethatisnullshouldnotwork()
    {
        assertFalse(saveFileValidator.validate(null));
    }

    @Test
    @DisplayName("loading different file format should not work")
    void loadingFileWithTXTFormatShouldNotWork()
    {
        File file = new File("./test.txt");
        assertFalse(saveFileValidator.validate(file));
    }

    @Test
    @DisplayName("Loading empty file should not work")
    void loadingEmptyFileShouldNotWork()
    {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("");
            writer.flush();
            assertFalse(saveFileValidator.validate(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Loading false file format should not work")
    void falseFileFormatShouldNotBeValid()
    {
        try
        {
            File txt = File.createTempFile("test", "txt");
            assertFalse(saveFileValidator.validate(txt));

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Loading a file without context should not work")
    void loadingFileWithoutContextShouldNotWork()
    {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("{}");
            writer.flush();
            assertFalse(saveFileValidator.validate(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    @DisplayName("Loading match file with incomplete data should not work")
    void loadingFileWithIncompleteDataShouldNotWork()
    {
        Match match = new Setup().build().getMatch();
        SlotSave slotSave = new SlotSave();
        String json = slotSave.serialize(match);
        JsonElement element = JsonParser.parseString(json);
        JsonObject jsonObj = element.getAsJsonObject();
        Set<String> keys = jsonObj.keySet();
        String key = keys.iterator().next();
        jsonObj.remove(key);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonWithoutBoard = gson.toJson(jsonObj);
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(jsonWithoutBoard);
            writer.flush();
            assertFalse(saveFileValidator.validate(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Loading match with manipulated data should not work")
    void loadingFileWithManipulatedDataShouldNotWork()
    {
        Match match = new Setup().build().getMatch();
        SlotSave slotSave = new SlotSave();
        String json = slotSave.serialize(match);
        JsonElement element = JsonParser.parseString(json);
        JsonObject jsonObj = element.getAsJsonObject();
        Set<String> keys = jsonObj.keySet();
        String key = keys.iterator().next();
        jsonObj.addProperty(key, 200);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String changedJson = gson.toJson(jsonObj);
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(changedJson);
            writer.flush();
            assertFalse(saveFileValidator.validate(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
