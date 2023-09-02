package SpendWise.Utils.Enums;

public enum AccountFields {
    NAME("Name"),
    USERNAME("Username"),
    EMAIL("E-mail"),
    PASSWORD("Password");

    private String name;

    AccountFields(String name) {
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
