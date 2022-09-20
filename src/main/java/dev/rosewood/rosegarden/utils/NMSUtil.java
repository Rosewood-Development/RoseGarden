package dev.rosewood.rosegarden.utils;

import org.bukkit.Bukkit;

public final class NMSUtil {

    private static String cachedVersion = null;
    private static int cachedVersionNumber = -1;

    private NMSUtil() {

    }

    /**
     * @return The server version
     */
    public static String getVersion() {
        if (cachedVersion == null) {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            cachedVersion = name.substring(name.lastIndexOf('.') + 1);
        }
        return cachedVersion;
    }

    /**
     * @return the server version major release number
     */
    public static int getVersionNumber() {
        if (cachedVersionNumber == -1) {
            String name = getVersion().substring(3);
            cachedVersionNumber = Integer.parseInt(name.substring(0, name.length() - 3));
        }
        return cachedVersionNumber;
    }

    /**
     * @return true if the server is running Paper or a fork of Paper, false otherwise
     */
    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
