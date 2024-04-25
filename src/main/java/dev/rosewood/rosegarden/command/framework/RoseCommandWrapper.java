package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.CommandMapUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class RoseCommandWrapper extends BukkitCommand {

    private final String namespace;
    private final File dataFolder;
    private final RosePlugin rosePlugin;
    private final BaseRoseCommand command;

    public RoseCommandWrapper(RosePlugin rosePlugin, BaseRoseCommand command) {
        this(rosePlugin.getName().toLowerCase(), rosePlugin.getDataFolder(), rosePlugin, command);
    }

    public RoseCommandWrapper(String namespace, File dataFolder, RosePlugin rosePlugin, BaseRoseCommand command) {
        super("");

        this.namespace = namespace;
        this.dataFolder = dataFolder;
        this.rosePlugin = rosePlugin;
        this.command = command;
    }

    public void register() {
        // Register commands
        File commandsDirectory = new File(this.dataFolder, "commands");
        commandsDirectory.mkdirs();

        String commandName = this.command.getCommandInfo().name();
        File commandConfigFile = new File(commandsDirectory, commandName + ".yml");
        boolean exists = commandConfigFile.exists();
        CommentedFileConfiguration commandConfig = CommentedFileConfiguration.loadConfiguration(commandConfigFile);

        AtomicBoolean modified = new AtomicBoolean(false);
        if (!exists) {
            commandConfig.addComments("This file lets you change the name and aliases for the " + commandName + " command.",
                    "If you edit the name/aliases at the top of this file, you will need to restart the server to see all the changes applied properly.");
            modified.set(true);
        }

        // Write default config values if they don't exist
        if (!commandConfig.contains("name")) {
            commandConfig.set("name", commandName);
            modified.set(true);
        }

        // Write default alias values if they don't exist
        if (!commandConfig.contains("aliases")) {
            commandConfig.set("aliases", new ArrayList<>(this.command.getCommandInfo().aliases()));
            modified.set(true);
        }

        // Write subcommands
        this.writeSubcommands(commandConfig, this.command, modified);

        if (modified.get())
            commandConfig.save(commandConfigFile);

        // Load command config values
        this.command.setNameAndAliases(commandConfig.getString("name"), commandConfig.getStringList("aliases"));

        // Load subcommand config values
        this.loadSubCommands(commandConfig, this.command);

        // Finally, register the command with the server
        CommandMapUtils.registerCommand(this.namespace, this);
    }

    private void writeSubcommands(ConfigurationSection section, RoseCommand command, AtomicBoolean modified) {
        CommandExecutionWalker walker = new CommandExecutionWalker(command);
        while (walker.hasNext()) {
            walker.step((cmd, argument) -> true, argument -> {
                List<BaseRoseCommand> editableSubcommands = argument.subCommands().stream()
                        .filter(BaseRoseCommand.class::isInstance)
                        .map(BaseRoseCommand.class::cast)
                        .collect(Collectors.toList());

                if (editableSubcommands.isEmpty())
                    return null;

                ConfigurationSection subCommandsSection = section.getConfigurationSection("subcommands");
                if (subCommandsSection == null) {
                    subCommandsSection = section.createSection("subcommands");
                    modified.set(true);
                }

                for (BaseRoseCommand subCommand : editableSubcommands) {
                    ConfigurationSection subCommandSection = subCommandsSection.getConfigurationSection(subCommand.getCommandInfo().name());
                    if (subCommandSection == null) {
                        subCommandSection = subCommandsSection.createSection(subCommand.getCommandInfo().name());
                        modified.set(true);
                    }

                    if (!subCommandSection.contains("name")) {
                        subCommandSection.set("name", subCommand.getCommandInfo().name());
                        modified.set(true);
                    }

                    if (!subCommandSection.contains("aliases")) {
                        subCommandSection.set("aliases", new ArrayList<>(subCommand.getCommandInfo().aliases()));
                        modified.set(true);
                    }

                    this.writeSubcommands(subCommandSection, subCommand, modified);
                }

                return null;
            });
        }
    }

    private void loadSubCommands(ConfigurationSection section, BaseRoseCommand command) {
        CommandExecutionWalker walker = new CommandExecutionWalker(command);
        while (walker.hasNext()) {
            walker.step((cmd, argument) -> true, argument -> {
                List<BaseRoseCommand> editableSubcommands = argument.subCommands().stream()
                        .filter(BaseRoseCommand.class::isInstance)
                        .map(BaseRoseCommand.class::cast)
                        .collect(Collectors.toList());

                if (editableSubcommands.isEmpty())
                    return null;

                ConfigurationSection subCommandsSection = section.getConfigurationSection("subcommands");
                if (subCommandsSection == null)
                    return null;

                for (BaseRoseCommand subCommand : editableSubcommands) {
                    ConfigurationSection subCommandSection = subCommandsSection.getConfigurationSection(subCommand.getCommandInfo().name());
                    if (subCommandSection == null)
                        continue;

                    subCommand.setNameAndAliases(subCommandSection.getString("name"), subCommandSection.getStringList("aliases"));
                    this.loadSubCommands(subCommandSection, subCommand);
                }

                return null;
            });
        }
    }

    public void unregister() {
        CommandMapUtils.unregisterCommand(this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);
        if (this.command.isPlayerOnly() && !(sender instanceof Player)) {
            localeManager.sendCommandMessage(sender, "only-player");
            return true;
        }

        if (!this.command.canUse(sender)) {
            localeManager.sendCommandMessage(sender, "no-permission");
            return true;
        }

        CommandContext context = new CommandContext(sender, commandLabel, args);
        CommandContext readonlyContext = context.readonly();
        CommandExecutionWalker walker = new CommandExecutionWalker(this.command);
        InputIterator inputIterator = new InputIterator(Arrays.asList(args));

        while (walker.hasNext()) {
            if (!inputIterator.hasNext()) {
                List<Argument> remainingArguments = walker.walkRemaining();
                if (remainingArguments.stream().allMatch(Argument::optional))
                    break; // All remaining arguments are optional, this command execution is valid

                long missingRequired = remainingArguments.stream().filter(x -> !x.optional()).count();
                if (remainingArguments.stream().anyMatch(x -> x instanceof Argument.SubCommandArgument)) {
                    localeManager.sendCommandMessage(sender, "missing-arguments-extra", StringPlaceholders.of("amount", missingRequired));
                } else {
                    localeManager.sendCommandMessage(sender, "missing-arguments", StringPlaceholders.of("amount", missingRequired));
                }
                return true;
            }

            walker.step((command, argument) -> {
                // Skip the argument if the condition is not met, insert a null
                if (!argument.condition().test(context)) {
                    context.put(argument, null);
                    return true;
                }

                InputIterator beforeState = inputIterator.clone();
                try {
                    ArgumentHandler<?> handler = argument.handler();
                    Object parsedArgument = handler.handle(readonlyContext, argument, inputIterator);
                    if (parsedArgument == null) {
                        localeManager.sendCommandMessage(sender, "invalid-argument-null", StringPlaceholders.of("name", argument.name()));
                        return false;
                    }

                    context.put(argument, parsedArgument);
                    return true;
                } catch (ArgumentHandler.HandledArgumentException e) {
                    if (argument.optional() && walker.hasNextStep()) { // Skip if optional and we have more arguments, try the next argument instead and insert a null
                        inputIterator.restore(beforeState);
                        context.put(argument, null);
                        return true;
                    }

                    String message = localeManager.getCommandLocaleMessage(e.getMessage(), e.getPlaceholders());
                    localeManager.sendCommandMessage(sender, "invalid-argument", StringPlaceholders.of("message", message));
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    localeManager.sendCommandMessage(sender, "unknown-command-error");
                    return false;
                }
            }, argument -> {
                // Skip the argument if the condition is not met
                if (!argument.condition().test(context))
                    return null;

                if (inputIterator.hasNext()) {
                    String input = inputIterator.next();
                    RoseCommand match = argument.subCommands().stream()
                            .filter(subCommand -> Stream.concat(Stream.of(subCommand.getName()), subCommand.getAliases().stream()).anyMatch(s -> s.equalsIgnoreCase(input)))
                            .findFirst()
                            .orElse(null);
                    if (match == null)
                        localeManager.sendCommandMessage(sender, "invalid-subcommand");
                    return match;
                }
                localeManager.sendCommandMessage(sender, "invalid-subcommand");
                return null;
            });
        }

        if (walker.isCompleted()) {
            RoseCommand commandToExecute = walker.getCurrentCommand();
            if (!commandToExecute.canUse(sender)) {
                localeManager.sendCommandMessage(sender, "no-permission");
                return true;
            }

            commandToExecute.execute(context);
        } else {
            // TODO: Usage message
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String commandLabel, String[] args) {
        boolean isPlayer = sender instanceof Player;
        if (this.command.isPlayerOnly() && !isPlayer || !this.command.canUse(sender))
            return Collections.emptyList();

        CommandContext context = new CommandContext(sender, commandLabel, args);
        CommandContext readonlyContext = context.readonly();
        CommandExecutionWalker walker = new CommandExecutionWalker(this.command);
        InputIterator inputIterator = new InputIterator(Arrays.asList(args));

        List<String> suggestions = new ArrayList<>();
        while (walker.hasNext()) {
            walker.step((command, argument) -> {
                // Skip the argument if the condition is not met
                if (!argument.condition().test(context))
                    return true;

                if (!inputIterator.hasNext()) {
                    suggestions.addAll(argument.handler().suggest(readonlyContext, argument, new String[0]));
                    return argument.optional();
                }

                inputIterator.clearStack();
                InputIterator beforeState = inputIterator.clone();
                try {
                    ArgumentHandler<?> handler = argument.handler();
                    String input = inputIterator.peek();
                    if (input.isEmpty()) // Force into the catch block, empty input should never be valid
                        throw new ArgumentHandler.HandledArgumentException("");

                    Object parsedArgument = handler.handle(readonlyContext, argument, inputIterator);
                    if (parsedArgument == null || !inputIterator.hasNext())
                        return false;

                    context.put(argument, parsedArgument);
                    return true;
                } catch (ArgumentHandler.HandledArgumentException e) {
                    List<String> remainingInput = new ArrayList<>(inputIterator.getStack());
                    while (inputIterator.hasNext())
                        remainingInput.add(inputIterator.next());
                    String[] remainingArgs = remainingInput.toArray(new String[0]);
                    argument.handler().suggest(readonlyContext, argument, remainingArgs).stream()
                            .filter(x -> StringUtil.startsWithIgnoreCase(x, String.join(" ", remainingInput)))
                            .forEach(suggestions::add);

                    if (argument.optional() && walker.hasNextStep()) {
                        inputIterator.restore(beforeState);
                        return true;
                    }

                    return false;
                } catch (Exception e) {
                    return false;
                }
            }, argument -> {
                // Skip the argument if the condition is not met
                if (!argument.condition().test(context))
                    return null;

                if (!inputIterator.hasNext()) {
                    this.streamUsableSubCommands(argument, sender)
                            .flatMap(x -> Stream.concat(Stream.of(x.getName()), x.getAliases().stream()))
                            .forEach(suggestions::add);
                    return null;
                }

                String input = inputIterator.next();
                RoseCommand subCommand = this.streamUsableSubCommands(argument, sender)
                        .filter(x -> Stream.concat(Stream.of(x.getName()), x.getAliases().stream()).anyMatch(s -> s.equalsIgnoreCase(input)))
                        .findFirst()
                        .orElse(null);

                if (subCommand != null) {
                    if (!inputIterator.hasNext())
                        return null;
                    return subCommand;
                }

                this.streamUsableSubCommands(argument, sender)
                        .flatMap(x -> Stream.concat(Stream.of(x.getName()), x.getAliases().stream()))
                        .filter(x -> StringUtil.startsWithIgnoreCase(x, input))
                        .forEach(suggestions::add);

                return null;
            });
        }

        return suggestions;
    }

    private Stream<RoseCommand> streamUsableSubCommands(Argument.SubCommandArgument argument, CommandSender sender) {
        return argument.subCommands().stream()
                .filter(x -> x.canUse(sender))
                .filter(x -> !x.isPlayerOnly() || sender instanceof Player);
    }

    @Override
    public String getName() {
        return this.command.getName();
    }

    @Override
    public List<String> getAliases() {
        return this.command.getAliases();
    }

    @Override
    public String getPermission() {
        return this.command.getPermission();
    }

}
