package SpendWise.Utils.Enums;

public enum SignUpLabels {
    NAME("Name"),
    USERNAME("Username"),
    EMAIL("E-mail"),
    PASSWORD("Password"),
    REPEAT_PASSWORD("Repeat Password"),
    SECURITY_QUESTION("Security Question"),
    SECURITY_ANSWER("Anwser");

    private final String name;

    SignUpLabels(String name) {
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