package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class OfflinePlayerArgumentHandler extends RoseCommandArgumentHandler<OfflinePlayer> {

    public OfflinePlayerArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, OfflinePlayer.class);
    }

    @Override
    protected OfflinePlayer handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        String input = argumentParser.next();
        OfflinePlayer offlinePlayer;
        if (NMSUtil.isPaper()) {
            offlinePlayer = Bukkit.getOfflinePlayerIfCached(input);
        } else {
            offlinePlayer = Bukkit.getOfflinePlayer(input);
        }

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore())
            throw new HandledArgumentException("argument-handler-player", StringPlaceholders.of("input", input));

        return offlinePlayer;
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
