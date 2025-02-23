package org.GUI.manual;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.GUI.Gallery;
import org.GUI.windowManager.WindowManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manual extends JDialog
{
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final String manuelFile  = "/Manual.json";
    private static final Gallery gallery = Gallery.galleryInstance();
    private static final int border = gallery.scaleWidth(5);

    public Manual(WindowManager windowManager)
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                windowManager.closeManualDialog();
            }
        });
        this.setAlwaysOnTop(true);
        this.setSize(new Dimension(gallery.scaleWidth(1700), gallery.scaleHeight(700)));
        this.setTitle("Spieleanleitung");
        BufferedImage image = Gallery.loadPicture("/icons/Manual.png");
        this.setIconImage(image);
        setup();
        this.pack();
        this.setSize(this.getWidth(), this.getHeight() -20);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void setup()
    {
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
        List<ManualElement> manualElements = getManuel();

        for(ManualElement me: manualElements)
        {
            scrollPanel.add(new ManualPanel(me.title, me.text, me.loadImages()));
        }

        JScrollPane jspPane = new JScrollPane(scrollPanel);
        jspPane.createVerticalScrollBar();
        jspPane.setVisible(true);
        JScrollBar vertical = jspPane.getVerticalScrollBar();
        vertical.setUnitIncrement(30);
        this.add(jspPane);
        SwingUtilities.invokeLater(() -> vertical.setValue(vertical.getMinimum()));
    }

    private ManualElement deserialize(JsonElement jsonElement)
    {
        return gson.fromJson(jsonElement, ManualElement.class);
    }

    private List<ManualElement> getManuel()
    {
        List<ManualElement> manualElements = new ArrayList<>();

        try (
            InputStream inputStream = getClass().getResourceAsStream(manuelFile);
            JsonReader reader = new JsonReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)))
        {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = jsonElement.getAsJsonObject().getAsJsonArray("Anleitungsinhalt");
            for(JsonElement jm: jsonArray)
            {
                manualElements.add(deserialize(jm));
            }
        }
        catch (IOException ioException)
        {
            System.err.println(ioException.getMessage());
        }
        return manualElements;
    }
}
