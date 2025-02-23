package org.GUI.match;

import org.model.cards.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CardsPanel extends JPanel
{
    private final MatchPanel matchPanel;

    public CardsPanel(MatchPanel matchPanel)
    {
        this.matchPanel = matchPanel;
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setOpaque(false);
        updateCards();
    }

    public void updateCards()
    {
        List<Card> playerHand = matchPanel.getCurrentHand();

        for(Component c :this.getComponents())
        {
            this.remove(c);
        }

        for(Card c: playerHand)
        {
            this.add(new CardPanel(this.matchPanel, c));
        }

        this.repaint();
    }
}
