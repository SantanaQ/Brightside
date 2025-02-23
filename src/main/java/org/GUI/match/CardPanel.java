package org.GUI.match;

import org.GUI.Gallery;
import org.model.cards.Card;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CardPanel extends JPanel implements MouseListener
{
    private static final int width = 162;
    private static final int height = 302;
    private final Card card;
    private final MatchPanel matchPanel;
    private final Gallery gallery = Gallery.galleryInstance();
    private final Timer hoverTimer;
    private boolean hovered = false;

    public CardPanel (MatchPanel matchPanel, Card card)
    {
        this.matchPanel = matchPanel;
        this.card = card;
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(gallery.scaleWidth(width/2), gallery.scaleHeight(height/2)));
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.addMouseListener(this);

        // Method Call Trigger Timer when hovering over card
        hoverTimer = new Timer(500, _ ->
        {
            if(hovered && card != null && cardIsStillInHand())
            {
                matchPanel.getController().setHoverTargets(card);
                matchPanel.boardPanel.updateBoard();
            }
        });
        hoverTimer.setRepeats(false);

    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(matchPanel.gallery.cards.get(card.getClass().toString()), 0,0, gallery.scaleWidth(150/2), gallery.scaleHeight(300/2), null);
    }

    @Override
    public void mouseClicked(MouseEvent e){}

    @Override
    public void mousePressed(MouseEvent e)
    {
        matchPanel.chooseCard(card);
    }

    @Override
    public void mouseReleased(MouseEvent e){}

    @Override
    public void mouseEntered(MouseEvent e)
    {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hovered = true;
        hoverTimer.restart();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        hovered = false;
        hoverTimer.stop();
        if(!matchPanel.getHoverTargets().isEmpty())
        {
            matchPanel.getController().setHoverTargets(null);
            matchPanel.boardPanel.updateBoard();
        }

    }

    private boolean cardIsStillInHand()
    {
        return matchPanel.getController().getCurrentHand().contains(card);
    }
}
