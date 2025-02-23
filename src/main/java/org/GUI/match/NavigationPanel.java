package org.GUI.match;

import org.GUI.Gallery;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationPanel extends JPanel
{
    public NavigationPanel(MatchPanel matchPanel)
    {

        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.setOpaque(false);
        JButton back = createButton("Zurück ins Hauptmenü", new Color(255, 255, 255), new Color(255, 255, 255));
        back.setIcon(new ImageIcon(Gallery.loadPicture("/icons/Conf.png")));
        back.addActionListener(_ -> {
            matchPanel.windowManager.closeSaveSlotsDialog();
            matchPanel.windowManager.closeManualDialog();
            matchPanel.windowManager.lockMenu();
            matchPanel.windowManager.showMenu();
        });

        JButton save = createButton("Spiel speichern", new Color(255, 255, 255),
                new Color(255, 255, 255));
        save.setIcon(new ImageIcon(Gallery.loadPicture("/icons/Safe.png")));
        save.addActionListener(_ -> matchPanel.windowManager.showSaveSlots());
        JButton rules = createButton("Spielregeln", new Color(255, 255, 255),
                new Color(255, 255, 255));
        rules.setIcon(new ImageIcon(Gallery.loadPicture("/icons/Manual.png")));
        rules.addActionListener(_ -> matchPanel.windowManager.showManuel());
        JButton reverse = createButton("Zug zurücksetzen", new Color(255, 255, 255),
                new Color(255, 255, 255));
        reverse.setIcon(new ImageIcon(Gallery.loadPicture("/icons/Back.png")));
        reverse.addActionListener(_ -> matchPanel.resetTurn());
        JButton endTurn = createButton("Zug beenden", new Color(255, 255, 255),
                new Color(255, 255, 255));
        endTurn.setIcon(new ImageIcon(Gallery.loadPicture("/icons/Next.png")));
        endTurn.addActionListener(_ -> matchPanel.endTurn());
        this.add(back);
        this.add(save);
        this.add(rules);
        this.add(reverse);
        this.add(endTurn);
    }

    JButton createButton(String buttonTxt, Color backgroundColor, Color borderColor)
    {
        JButton button = new JButton(buttonTxt);
        button.setBackground(backgroundColor);
        button.setBorder(new LineBorder(borderColor));
        button.setPreferredSize(new Dimension(180,40));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                button.setBorder(new LineBorder(new Color(255, 255, 255)));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setBackground(new Color(0, 76, 108));
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e)
            {
                button.setBorder(new LineBorder(borderColor));
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                button.setBackground(backgroundColor);
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }
}
