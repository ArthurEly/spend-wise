package SpendWise.Logic;

import java.io.Serializable;
import java.util.Objects;

import SpendWise.Logic.Managers.ExpensesManager;
import SpendWise.Logic.Managers.GroupManager;
import SpendWise.Utils.Enums.AccountFields;

public class User implements Serializable {
    // Atributos
    private String username;
    private String name;
    private String email;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private double income;
    private double monthlyLimit;
    
    private GroupManager groupManager;
    private ExpensesManager expensesManager;
    
    /**
     * @return the expensesManager
     */
    public ExpensesManager getExpensesManager() {
        return expensesManager;
    }
    
    // Construtores
    public User(String username, String name, String email, String password, String securityQuestion, String securityAnswer, double income, double monthlyLimit) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.income = income;
        this.monthlyLimit = monthlyLimit;
        this.expensesManager = new ExpensesManager();
        this.groupManager = new GroupManager();
    }
    
    public User() {
        this.username = "";
        this.name = "";
        this.email = "";
        this.password = "";
        this.securityQuestion = "";
        this.securityAnswer = "";
        this.income = 0;
        this.monthlyLimit = 0;
        this.expensesManager = new ExpensesManager();
    }
    
    // Metodos
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!checkPassword(oldPassword)) {
            return false;
        }
        this.password = newPassword;
        return true;
    }
    
    public void changeSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void changeSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public boolean checkSecurityAnswer(String securityAnswer) {
        return this.securityAnswer.equals(securityAnswer);
    }
   
    public boolean changeForgottenPassword(String securityAnswer, String newPassword){
        if (!checkSecurityAnswer(securityAnswer)){
            return false;
        }
        this.password = newPassword;
        return true;
    }
    
    public String getField(AccountFields field) {
        switch (field) {
            case NAME:
            return this.name;
            case USERNAME:
            return this.username;
            case EMAIL:
            return this.email;
            case PASSWORD:
            return this.password;
            case SECURITY_QUESTION:
            return this.securityQuestion;
            case SECURITY_ANSWER:
            return this.securityAnswer;
            default:
            return "*".repeat(this.getPasswordSize());
        }
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * @return the security question
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    
    /**
     * @param email the email to set
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
    
    /**
     * @return the income
     */
    public double getIncome() {
        return income;
    }
    
    /**
     * @param income the income to set
     */
    public void setIncome(double income) {
        this.income = income;
    }
    
    /**
     * @return the monthlyLimit
     */
    public double getMonthlyLimit() {
        return monthlyLimit;
    }
    
    /**
     * @param monthlyLimit the monthlyLimit to set
     */
    public void setMonthlyLimit(double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }
    
    public int getPasswordSize() {
        return this.password.length();
    }
    
    public GroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public String toString() {
        return this.username;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;
        boolean sameUsername = this.username.equals(user.getUsername());
        boolean samePassword = this.password.equals(user.password);

        return sameUsername && samePassword;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.password);
    }
}
