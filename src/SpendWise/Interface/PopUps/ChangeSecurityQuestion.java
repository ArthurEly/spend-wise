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

public class ChangeSecurityQuestion extends PopUp {
    private User loggedUser;
    private String newQuestion;
    private String newAnswer;
    private Runnable updateFields;

    JTextField fieldSecurityQuestion;
    JTextField fieldSecurityAnswer;

    public ChangeSecurityQuestion(Screen parent, String title, User loggedUser, Runnable callback) {
        super(parent, title);
        this.loggedUser = loggedUser;
        this.newQuestion = "";
        this.newAnswer = "";
        this.updateFields = callback;
    }

    @Override
    public void run() {
        Offsets offsets = new Offsets(50, 0, 100, 100);
        blankPanels = Panels.initializeOffsets((JPanel) this.getContentPane(), offsets);

        // Creating the sign up panel and it's fields
        JPanel editPanel = new JPanel(new GridLayout(6, 1));
        editPanel.setBackground(BACKGROUND_COLOR);

        fieldSecurityQuestion = Components.addTextFieldCenter(editPanel, "Question:", loggedUser.getSecurityQuestion(),
                10, false, true);
        fieldSecurityQuestion.addActionListener(e -> updateSecurityQuestion(e));

        fieldSecurityAnswer = Components.addTextFieldCenter(editPanel, "Answer:", loggedUser.getSecurityAnswer(), 10, false,
                true);
        fieldSecurityAnswer.addActionListener(e -> updateSecurityQuestion(e));

        this.add(editPanel, BorderLayout.CENTER);

        // Creating the south panel and button
        JPanel pnlSouth = new JPanel();
        Offsets southOffsets = new Offsets(5, 20, 200, 200);
        Panels.initializeOffsets(pnlSouth, southOffsets);

        JButton btnApplyChanges = Components.createButton("Change Security Question", BTN_BG_DARK_COLOR,
                BTN_TXT_SECOND_COLOR, null,
                e -> updateSecurityQuestion(e));
        pnlSouth.add(btnApplyChanges, BorderLayout.CENTER);

        this.add(pnlSouth, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void updateSecurityQuestion(ActionEvent e) {
        JPanel alertPanel = getBlankPanel(PanelOrder.NORTH);
        Alerts.clearMessage(alertPanel);
        
        String securityQuestion = fieldSecurityQuestion.getText();
        Alerts.clearBorder(fieldSecurityQuestion);

        String securityAnswer = fieldSecurityAnswer.getText();
        Alerts.clearBorder(fieldSecurityAnswer);
        
        boolean questionFieldIsEmpty = fieldSecurityQuestion.getText().isEmpty();
        if (questionFieldIsEmpty){
            Alerts.errorMessage(alertPanel, "Fill on the question field!");
            Alerts.errorBorder(fieldSecurityQuestion);
            return;
        }
        
        boolean answerFieldIsEmpty = fieldSecurityAnswer.getText().isEmpty();
        if (answerFieldIsEmpty){
            Alerts.errorMessage(alertPanel, "Fill on the answer field!");
            Alerts.errorBorder(fieldSecurityAnswer);
            return;
        }

        //if no errors occurs...
        loggedUser.changeSecurityQuestion(securityQuestion);
        loggedUser.changeSecurityAnswer(securityAnswer);
        Alerts.showMessage(alertPanel, "Security QA changed succesfully!", SECUNDARY_SUCCESS_COLOR);
    }
}
