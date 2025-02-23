package org.GUI.windowManager;

import org.GUI.manual.Manual;
import org.GUI.match.MatchFrame;
import org.GUI.match.saveSlots.SaveSlotsDialog;
import org.GUI.menu.MenuFrame;
import org.GUI.menu.loadSlots.LoadSlotsDialog;
import org.controller.MatchController;
import org.controller.MenuController;
import org.model.board.BalancedRockSpawner;
import org.model.board.MeanRockSpawner;
import org.model.board.RandomRockSpawner;
import org.model.board.RockSpawner;
import org.model.match.Setup;
import org.saveload.FileSave;
import org.saveload.SlotSave;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import org.GUI.menu.SettingPanel.RockSpawnerEnums;

public class WindowManager
{
    private final MenuController menuController;
    private MatchController controller;
    private MenuFrame menuFrame = null;
    private MatchFrame matchFrame = null;
    private final SlotSave slotSave;


    final int windowWidth = Math.min(1200, Toolkit.getDefaultToolkit().getScreenSize().width);
    final int windowHeight = Math.min(800, Toolkit.getDefaultToolkit().getScreenSize().height);

    JDialog saveSlotsDialog;
    JDialog loadSlotsDialog;
    JDialog manualDialog;
    JDialog settings;


    public WindowManager()
    {
        this.slotSave = new SlotSave();
        menuController = new MenuController();
    }

    private void createMenu()
    {
        MenuFrame menuFrame = new MenuFrame(this);
        this.menuFrame = menuFrame;
        menuFrame.setSize(1200,650);
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuFrame.setupMenu();
        showMenu();
    }
    public void createGame()
    {
        int progress = this.controller.getMatch().getBoard().maxProgress();
        int lane = this.controller.getMatch().getBoard().maxLane();

        this.matchFrame = new MatchFrame(this, this.controller, progress, lane);
        this.matchFrame.setSize(this.windowWidth, this.windowHeight);
        showMatch();
    }

    public void showMenu()
    {
        if (doesMatchExist())
        {
            matchFrame.setVisible(false);
        }

        if (doesMenuExist())
        {
            menuFrame.setupMenu();
            menuFrame.setVisible(true);
        }
        else
        {
            createMenu();
            menuFrame.setVisible(true);
        }
    }

    public void showSaveSlots()
    {
        if (saveSlotsDialog == null)
        {
            saveSlotsDialog = new SaveSlotsDialog(this);
            saveSlotsDialog.setVisible(true);
        }
    }

    public void showLoadSlots()
    {
        if (loadSlotsDialog == null)
        {
        loadSlotsDialog = new LoadSlotsDialog(this);
        loadSlotsDialog.setVisible(true);
        }
    }

    public void showManuel()
    {
        if (manualDialog == null)
        {
            manualDialog = new Manual(this);
            manualDialog.setVisible(true);
        }
        else
        {
         manualDialog.setVisible(true);
        }
    }
    public void showMatch()
    {
        if (doesMatchExist())
        {
            matchFrame.setVisible(true);
        }
        else
        {
            createGame();
            matchFrame.setVisible(true);
        }

        if (doesMenuExist())
        {
            menuFrame.setVisible(false);
        }
    }

    public void showGameSettings()
    {
        menuFrame.setupSettings();
    }

    public void saveGame(String slotDigit)
    {
        String filename = "saveSlot" + slotDigit + ".json";
        if(!controller.saveMatch(slotSave, filename))
        {
            JOptionPane.showMessageDialog(null,
                    "Abgeschlossene Spiele können nicht gespeichert werden.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Speichern erfolgreich!",
                    "Erfolg",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void saveGameFile(String filepath)
    {
        FileSave fileSave = new FileSave();
        if(!controller.saveMatch(fileSave, filepath))
        {
            JOptionPane.showMessageDialog(null,
                    "Abgeschlossene Spiele können nicht gespeichert werden.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                "Speichern erfolgreich!",
                "Erfolg",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadGame(String slotDigit)
    {
        String filename = "saveSlot" + slotDigit + ".json";
        try
        {
            this.controller = menuController.loadMatch(slotSave, filename);
        }
        catch (RuntimeException e)
        {
            JOptionPane.showMessageDialog(null,
                    "Speicherstand ist beschädigt.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        createGame();
    }

    public void loadGameFile(String filepath)
    {
        FileSave fileSave = new FileSave();
        try
        {
            this.controller = menuController.loadMatch(fileSave, filepath);
        }
        catch (RuntimeException e)
        {
            JOptionPane.showMessageDialog(null, "Speicherstand ist beschädigt.");
            return;
        }
        createGame();
    }

    public boolean deleteGameFile(String slotDigit)
    {
        String filename = "saveSlot" + slotDigit + ".json";
        File file = new File("src/savefiles/"+filename);
        return file.delete();
    }

    private boolean doesMenuExist ()
    {
        return menuFrame != null;
    }

    public boolean doesMatchExist ()
    {
        return matchFrame != null;
    }

    public void lockMenu()
    {
       menuFrame.setRunningGame();
    }

    public void gameSettings(Integer playerAmount, Integer maxLane, Integer maxProgress, RockSpawnerEnums rockSpawnerEnum)
    {
        this.createController(playerAmount, maxLane, maxProgress, rockSpawnerEnum);
    }

    public void closeSaveSlotsDialog()
    {
        if(saveSlotsDialog != null)
        {
            saveSlotsDialog.dispose();
            saveSlotsDialog = null;
        }
    }

    public void closeLoadSlotsDialog()
    {
        if(loadSlotsDialog != null)
        {
            loadSlotsDialog.dispose();
            loadSlotsDialog = null;
        }
    }

    public void closeManualDialog()
    {
        if (manualDialog != null)
        {
            manualDialog.dispose();
            manualDialog = null;
        }
    }

    public void closeGameSettings()
    {
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
    }

    public void closeMatch()
    {
        if (matchFrame != null)
        {
            matchFrame.dispose();
            matchFrame = null;
        }
    }

    private RockSpawner getRockSpawner(RockSpawnerEnums rockSpawnerEnum)
    {
        RockSpawner rockSpawner;
        if (rockSpawnerEnum == RockSpawnerEnums.DEFAULT)
        {
            rockSpawner = new RandomRockSpawner();
        }
        else if (rockSpawnerEnum == RockSpawnerEnums.BALANCED)
        {
            rockSpawner = new BalancedRockSpawner();
        }
        else if (rockSpawnerEnum == RockSpawnerEnums.MEAN)
        {
            rockSpawner = new MeanRockSpawner();
        }
        else
        {
            rockSpawner = new RandomRockSpawner();
        }
        return rockSpawner;
    }

    private void createController(Integer playerAmount, Integer maxLane, Integer maxProgress, RockSpawnerEnums rockSpawnerEnum)
    {
        RockSpawner rockSpawner = getRockSpawner(rockSpawnerEnum);
        Setup setup = new Setup();
        this.controller = setup.withBoard(maxLane, maxProgress)
                .withPlayers(playerAmount).withRockSpawner(rockSpawner)
                .build();
    }
}
