package SpendWise.Interface.PopUps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SpendWise.Interface.PopUp;
import SpendWise.Interface.Screen;
import SpendWise.Logic.User;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Alerts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Panels;

public class ChangePassword extends PopUp {
    private User loggedUser;
    private String newPassword;
    private Runnable updateFields;

    JTextField fieldRepeatPassword;
    JTextField fieldOldPassword;

    public ChangePassword(Screen parent, String title, User loggedUser, String newPassword, Runnable callback) {
        super(parent, title);
        this.loggedUser = loggedUser;
        this.newPassword = newPassword;
        this.updateFields = callback;
    }

    @Override
    public void run() {
        Offsets offsets = new Offsets(50, 0, 100, 100);
        blankPanels = Panels.initializeOffsets((JPanel) this.getContentPane(), offsets);

        // Creating the sign up panel and it's fields
        JPanel editPanel = new JPanel(new GridLayout(6, 1));
        editPanel.setBackground(BACKGROUND_COLOR);

        fieldRepeatPassword = (JPasswordField) Components.addTextFieldCenter(editPanel, "Repeat New Password:", "",
                15, true, true);
        fieldRepeatPassword.addActionListener(e -> updatePassword(e));

        fieldOldPassword = (JPasswordField) Components.addTextFieldCenter(editPanel, "Old Password:", "", 15, true,
                true);
        fieldOldPassword.addActionListener(e -> updatePassword(e));

        this.add(editPanel, BorderLayout.CENTER);

        // Creating the south panel and button
        JPanel pnlSouth = new JPanel();
        Offsets southOffsets = new Offsets(5, 20, 200, 200);
        Panels.initializeOffsets(pnlSouth, southOffsets);

        JButton btnApplyChanges = Components.createButton("Change Password", BTN_BG_DARK_COLOR,
                BTN_TXT_SECOND_COLOR, null,
                e -> updatePassword(e));
        pnlSouth.add(btnApplyChanges, BorderLayout.CENTER);

        this.add(pnlSouth, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void updatePassword(ActionEvent e) {
        JPanel alertPanel = getBlankPanel(PanelOrder.NORTH);
        Alerts.clearMessage(alertPanel);

        String oldPassword = fieldOldPassword.getText();
        Alerts.clearBorder(fieldOldPassword);

        String repeatNewPassword = fieldRepeatPassword.getText();
        Alerts.clearBorder(fieldRepeatPassword);

        if (!newPassword.equals(repeatNewPassword)) {
            Alerts.errorMessage(alertPanel, "New Passwords do not match!");
            Alerts.errorBorder(fieldOldPassword);
            return;
        }

        if (loggedUser.changePassword(oldPassword, newPassword)) {
            this.dispose();
            this.updateFields.run();
        } else {
            Alerts.errorMessage(alertPanel, "Old Password Incorrect!");
            Alerts.errorBorder(fieldOldPassword);
        }
    }
}
