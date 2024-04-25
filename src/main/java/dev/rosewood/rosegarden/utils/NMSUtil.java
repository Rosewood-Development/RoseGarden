package dev.rosewood.rosegarden.utils;

import org.bukkit.Bukkit;

public final class NMSUtil {

    private static final int VERSION_NUMBER;
    private static final int MINOR_VERSION_NUMBER;
    static {
        String bukkitVersion = Bukkit.getBukkitVersion();
        String[] parts = bukkitVersion.split("\\.");
        VERSION_NUMBER = Integer.parseInt(parts[0]);
        MINOR_VERSION_NUMBER = Integer.parseInt(parts[1]);
    }

    private NMSUtil() {

    }

    /**
     * @return the server version major release number
     */
    public static int getVersionNumber() {
        return VERSION_NUMBER;
    }

    /**
     * @return the server version minor release number
     */
    public static int getMinorVersionNumber() {
        return MINOR_VERSION_NUMBER;
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
