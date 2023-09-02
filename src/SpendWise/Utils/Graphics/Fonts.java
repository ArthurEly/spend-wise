package SpendWise.Utils.Graphics;

import java.awt.Font;

import SpendWise.Utils.Paths;

public interface Fonts {
    public final Font STD_FONT = new Font("Arial", Font.PLAIN, 14);
    public final Font STD_FONT_BOLD = new Font("Arial", Font.BOLD, 14);
    public final Font LOGO_FONT = ResourceLoader.resourceToFont(Paths.LOGO_FONT_PATH).deriveFont(30f);

}