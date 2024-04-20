package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SelectorPlayerArgumentHandler extends ArgumentHandler<Player> {

    protected SelectorPlayerArgumentHandler() {
        super(Player.class);
    }

    @Override
    public Player handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        if (input.startsWith("@")) {
            // Running a selector, try to find exactly one entity which must be a player
            List<Entity> entities;
            try {
                entities = Bukkit.selectEntities(context.getSender(), input);
            } catch (Exception e) {
                throw new HandledArgumentException("argument-handler-player-selector-syntax");
            }

            if (entities.isEmpty())
                throw new HandledArgumentException("argument-handler-player-selector-none");

            if (entities.size() > 1)
                throw new HandledArgumentException("argument-handler-player-selector-multiple");

            Entity selected = entities.get(0);
            if (!(selected instanceof Player player))
                throw new HandledArgumentException("argument-handler-player-selector-entity");

            return player;
        }

        Player player = Bukkit.getPlayer(input);
        if (player == null)
            throw new HandledArgumentException("argument-handler-player", StringPlaceholders.of("input", input));
        return player;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        List<String> suggestions = new ArrayList<>(List.of("@p", "@r"));
        suggestions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        return suggestions;
    }

}
