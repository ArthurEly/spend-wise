package SpendWise.Utils.Enums;

import java.awt.BorderLayout;

public enum PanelOrder {
    NORTH(BorderLayout.NORTH),
    WEST(BorderLayout.WEST),
    CENTRAL(BorderLayout.CENTER),
    EAST(BorderLayout.EAST),
    SOUTH(BorderLayout.SOUTH);

    private final String constrains;

    PanelOrder(String constrains) {
        this.constrains = constrains;
    }

    public String getConstrains() {
        return constrains;
    }

}
