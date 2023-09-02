package SpendWise.Utils.Enums;

public enum GroupFields {
    SELECT_GROUP("Select Group"),
    SELECT_OPERATION("Select Operation"),
    USERNAME("Username");

    private final String name;

    private GroupFields(String name) {
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
