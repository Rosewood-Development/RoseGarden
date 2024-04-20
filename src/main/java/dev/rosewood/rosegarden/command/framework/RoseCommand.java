package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.permissions.Permissible;

public interface RoseCommand {

    /**
     * The execute method to be called after the {@link CommandContext} has been fully populated with arguments
     * based on the {@link ArgumentsDefinition} returned from {@link #getCommandArguments()}.
     *
     * @param context the CommandContext populated with arguments
     */
    void execute(CommandContext context);

    /**
     * @return the name of the command
     */
    String getName();

    /**
     * @return the aliases of the command
     */
    List<String> getAliases();

    /**
     * @return the permission required to use the command
     */
    String getPermission();

    /**
     * @return true if the command can only be used by players, false otherwise
     */
    boolean isPlayerOnly();

    /**
     * @return the description key of the command
     */
    String getDescriptionKey();

    /**
     * @return the {@link ArgumentsDefinition} of the command
     */
    ArgumentsDefinition getCommandArguments();

    /**
     * @return a displayable output of this command's parameters
     */
    default String getParametersString(CommandContext context) {
        StringBuilder stringBuilder = new StringBuilder();
        ArgumentsDefinition argumentsDefinition = this.getCommandArguments();
        for (int i = 0; i < argumentsDefinition.size(); i++) {
            Argument argument = argumentsDefinition.get(i);
            if (!argument.condition().test(context))
                continue;

            if (i > 0)
                stringBuilder.append(' ');
            stringBuilder.append(argument.parameter());
        }
        return stringBuilder.toString();
    }

    /**
     * @return the methods annotated with {@link RoseExecutable}
     */
    default List<Method> getExecuteMethods() {
        return Stream.of(this.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(RoseExecutable.class))
                .toList();
    }

    /**
     * @return true if the permissible can use the command, false otherwise
     */
    default boolean canUse(Permissible permissible) {
        String permission = this.getPermission();
        return permission == null || permissible.hasPermission(permission);
    }

}
