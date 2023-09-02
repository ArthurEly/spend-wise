package SpendWise.Utils.Graphics;

import javax.swing.ImageIcon;

import SpendWise.Utils.Paths;

public interface Icons {
    public final ImageIcon APP_LOGO = Images.createResizeAndRecolorIcon(
            Paths.IMAGES_PATH + "logo" + Paths.IMAGES_EXT, 30, 30,
            Colors.BACKGROUND_COLOR, true);
}
