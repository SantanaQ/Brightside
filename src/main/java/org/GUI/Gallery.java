package org.GUI;

import org.model.cards.*;
import org.tiles.TileSheet;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
public class Gallery
{
    private static Gallery INSTANCE;

    private static final int defaultMenuFontSize = 50;
    private static final int defaultScoreboardFontSize = 25;
    private static final int defaultScoreboardHeaderFontSize = 25;

    private static final int defaultWindowHeight = 1080;
    private static final int defaultWindowWidth = 1920;

    public static final int wavesCount = 6;
    public static final int rockCount = 6;
    public static final int shipCount = 6;
    public static final int markerCount = 3;
    public static final int mineCount = 1;

    private static final String wavesPfad = "/waves/waves.png";
    private static final String rockPfad = "/rocks/Felsen_oben.png";
    private static final String shipPfad = "/ships/Schiffe.png";
    private static final String cardPfad = "/cards/cards.png";
    private static final String markerPfad = "/marker/Fieldmarker_64x32.png";
    private static final String minePfad = "/mine/Mine.png";
    private static final String shipHighlightPfad = "/ships/Schiff_ausgewaehlt.png";

    public ArrayList<BufferedImage> backgrounds = new ArrayList<>();
    public ArrayList<BufferedImage> waveAtlas;
    public ArrayList<BufferedImage> rockAtlas;
    public ArrayList<BufferedImage> shipAtlas;
    public ArrayList<BufferedImage> markerAtlas;
    public ArrayList<BufferedImage> mineAtlas;
    public Map<String, BufferedImage> cards;
    public BufferedImage shipHighlight;

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    AffineTransform transform = gc.getDefaultTransform();

    private final Map<String, Font> fontMap = new HashMap<>();

    public static BufferedImage loadPicture(String file)
    {
        BufferedImage image = null;
        try{
            image = ImageIO.read(Objects.requireNonNull(Gallery.class.getResourceAsStream(file)));
        }
        catch (IOException ioException)
        {
            System.err.println(ioException.getMessage());
        }
        return image;
    }

    public Font getFont(String fontName)
    {
        return fontMap.get(fontName);
    }

    public int scaleWidth(int width)
    {
        float temp = ((float) width / (float) defaultWindowWidth * (float) gd.getDisplayMode().getWidth());
        double scaleX = 1 + (1 - transform.getScaleX());
        return (int) (temp *scaleX);
    }

    public int scaleHeight(int height)
    {
        float temp = ((float) height / (float) defaultWindowHeight * (float) gd.getDisplayMode().getHeight());
        double scaleY = 1 +(1 - transform.getScaleY());
        return (int) (temp * scaleY);
    }

    private Gallery()
    {
        waveAtlas = loadFiles(wavesPfad, wavesCount);
        rockAtlas = loadFiles(rockPfad, rockCount);
        shipAtlas = loadFiles(shipPfad, shipCount);
        markerAtlas = loadFiles(markerPfad, markerCount);
        mineAtlas = loadFiles(minePfad, mineCount);
        shipHighlight = loadFile(shipHighlightPfad);
        cards = loadCards();
        loadBackgrounds();
        fillFontMap();
    }

    public static Gallery galleryInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new Gallery();
        }
        return INSTANCE;
    }


    private ArrayList<BufferedImage> loadFiles(String path, int count)
    {
        TileSheet ts = TileSheet.getTiles(path);
        ArrayList<BufferedImage> atlas = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            atlas.add(ts.getTile(i, 0));
        }
        return atlas;
    }

    private void loadBackgrounds()
    {
        backgrounds.add(loadPicture("/backgrounds/background.png"));
        backgrounds.add(loadPicture("/backgrounds/startscreen.png"));
    }

    private Map<String, BufferedImage> loadCards()
    {
        TileSheet cardSheet = TileSheet.getCards(cardPfad);
        Map<String, BufferedImage> cards = new HashMap<>();
        cards.put(String.valueOf(BroadsideLeft.class), cardSheet.getCard(0, 0));
        cards.put(String.valueOf(BroadsideRight.class), cardSheet.getCard(1, 0));
        cards.put(String.valueOf(Ram.class), cardSheet.getCard(2, 0));
        cards.put(String.valueOf(Maneuver.class), cardSheet.getCard(3, 0));
        cards.put(String.valueOf(Hook.class), cardSheet.getCard(4, 0));
        cards.put(String.valueOf(Steer.class), cardSheet.getCard(5, 0));
        cards.put(String.valueOf(LayMines.class), cardSheet.getCard(6, 0));
        cards.put(String.valueOf(ForwardOne.class), cardSheet.getCard(7, 0));
        cards.put(String.valueOf(ForwardTwo.class), cardSheet.getCard(8, 0));
        return cards;
    }

    @SuppressWarnings("SameParameterValue")
    private BufferedImage loadFile(String path)
    {
        TileSheet ts = TileSheet.getTiles(path);
        return ts.getTile(0,0);
    }


    private void fillFontMap()
    {
        fontMap.put("menuFont", new Font("Dialog", Font.BOLD, calcFontSizes(defaultMenuFontSize)));
        fontMap.put("scoreboardFont",new Font("Dialog", Font.BOLD, calcFontSizes(defaultScoreboardFontSize)));
        fontMap.put("scoreboardHeaderFont", new Font("Dialog", Font.BOLD, calcFontSizes(defaultScoreboardHeaderFontSize)));
        fontMap.put("manualFont",new Font(Font.DIALOG, Font.PLAIN, calcFontSizes(20)));
        fontMap.put("gameOverFont",new Font(Font.DIALOG, Font.BOLD, calcFontSizes(180)));
    }

    private int calcFontSizes(int defaultSize)
    {
        float temp =  ((float)defaultSize / (float)defaultWindowHeight * (float)gd.getDisplayMode().getHeight());
        double scaleX = 1 + (1 - transform.getScaleX());
        return (int) (temp* scaleX);
    }
}
