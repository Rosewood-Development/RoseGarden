package dev.rosewood.rosegarden.gui;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    /**
     * Rounds a given size to a multiple of 9.
     *
     * @param size The size to round.
     * @return The rounded size as a multiple of 9, used for GUI sizes.
     */
    public static int roundSize(int size) {
        if (size <= 9)
            return 9;

        if (size >= 54)
            return 54;

        int remainder = size % 9;
        if (remainder == 0)
            return size;

        int rounded = (size + (9 - remainder));
        return Math.min(rounded, 54);
    }

    /**
     * @param startCorner The start of the border.
     * @param endCorner The end of the border.
     * @return A list of slots representing a border from the start corner to the end corner.
     */
    public static List<Integer> getBoundingSlots(int startCorner, int endCorner) {
        int startRow = getRow(startCorner);
        int startColumn = getColumn(startCorner);
        int endRow = getRow(endCorner);
        int endColumn = getColumn(endCorner);

        List<Integer> points = new ArrayList<>();

        // Top and bottom borders
        for (int i = startColumn; i <= endColumn; i++) {
            points.add(getSlot(startRow, i));
            points.add(getSlot(endRow, i));
        }

        // Left and right borders
        for (int i = startRow; i <= endRow; i++) {
            points.add(getSlot(i, startColumn));
            points.add(getSlot(i, endColumn));
        }

        return points;
    }

    /**
     * @return The inventory row of the given slot.
     */
    public static int getRow(int slot) {
        return (int) Math.floor(slot / 9.0);
    }

    /**
     * @return The inventory column of a slot.
     */
    public static int getColumn(int slot) {
        return slot % 9;
    }

    /**
     * @return A slot from the given row and column.
     */
    public static int getSlot(int row, int column) {
        return row * 9 + column;
    }

}
