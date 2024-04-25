package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class OfflinePlayerArgumentHandler extends ArgumentHandler<OfflinePlayer> {

    protected OfflinePlayerArgumentHandler() {
        super(OfflinePlayer.class);
    }

    @Override
    public OfflinePlayer handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
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
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

}
