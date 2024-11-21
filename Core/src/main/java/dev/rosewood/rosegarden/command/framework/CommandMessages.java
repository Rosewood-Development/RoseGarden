package dev.rosewood.rosegarden.command.framework;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandMessages {

    public static final Map<String, String> DEFAULT_MESSAGES = new LinkedHashMap<String, String>() {{
        // Generic Command Messages
        this.put("no-permission", "&cYou don't have permission for that!");
        this.put("only-player", "&cThis command can only be executed by a player.");
        this.put("only-console", "&cThis command can only be executed through the console.");
        this.put("unknown-command", "&cUnknown command, use &b/%cmd% help &cfor more info.");
        this.put("unknown-command-error", "&cAn unknown error occurred; details have been printed to console. Please contact a server administrator.");
        this.put("invalid-subcommand", "&cInvalid subcommand.");
        this.put("invalid-argument", "&cInvalid argument: %message%.");
        this.put("command-usage", "&cUsage: &b/%cmd% %args%");

        // Base Command Message
        this.put("base-command-color", "&e");
        this.put("base-command-help", "&eUse &b/%cmd% help &efor command information.");

        // Help Command
        this.put("command-help-description", "Displays the help menu... You have arrived");
        this.put("command-help-title", "&eAvailable Commands:");
        this.put("command-help-list-description", "&8 - &d/%cmd% %subcmd% %args% &7- %desc%");
        this.put("command-help-list-description-no-args", "&8 - &d/%cmd% %subcmd% &7- %desc%");

        // Reload Command
        this.put("command-reload-description", "Reloads the plugin");
        this.put("command-reload-reloaded", "&eConfiguration and locale files were reloaded.");

        // Argument Handler Error Messages
        this.put("argument-handler-color-hex", "Hex code [%input%] is in the wrong format");
        this.put("argument-handler-color-rgb", "RGB formatted Color was not recognized");
        this.put("argument-handler-boolean", "Boolean [%input%] must be true or false");
        this.put("argument-handler-byte", "Byte [%input%] must be a whole number between -128 and 127 inclusively");
        this.put("argument-handler-character", "Character [%input%] must be exactly 1 character");
        this.put("argument-handler-double", "Double [%input%] must be a number within bounds");
        this.put("argument-handler-enum", "%enum% type [%input%] does not exist");
        this.put("argument-handler-enum-list", "%enum% type [%input%] does not exist. Valid types: %types%");
        this.put("argument-handler-float", "Float [%input%] must be a number within bounds");
        this.put("argument-handler-string", "String cannot be empty");
        this.put("argument-handler-integer", "Integer [%input%] must be a whole number between -2^31 and 2^31-1 inclusively");
        this.put("argument-handler-long", "Long [%input%] must be a whole number between -2^63 and 2^63-1 inclusively");
        this.put("argument-handler-player", "No Player with the username [%input%] was found online");
        this.put("argument-handler-player-selector-syntax", "Selector has a syntax error");
        this.put("argument-handler-player-selector-none", "No Players were found for the given selector");
        this.put("argument-handler-player-selector-multiple", "Selector resulted in multiple entities being selected");
        this.put("argument-handler-player-selector-entity", "Selector resulted in a selected non-player entity");
        this.put("argument-handler-short", "Short [%input%] must be a whole number between -32,768 and 32,767 inclusively");
        this.put("argument-handler-value", "Value [%input%] must be one of the following: %values%");
        this.put("argument-handler-registry-value", "Value [%input%] is not a valid %type%.");
    }};

}
