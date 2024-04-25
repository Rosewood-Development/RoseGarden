package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FilteredPlayerArgumentHandler extends ArgumentHandler<Player> {

    private final Predicate<Player> filter;

    protected FilteredPlayerArgumentHandler(Predicate<Player> filter) {
        super(Player.class);
        this.filter = filter;
    }

    @Override
    public Player handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        Player player = Bukkit.getPlayer(input);
        if (player == null)
            throw new HandledArgumentException("argument-handler-player", StringPlaceholders.of("input", input));
        return player;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(this.filter)
                .map(Player::getName)
                .collect(Collectors.toList());
    }

}
