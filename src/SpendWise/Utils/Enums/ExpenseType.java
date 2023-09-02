package SpendWise.Utils.Enums;

public enum ExpenseType {
    FIXED("Fixed"),
    ONETIME("One-time"),
    RECURRING("Recurring");

    private final String name;

    ExpenseType(String name) {
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