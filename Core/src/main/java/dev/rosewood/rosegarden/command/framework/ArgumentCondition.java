package dev.rosewood.rosegarden.command.framework;

import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface ArgumentCondition extends Predicate<CommandContext> {

    ArgumentCondition IS_PLAYER = context -> context.getSender() instanceof Player;
    ArgumentCondition IS_CONSOLE = context -> context.getSender().equals(Bukkit.getConsoleSender());

    static ArgumentCondition hasPermission(String permission) {
        return context -> context.getSender().hasPermission(permission);
    }

}
