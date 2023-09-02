package SpendWise.Utils.Graphics;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

public abstract class Alerts implements Colors {

    public static void setBorder(JTextField field, Color color) {
        Border border = BorderFactory.createLineBorder(color);
        field.setBorder(border);
    }

    public static void clearBorder(JTextField field) {
        field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }

    public static void errorBorder(JTextField field) {
        setBorder(field, ERROR_COLOR);
    }

    public static void showMessage(JPanel panel, String msg, Color color) {
        panel.removeAll();
        JLabel errorLabel = new JLabel(msg);
        errorLabel.setForeground(color);
        panel.add(errorLabel);
        panel.revalidate();
        panel.repaint();
    }

    public static void errorMessage(JPanel panel, String error) {
        showMessage(panel, error, ERROR_COLOR);
    }

    public static void clearMessage(JPanel panel) {
        showMessage(panel, "", TEXT_COLOR);
    }

}
