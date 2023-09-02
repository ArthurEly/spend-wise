package SpendWise.Utils;

import SpendWise.Utils.Enums.PanelOrder;

public class Offsets {
    private int north;
    private int south;
    private int west;
    private int east;

    public Offsets(int north, int south, int west, int east) {
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
    }

    public int getOffset(PanelOrder panel) {
        switch (panel) {
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case WEST:
                return west;
            case EAST:
                return east;
            default:
                return 0;
        }
    }

    public int getOffset(int panel) {
        return getOffset(PanelOrder.values()[panel]);
    }

    public int getOffset(String panel) {
        return getOffset(PanelOrder.valueOf(panel));
    }

    public int getNorth() {
        return north;
    }

    public int getSouth() {
        return south;
    }

    public int getWest() {
        return west;
    }

    public int getEast() {
        return east;
    }
}
