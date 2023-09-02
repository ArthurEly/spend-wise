package SpendWise.Utils.Graphics;

import javax.swing.JPanel;

import SpendWise.Utils.Enums.PanelOrder;

public interface BlankPanels {
    /**
     * @param panel the panel to get
     * @return the blankPanels
     */
    public abstract JPanel getBlankPanel(PanelOrder panel);
}
