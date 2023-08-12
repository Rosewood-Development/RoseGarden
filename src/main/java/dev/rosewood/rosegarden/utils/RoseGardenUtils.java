package dev.rosewood.rosegarden.utils;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSettingSection;
import dev.rosewood.rosegarden.config.RoseSettingValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public final class RoseGardenUtils {

    public static final String GRADIENT = "<g:#8A2387:#E94057:#F27121>";
    public static final String PREFIX = "&7[" + GRADIENT + "RoseGarden&7] ";

    public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<Class<?>, Class<?>>() {{
        this.put(boolean.class, Boolean.class);
        this.put(byte.class, Byte.class);
        this.put(char.class, Character.class);
        this.put(double.class, Double.class);
        this.put(float.class, Float.class);
        this.put(int.class, Integer.class);
        this.put(long.class, Long.class);
        this.put(short.class, Short.class);
        this.put(void.class, Void.class);
    }};

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

        if (defaultValue instanceof RoseSettingSection settingSection) {
            baseConfiguration.addPathedComments(keyPath, comments);
            currentSection = currentSection.createSection(key);

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
     * Checks if there is an update available
     *
     * @param latest The latest version of the plugin from Spigot
     * @param current The currently installed version of the plugin
     * @return true if available, otherwise false
     */
    public static boolean isUpdateAvailable(String latest, String current) {
        if (latest == null || current == null)
            return false;

        // Break versions into individual numerical pieces separated by periods
        int[] latestSplit = Arrays.stream(latest.replaceAll("[^0-9.]", "").split(Pattern.quote("."))).mapToInt(Integer::parseInt).toArray();
        int[] currentSplit = Arrays.stream(current.replaceAll("[^0-9.]", "").split(Pattern.quote("."))).mapToInt(Integer::parseInt).toArray();

        // Make sure both arrays are the same length
        if (latestSplit.length > currentSplit.length) {
            currentSplit = Arrays.copyOf(currentSplit, latestSplit.length);
        } else if (currentSplit.length > latestSplit.length) {
            latestSplit = Arrays.copyOf(latestSplit, currentSplit.length);
        }

        // Compare pieces from most significant to least significant
        for (int i = 0; i < latestSplit.length; i++) {
            if (latestSplit[i] > currentSplit[i]) {
                return true;
            } else if (currentSplit[i] > latestSplit[i]) {
                break;
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
     * Gets a primitive class as its wrapper counterpart
     *
     * @param clazz The class to get the wrapped class of
     * @return The wrapped class, or the same class if not primitive
     */
    public static Class<?> getPrimitiveAsWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ? PRIMITIVE_TO_WRAPPER.get(clazz) : clazz;
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
