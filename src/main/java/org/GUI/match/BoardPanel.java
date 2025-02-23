package org.GUI.match;

import org.GUI.Gallery;
import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel
{
    int currentWaveTile;
    int currentRockTile;


    private int currentLoop = 0;
    private final int boardWidth, boardHeight;
    private final MatchPanel matchPanel;
    private final Gallery gallery = Gallery.galleryInstance();
    final int TILE_SIZE = gallery.scaleWidth(64);


    public BoardPanel(MatchPanel matchPanel, int boardWidth, int boardHeight)
    {
        this.matchPanel = matchPanel;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;


        this.setLayout(new GridLayout(boardHeight, boardWidth));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(boardWidth*TILE_SIZE, boardHeight*TILE_SIZE));
        this.setBounds(0, 0, boardWidth*TILE_SIZE, boardHeight*TILE_SIZE);


        initFields();

        currentWaveTile = 0;
        currentRockTile = 0;
    }

    private void updateTile()
    {
        if (currentLoop >= 0)
        {
            currentWaveTile = (currentWaveTile + 1) % (Gallery.wavesCount);
            currentRockTile = (currentRockTile + 1) % (Gallery.rockCount);
            currentLoop = 0;
        }
        currentLoop ++;
        repaint();
        revalidate();
    }

    private void initFields()
    {
        for(int lane = 1; lane <= boardHeight; lane++)
        {
            for(int progress = 1; progress <= boardWidth; progress++)
            {
                FieldPanel field = new FieldPanel(matchPanel, lane,progress,this);
                this.add(field);
            }
        }
    }

    public void updateBoardAnimation()
    {
        updateTile();
    }

    public void updateBoard()
    {
        for(Component fp: this.getComponents())
        {
            ((FieldPanel) fp).updateField();
        }
        repaint();
        revalidate();
    }
}
