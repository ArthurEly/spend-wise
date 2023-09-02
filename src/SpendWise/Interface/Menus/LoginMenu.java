package SpendWise.Interface.Menus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SpendWise.Interface.PopUp;
import SpendWise.Interface.Screen;
import SpendWise.Interface.PopUps.SignUp;
import SpendWise.Logic.Managers.UserManager;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Alerts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Images;
import SpendWise.Utils.Graphics.Panels;

public class LoginMenu extends Screen {
    private JButton btnLogin;
    private JButton btnSignUp;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private UserManager userManager;

    public LoginMenu(ActionListener loginAction, UserManager userManager) {
        this.initialize();
        this.userManager = userManager;
        btnLogin.addActionListener(loginAction);
        btnSignUp.addActionListener(e -> this.signUp(e));
    }

    @Override
    protected void initialize() {
        this.setLayout(new BorderLayout());

        Offsets outerOffsets = new Offsets(100, 100, 270, 270);
        Offsets innerOffsets = new Offsets(50, 100, 50, 50);
        blankPanels = Panels.createPanelWithCenter(this, innerOffsets, outerOffsets, ACCENT_COLOR);

        // Login Panel
        JPanel pnlLogin = getBlankPanel(PanelOrder.CENTRAL);
        pnlLogin.setLayout(new BoxLayout(pnlLogin, BoxLayout.Y_AXIS));
        pnlLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        final int TEXT_WIDTH = 200;

        // Username Fields
        txtLogin = Components.addTextFieldCenter(pnlLogin, "Username", "", TEXT_WIDTH, false, true);
        txtLogin.addActionListener(e -> btnLogin.doClick());
        pnlLogin.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password Fields
        txtPassword = (JPasswordField) Components.addTextFieldCenter(pnlLogin, "Password", "", TEXT_WIDTH, true,
                true);
        txtPassword.addActionListener(e -> btnLogin.doClick());
        pnlLogin.add(Box.createRigidArea(new Dimension(0, 50)));

        // Buttons
        final Dimension BUTTON_SIZE = new Dimension(95, 30);

        btnLogin = Components.createButton("Login", BTN_BG_LIGHT_COLOR,
                BTN_TXT_LIGHT_COLOR, BUTTON_SIZE);
        getBlankPanel(PanelOrder.SOUTH).add(btnLogin);

        btnSignUp = Components.createButton("Sign Up!", BTN_BG_DARK_COLOR,
                BTN_TXT_SECOND_COLOR, BUTTON_SIZE);
        getBlankPanel(PanelOrder.SOUTH).add(btnSignUp);

        // Logo
        JPanel pnlLogo = (JPanel) this.getComponent(0);
        JPanel[] logoPanels = Panels.createPanelWithCenter(pnlLogo, new Offsets(5, 0, 0, 100), BACKGROUND_COLOR);
        pnlLogo = logoPanels[PanelOrder.CENTRAL.ordinal()];

        ImageIcon logoIcon = Images.createResizeAndRecolorIcon(IMAGES_PATH + "logo" + IMAGES_EXT, 70, 70, ACCENT_COLOR,
                true);
        JLabel logo = new JLabel(logoIcon);
        pnlLogo.add(logo);

        JLabel lblLogo = new JLabel("pendWise");
        lblLogo.setFont(LOGO_FONT);
        lblLogo.setForeground(ACCENT_COLOR);
        pnlLogo.add(lblLogo);
    }

    private void singUpSuccess(ActionEvent e) {
        Alerts.showMessage(getBlankPanel(PanelOrder.NORTH), "Sign up successful!", BACKGROUND_COLOR);
        Alerts.clearBorder(txtLogin);
        Alerts.clearBorder(txtPassword);
    }

    private void signUp(ActionEvent action) {
        PopUp signUpWindow = new SignUp(this, "Sign Up", userManager, e -> singUpSuccess(e));
        signUpWindow.run();
    }

    public boolean authorizeUser() {
        String username = txtLogin.getText();
        String password = new String(txtPassword.getPassword());
        JPanel pnlError = getBlankPanel(PanelOrder.NORTH);
        Alerts.clearBorder(txtLogin);
        Alerts.clearBorder(txtPassword);
        Alerts.clearMessage(pnlError);

        if (username.equals("")) {
            Alerts.errorMessage(pnlError, "Please enter a username.");
            Alerts.errorBorder(txtLogin);
            return false;
        }

        if (password.equals("")) {
            Alerts.errorMessage(pnlError, "Please enter a password.");
            Alerts.errorBorder(txtPassword);
            return false;
        }

        if (userManager.validateLogin(username, password)) {
            return true;
        } else {
            Alerts.errorMessage(pnlError, "Invalid username or password.");
            Alerts.errorBorder(txtLogin);
            Alerts.errorBorder(txtPassword);
            return false;
        }
    }

    @Override
    public void refresh() {
        txtLogin.setText("");
        txtPassword.setText("");
        super.refresh();
    }
}
