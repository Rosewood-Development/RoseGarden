package dev.rosewood.rosegarden.utils;

import dev.rosewood.rosegarden.RosePlugin;
import org.bukkit.command.CommandSender;

public final class RoseGardenUtils {

    public static final String GRADIENT = "<g:#8A2387:#E94057:#F27121>";
    public static final String PREFIX = "&7[" + GRADIENT + "RoseGarden&7] ";

    private RoseGardenUtils() {

    }

    /**
     * Checks if a String contains any values for a yaml value that need to be quoted
     *
     * @param string The string to check
     * @return true if any special characters need to be escaped, otherwise false
     */
    public static boolean containsConfigSpecialCharacters(String string) {
        for (char c : string.toCharArray()) {
            // Range taken from SnakeYAML's Emitter.java
            if (!(c == '\n' || (0x20 <= c && c <= 0x7E)) &&
                    (c == 0x85 || (c >= 0xA0 && c <= 0xD7FF)
                            || (c >= 0xE000 && c <= 0xFFFD)
                            || (c >= 0x10000 && c <= 0x10FFFF))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if RoseGarden has been relocated properly, otherwise false
     */
    public static boolean isRelocated() {
        String defaultPackage = new String(new byte[]{'d', 'e', 'v', '.', 'r', 'o', 's', 'e', 'w', 'o', 'o', 'd', '.', 'r', 'o', 's', 'e', 'g', 'a', 'r', 'd', 'e', 'n'});
        return !RosePlugin.class.getPackage().getName().equals(defaultPackage);
    }

    /**
     * Sends a RoseGarden message to a recipient
     */
    public static void sendMessage(CommandSender recipient, String message) {
        recipient.sendMessage(HexUtils.colorify(PREFIX + message));
    }

    /**
     * Sends a RoseGarden message to a recipient
     */
    public static void sendMessage(CommandSender recipient, String message, StringPlaceholders placeholders) {
        sendMessage(recipient, placeholders.apply(message));
    }

}
