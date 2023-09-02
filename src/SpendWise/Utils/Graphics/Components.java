package SpendWise.Utils.Graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SpendWise.Utils.Offsets;
import SpendWise.Utils.Pair;

public abstract class Components implements Colors, Fonts {

    public static Pair<JLabel, JTextField> createTextField(String label, String userValue, int width,
            Boolean isPassword,
            Boolean isEditable) {
        JLabel lbl = new JLabel(label);

        JTextField textField = isPassword ? new JPasswordField(userValue, 20) : new JTextField(userValue, 20);
        textField.setEditable(isEditable);
        textField.setBackground(ACCENT_COLOR);

        Dimension size = new Dimension(width, 30);
        Components.defineSize(textField, size);

        return new Pair<JLabel, JTextField>(lbl, textField);
    }

    public static JTextField addTextField(JPanel panel, String label, String userValue, int width, Boolean isPassword,
            Boolean isEditable) {
        Pair<JLabel, JTextField> pair = createTextField(label, userValue, width, isPassword, isEditable);
        panel.add(pair.getKey());
        panel.add(pair.getValue());
        return pair.getValue();
    }

    public static JTextField addTextFieldCenter(JPanel panel, String label, String userValue, int width,
            Boolean isPassword,
            Boolean isEditable) {
        Pair<JLabel, JTextField> pair = createTextField(label, userValue, width, isPassword, isEditable);
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setOpaque(false);
        innerPanel.add(pair.getKey());
        panel.add(innerPanel);
        panel.add(pair.getValue());
        return pair.getValue();
    }

    public static JPanel createPanelText(String label, String userValue, int width, Boolean isPassword,
            Boolean isEditable) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        addTextField(panel, label, userValue, width, isPassword, isEditable);

        return panel;
    }

    public static JButton createButton(String text, Color background, Color foreground, Dimension size,
            ActionListener actionListener) {
        JButton button = new JButton(text);

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(STD_FONT_BOLD);
        button.addActionListener(actionListener);

        Components.defineSize(button, size);

        return button;
    }

    public static JButton createButton(String text, Color background, Color foreground, Dimension size) {
        JButton button = new JButton(text);

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(STD_FONT_BOLD);

        Components.defineSize(button, size);

        return button;
    }

    public static JButton initializeButton(JPanel mainPanel, Offsets offsets, String text, Color color,
            ActionListener actionListener) {
        // Creates the panel that is going to the south of the screen
        JPanel panel = new JPanel();
        Panels.initializeOffsets(panel, offsets, color);

        // Creates the button itself and adds it to the east panel
        JButton btnEdit = createButton(text, BTN_BG_DARK_COLOR, BTN_TXT_SECOND_COLOR, null,
                actionListener);
        panel.add(btnEdit, BorderLayout.EAST);

        // And, finnaly, add the south panel to the screen
        mainPanel.add(panel, BorderLayout.SOUTH);

        return btnEdit;
    }

    public static JButton[] initializeButtons(JPanel mainPanel, Offsets offsets, String[] texts, Color color,
            ActionListener[] actionListeners) {
        // Creates the panel that is going to the south of the screen
        JPanel panel = new JPanel();
        Panels.initializeOffsets(panel, offsets, color);

        // Creates the button itself and adds it to the east panel
        if (texts.length != actionListeners.length)
            return null;

        JButton[] buttons = new JButton[texts.length];
        final String[] LayoutPlace = { BorderLayout.WEST, BorderLayout.EAST, BorderLayout.CENTER};
        for (int i = 0; i < texts.length; i++) {
            buttons[i] = createButton(texts[i], BTN_BG_DARK_COLOR, BTN_TXT_SECOND_COLOR, null,
                    actionListeners[i]);
            panel.add(buttons[i], LayoutPlace[i % LayoutPlace.length]);
        }

        // And, finnaly, add the south panel to the screen
        mainPanel.add(panel, BorderLayout.SOUTH);

        return buttons;
    }

    public static void defineSize(JComponent component, Dimension size) {
        component.setMinimumSize(size);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
    }

    public static void refresh(JComponent component) {
        component.revalidate();
        component.repaint();
    }

}
