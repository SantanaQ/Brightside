package org.GUI.menu.loadSlots;

import org.GUI.windowManager.WindowManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


public class LoadSlotsDialog extends JDialog
{
    int numOfSlots = 5;
    WindowManager windowManager;
    JFileChooser fileChooser;


    public LoadSlotsDialog(WindowManager windowManager)
    {
        this.windowManager = windowManager;
        this.setAlwaysOnTop(true);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                windowManager.closeLoadSlotsDialog();
            }
        });

        fileChooser = new JFileChooser();
        this.setTitle("Speicherstände");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 400);
        JPanel slotsPanel = new JPanel();
        slotsPanel.setLayout(new BoxLayout(slotsPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < this.numOfSlots; i++)
        {
            int slotNum = (i+1);
            JPanel slotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            slotsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // nach von Oben

            JButton slotButton = new JButton("Slot " + slotNum);

            slotButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            slotButton.addActionListener(e -> loadSlot(e.getSource()));
            slotPanel.add(slotButton);
            if (isFileMissing(""+slotNum))
            {
                slotButton.setEnabled(false);
            }
            else
            {
                JButton deleteButton = new JButton("Delete");
                deleteButton.addActionListener(e -> deleteFile(e.getSource()));
                slotPanel.add(deleteButton);
            }
            slotsPanel.add(slotPanel);

            slotsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Abstand zwischen den Slots
        }

        this.add(slotsPanel);
        JButton loadAutoSave = new JButton("Auto Laden");
        loadAutoSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadAutoSave.addActionListener(_ -> autoLoad());
        slotsPanel.add(loadAutoSave);

        JButton fileChooserButton = new JButton("Speicherstand auswählen");
        fileChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileChooserButton.addActionListener(e -> fileChooserFunction(e.getSource()));

        slotsPanel.add(fileChooserButton);
        this.setVisible(true);
    }

    public void loadSlot(Object buttonObj)
    {
        if (buttonObj == null)
        {
            return;
        }
        JButton button = (JButton) buttonObj;

        String digit = determineLoadSlotDigit(button);
        windowManager.loadGame(digit);
        windowManager.closeLoadSlotsDialog();
    }

    private  void deleteFile(Object buttonObj)
    {
        if (buttonObj == null)
        {
            return;
        }
        JButton button = (JButton) buttonObj;
        Container panel = button.getParent();
        if (panel instanceof JPanel)
        {
            JButton loadButton = (JButton) panel.getComponent(0);
            String digit = determineLoadSlotDigit(loadButton);
            windowManager.deleteGameFile(digit);
        }
        windowManager.closeLoadSlotsDialog();
    }

    private String determineLoadSlotDigit(JButton button){
        return ("" + button.getText().charAt(5));
    }

    private void fileChooserFunction(Object ignoredSrc)
    {
        int returnVal = fileChooser.showOpenDialog(null);
        //fileChooser.setFileFilter(new FileNameExtensionFilter("JSON-Dateien (*.json)", "json"));
        fileChooser.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                // Accept directories and JSON files
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".json");
            }

            @Override
            public String getDescription() {
                return "JSON Files (*.json)";
            }
        });
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File filePath = fileChooser.getSelectedFile();
            if (filePath != null)
            {
                windowManager.loadGameFile(filePath.getAbsolutePath());
                windowManager.closeLoadSlotsDialog();
            }
        }
    }

    private void autoLoad()
    {
        windowManager.loadGameFile("src/savefiles/autosave.json");
        windowManager.closeLoadSlotsDialog();

    }

    private boolean doesFileExist(String slotDigit)
    {
        String filename = "saveSlot" + slotDigit + ".json";
        File file = new File("src/savefiles/"+filename);
        return file.exists();
    }

    private boolean isFileMissing(String slotDigit){
        return !doesFileExist(slotDigit);
    }
}