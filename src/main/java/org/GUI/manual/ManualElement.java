package org.GUI.manual;
import org.GUI.Gallery;
import java.awt.image.BufferedImage;

public class ManualElement
{
    public String title, text;
    public String[] picPath;

    public ManualElement(String title, String text, String... picPath)
    {
        this.picPath = picPath;
        this.text = text;
        this.title = title;
    }

    public BufferedImage[] loadImages()
    {
        BufferedImage[] pics;
        int length = picPath.length;
        pics = new BufferedImage[length];

        for(int i = 0; i < length; i++){
            pics[i] = Gallery.loadPicture(picPath[i]);
        }
        return pics;
    }
}
