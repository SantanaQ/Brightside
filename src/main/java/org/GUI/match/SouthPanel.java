package org.GUI.match;


import org.GUI.Gallery;
import org.controller.MatchController;
import org.model.match.Loadout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SouthPanel extends JPanel
{
    private final CardsPanel cardsPanel;
    private final Gallery gallery = Gallery.galleryInstance();
    private final JLabel drawPileNumber = new JLabel();
    private final JLabel discardPileNumber = new JLabel();
    private final BufferedImage cardBack = Gallery.loadPicture("/cards/CardBack.png");
    private final MatchController controller;

    public SouthPanel(MatchPanel matchPanel, MatchController controller)
    {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.cardsPanel = new CardsPanel(matchPanel);

        setupPiles();

        JPanel right = new JPanel();
        right.setBackground(Color.WHITE);
        this.add(cardsPanel, BorderLayout.CENTER);
        this.add(configPile("Deck", drawPileNumber), BorderLayout.EAST);
        this.add(configPile("Ablagestapel", discardPileNumber), BorderLayout.WEST);
        updatePiles();
    }

    private JPanel configPile(String text, JLabel label)
    {
        JPanel pile = pilePanel(text);
        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        JPanel drawPile = new JPanel(){
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(cardBack,0, 0,
                        gallery.scaleWidth(75),
                        gallery.scaleHeight(150),
                        null);
            }
        };
        drawPile.setPreferredSize(new Dimension(
                gallery.scaleWidth(75),
                gallery.scaleHeight(150)
        ));

        drawPile.setBorder(new EmptyBorder(
                gallery.scaleHeight(0),
                gallery.scaleWidth(100),
                gallery.scaleHeight(0),
                gallery.scaleWidth(100)
        ));
        drawPile.setBackground(Color.GRAY);
        drawPile.add(label);
        innerPanel.add(drawPile);
        pile.add(innerPanel, BorderLayout.CENTER);


        return pile;
    }

    private JPanel pilePanel(String text)
    {
        JPanel pile = new JPanel();
        pile.setLayout(new BorderLayout());
        pile.setOpaque(false);
        pile.setPreferredSize(new Dimension(
                gallery.scaleWidth(400),
                gallery.scaleHeight(250)
        ));
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(gallery.getFont("scoreboardHeaderFont"));
        textLabel.setForeground(Color.BLACK);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(textLabel);
        pile.add(textPanel, BorderLayout.NORTH);
        return pile;
    }

    private void setupPiles()
    {
        int border = gallery.scaleWidth(5);
        drawPileNumber.setBorder(new EmptyBorder(border,border,border,border));
        discardPileNumber.setBorder(new EmptyBorder(border,border,border,border));
        drawPileNumber.setForeground(Color.WHITE);
        discardPileNumber.setForeground(Color.WHITE);
        drawPileNumber.setFont(gallery.getFont("manualFont"));
        discardPileNumber.setFont(gallery.getFont("manualFont"));
    }

    private void updatePiles()
    {
        Loadout loadout = controller.getCurrentPlayer().getLoadout();
        int drawPileSize = loadout.getDrawPile().size();
        int discardPileSize = loadout.getDiscardPile().size();

        drawPileNumber.setText(""+drawPileSize);
        discardPileNumber.setText(""+discardPileSize);
        this.revalidate();
        this.repaint();
    }

    public void updateSouthPanel()
    {
        this.cardsPanel.updateCards();
        updatePiles();
    }
}


