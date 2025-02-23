package org.GUI.menu;

import org.GUI.Gallery;
import org.GUI.windowManager.WindowManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SettingPanel extends JPanel
{
    //Enum Klasse um Konstante auf String-Text abzubilden.
    public enum RockSpawnerEnums
    {
        DEFAULT("Standard"),
        BALANCED("Balanced"),
        MEAN("Mean");

        private final String text;
        RockSpawnerEnums(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }
    }

    private static final Gallery gallery = Gallery.galleryInstance();
    private static final Font font = gallery.getFont("menuFont");
    private final WindowManager windowManager;
    private Integer playerAmount = 2;
    private Integer maxLane = 5;
    private Integer maxProgress = 8;
    private RockSpawnerEnums rockSpawnerEnum;

    public SettingPanel(WindowManager windowManager)
    {
        this.windowManager = windowManager;
        setup();
    }

    private void setup()
    {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        setupButtonPanel();
        setupInputPanel();
    }

    private void setupButtonPanel()
    {
        JPanel buttonPanel = new JPanel();
        JButton startGame =  MenuFrame.configureButton("Spiel starten");
        startGame.addActionListener(_ -> startGame());

        JButton goBack = MenuFrame.configureButton("Zurück");
        goBack.addActionListener(_ -> goBack());

        buttonPanel.add(startGame);
        buttonPanel.add(goBack);
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(200,200,200,150));

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void mapRadioButtonSelected(ActionEvent e)
    {
        Object source = e.getSource();

        if (source instanceof JRadioButton radioButton)
        {
            String radioText = radioButton.getText();
            String[] splitedText = radioText.split("X");

            this.maxLane = Integer.parseInt(splitedText[0]);
            this.maxProgress = Integer.parseInt(splitedText[1]);
        }
    }

    private void setupInputPanel()
    {
        JPanel inputPanel = new JPanel(new GridLayout(3,1));
        inputPanel.setBorder(new EmptyBorder(
                gallery.scaleHeight(500),
                gallery.scaleWidth(0),
                gallery.scaleHeight(100),
                gallery.scaleWidth(0)));

        JPanel playerCountPanel = new JPanel();
        playerCountPanel.setOpaque(true);
        JLabel playerCountLabel = MenuFrame.configureLabel("Spieleranzahl");
        playerCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel playerRadioPanel = createPlayerRadioPanel();
        playerCountPanel.add(playerCountLabel);
        playerCountPanel.add(playerRadioPanel);

        JPanel mapSizePanel = new JPanel();
        mapSizePanel.setOpaque(true);
        JLabel mapSizeLabel = MenuFrame.configureLabel("Kartengröße");
        JPanel mapRadioPanel =  createMapRadioPanel();
        mapSizePanel.add(mapSizeLabel);
        mapSizePanel.add(mapRadioPanel);

        JPanel rockSpawnPanel = new JPanel();
        rockSpawnPanel.setOpaque(true);
        JLabel rockSpawnLabel = MenuFrame.configureLabel("Rock Spawner");
        rockSpawnLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel rockRadioPanel = createRockRadioPanel();
        rockSpawnPanel.add(rockSpawnLabel);
        rockSpawnPanel.add(rockRadioPanel);

        inputPanel.add(playerCountPanel);
        inputPanel.add(mapSizePanel);
        inputPanel.add(rockSpawnPanel);

        inputPanel.setOpaque(false);
        inputPanel.setBackground(new Color(200,200,200,100));
        inputPanel.setPreferredSize(new Dimension(gallery.scaleWidth(600), gallery.scaleHeight(900)));

        this.add(inputPanel, BorderLayout.CENTER);
    }

    private void playerRadioButtonSelected(ActionEvent e)
    {
        Object source = e.getSource();

        if (source instanceof JRadioButton radioButton)
        {
            this.playerAmount = Integer.parseInt(radioButton.getText());
        }
    }

    private void rockRadioButtonSelected(ActionEvent e)
    {
        Object source = e.getSource();

        if (source instanceof JRadioButton radioButton)
        {
            for (RockSpawnerEnums rockSpawner : RockSpawnerEnums.values())
            {
                if(rockSpawner.getText().equals(radioButton.getText()))
                {
                    this.rockSpawnerEnum = rockSpawner;
                    return;
                }
            }
        }
    }

    private JPanel createRockRadioPanel()
    {
        java.util.List<String> radioTexts = new ArrayList<>();
        radioTexts.add(RockSpawnerEnums.DEFAULT.getText());
        radioTexts.add(RockSpawnerEnums.BALANCED.getText());
        radioTexts.add(RockSpawnerEnums.MEAN.getText());

        return createRadioPanel(radioTexts, this::rockRadioButtonSelected);
    }

    private JPanel createPlayerRadioPanel()
    {
        java.util.List<String> radioTexts = new ArrayList<>();
        radioTexts.add("2");
        radioTexts.add("3");
        radioTexts.add("4");
        return createRadioPanel(radioTexts, this::playerRadioButtonSelected);
    }

    private JPanel createMapRadioPanel()
    {
        java.util.List<String> radioTexts = new ArrayList<>();
        radioTexts.add("5X8");
        radioTexts.add("6X9");
        radioTexts.add("7X20");
        return createRadioPanel(radioTexts, this::mapRadioButtonSelected);
    }

    private JPanel createRadioPanel(java.util.List<String> radioTexts, ActionListener eventFunction)
    {
        JPanel mapRadioPanel = new JPanel(new GridBagLayout());
        ButtonGroup buttonGroup = new ButtonGroup();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,gallery.scaleWidth(20),0,gallery.scaleWidth(20));

        for (int textNum = 0; textNum < radioTexts.size(); textNum++)
        {
            JRadioButton radioButton  = new JRadioButton(radioTexts.get(textNum));
            if (textNum == 0)
            {
                radioButton.setSelected(true);
            }
            radioButton.setOpaque(false);
            radioButton.setForeground(Color.BLACK);
            radioButton.addActionListener(eventFunction);
            radioButton.setFont(font);
            buttonGroup.add(radioButton);
            mapRadioPanel.add(radioButton,gbc);
        }
        mapRadioPanel.setOpaque(false);
        return mapRadioPanel;
    }

    public void startGame()
    {
        this.windowManager.gameSettings(this.playerAmount, this.maxLane, this.maxProgress, this.rockSpawnerEnum);
        this.windowManager.showMatch();
        this.windowManager.closeGameSettings();
    }

    public void goBack()
    {
        this.windowManager.showMenu();
    }

}
