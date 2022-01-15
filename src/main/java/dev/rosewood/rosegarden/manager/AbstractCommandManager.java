package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.EnumArgumentHandler;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.command.framework.RoseSubCommand;
import dev.rosewood.rosegarden.utils.ClassUtils;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

@SuppressWarnings("rawtypes")
public abstract class AbstractCommandManager extends Manager implements TabExecutor {

    private static final String COMMAND_PACKAGE = "dev.rosewood.rosegarden.command.command";
    private static final String ARGUMENT_PACKAGE = "dev.rosewood.rosegarden.command.argument";

    private final Map<Class<? extends RoseCommandArgumentHandler>, RoseCommandArgumentHandler<?>> argumentHandlers;
    private final Map<String, RoseCommand> commandLookupMap;
    private final AbstractLocaleManager localeManager;

    public AbstractCommandManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.argumentHandlers = new HashMap<>();
        this.commandLookupMap = new HashMap<>();
        this.localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);

        PluginCommand command = this.rosePlugin.getCommand(this.getCommandName());
        if (command != null) {
            command.setExecutor(this);
        } else {
            throw new IllegalStateException("Failed to find command in plugin.yml named '" + this.getCommandName() + "'");
        }
    }

    @Override
    public final void reload() {
        try {
            // Load arguments
            List<Class<RoseCommandArgumentHandler>> argumentHandlerClasses = new ArrayList<>(ClassUtils.getClassesOf(this.rosePlugin, ARGUMENT_PACKAGE, RoseCommandArgumentHandler.class));
            this.getArgumentHandlerPackages().stream().map(x -> ClassUtils.getClassesOf(this.rosePlugin, x, RoseCommandArgumentHandler.class)).forEach(argumentHandlerClasses::addAll);

            for (Class<RoseCommandArgumentHandler> argumentHandlerClass : argumentHandlerClasses) {
                // Ignore abstract/interface classes
                if (Modifier.isAbstract(argumentHandlerClass.getModifiers()) || Modifier.isInterface(argumentHandlerClass.getModifiers()))
                    continue;

                RoseCommandArgumentHandler<?> argumentHandler = argumentHandlerClass.getConstructor(RosePlugin.class).newInstance(this.rosePlugin);
                this.argumentHandlers.put(argumentHandlerClass, argumentHandler);
            }

            // Load commands
            List<Class<RoseCommand>> commandClasses = new ArrayList<>(ClassUtils.getClassesOf(this.rosePlugin, COMMAND_PACKAGE, RoseCommand.class));
            this.getCommandPackages().stream().map(x -> ClassUtils.getClassesOf(this.rosePlugin, x, RoseCommand.class)).forEach(commandClasses::addAll);

            for (Class<RoseCommand> commandClass : commandClasses) {
                // Ignore abstract/interface classes
                if (Modifier.isAbstract(commandClass.getModifiers()) || Modifier.isInterface(commandClass.getModifiers()))
                    continue;

                // Subcommands get loaded within commands
                if (RoseSubCommand.class.isAssignableFrom(commandClass))
                    continue;

                RoseCommand command = commandClass.getConstructor(RosePlugin.class).newInstance(this.rosePlugin);
                this.commandLookupMap.put(command.getName().toLowerCase(), command);
                List<String> aliases = command.getAliases();
                if (aliases != null)
                    aliases.forEach(x -> this.commandLookupMap.put(x.toLowerCase(), command));
            }
        } catch (Exception e) {
            this.rosePlugin.getLogger().severe("Fatal error initializing commands");
            e.printStackTrace();
        }
    }

    @Override
    public final void disable() {
        this.argumentHandlers.clear();
        this.commandLookupMap.clear();
    }

    public RoseCommandArgumentHandler<?> resolveArgumentHandler(Class<?> handledParameterClass) {
        if (Enum.class.isAssignableFrom(handledParameterClass))
            return this.argumentHandlers.get(EnumArgumentHandler.class);

        // Map primitive types to their wrapper handlers
        if (handledParameterClass.isPrimitive())
            handledParameterClass = RoseGardenUtils.getPrimitiveAsWrapper(handledParameterClass);

        Class<?> finalHandledParameterClass = handledParameterClass;
        return this.argumentHandlers.values()
                .stream()
                .filter(x -> x.getHandledType() != null && x.getHandledType() == finalHandledParameterClass)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tried to resolve a RoseCommandArgumentHandler for an unhandled type"));
    }

    public RoseCommand getCommand(String commandName) {
        return this.commandLookupMap.get(commandName);
    }

    public List<RoseCommand> getCommands() {
        return this.commandLookupMap.values().stream()
                .distinct()
                .sorted(Comparator.comparing(RoseCommand::getName))
                .collect(Collectors.toList());
    }

    public RoseSubCommand getSubCommand(RoseCommand command, String commandName) {
        return command.getSubCommands().get(commandName.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (args.length == 0) {
                String baseColor = this.localeManager.getLocaleMessage("base-command-color");
                this.localeManager.sendCustomMessage(sender, baseColor + "Running <g:#8A2387:#E94057:#F27121>" + this.rosePlugin.getDescription().getName() + baseColor + " v" + this.rosePlugin.getDescription().getVersion());
                this.localeManager.sendCustomMessage(sender, baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + this.rosePlugin.getDescription().getAuthors().get(0));
                this.localeManager.sendSimpleMessage(sender, "base-command-help");
                return true;
            }

            RoseCommand command = this.getCommand(args[0]);
            if (command == null) {
                this.localeManager.sendCommandMessage(sender, "unknown-command");
                return true;
            }

            String[] cmdArgs = new String[args.length - 1];
            System.arraycopy(args, 1, cmdArgs, 0, cmdArgs.length);
            CommandContext context = new CommandContext(sender, cmdArgs);
            ArgumentParser argumentParser = new ArgumentParser(context, new LinkedList<>(Arrays.asList(cmdArgs)));

            this.runCommand(sender, command, argumentParser, new ArrayList<>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            this.localeManager.sendCommandMessage(sender, "unknown-command-error");
        }
        return true;
    }

    private void runCommand(CommandSender sender, RoseCommand command, ArgumentParser argumentParser, List<Object> parsedArgs, int commandLayer) throws ReflectiveOperationException {
        if (!command.canUse(sender)) {
            this.localeManager.sendCommandMessage(sender, "no-permission");
            return;
        }

        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            this.localeManager.sendCommandMessage(sender, "only-player");
            return;
        }

        // Start parsing parameters based on the command requirements, print errors out as we go
        for (RoseCommandArgumentInfo argumentInfo : command.getArgumentInfo()) {
            if (!argumentParser.hasNext()) {
                // All other arguments are optional, this is fine
                if (argumentInfo.isOptional())
                    break;

                // Ran out of arguments while parsing
                if (command.hasSubCommand()) {
                    this.localeManager.sendCommandMessage(sender, "missing-arguments-extra", StringPlaceholders.single("amount", command.getNumRequiredArguments()));
                } else {
                    this.localeManager.sendCommandMessage(sender, "missing-arguments", StringPlaceholders.single("amount", parsedArgs.size() + command.getNumRequiredArguments() + commandLayer));
                }
                return;
            }

            if (argumentInfo.isSubCommand()) {
                RoseSubCommand subCommand = this.getSubCommand(command, argumentParser.next());
                if (subCommand == null) {
                    this.localeManager.sendCommandMessage(sender, "invalid-subcommand");
                    return;
                }

                this.runCommand(sender, subCommand, argumentParser, parsedArgs, commandLayer + 1);
                return;
            }

            try {
                Object parsedArgument = this.resolveArgumentHandler(argumentInfo.getType()).handle(argumentInfo, argumentParser);
                if (parsedArgument == null) {
                    this.localeManager.sendCommandMessage(sender, "invalid-argument-null", StringPlaceholders.single("name", argumentInfo.toString()));
                    return;
                }

                parsedArgs.add(parsedArgument);
            } catch (RoseCommandArgumentHandler.HandledArgumentException e) {
                String message = this.localeManager.getCommandLocaleMessage(e.getMessage(), e.getPlaceholders());
                this.localeManager.sendCommandMessage(sender, "invalid-argument", StringPlaceholders.single("message", message));
                return;
            }
        }

        this.executeCommand(argumentParser.getContext(), command, parsedArgs);
    }

    private void executeCommand(CommandContext context, RoseCommand command, List<Object> parsedArgs) throws ReflectiveOperationException {
        Stream.Builder<Object> argumentBuilder = Stream.builder().add(context);
        parsedArgs.forEach(argumentBuilder::add);

        // Fill optional parameters with nulls
        for (int i = parsedArgs.size(); i < command.getNumParameters(); i++)
            argumentBuilder.add(null);

        command.getExecuteMethod().invoke(command, argumentBuilder.build().toArray());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0)
            return new ArrayList<>(this.commandLookupMap.keySet());

        if (args.length <= 1)
            return this.commandLookupMap.keySet().stream()
                    .filter(x -> StringUtil.startsWithIgnoreCase(x, args[args.length - 1]))
                    .collect(Collectors.toList());

        RoseCommand command = this.getCommand(args[0]);
        if (command == null)
            return Collections.emptyList();

        String[] cmdArgs = new String[args.length - 1];
        System.arraycopy(args, 1, cmdArgs, 0, cmdArgs.length);
        CommandContext context = new CommandContext(sender, cmdArgs);
        ArgumentParser argumentParser = new ArgumentParser(context, new LinkedList<>(Arrays.asList(cmdArgs)));

        return this.tabCompleteCommand(sender, command, argumentParser);
    }

    private List<String> tabCompleteCommand(CommandSender sender, RoseCommand command, ArgumentParser argumentParser) {
        if (!command.canUse(sender) || (command.isPlayerOnly() && !(sender instanceof Player)))
            return Collections.emptyList();

        // Consume all arguments until there are no more, then print those results
        for (RoseCommandArgumentInfo argumentInfo : command.getArgumentInfo()) {
            if (argumentInfo.isSubCommand()) {
                if (!argumentParser.hasNext())
                    return new ArrayList<>(command.getSubCommands().keySet());

                String input = argumentParser.next();
                RoseSubCommand subCommand = this.getSubCommand(command, input);
                if (subCommand == null)
                    return command.getSubCommands().keySet()
                            .stream()
                            .filter(x -> StringUtil.startsWithIgnoreCase(x, input))
                            .collect(Collectors.toList());

                if (argumentParser.hasNext())
                    return this.tabCompleteCommand(sender, subCommand, argumentParser);

                return Collections.emptyList();
            }

            List<String> suggestions = this.resolveArgumentHandler(argumentInfo.getType()).suggest(argumentInfo, argumentParser);
            String input = argumentParser.previous();
            if (!argumentParser.hasNext())
                return suggestions.stream()
                        .filter(x -> StringUtil.startsWithIgnoreCase(x, input))
                        .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public abstract List<String> getCommandPackages();

    public abstract List<String> getArgumentHandlerPackages();

    public abstract String getCommandName();

    public abstract List<String> getCommandAliases();

}
