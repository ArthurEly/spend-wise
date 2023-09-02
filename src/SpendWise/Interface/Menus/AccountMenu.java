package SpendWise.Interface.Menus;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SpendWise.Interface.PopUp;
import SpendWise.Interface.Screen;
import SpendWise.Interface.PopUps.ChangePassword;
import SpendWise.Logic.User;
import SpendWise.Logic.Managers.UserManager;
import SpendWise.Utils.Email;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.AccountFields;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Alerts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Panels;

public class AccountMenu extends Screen {
    private JPanel pnlUserData;
    private User loggedUser;
    private UserManager userManager;
    private JButton btnEditAccount;
    private boolean isEditing = false;
    private JTextField[] txtFields;
    private Runnable logout;

    public AccountMenu(UserManager userManager, Runnable logout) {
        this.userManager = userManager;
        this.loggedUser = userManager.getLoggedUser();
        pnlUserData = new JPanel();
        this.logout = logout;
        this.initialize();
    }

    @Override
    protected void initialize() {
        blankPanels = Panels.createPanelWithCenter(this, ACCENT_COLOR);

        pnlUserData = getBlankPanel(PanelOrder.CENTRAL);
        pnlUserData.setLayout(new GridLayout(AccountFields.values().length * 3, 1)); // Label + TextField + Spacer
        pnlUserData.setBackground(ACCENT_COLOR);
        pnlUserData.setAlignmentY(CENTER_ALIGNMENT);

        txtFields = new JTextField[AccountFields.values().length];
        this.updateAccountFields();

        Offsets offsetsBtn = new Offsets(10, 10, 400, 20);
        final String[] btnNames = { "Edit Account", "Delete Account" };
        final ActionListener[] btnActions = { e -> edit(e), e -> deleteAccount() };
        JButton[] bnts = Components.initializeButtons(this.getBlankPanel(PanelOrder.SOUTH), offsetsBtn,
                btnNames,
                ACCENT_COLOR,
                btnActions);
        btnEditAccount = bnts[0];
    }

    private void updateAccountFields() {
        pnlUserData.removeAll();

        final int textFieldSize = 300;
        final boolean startingEditState = false;
        final int verticalGap = 10;

        for (AccountFields field : AccountFields.values()) {
            String label = field.getName() + ": ";
            Boolean isPassword = field.getName().toLowerCase().contains("password");
            String userValue = loggedUser.getField(field);

            txtFields[field.ordinal()] = Components.addTextField(pnlUserData, label, userValue, textFieldSize,
                    isPassword,
                    startingEditState);

            pnlUserData.add(Box.createVerticalStrut(verticalGap));
        }

        Components.refresh(pnlUserData);
    }

    private void edit(ActionEvent e) {
        boolean nextState = !isEditing;

        JPanel alertPanel = super.getBlankPanel(PanelOrder.NORTH);
        JTextField txtName = txtFields[AccountFields.NAME.ordinal()];
        JTextField txtUsername = txtFields[AccountFields.USERNAME.ordinal()];
        JTextField txtEmail = txtFields[AccountFields.EMAIL.ordinal()];
        JPasswordField txtPassword = (JPasswordField) txtFields[AccountFields.PASSWORD.ordinal()];

        Alerts.clearMessage(alertPanel);
        if (!nextState) {
            boolean alerting = false;
            for (JTextField txtField : txtFields) {
                Alerts.clearBorder(txtField);
                if (txtField.getText().isEmpty() && !(txtField instanceof JPasswordField)) {
                    Alerts.clearBorder(txtField);
                    Alerts.errorMessage(alertPanel,
                            "Please fill all Non Password fields");
                    alerting = true;
                    nextState = true;
                }
            }

            boolean differentUsername = !txtUsername.getText().equals(loggedUser.getUsername());
            boolean usernameAvailable = userManager.checkUsernameAvailability(txtUsername.getText());
            if (differentUsername && !usernameAvailable) {
                if (!alerting) {
                    Alerts.errorMessage(alertPanel, "Username already taken");
                }
                Alerts.errorBorder(txtUsername);
                nextState = true;
            } else {
                Alerts.clearBorder(txtUsername);
            }

            if (!Email.isEmailValid(txtEmail)) {
                if (!alerting) {
                    Alerts.errorMessage(alertPanel, "Please enter a valid email");
                }
                nextState = true;
            } else {
                Alerts.clearBorder(txtEmail);
            }
        }

        // Decision to edit or not based on previous checks
        if (nextState) {
            txtPassword = (JPasswordField) txtFields[3];
            txtPassword.setText("");
        } else {
            String newName = txtName.getText();
            String newUsername = txtUsername.getText();
            String newEmail = txtEmail.getText();
            String newPassword = new String(txtPassword.getPassword());

            if (!loggedUser.getName().equals(newName)) {
                loggedUser.setName(newName);
            }

            if (!loggedUser.getUsername().equals(newUsername)) {
                userManager.changeUsername(loggedUser.getUsername(), newUsername);
            }

            if (!loggedUser.getEmail().equals(newEmail)) {
                loggedUser.setEmail(newEmail);
            }

            if (!newPassword.isEmpty()) {
                if (!loggedUser.checkPassword(newPassword)) {
                    PopUp changePassword = new ChangePassword(this, "Change Password", loggedUser, newPassword,
                            this::changePassword);
                    changePassword.run();
                }
            }

            txtPassword.setText(loggedUser.getField(AccountFields.PASSWORD));
        }

        btnEditAccount.setText(nextState ? "Apply Changes" : "Edit Account");

        for (JTextField txtField : txtFields) {
            txtField.setEditable(nextState);
        }

        isEditing = nextState;
    }

    private void deleteAccount() {
        userManager.removeUser(loggedUser);
        userManager.clearLoggedUser();
        this.logout.run();
    }

    private void changePassword() {
        this.updateAccountFields();
        Alerts.clearMessage(getBlankPanel(PanelOrder.NORTH));
        Alerts.showMessage(getBlankPanel(PanelOrder.NORTH), "Password changed successfully!",
                BACKGROUND_COLOR);
    }
}
