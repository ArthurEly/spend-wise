package SpendWise.Utils;

import javax.swing.JTextField;

import SpendWise.Utils.Graphics.Alerts;

public abstract class Email {
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static boolean isEmailValid(JTextField emailField) {
        Alerts.clearBorder(emailField);
        String email = emailField.getText();
        if (email.matches(EMAIL_REGEX)) {
            return true;
        } else {
            Alerts.errorBorder(emailField);
            return false;
        }
    }
}
