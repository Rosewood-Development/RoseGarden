package dev.rosewood.rosegarden.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.objects.RosePluginData;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class RwdCommand extends BukkitCommand {

    private final RosePlugin rosePlugin;

    public RwdCommand(RosePlugin rosePlugin) {
        super("rwd", "Rosewood Development information command", "/rwd", Collections.emptyList());

        this.rosePlugin = rosePlugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.isOp()) {
            RoseGardenUtils.sendMessage(sender, "&cYou do not have permission to use this command.");
            return true;
        }

        List<RosePluginData> pluginData = this.rosePlugin.getLoadedRosePluginsData();

        ComponentBuilder builder = new ComponentBuilder();
        builder.append(TextComponent.fromLegacyText(HexUtils.colorify(
                RoseGardenUtils.PREFIX + "&ePlugins installed by " + RoseGardenUtils.GRADIENT + "Rosewood Development&e. Hover over to view info: ")));

        boolean first = true;
        for (RosePluginData data : pluginData) {
            if (!first)
                builder.append(TextComponent.fromLegacyText(HexUtils.colorify("&e, ")), FormatRetention.NONE);
            first = false;

            String updateVersion = data.getUpdateVersion();
            String website = data.getWebsite();

            List<Text> content = new ArrayList<>();
            content.add(new Text(TextComponent.fromLegacyText(HexUtils.colorify("&eVersion: &b" + data.getVersion()))));
            content.add(new Text(TextComponent.fromLegacyText(HexUtils.colorify("\n&eRoseGarden Version: &b" + data.getRoseGardenVersion()))));
            if (updateVersion != null)
                content.add(new Text(TextComponent.fromLegacyText(HexUtils.colorify("\n&eAn update (&bv" + updateVersion + "&e) is available! Click to open the Spigot page."))));

            TextComponent pluginName = new TextComponent(TextComponent.fromLegacyText(HexUtils.colorify(RoseGardenUtils.GRADIENT + data.getName())));
            pluginName.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, content.toArray(new Text[0])));

            if (website != null)
                pluginName.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, data.getWebsite()));

            builder.append(pluginName);
        }

        sender.spigot().sendMessage(builder.create());

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias,String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

}
