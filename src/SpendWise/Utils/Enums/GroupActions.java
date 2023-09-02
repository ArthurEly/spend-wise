package SpendWise.Utils.Enums;

public enum GroupActions {
    ADD("Add User"),
    DELETE("Delete User");

    private final String name;

    GroupActions(String name) {
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