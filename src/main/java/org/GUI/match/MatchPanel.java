package org.GUI.match;

import org.GUI.Gallery;
import org.GUI.scoreboard.Scoreboard;
import org.GUI.windowManager.WindowManager;
import org.controller.MatchController;
import org.model.board.Coordinate;
import org.model.match.Player;
import org.model.cards.Card;
import org.model.entities.Entity;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;

public class MatchPanel extends JPanel
{
    private final MatchController controller;
    public WindowManager windowManager;
    public Gallery gallery;

    NavigationPanel navPanel;
    BoardPanel boardPanel;
    SouthPanel southPanel;
    Scoreboard scoreboard;
    JPanel centerContainer;

    Timer tileChangeTimer;

    public MatchPanel(MatchController controller ,WindowManager windowManager, int boardWidth, int boardHeight)
    {
        this.controller = controller;
        this.windowManager = windowManager;
        gallery = Gallery.galleryInstance();
        navPanel = new NavigationPanel(this);
        boardPanel = new BoardPanel(this, boardWidth, boardHeight);
        southPanel = new SouthPanel(this, controller);

        this.setLayout(new BorderLayout());
        scoreboard = new Scoreboard(controller);
        scoreboard.setPreferredSize(new Dimension(gallery.scaleWidth(300),gallery.scaleHeight(100)));

        centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS)); // Vertikale Anordnung
        centerContainer.setOpaque(false);

        /*
        Wrapper Panel hält BoardPanel, damit die Größe vom Board nicht über den gesamten
        Bildschirm gestretched wird.
         */
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(boardPanel);

        centerContainer.add(Box.createVerticalGlue()); // Oben Platzhalter für Zentrierung
        centerContainer.add(wrapperPanel);             // GridPanel einfügen
        centerContainer.add(Box.createVerticalGlue()); // Unten Platzhalter für Zentrierung

        this.add(navPanel, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(scoreboard, BorderLayout.EAST);

        tileChangeTimer = new Timer(500, (ActionEvent _) -> this.updateBoardAnimation());
        tileChangeTimer.start();

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(gallery.backgrounds.getFirst(), 0, 0, getWidth(), getHeight(), this);
    }

    public Player getCurrentPlayer(){
       return this.controller.getCurrentPlayer();
    }

    public List<Coordinate> getPossibleTargets(){
        return this.controller.getPossibleTargets();
    }

    public HashMap<Coordinate, Entity> getBoard(){
        return this.controller.getBoard();
    }

    public List<Card> getCurrentHand(){
        return this.controller.getCurrentHand();
    }

    public MatchController getController()
    {
        return this.controller;
    }

    public List<Coordinate> getHoverTargets()
    {
        return this.controller.getHoverTargets();
    }

    public void chooseCard(Card card)
    {
        this.controller.chooseCard(card);
        this.updateAll();
    }

    public void setTarget (Coordinate target)
    {
        controller.setTarget(target);
        this.updateAll();
    }

    public void resetTurn()
    {
        this.controller.resetTurn();
        this.updateAll();
    }

    public void endTurn()
    {
        this.controller.endTurn();
        this.updateAll();
    }

    public void updateBoardAnimation(){
        boardPanel.updateBoardAnimation();
    }

    public void updateAll()
    {
        controller.setHoverTargets(null);
        boardPanel.updateBoard();
        southPanel.updateSouthPanel();
        scoreboard.update();
        if(controller.isGameOver()){
            gameOver();
        }
    }

    public void gameOver()
    {
        centerContainer.removeAll();
        southPanel.removeAll();
        centerContainer.setLayout(new BorderLayout());
        JLabel winnerText = new JLabel("Game Over");
        winnerText.setHorizontalAlignment(SwingConstants.CENTER);
        winnerText.setVerticalAlignment(SwingConstants.CENTER);
        winnerText.setFont(gallery.getFont("gameOverFont"));
        winnerText.setForeground(Color.BLACK);
        centerContainer.add(winnerText, BorderLayout.CENTER);
    }
}
