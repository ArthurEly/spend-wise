package SpendWise.Utils.Graphics;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

import SpendWise.Utils.Paths;

public abstract class ResourceLoader implements Paths {
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    public static ImageIcon resourceToImage(String path) {
        URL place = ResourceLoader.class.getResource(path);
        Image image = toolkit.getImage(place);
        return new ImageIcon(image);
    }

    public static Font resourceToFont(String Path) {
        URL place = ResourceLoader.class.getResource(Path);
        try {
            InputStream is = place.openStream();
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            System.out.println("Error loading font.");
        }
        return null;
    }
}
