package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
        if (this.rosePlugin.getSpigotId() == -1 || this.rosePlugin.isSnapshot())
            return;

        File configFile = new File(this.rosePlugin.getRoseGardenDataFolder(), "config.yml");

        boolean firstLoad = false;
        CommentedFileConfiguration configuration = CommentedFileConfiguration.loadConfiguration(configFile);
        if (!configuration.contains("check-updates")) {
            configuration.set("check-updates", true, "Should all plugins running RoseGarden check for updates?", "RoseGarden is a core library created by Rosewood Development", "Plugin update messages will be sent to players with the 'rosegarden.updates' permission");
            configuration.save(configFile);
            firstLoad = true;
        }

        if (firstLoad || !configuration.getBoolean("check-updates"))
            return;

        // Check for updates
        String currentVersion = this.rosePlugin.getDescription().getVersion().toLowerCase();
        this.rosePlugin.getScheduler().runTaskAsync(() -> this.checkForUpdate(currentVersion));
    }

    private void checkForUpdate(String currentVersion) {
        try {
            String latestVersion = this.getLatestVersion();

            if (RoseGardenUtils.isUpdateAvailable(latestVersion, currentVersion)) {
                this.updateVersion = latestVersion;
                this.rosePlugin.getLogger().info("An update for " + this.rosePlugin.getName() + " (v" + this.updateVersion + ") is available! You are running v" + currentVersion + ".");
            }
        } catch (Exception e) {
            this.rosePlugin.getLogger().warning("An error occurred checking for an update. There is either no internet connection or the Spigot API could not be reached.");
        }
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
        if (this.updateVersion == null || !player.hasPermission("rosegarden.updates"))
            return;

        String website = this.rosePlugin.getDescription().getWebsite();
        String updateMessage = "&eAn update for " + RoseGardenUtils.GRADIENT +
                this.rosePlugin.getName() + " &e(&b%new%&e) is available! You are running &b%current%&e." +
                (website != null ? " " + website : "");

        StringPlaceholders placeholders = StringPlaceholders.of("new", this.updateVersion, "current", this.rosePlugin.getDescription().getVersion());

        RoseGardenUtils.sendMessage(player, updateMessage, placeholders);
    }

}
