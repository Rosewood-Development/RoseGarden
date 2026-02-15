package dev.rosewood.rosegarden.utils;

import org.bukkit.Bukkit;

public final class NMSUtil {

    private static int VERSION_NUMBER;
    private static int MINOR_VERSION_NUMBER;
    private static int PATCH_VERSION_NUMBER;
    private static boolean IS_PAPER;
    private static boolean IS_FOLIA;

    static {
        initialize();
    }

    static void initialize() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        String[] parts = bukkitVersion.split("-")[0].split("\\.");
        int part1 = Integer.parseInt(parts[0]);
        int part2 = Integer.parseInt(parts[1]);
        int part3 = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
        boolean yearBasedVersioning = part1 > 1;
        VERSION_NUMBER = yearBasedVersioning ? part1 : part2;
        MINOR_VERSION_NUMBER = yearBasedVersioning ? part2 : parts.length >= 3 ? part3 : 0;
        PATCH_VERSION_NUMBER = yearBasedVersioning ? part3 : 0;
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
     * @return the server version patch release number, will be 0 for non-year based versioning
     */
    public static int getPatchVersionNumber() {
        return PATCH_VERSION_NUMBER;
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
