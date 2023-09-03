package SpendWise.Utils.Enums;

public enum ForgotPasswordLabels {
    USERNAME("Username"),
    NEW_PASSWORD("New Password"),
    SECURITY_ANWSER("Anwser to Security Question");

    private final String name;

    ForgotPasswordLabels(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}