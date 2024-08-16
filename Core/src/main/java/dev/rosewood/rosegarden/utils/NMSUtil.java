package dev.rosewood.rosegarden.utils;

import org.bukkit.Bukkit;

public final class NMSUtil {

    private static final int VERSION_NUMBER;
    private static final int MINOR_VERSION_NUMBER;
    private static final boolean IS_PAPER;
    private static final boolean IS_FOLIA;
    static {
        String bukkitVersion = Bukkit.getBukkitVersion();
        String[] parts = bukkitVersion.split("-")[0].split("\\.");
        VERSION_NUMBER = Integer.parseInt(parts[1]);
        MINOR_VERSION_NUMBER = parts.length >= 3 ? Integer.parseInt(parts[2]) : 0;
        IS_PAPER = ClassUtils.checkClass("com.destroystokyo.paper.PaperConfig");
        IS_FOLIA = ClassUtils.checkClass("io.papermc.paper.threadedregions.RegionizedServer");
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
        return IS_PAPER;
    }

    /**
     * @return true if the server is running Folia, false otherwise
     */
    public static boolean isFolia() {
        return IS_FOLIA;
    }

}
