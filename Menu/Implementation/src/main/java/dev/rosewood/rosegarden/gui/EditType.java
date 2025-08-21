package dev.rosewood.rosegarden.gui;

/**
 * Decides how a slot can be interacted with.
 */
public enum EditType {

    /**
     * Items can be added to the slot.
     */
    ADD,

    /**
     * Items can be taken from the slot.
     */
    TAKE,

    /**
     * Items can be added and taken to/from the slot.
     */
    REPLACE,

    /**
     * Items can not be moved.
     */
    NONE

}
