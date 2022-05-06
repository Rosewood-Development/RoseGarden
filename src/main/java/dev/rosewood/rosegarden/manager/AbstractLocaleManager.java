package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandMessages;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.locale.Locale;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractLocaleManager extends Manager {

    protected CommentedFileConfiguration locale;

    public AbstractLocaleManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    /**
     * Creates a .lang file if one doesn't exist
     * Cross merges values between files into the .lang file, the .lang values take priority
     *
     * @param locale The Locale to register
     */
    private void registerLocale(Locale locale) {
        File file = new File(this.rosePlugin.getDataFolder() + "/locale", locale.getLocaleName() + ".lang");
        boolean newFile = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                newFile = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean changed = false;
        CommentedFileConfiguration configuration = CommentedFileConfiguration.loadConfiguration(file);
        if (newFile) {
            configuration.addComments(locale.getLocaleName() + " translation by " + locale.getTranslatorName());
            Map<String, Object> defaultLocaleStrings = locale.getDefaultLocaleValues();
            for (String key : defaultLocaleStrings.keySet()) {
                Object value = defaultLocaleStrings.get(key);
                if (key.startsWith("#")) {
                    configuration.addComments((String) value);
                } else {
                    configuration.set(key, value);
                }
            }
            changed = true;
        } else {
            Map<String, Object> defaultLocaleStrings = locale.getDefaultLocaleValues();
            for (String key : defaultLocaleStrings.keySet()) {
                if (key.startsWith("#"))
                    continue;

                Object value = defaultLocaleStrings.get(key);
                if (!configuration.contains(key)) {
                    configuration.set(key, value);
                    changed = true;
                }
            }
        }

        if (changed)
            configuration.save();
    }

    @Override
    public final void reload() {
        File localeDirectory = new File(this.rosePlugin.getDataFolder(), "locale");
        if (!localeDirectory.exists())
            localeDirectory.mkdirs();

        this.getLocales().forEach(this::registerLocale);

        String locale;
        if (this.rosePlugin.hasConfigurationManager()) {
            locale = this.rosePlugin.getManager(AbstractConfigurationManager.class).getSettings().get("locale").getString();
        } else {
            locale = "en_US";
        }

        File targetLocaleFile = new File(this.rosePlugin.getDataFolder() + "/locale", locale + ".lang");
        if (!targetLocaleFile.exists()) {
            targetLocaleFile = new File(this.rosePlugin.getDataFolder() + "/locale", "en_US.lang");
            this.rosePlugin.getLogger().severe("File " + targetLocaleFile.getName() + " does not exist. Defaulting to en_US.lang");
        }

        this.locale = CommentedFileConfiguration.loadConfiguration(targetLocaleFile);
    }

    @Override
    public final void disable() {

    }

    public abstract List<Locale> getLocales();

    /**
     * Gets a locale message
     *
     * @param messageKey The key of the message to get
     * @return The locale message
     */
    public final String getLocaleMessage(String messageKey) {
        return this.getLocaleMessage(messageKey, StringPlaceholders.empty());
    }

    /**
     * Gets a locale message with the given placeholders applied
     *
     * @param messageKey The key of the message to get
     * @param stringPlaceholders The placeholders to apply
     * @return The locale message with the given placeholders applied
     */
    public final String getLocaleMessage(String messageKey, StringPlaceholders stringPlaceholders) {
        String message = this.locale.getString(messageKey);
        if (message == null)
            return ChatColor.RED + "Missing message in locale file: " + messageKey;
        return HexUtils.colorify(stringPlaceholders.apply(message));
    }

    /**
     * Gets a locale message, falling back to the default command messages if none found
     *
     * @param messageKey The key of the message to get
     * @return The locale message
     */
    public final String getCommandLocaleMessage(String messageKey) {
        return this.getCommandLocaleMessage(messageKey, StringPlaceholders.empty());
    }

    /**
     * Gets a locale message with the given placeholders applied, falling back to the default command messages if none found
     *
     * @param messageKey The key of the message to get
     * @param stringPlaceholders The placeholders to apply
     * @return The locale message with the given placeholders applied
     */
    public final String getCommandLocaleMessage(String messageKey, StringPlaceholders stringPlaceholders) {
        String message = this.locale.getString(messageKey);
        if (message == null)
            message = CommandMessages.DEFAULT_MESSAGES.get(messageKey);
        if (message == null)
            return ChatColor.RED + "Missing message key in command messages: " + messageKey;
        return HexUtils.colorify(stringPlaceholders.apply(message));
    }

    /**
     * Sends a message to a CommandSender with the prefix with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public final void sendMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendParsedMessage(sender, this.getLocaleMessage("prefix") + this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender with the prefix
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public final void sendMessage(CommandSender sender, String messageKey) {
        this.sendMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with the prefix with placeholders applied, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public final void sendCommandMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendParsedMessage(sender, this.getLocaleMessage("prefix") + this.getCommandLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender with the prefix, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public final void sendCommandMessage(CommandSender sender, String messageKey) {
        this.sendCommandMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public final void sendSimpleMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendParsedMessage(sender, this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public final void sendSimpleMessage(CommandSender sender, String messageKey) {
        this.sendSimpleMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with placeholders applied, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public final void sendSimpleCommandMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendParsedMessage(sender, this.getCommandLocaleMessage(messageKey, stringPlaceholders));
    }

    public final void sendSimpleCommandMessage(CommandSender sender, String messageKey) {
        this.sendSimpleCommandMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a custom message to a CommandSender
     *
     * @param sender The CommandSender to send to
     * @param message The message to send
     */
    public final void sendCustomMessage(CommandSender sender, String message) {
        this.sendParsedMessage(sender, message);
    }

    /**
     * Replaces PlaceholderAPI placeholders if PlaceholderAPI is enabled
     *
     * @param sender The potential Player to replace with
     * @param message The message
     * @return A placeholder-replaced message
     */
    protected final String parsePlaceholders(CommandSender sender, String message) {
        if (sender instanceof Player)
            return PlaceholderAPIHook.applyPlaceholders((Player) sender, message);
        return message;
    }

    /**
     * Sends a message with placeholders and colors parsed to a CommandSender
     *
     * @param sender The sender to send the message to
     * @param message The message
     */
    protected final void sendParsedMessage(CommandSender sender, String message) {
        if (!message.isEmpty())
            HexUtils.sendMessage(sender, this.parsePlaceholders(sender, message));
    }

}
