package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class OfflinePlayerArgumentHandler extends RoseCommandArgumentHandler<OfflinePlayer> {

    private static Method getOfflinePlayerIfCachedMethod;
    static {
        try { // This is a Paper-only method, prefer this since it doesn't require a blocking UUID lookup
            getOfflinePlayerIfCachedMethod = Bukkit.class.getDeclaredMethod("getOfflinePlayerIfCached", String.class);
        } catch (ReflectiveOperationException ignored) { }
    }

    public OfflinePlayerArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, OfflinePlayer.class);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected OfflinePlayer handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        String input = argumentParser.next();
        if (getOfflinePlayerIfCachedMethod != null) {
            try {
                return (OfflinePlayer) getOfflinePlayerIfCachedMethod.invoke(null, input);
            } catch (ReflectiveOperationException ignored) { }
        }
        return Bukkit.getOfflinePlayer(input);
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
