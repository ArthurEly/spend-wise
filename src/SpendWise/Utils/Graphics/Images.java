package SpendWise.Utils.Graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public abstract class Images {
    public static ImageIcon resizedIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public static ImageIcon resizedIcon(String path, int width, int height) {
        ImageIcon initialImage = ResourceLoader.resourceToImage(path);
        return resizedIcon(initialImage, width, height);
    }

    public static BufferedImage convToBufferedImage(Image img) {
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(img, 0, 0, null);
        return bufferedImage;
    }

    public static ImageIcon recolorIcon(Image initialImage, Color color, boolean invert) {
        BufferedImage image = convToBufferedImage(initialImage);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color imageColor = new Color(image.getRGB(x, y), true);

                imageColor = multiply(imageColor, color, invert);

                image.setRGB(x, y, imageColor.getRGB());
            }
        }

        return new ImageIcon(image);
    }

    public static ImageIcon setColorIcon(Image initialImage, Color color, boolean invert) {
        BufferedImage image = convToBufferedImage(initialImage);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color imageColor = new Color(image.getRGB(x, y), true);

                imageColor = multiply(imageColor, color, invert);

                image.setRGB(x, y, imageColor.getRGB());
            }
        }

        return new ImageIcon(image);
    }

    public static Color multiply(Color color1, Color color2, boolean invert) {
        final int ColorChannelCount = 3;
        float[] color1Components = color1.getRGBComponents(null);
        float[] color2Components = color2.getRGBColorComponents(null);
        float[] newComponents = new float[ColorChannelCount];

        for (int i = 0; i < ColorChannelCount; i++) {
            if (invert)
                newComponents[i] = (1 - color1Components[i]) * color2Components[i];
            else
                newComponents[i] = color1Components[i] * color2Components[i];
        }

        return new Color(newComponents[0], newComponents[1], newComponents[2],
                color1Components[3]);
    }

    public static ImageIcon createResizeAndRecolorIcon(String path, int width, int height, Color color,
            boolean invert) {
        ImageIcon initialImage = resizedIcon(path, width, height);
        return recolorIcon(initialImage.getImage(), color, invert);
    }

    public static String getColorHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }
}
