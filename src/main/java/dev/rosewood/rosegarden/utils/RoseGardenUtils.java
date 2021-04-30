package dev.rosewood.rosegarden.utils;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSettingSection;
import dev.rosewood.rosegarden.config.RoseSettingValue;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public final class RoseGardenUtils {

    public static final String GRADIENT = "<g:#8A2387:#E94057:#F27121>";
    public static final String PREFIX = "&7[" + GRADIENT + "RoseGarden&7] ";

    private static Logger logger;

    private RoseGardenUtils() {

    }

    /**
     * @return the Logger for RoseGarden
     */
    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger("RoseGarden", null) { };
            logger.setParent(Bukkit.getLogger());
            logger.setLevel(Level.ALL);
        }
        return logger;
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

    public static void recursivelyWriteRoseSettingValues(CommentedFileConfiguration fileConfiguration, RoseSettingValue settingValue) {
        recursivelyWriteRoseSettingValues(fileConfiguration, fileConfiguration, settingValue);
    }

    private static void recursivelyWriteRoseSettingValues(CommentedFileConfiguration baseConfiguration, CommentedConfigurationSection currentSection, RoseSettingValue settingValue) {
        String key = settingValue.getKey();
        Object defaultValue = settingValue.getDefaultValue();
        String[] comments = settingValue.getComments();

        String keyPath = currentSection.getCurrentPath() == null ? key : currentSection.getCurrentPath() + "." + key;

        if (defaultValue instanceof RoseSettingSection) {
            baseConfiguration.addPathedComments(keyPath, comments);
            currentSection = currentSection.createSection(key);

            RoseSettingSection settingSection = (RoseSettingSection) defaultValue;
            for (RoseSettingValue value : settingSection.getValues())
                recursivelyWriteRoseSettingValues(baseConfiguration, currentSection, value);
        } else {
            baseConfiguration.set(keyPath, defaultValue, comments);
        }
    }

    /**
     * Gets an Object as a numerical value
     *
     * @param value The Object to cast
     * @return The Object as a numerical value
     * @throws ClassCastException when the value is not a numerical value
     */
    public static double getNumber(Object value) {
        if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof Short) {
            return (short) value;
        } else if (value instanceof Byte) {
            return (byte) value;
        } else if (value instanceof Float) {
            return (float) value;
        }

        return (double) value;
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
