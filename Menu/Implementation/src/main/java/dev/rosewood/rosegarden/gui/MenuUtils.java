package dev.rosewood.rosegarden.gui;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

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

    public static int getRow(int slot) {
        return (int) Math.floor(slot / 9.0);
    }

    // Gets a slot's column starting at 0
    public static int getColumn(int slot) {
        return slot % 9;
    }

    // Gets a slot from a row and column
    public static int getSlot(int row, int column) {
        return row * 9 + column;
    }

}
