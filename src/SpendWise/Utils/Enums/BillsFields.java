package SpendWise.Utils.Enums;

public enum BillsFields {
    VALUE("Value"),
    ESSENTIAL("Essential"),
    DATE("Date"),
    DESCRIPTION("Description"),
    TYPE("Type");

    private final String name;

    private BillsFields(String name) {
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
