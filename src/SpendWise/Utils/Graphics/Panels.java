package SpendWise.Utils.Graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.PanelOrder;

public abstract class Panels implements Colors, Sizes {

    public static void initializeBlankPanel(JPanel blankPanel, int width, int height, Color color) {
        blankPanel.setBackground(BACKGROUND_COLOR);
        blankPanel.setPreferredSize(new Dimension(width, height));
    }

    public static void initializeBlankPanel(JPanel blankPanel, int width, int height) {
        initializeBlankPanel(blankPanel, width, height, BACKGROUND_COLOR);
    }

    public static JPanel createBlankPanel(int width, int height) {
        JPanel blankPanel = new JPanel();
        initializeBlankPanel(blankPanel, width, height);
        return blankPanel;
    }

    public static JPanel createBlankPanel(int width, int height, Color color) {
        JPanel blankPanel = new JPanel();
        initializeBlankPanel(blankPanel, width, height, color);
        return blankPanel;
    }

    public static JPanel[] createOffsets(Offsets offsets) {
        JPanel[] blankPanels = new JPanel[PanelOrder.values().length];

        blankPanels[PanelOrder.NORTH.ordinal()] = createBlankPanel(0, offsets.getNorth());

        blankPanels[PanelOrder.WEST.ordinal()] = createBlankPanel(offsets.getWest(), 0);

        blankPanels[PanelOrder.CENTRAL.ordinal()] = new JPanel();

        blankPanels[PanelOrder.EAST.ordinal()] = createBlankPanel(offsets.getEast(), 0);

        blankPanels[PanelOrder.SOUTH.ordinal()] = createBlankPanel(0, offsets.getSouth());

        return blankPanels;
    }

    /**
     * Creates blank panels and adds them to the given panel
     * 
     * @return the blank panels created, in the order of PanelOrder
     */
    public static JPanel[] initializeOffsets(JPanel panel, Offsets offsets) {
        panel.setLayout(new BorderLayout());
        JPanel[] blankPanels = createOffsets(offsets);

        for (PanelOrder pnl : PanelOrder.values()) {
            if (blankPanels[pnl.ordinal()] != null)
                panel.add(blankPanels[pnl.ordinal()], pnl.getConstrains());
        }

        return blankPanels; // Return the blank panels so that the caller can add stuff to them
    }

    /**
     * Creates blank panels and adds them to the given panel with the given color
     * 
     * @return the blank panels created, in the order of PanelOrder
     */
    public static JPanel[] initializeOffsets(JPanel panel, Offsets offsets, Color color) {
        JPanel[] blankPanels = initializeOffsets(panel, offsets);

        for (PanelOrder pnl : PanelOrder.values()) {
            if (blankPanels[pnl.ordinal()] != null) {
                blankPanels[pnl.ordinal()].setBackground(color);
                panel.add(blankPanels[pnl.ordinal()], pnl.getConstrains());
            }
        }

        return blankPanels;
    }

    public static JPanel[] createPanelWithCenter(JPanel mainPanel, Offsets innerOffsets, Offsets outerOffsets,
            Color innerColor) {
        JPanel[] outerBlankPanels = initializeOffsets(mainPanel, outerOffsets, BACKGROUND_COLOR);

        JPanel[] innerBlankPanels = initializeOffsets(outerBlankPanels[PanelOrder.CENTRAL.ordinal()], innerOffsets,
                innerColor);
        return innerBlankPanels;
    }

    public static JPanel[] createPanelWithCenter(JPanel mainPanel, Offsets innerOffsets, Color innerColor) {
        final int OUTER_LEFT = 100;
        final int OUTER_RIGHT = 0;
        Offsets outerOffsets = new Offsets(innerOffsets.getNorth(), innerOffsets.getSouth(), OUTER_LEFT, OUTER_RIGHT);
        JPanel[] outerBlankPanels = initializeOffsets(mainPanel, outerOffsets, BACKGROUND_COLOR);

        JPanel[] innerBlankPanels = initializeOffsets(outerBlankPanels[PanelOrder.CENTRAL.ordinal()], innerOffsets,
                innerColor);
        return innerBlankPanels;
    }

    public static JPanel[] createPanelWithCenter(JPanel mainPanel, Color innerColor) {
        JPanel[] outerBlankPanels = initializeOffsets(mainPanel, DEFAULT_OUTER_OFFSETS, BACKGROUND_COLOR);

        JPanel[] innerBlankPanels = initializeOffsets(outerBlankPanels[PanelOrder.CENTRAL.ordinal()],
                DEFAULT_INNER_OFFSETS,
                innerColor);
        return innerBlankPanels;
    }

}
