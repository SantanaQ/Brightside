package org.saveload;

import org.model.match.Match;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class AutoSaving {

    public void autoSave(Match match)
    {
        SlotSave slotSave = new SlotSave();
        String matchJson = slotSave.serialize(match);
        try
        {
            File directory = new File(SlotSave.SAVE_FOLDER);
            if (!directory.exists() && !directory.mkdirs())
            {
                throw new FileNotFoundException("Could not create folder: " + SlotSave.SAVE_FOLDER);
            }
            File file = new File(SlotSave.SAVE_FOLDER, SlotSave.SAVE_SLOT_AUTO);
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write(matchJson);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
