package org.GUI.match;

import org.model.board.Coordinate;
import org.model.entities.Entity;
import org.model.entities.Mine;
import org.model.entities.Rock;
import org.model.entities.Ship;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class FieldPanel extends JPanel
{

    private BufferedImage imgEntity = null;
    private Entity entity = null;

    private final BoardPanel boardPanel;

    private final int lane;
    private final int progress;

    private BufferedImage markerImage;
    private boolean shipOfCurrentPlayer = false;
    private final MatchPanel matchPanel;


    public FieldPanel(MatchPanel matchPanel, int lane, int progress, BoardPanel board)
    {
        this.matchPanel = matchPanel;
        this.lane = lane;
        this.progress = progress;
        this.boardPanel = board;

        this.markerImage = matchPanel.gallery.markerAtlas.getFirst();

        updateField();
        this.setPreferredSize(new Dimension(board.TILE_SIZE, board.TILE_SIZE));
        this.setMinimumSize(new Dimension(board.TILE_SIZE, board.TILE_SIZE));
        this.setMaximumSize(new Dimension(board.TILE_SIZE, board.TILE_SIZE));
        this.setOpaque(false);
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                matchPanel.setTarget(new Coordinate(lane, progress));
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                markerImage = matchPanel.gallery.markerAtlas.get(1);
                repaint();
            }


            @Override
            public void mouseExited(MouseEvent e)
            {
                updateField();
                repaint();
            }
        });
    }


    public void updateField()
    {

        Coordinate c = new Coordinate(lane, progress);
        this.imgEntity = getEntityImg(matchPanel.getBoard().get(c));

        List<Coordinate> possibleTargets = matchPanel.getPossibleTargets();
        List<Coordinate> hoverTargets = matchPanel.getHoverTargets();

        if ((possibleTargets != null && possibleTargets.contains(c)) || (hoverTargets != null && hoverTargets.contains(c)))
        {
            markerImage = matchPanel.gallery.markerAtlas.get(2);
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        else{
            markerImage = matchPanel.gallery.markerAtlas.getFirst();
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private BufferedImage getEntityImg (Entity entity)
    {
        this.entity = entity;
        if (entity instanceof Ship ship)
        {
            int player = ship.playerID();
            shipOfCurrentPlayer = (player == matchPanel.getCurrentPlayer().getPlayerID());
            return matchPanel.gallery.shipAtlas.get(player-1);

        }
        else if (entity instanceof Rock)
        {
            return matchPanel.gallery.rockAtlas.get(boardPanel.currentRockTile);
        }
        else if (entity instanceof Mine)
        {
            return matchPanel.gallery.mineAtlas.getFirst();
        }
        return null;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.drawImage(matchPanel.gallery.waveAtlas.get(boardPanel.currentWaveTile),
                0,0,boardPanel.TILE_SIZE, boardPanel.TILE_SIZE, null);

        g.drawImage(markerImage,0,0,boardPanel.TILE_SIZE, boardPanel.TILE_SIZE, null);

        if (this.entity instanceof Ship)
        {
            if(shipOfCurrentPlayer)
            {
                g.drawImage(matchPanel.gallery.shipHighlight, 0,0,boardPanel.TILE_SIZE, boardPanel.TILE_SIZE, null);
            }
        }
        else if(this.entity instanceof Rock)
        {
            this.imgEntity = matchPanel.gallery.rockAtlas.get(boardPanel.currentRockTile);
        }
        g.drawImage(this.imgEntity, 0,0,boardPanel.TILE_SIZE, boardPanel.TILE_SIZE, null);
    }
}
