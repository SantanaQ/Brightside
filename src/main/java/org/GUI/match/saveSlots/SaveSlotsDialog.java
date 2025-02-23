package org.GUI.match.saveSlots;

import org.GUI.Gallery;
import org.GUI.windowManager.WindowManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class SaveSlotsDialog extends JDialog
{
    int numOfSlots = 5;
    WindowManager windowManager;
    JFileChooser fileChooser;

    public SaveSlotsDialog(WindowManager windowManager)
    {
        this.windowManager = windowManager;
        this.setAlwaysOnTop(true);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                windowManager.closeSaveSlotsDialog();
            }
        });

        fileChooser = new JFileChooser();

        BufferedImage image = Gallery.loadPicture("/icons/Safe.png");
        this.setIconImage(image);
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
            slotButton.addActionListener(e -> saveSlot(e.getSource()));
            slotPanel.add(slotButton);
            if (doesFileExist(""+slotNum))
            {
                slotButton.setText(slotButton.getText() + " (Überschreiben?)");
                JButton deleteButton = new JButton("Delete");
                deleteButton.addActionListener(e -> deleteFile(e.getSource()));
                slotPanel.add(deleteButton);
            }
            else
            {
                slotButton.setText(slotButton.getText() + " (Frei!)");
            }
            slotsPanel.add(slotPanel);
            slotsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Abstand zwischen den Slots
        }

        JButton fileChooserButton = new JButton("Speicherstand erstellen");
        fileChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileChooserButton.addActionListener(e -> fileChooserFunction(e.getSource()));

        slotsPanel.add(fileChooserButton);
        this.add(slotsPanel);
        this.setVisible(true);
    }

    public void saveSlot(Object buttonObj)
    {
        if (buttonObj == null)
        {
            return;
        }
        JButton button = (JButton) buttonObj;

        String digit = determineSaveSlotDigit(button);
        windowManager.saveGame(digit);
        windowManager.closeSaveSlotsDialog();
    }

    private boolean doesFileExist(String slotDigit)
    {
        String filename = "saveSlot" + slotDigit + ".json";
        File file = new File("src/savefiles/"+filename);
        return file.exists();
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
            JButton saveButton = (JButton) panel.getComponent(0);
            String digit = determineSaveSlotDigit(saveButton);
            if(!windowManager.deleteGameFile(digit))
            {
                System.err.println("File konnte nicht gelöscht werden");
            }
        }
        windowManager.closeSaveSlotsDialog();
    }

    private String determineSaveSlotDigit(JButton button)
    {
        return ("" + button.getText().charAt(5));
    }

    private void fileChooserFunction(Object ignoredSrc)
    {
        int returnVal = fileChooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            windowManager.saveGameFile(fileChooser.getSelectedFile().getAbsolutePath());
        }
        windowManager.closeSaveSlotsDialog();
    }
}