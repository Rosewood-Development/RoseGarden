package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginUpdateManager extends Manager implements Listener {

    private String updateVersion;

    public PluginUpdateManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        Bukkit.getPluginManager().registerEvents(this, this.rosePlugin);
    }

    @Override
    public void reload() {
        File configDir = new File(this.rosePlugin.getDataFolder().getParentFile(), "RoseGarden");
        if (!configDir.exists())
            configDir.mkdirs();

        File configFile = new File(configDir, "config.yml");

        CommentedFileConfiguration configuration = CommentedFileConfiguration.loadConfiguration(configFile);
        if (!configuration.contains("check-updates")) {
            configuration.set("check-updates", true, "Should all plugins running RoseGarden check for updates?", "RoseGarden is a core library created by Rosewood Development");
            configuration.save();
        }

        if (!configuration.getBoolean("check-updates") || this.rosePlugin.getSpigotId() == -1)
            return;

        // Check for updates
        Bukkit.getScheduler().runTaskAsynchronously(this.rosePlugin, () -> {
            try {
                String latestVersion = this.getLatestVersion();
                String currentVersion = this.rosePlugin.getDescription().getVersion();

                if (this.isUpdateAvailable(latestVersion, currentVersion)) {
                    this.updateVersion = latestVersion;
                    RoseGardenUtils.getLogger().info("An update for " + this.rosePlugin.getName() + " (v" + this.updateVersion + ") is available! You are running v" + currentVersion + ".");
                }
            } catch (Exception e) {
                RoseGardenUtils.getLogger().warning("An error occurred checking for an update. There is either no established internet connection or the Spigot API is down.");
            }
        });
    }

    @Override
    public void disable() {

    }

    /**
     * Checks if there is an update available
     *
     * @param latest The latest version of the plugin from Spigot
     * @param current The currently installed version of the plugin
     * @return true if available, otherwise false
     */
    private boolean isUpdateAvailable(String latest, String current) {
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
     * Gets the latest version of the plugin from the Spigot Web API
     *
     * @return the latest version of the plugin from Spigot
     * @throws IOException if a network error occurs
     */
    private String getLatestVersion() throws IOException {
        URL spigot = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.rosePlugin.getSpigotId());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(spigot.openStream()))) {
            return reader.readLine();
        }
    }

    /**
     * @return the version of the latest update of this plugin, or null if there is none
     */
    public String getUpdateVersion() {
        return this.updateVersion;
    }

    /**
     * Called when a player joins and notifies ops if an update is available
     *
     * @param event The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.updateVersion == null || !player.isOp())
            return;

        String website = this.rosePlugin.getDescription().getWebsite();
        String updateMessage = "&eAn update for <g:#8A2387:#E94057:#F27121>" +
                this.rosePlugin.getName() + " &e(&bv%new%&e) is available! You are running &bv%current%&e." +
                (website != null ? " " + website : "");

        StringPlaceholders placeholders = StringPlaceholders.builder("new", this.updateVersion)
                .addPlaceholder("current", this.rosePlugin.getDescription().getVersion())
                .build();

        RoseGardenUtils.sendMessage(player, updateMessage, placeholders);
    }

}
