package org.GUI.match;

import org.GUI.Gallery;
import org.GUI.windowManager.WindowManager;
import org.controller.MatchController;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MatchFrame extends JFrame
{
    public MatchFrame(WindowManager windowManager, MatchController controller, int boardWidth, int boardHeight)
    {
        this.setTitle("Brightside");
        this.setIconImage(Gallery.loadPicture("/ships/Schiff_pink.png"));
        MatchPanel matchPanel = new MatchPanel(controller, windowManager, boardWidth, boardHeight);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        MatchFrame.this,
                        "Möchten Sie das Fenster wirklich schließen?",
                        "Bestätigung",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        this.add(matchPanel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
    }
}
