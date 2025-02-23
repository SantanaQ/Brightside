package org.GUI.menu;

import org.GUI.windowManager.WindowManager;
import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel
{
    private final WindowManager windowManager;
    private final JPanel buttonPanel = new JPanel();
    private JButton newGame = null;

    public MenuPanel(WindowManager windowManager)
    {
        this.windowManager = windowManager;
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setupButtonPanel();
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void startGame()
    {
        windowManager.closeLoadSlotsDialog();
        if (windowManager.doesMatchExist()){
            windowManager.showMatch();
        }
        else{
            windowManager.showGameSettings();
        }
        update();
    }
    private void startNewGame()
    {
        windowManager.closeLoadSlotsDialog();
        windowManager.closeMatch();
        windowManager.showGameSettings();
    }

    private void loadGame()
    {
        windowManager.closeGameSettings();
        windowManager.showLoadSlots();
    }

    public void setRunningGame()
    {
        for (Component component : buttonPanel.getComponents())
        {
            if (component instanceof JButton button)
            {
                if (button.getText().equals("Start") || button.getText().equals("Weiter"))
                {
                    button.setText("Weiter");
                }
                button.setEnabled(true);
            }
        }
        if(newGame == null)
        {
            newGame = MenuFrame.configureButton("Neues Spiel");
            newGame.addActionListener(_ -> startNewGame());
            buttonPanel.add(newGame, BorderLayout.AFTER_LAST_LINE);
        }
    }

    private void setupButtonPanel()
    {
        JButton start =  MenuFrame.configureButton("Start");
        start.addActionListener(_ -> startGame());

        JButton load = MenuFrame.configureButton("Laden");
        load.addActionListener(_ -> loadGame());

        JButton close = MenuFrame.configureButton("Beenden");
        close.addActionListener(_ -> System.exit(0));

        buttonPanel.add(start);
        buttonPanel.add(load);
        buttonPanel.add(close);
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(200,200,200,150));

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void update()
    {
        this.revalidate();
        this.repaint();
    }
}
