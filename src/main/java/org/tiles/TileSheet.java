package org.tiles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileSheet {

    private BufferedImage tileSheet;
    private static final int tileWidth = 32;
    private static final int tileHeight = 32;
    private static final int cardHeight = 302;
    private static final int cardWidth = 162;

    public static TileSheet getTiles(String path){
        return new TileSheet(path);
    }

    public static TileSheet getCards(String path){
        return new TileSheet(path);
    }


    private TileSheet(String path)
    {
        try
        {
            //tileSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            tileSheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public BufferedImage getTile(int col, int row)
    {
        return tileSheet.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
    }
    public BufferedImage getCard(int col, int row)
    {
        return tileSheet.getSubimage(col * cardWidth, row * cardHeight, cardWidth, cardHeight);
    }


}
