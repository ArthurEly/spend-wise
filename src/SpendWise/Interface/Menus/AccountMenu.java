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
import SpendWise.Interface.PopUps.ChangeSecurityQuestion;
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
    private int fieldsLength;

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

    private void setFieldsLength(AccountFields[] a){
        int fieldsLength = 0;

        for(AccountFields name : a){
            Boolean isSecurityField = name.getName().toLowerCase().contains("security");
            if(!isSecurityField){
                fieldsLength++;
            }
        }

        this.fieldsLength = fieldsLength;
    };

    private int getFieldsLength() {
        return fieldsLength;
    }

    @Override
    protected void initialize() {
        blankPanels = Panels.createPanelWithCenter(this, ACCENT_COLOR);

        pnlUserData = getBlankPanel(PanelOrder.CENTRAL);

        setFieldsLength(AccountFields.values());

        pnlUserData.setLayout(new GridLayout(getFieldsLength() * 3, 1)); // Label + TextField + Spacer
        pnlUserData.setBackground(ACCENT_COLOR);
        pnlUserData.setAlignmentY(CENTER_ALIGNMENT);

        txtFields = new JTextField[getFieldsLength()];
        this.updateAccountFields();

        Offsets offsetsBtn = new Offsets(10, 10, 400, 20);
        final String[] btnNames = { "Edit Account", "Delete Account", "Change Security Question" };
        final ActionListener[] btnActions = { e -> edit(e), e -> deleteAccount(), e -> editSecurityQuestion(e)};
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
            Boolean isSecurityField = field.getName().toLowerCase().contains("security");

            if(isSecurityField) continue;

            txtFields[field.ordinal()] = Components.addTextField(pnlUserData, label, userValue, textFieldSize,
                    isPassword,
                    startingEditState);

            pnlUserData.add(Box.createVerticalStrut(verticalGap));
        }

        Components.refresh(pnlUserData);
    }

    private boolean hasEmptyFields(JTextField[] txtFields){
        for (JTextField txtField : txtFields) {
            Alerts.clearBorder(txtField);
            if (txtField.getText().isEmpty() && !(txtField instanceof JPasswordField)) {
                Alerts.errorBorder(txtField);
                return true;
            }
        }
        return false;
    }
    
    private boolean isUsernameValid(String username){
        boolean sameUsername = username.equals(loggedUser.getUsername());
        boolean usernameAvailable = userManager.checkUsernameAvailability(username);
        return sameUsername || usernameAvailable;
    }
    
    private void applyChangesOnAccount(String newName, String newUsername, String newEmail, String newPassword){
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
    }

    private void edit(ActionEvent e) {
        boolean editingMode = isEditing;
        
        JPanel alertPanel = super.getBlankPanel(PanelOrder.NORTH);
        JTextField txtName = txtFields[AccountFields.NAME.ordinal()];
        JTextField txtUsername = txtFields[AccountFields.USERNAME.ordinal()];
        JTextField txtEmail = txtFields[AccountFields.EMAIL.ordinal()];
        JPasswordField txtPassword = (JPasswordField) txtFields[AccountFields.PASSWORD.ordinal()];
        
        if (!editingMode) {
            txtPassword.setText("");
            editingMode = true;
        } else {
            System.out.println(txtFields.length);
            for (int i = 0; i < txtFields.length; i++) {
                String valor = txtFields[i].getText();
                System.out.println("Campo " + (i + 1) + ": " + valor);
            }
            
            if (hasEmptyFields(txtFields)){
                Alerts.errorMessage(alertPanel,"Please fill all Non Password fields");
                return;
            }

            if (!isUsernameValid(txtUsername.getText())) {
                Alerts.errorMessage(alertPanel, "Username already taken");
                Alerts.errorBorder(txtUsername);
                return;
            } 
            Alerts.clearBorder(txtUsername);

            if (!Email.isEmailValid(txtEmail)) {
                Alerts.errorMessage(alertPanel, "Please enter a valid email");
                Alerts.errorBorder(txtEmail);
                return;
            }
            Alerts.clearBorder(txtEmail);

            //if no errors occurrs...
            String newName = txtName.getText();
            String newUsername = txtUsername.getText();
            String newEmail = txtEmail.getText();
            String newPassword = new String(txtPassword.getPassword());

            applyChangesOnAccount(newName,newUsername,newEmail,newPassword);
    
            txtPassword.setText(loggedUser.getField(AccountFields.PASSWORD));
            Alerts.clearMessage(alertPanel);

            editingMode = false;
        }
        btnEditAccount.setText(editingMode ? "Apply Changes" : "Edit Account");

        for (JTextField txtField : txtFields) {
            txtField.setEditable(editingMode);
        }

        isEditing = editingMode;
    }

    private void editSecurityQuestion(ActionEvent e){
        PopUp changePassword = new ChangeSecurityQuestion(this, "Change Security Question", loggedUser,
                        this::changeSecurityQuestion);
                changePassword.run();
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

    private void changeSecurityQuestion() {
        this.updateAccountFields();
        Alerts.clearMessage(getBlankPanel(PanelOrder.NORTH));
        Alerts.showMessage(getBlankPanel(PanelOrder.NORTH), "fodase",
                BACKGROUND_COLOR);
    }
}
