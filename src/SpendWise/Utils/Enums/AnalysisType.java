package SpendWise.Utils.Enums;

public enum AnalysisType {
    USER("User"),
    GROUP("Group"),
    YEAR("Yearly");

    private final String name;

    private AnalysisType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
