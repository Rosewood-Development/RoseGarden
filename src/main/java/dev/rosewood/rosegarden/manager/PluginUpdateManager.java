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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginUpdateManager extends Manager implements Listener {

    private static final String[] SNAPSHOT_HEADER = {
            "================================================",
            " You are currently running a DEVELOPMENT BUILD!",
            " These types of builds are not meant to be run",
            " on a production server, and are not supported.",
            "================================================"
    };

    private String updateVersion;

    public PluginUpdateManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        Bukkit.getPluginManager().registerEvents(this, this.rosePlugin);
    }

    @Override
    public void reload() {
        File configFile = new File(this.rosePlugin.getRoseGardenDataFolder(), "config.yml");

        String currentVersion = this.rosePlugin.getDescription().getVersion();
        if (currentVersion.contains("-SNAPSHOT")) {
            for (String line : SNAPSHOT_HEADER)
                this.rosePlugin.getLogger().warning(line);
            return;
        }

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

                if (RoseGardenUtils.isUpdateAvailable(latestVersion, currentVersion)) {
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
