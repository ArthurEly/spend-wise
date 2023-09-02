package SpendWise.Utils.Enums;

public enum AnalysisUserChoice {
    BOTH("Both"),
    ESSENTIAL("Essential"),
    NONESSENTIAL("Non-essential");

    private final String name;

    private AnalysisUserChoice(String name) {
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
