package org.GUI.menu;

import org.GUI.Gallery;
import org.GUI.windowManager.WindowManager;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuFrame extends JFrame
{
    private static final Gallery gallery = Gallery.galleryInstance();
    private static final Font font = gallery.getFont("menuFont");
    private final WindowManager windowManager;
    private final JPanel backgroundPanel;
    private MenuPanel menuPanel;
    private SettingPanel settingPanel;

    public MenuFrame(WindowManager windowManager)
    {
        this.setTitle("Brightside");
        this.setIconImage(Gallery.loadPicture("/icons/Conf.png"));
        this.windowManager = windowManager;
        BufferedImage background = gallery.backgrounds.get(1);

        backgroundPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        backgroundPanel.setOpaque(false);
        backgroundPanel.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.add(backgroundPanel);
    }

    public void setupMenu()
    {
        this.backgroundPanel.removeAll();
        this.backgroundPanel.add(getMenuPanel(), BorderLayout.CENTER);
        update();
    }

    public void setupSettings()
    {
        this.backgroundPanel.removeAll();
        this.backgroundPanel.add(getSettingPanel(), BorderLayout.CENTER);
        update();
    }

    public static JButton configureButton(String text)
    {
        int defaultWidth = 400;
        int defaultHeight = 100;

        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(gallery.scaleWidth(defaultWidth), gallery.scaleHeight(defaultHeight)));
        b.setFont(font);
        b.setForeground(Color.BLACK);
        return b;
    }

    public static JLabel configureLabel(String text)
    {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(font);
        label.setForeground(Color.BLACK);
        return label;
    }

    public void update()
    {
        this.revalidate();
        this.repaint();
    }

    public void setRunningGame(){
        getMenuPanel().setRunningGame();
    }

    private MenuPanel getMenuPanel()
    {
        if(menuPanel == null)
        {
            menuPanel = new MenuPanel(windowManager);
        }
        return menuPanel;
    }

    private SettingPanel getSettingPanel()
    {
        if(settingPanel == null)
        {
            settingPanel = new SettingPanel(windowManager);
        }
        return settingPanel;
    }
}