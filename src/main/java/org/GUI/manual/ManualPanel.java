package org.GUI.manual;

import org.GUI.Gallery;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ManualPanel extends JPanel
{

    private static final Gallery gallery = Gallery.galleryInstance();
    private static final int defaultBorder =  gallery.scaleWidth(5);
    private static final int titleBorder =  gallery.scaleWidth(2);

    public ManualPanel(String title, String text, BufferedImage... pics){
        this.setBorder(BorderFactory.createEmptyBorder(defaultBorder, defaultBorder, defaultBorder, defaultBorder));
        this.setBackground(Color.LIGHT_GRAY);

        JPanel widthPanel = new JPanel();
        widthPanel.setPreferredSize(new Dimension(gallery.scaleWidth(1500), gallery.scaleHeight(1)));
        widthPanel.setVisible(true);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setBorder(new LineBorder(Color.DARK_GRAY, defaultBorder));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(gallery.getFont("scoreboardHeaderFont"));
        titlePanel.setBorder(BorderFactory.createMatteBorder(0,0, titleBorder,0, Color.DARK_GRAY));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(widthPanel, BorderLayout.CENTER);
        innerPanel.add(titlePanel, BorderLayout.NORTH);


        JPanel picturePanel = new JPanel();
        picturePanel.setLayout(new GridLayout(1, 4));

        for(BufferedImage pic : pics)
        {
            pic = scaleImage(pic);
            JLabel picLabel = new JLabel();
            picLabel.setIcon(new ImageIcon(pic));
            picLabel.setBorder(BorderFactory.createEmptyBorder(defaultBorder, defaultBorder, defaultBorder, defaultBorder));
            picturePanel.add(picLabel);
        }
        picturePanel.revalidate();
        picturePanel.repaint();

        innerPanel.add(picturePanel, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setFont(gallery.getFont("manualFont"));
        innerPanel.add(textArea, BorderLayout.SOUTH);

        innerPanel.setAlignmentX(Label.LEFT);
        this.add(innerPanel);
    }

    private BufferedImage scaleImage (BufferedImage bufferedImage)
    {
        int scaleFactor = 3;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (width <= 32 && height <= 32)
        {

            BufferedImage resizedImage = new BufferedImage(width * scaleFactor, height * scaleFactor, bufferedImage.getType());
            Graphics2D g2d = resizedImage.createGraphics();

            // Hochwertige Skalierung aktivieren
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            // Bild zeichnen und skalieren
            g2d.drawImage(bufferedImage, 0, 0, width * scaleFactor, height * scaleFactor, null);
            g2d.dispose();

            return resizedImage;
        }
        return bufferedImage;
    }
}
