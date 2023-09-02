package SpendWise.Interface;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JButton;

import SpendWise.Utils.Paths;
import SpendWise.Utils.Enums.Contexts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Images;

public class MenuButton extends JButton implements Paths {
    private String name;
    private Color Background;
    private Color Foreground;
    private Image resizedIcon;

    public MenuButton(String name, Color background, Color foreground, Dimension size) {
        this.name = name;
        this.Background = background;
        this.Foreground = foreground;

        String fileName = Contexts.getContext(name).toString().toLowerCase();
        String imagePath = ICONS_PATH + fileName + IMAGES_EXT;

        resizedIcon = Images.resizedIcon(imagePath, 30, 30).getImage();

        Components.defineSize(this, size);

        this.initialize();
    }

    private void initialize() {
        // Visual settings
        switchIconColor(Background);
        this.setBackground(Background);
        this.setText("");
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(true);
        this.setOpaque(false);
        this.setVisible(true);

        // Aligment settings
        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.setHorizontalAlignment(JButton.RIGHT);
        this.setHorizontalTextPosition(JButton.LEFT);
        this.setVerticalTextPosition(JButton.CENTER);
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if (b) {
            this.openMenu();
        } else {
            this.closeMenu();
        }
    }

    private void openMenu() {
        this.setText(this.name);
        this.setOpaque(true);
        switchIconColor(Foreground);
    }

    private void closeMenu() {
        this.setText("");
        this.setOpaque(false);
        switchIconColor(Background);
    }

    private void switchIconColor(Color color) {
        this.setIcon(Images.recolorIcon(resizedIcon, color, true));
    }
}
