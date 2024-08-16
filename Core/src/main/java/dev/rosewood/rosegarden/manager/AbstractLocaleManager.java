package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandMessages;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.RoseSettingSerializers;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.locale.Locale;
import dev.rosewood.rosegarden.locale.YamlFileLocale;
import dev.rosewood.rosegarden.locale.provider.JarResourceLocaleProvider;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLocaleManager extends Manager {

    public static final class SettingKey {
        private static final List<RoseSetting<?>> KEYS = new ArrayList<>();

        public static final RoseSetting<String> LOCALE = create(RoseSetting.of("locale", RoseSettingSerializers.STRING, "en_US", "The locale to use in the /locale folder"));

        private static <T> RoseSetting<T> create(RoseSetting<T> setting) {
            KEYS.add(setting);
            return setting;
        }

        public static List<RoseSetting<?>> getKeys() {
            return Collections.unmodifiableList(KEYS);
        }

        private SettingKey() {}
    }

    protected final File localeDirectory;
    protected Locale defaultLocale;
    protected Locale loadedLocale;

    public AbstractLocaleManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.localeDirectory = new File(this.rosePlugin.getDataFolder(), "locale");
    }

    @SuppressWarnings("unchecked")
    private void registerLocales(Collection<Locale> locales) {
        // Inject {{EXAMPLE}} placeholders into the locale files
        StringPlaceholders.Builder placeholdersBuilder = StringPlaceholders.builder().delimiters("{{", "}}");
        this.injectPlaceholderConstants(placeholdersBuilder);
        StringPlaceholders placeholders = placeholdersBuilder.build();
        for (Locale locale : locales) {
            for (Map.Entry<String, Object> entry : locale.getLocaleValues().entrySet()) {
                if (entry.getValue() instanceof String) {
                    String value = (String) entry.getValue();
                    locale.getLocaleValues().put(entry.getKey(), placeholders.apply(value));
                } else if (entry.getValue() instanceof List) {
                    List<String> list = (List<String>) entry.getValue();
                    list.replaceAll(placeholders::apply);
                    locale.getLocaleValues().put(entry.getKey(), list);
                }
            }
        }

        Optional<Locale> defaultLocaleOptional = locales.stream()
                .filter(x -> x.getLocaleName().equals("en_US"))
                .findFirst();

        if (defaultLocaleOptional.isPresent()) {
            this.defaultLocale = defaultLocaleOptional.get();
        } else {
            this.rosePlugin.getLogger().warning("No default 'locale/en_US.yml' locale found!");
            this.defaultLocale = new Locale() {
                public String getLocaleName() { return "none"; }
                public Map<String, Object> getLocaleValues() { return new LinkedHashMap<>(); }
            };
        }

        if (!this.localeDirectory.exists())
            this.localeDirectory.mkdirs();

        // Transform any .lang files to .yml files
        File[] files = this.localeDirectory.listFiles();
        if (files != null) {
            int migrated = 0;
            for (File file : files) {
                if (!file.getName().endsWith(".lang"))
                    continue;

                File newFile = new File(this.localeDirectory, file.getName().replace(".lang", ".yml"));
                file.renameTo(newFile);
                migrated++;
            }

            if (migrated > 0)
                this.rosePlugin.getLogger().info("Migrated " + migrated + " locale files to the new .yml format");
        }

        locales.forEach(this::registerLocale);

        // Find the desired locale file in the locale directory, allow both .lang and .yml file extensions
        String localeName = this.rosePlugin.getRoseConfig().get(SettingKey.LOCALE);
        File localeFile = new File(this.localeDirectory, localeName + ".yml");
        if (localeFile.exists()) {
            this.loadedLocale = new YamlFileLocale(localeFile);
        } else {
            this.rosePlugin.getLogger().warning("Locale file '" + localeFile.getName() + "' not found, using default locale");
            this.loadedLocale = this.defaultLocale;
        }
    }

    /**
     * Creates a locale file if one doesn't exist
     * Cross merges values between files into the locale file, the locale values take priority
     *
     * @param locale The Locale to register
     */
    private void registerLocale(Locale locale) {
        File file = new File(this.rosePlugin.getDataFolder() + "/locale", locale.getLocaleName() + ".yml");
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
        Map<String, Object> defaultLocaleStrings = locale.getLocaleValues();

        // Write new locale values that are missing
        // If the file is new, also write the comments
        for (String key : defaultLocaleStrings.keySet()) {
            Object value = defaultLocaleStrings.get(key);
            if (newFile && key.startsWith(CommentedFileConfiguration.COMMENT_KEY_PREFIX)) {
                configuration.addComments(((String) value).substring(1));
                changed = true;
            } else if (!configuration.contains(key)) {
                configuration.set(key, value);
                changed = true;
            }
        }

        if (changed)
            configuration.save(file);
    }

    @Override
    public void reload() {
        this.registerLocales(this.getJarResourceLocales());
    }

    @Override
    public void disable() {

    }

    protected Collection<Locale> getJarResourceLocales() {
        return new JarResourceLocaleProvider("locale").getLocales();
    }

    protected void injectPlaceholderConstants(StringPlaceholders.Builder builder) {
        builder.add("PLUGIN_NAME", this.rosePlugin.getName());
    }

    /**
     * Handles sending a message, can be edited to add additional functionality
     *
     * @param sender The CommandSender to send the message to
     * @param message The fully parsed message to send
     */
    protected void handleMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    @NotNull
    protected String getLocaleString(String key) {
        Object value = this.loadedLocale.getLocaleValues().get(key);
        if (!(value instanceof String))
            value = this.defaultLocale.getLocaleValues().get(key);
        if (!(value instanceof String))
            value = this.getErrorMessage(key);
        return (String) value;
    }

    /**
     * Gets a locale message
     *
     * @param messageKey The key of the message to get
     * @return The locale message
     */
    public String getLocaleMessage(String messageKey) {
        return this.getLocaleMessage(messageKey, StringPlaceholders.empty());
    }

    /**
     * Gets a locale message with the given placeholders applied
     *
     * @param messageKey The key of the message to get
     * @param stringPlaceholders The placeholders to apply
     * @return The locale message with the given placeholders applied
     */
    public String getLocaleMessage(String messageKey, StringPlaceholders stringPlaceholders) {
        return HexUtils.colorify(stringPlaceholders.apply(this.getLocaleString(messageKey)));
    }

    /**
     * Gets a locale message, falling back to the default command messages if none found
     *
     * @param messageKey The key of the message to get
     * @return The locale message
     */
    public String getCommandLocaleMessage(String messageKey) {
        return this.getCommandLocaleMessage(messageKey, StringPlaceholders.empty());
    }

    /**
     * Gets a locale message with the given placeholders applied, falling back to the default command messages if none found
     *
     * @param messageKey The key of the message to get
     * @param stringPlaceholders The placeholders to apply
     * @return The locale message with the given placeholders applied
     */
    public String getCommandLocaleMessage(String messageKey, StringPlaceholders stringPlaceholders) {
        String message;
        if (this.loadedLocale.getLocaleValues().containsKey(messageKey)) {
            message = this.getLocaleMessage(messageKey, stringPlaceholders);
        } else {
            message = CommandMessages.DEFAULT_MESSAGES.get(messageKey);
            if (message == null)
                message = this.getErrorMessage(messageKey);
        }
        return HexUtils.colorify(stringPlaceholders.apply(message));
    }

    /**
     * Sends a message to a CommandSender with the prefix with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        String prefix = this.getLocaleMessage("prefix");
        String message = this.getLocaleMessage(messageKey, stringPlaceholders);
        this.sendParsedMessage(sender, prefix + message);
    }

    /**
     * Sends a message to a CommandSender with the prefix
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendMessage(CommandSender sender, String messageKey) {
        this.sendMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with the prefix with placeholders applied, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendCommandMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        String prefix = this.getLocaleMessage("prefix");
        String message = this.getCommandLocaleMessage(messageKey, stringPlaceholders);
        this.sendUnparsedMessage(sender, prefix + message);
    }

    /**
     * Sends a message to a CommandSender with the prefix, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendCommandMessage(CommandSender sender, String messageKey) {
        this.sendCommandMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendSimpleMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendParsedMessage(sender, this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendSimpleMessage(CommandSender sender, String messageKey) {
        this.sendSimpleMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with placeholders applied, falling back to the default command messages if none found
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendSimpleCommandMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendUnparsedMessage(sender, this.getCommandLocaleMessage(messageKey, stringPlaceholders));
    }

    public void sendSimpleCommandMessage(CommandSender sender, String messageKey) {
        this.sendSimpleCommandMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a custom message to a CommandSender
     *
     * @param sender The CommandSender to send to
     * @param message The message to send
     */
    public void sendCustomMessage(CommandSender sender, String message) {
        this.sendParsedMessage(sender, message);
    }

    /**
     * Replaces PlaceholderAPI placeholders if PlaceholderAPI is enabled
     *
     * @param sender The potential Player to replace with
     * @param message The message
     * @return A placeholder-replaced message
     */
    protected String parsePlaceholders(CommandSender sender, String message) {
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
    protected void sendParsedMessage(CommandSender sender, String message) {
        if (message.isEmpty())
            return;

        String parsedMessage = HexUtils.colorify(this.parsePlaceholders(sender, message));
        this.handleMessage(sender, parsedMessage);
    }

    /**
     * Sends a message with only colors parsed to a CommandSender
     *
     * @param sender The sender to send the message to
     * @param message The message
     */
    protected void sendUnparsedMessage(CommandSender sender, String message) {
        if (message.isEmpty())
            return;

        String parsedMessage = HexUtils.colorify(message);
        this.handleMessage(sender, parsedMessage);
    }

    protected String getErrorMessage(String messageKey) {
        new LocaleException(messageKey).printStackTrace();
        return "&cMissing locale string: " + messageKey;
    }

    public static class LocaleException extends RuntimeException {

        public LocaleException(String messageKey) {
            super(String.format("Missing locale string: %s", messageKey));
        }

    }

}
