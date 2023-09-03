package SpendWise.Interface.PopUps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;


import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import SpendWise.Interface.PopUp;
import SpendWise.Interface.Screen;
import SpendWise.Logic.User;
import SpendWise.Logic.Managers.UserManager;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Enums.ForgotPasswordLabels;
import SpendWise.Utils.Graphics.Alerts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Panels;

public class ForgotPassword extends PopUp {
    private JTextField[] forgotPasswordFields;
    private JButton btnSavePassword; 
    private JButton btnGetQuestion; 
    private UserManager userManager;
    //private ActionListener forgotPasswordAction;

    public ForgotPassword(Screen parent, String title, UserManager userManager) {
        super(parent, title);
        this.userManager = userManager;
        //this.forgotPasswordAction = forgotPasswordAction;
    }

    @Override
    public void run() {

        // Creating the panel and it's fields
        this.setLayout(new BorderLayout());
        Offsets offsets = new Offsets(50, 0, 100, 100);
        blankPanels = Panels.initializeOffsets((JPanel) this.getContentPane(), offsets);

        JPanel forgotPasswordPanel = new JPanel(new GridLayout(ForgotPasswordLabels.values().length * 2, 1));
        forgotPasswordPanel.setBackground(BACKGROUND_COLOR);

        final int TEXT_WIDTH = 200;

        // fields 
        forgotPasswordFields = new JTextField[ForgotPasswordLabels.values().length];
        for (ForgotPasswordLabels label : ForgotPasswordLabels.values()) {
            boolean isPassword = label.getName().toLowerCase().contains("password");
            String labelText = label.getName() + ": ";
            forgotPasswordFields[label.ordinal()] = Components.addTextFieldCenter(forgotPasswordPanel, labelText,
                    "", TEXT_WIDTH, isPassword, true);
            //signUpFields[label.ordinal()].addActionListener(e -> this.createUser(e));

        }
        this.add(forgotPasswordPanel, BorderLayout.CENTER);


        // Creating the south panel
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(BACKGROUND_COLOR);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        //pnlCenter.setBackground(BACKGROUND_COLOR);

        Offsets southOffsets = new Offsets(5, 20, 200, 200);
        Panels.initializeOffsets(pnlSouth, southOffsets);

        final Dimension BUTTON_SIZE = new Dimension(100, 30);


        //Creating the get question button
        btnGetQuestion = Components.createButton("Question", BTN_BG_LIGHT_COLOR,
                BTN_TXT_SECOND_COLOR, BUTTON_SIZE,
                e -> getSecurityQuestion(e));
        pnlCenter.add(btnGetQuestion, BorderLayout.LINE_END);
        
        //Creating the save new password button
        btnSavePassword = Components.createButton("Save", BTN_BG_DARK_COLOR,
                BTN_TXT_SECOND_COLOR, BUTTON_SIZE,
                e-> this.saveNewPassword(e));
        pnlCenter.add(btnSavePassword, BorderLayout.LINE_START);

        pnlSouth.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);
        this.setVisible(true);



    }    

    

    public boolean authorizeChange() {
        String username = forgotPasswordFields[0].getText();
        String newPassword = new String(((JPasswordField) forgotPasswordFields[ForgotPasswordLabels.NEW_PASSWORD.ordinal()]).getPassword());
        String securityAnswer = forgotPasswordFields[2].getText();

        JPanel pnlError = getBlankPanel(PanelOrder.NORTH);
        Alerts.clearBorder(forgotPasswordFields[0]);
        Alerts.clearBorder(forgotPasswordFields[1]);
        Alerts.clearBorder(forgotPasswordFields[2]);

        Alerts.clearMessage(pnlError);

        if (username.equals("")) {
            Alerts.errorMessage(pnlError, "Please enter a username.");
            Alerts.errorBorder(forgotPasswordFields[0]);
            return false;
        }

        if (newPassword.equals("")) {
            Alerts.errorMessage(pnlError, "Please enter a password.");
            Alerts.errorBorder(forgotPasswordFields[1]);
            return false;
        }

        if (securityAnswer.equals("")) {
            Alerts.errorMessage(pnlError, "Please enter the awnswer to security question.");
            Alerts.errorBorder(forgotPasswordFields[2]);
            return false;
        }
    
        if (userManager.validateAnswer(username, securityAnswer, newPassword)) {
            return true;
        } else {
            Alerts.errorMessage(pnlError, "Invalid username or answer.");
            Alerts.errorBorder(forgotPasswordFields[0]);
            Alerts.errorBorder(forgotPasswordFields[2]);
            return false;
        }
    }


    private void saveNewPassword(ActionEvent e){
        if (!authorizeChange()){
            System.out.println("sei la po");
        }
    }

    private void getSecurityQuestion(ActionEvent e){
        String username = forgotPasswordFields[0].getText();

        forgotPasswordFields[2].setText(userManager.getSecurityQuestion(username));
    }
}
