package dev.rosewood.rosegarden.command.rwd;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.objects.RosePluginData;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

@SuppressWarnings("deprecation")
public class RwdCommand extends BaseRoseCommand {

    public RwdCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("rwd")
                .permission("rosegarden.rwd")
                .build();
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        List<RosePluginData> pluginData = this.rosePlugin.getLoadedRosePluginsData();

        ComponentBuilder builder = new ComponentBuilder();
        builder.append(TextComponent.fromLegacyText(HexUtils.colorify(
                RoseGardenUtils.PREFIX + "&ePlugins installed using " + RoseGardenUtils.GRADIENT + "RoseGarden &eby " + RoseGardenUtils.GRADIENT + "Rosewood Development&e. Hover over to view info: ")));

        boolean first = true;
        for (RosePluginData data : pluginData) {
            if (!first)
                builder.append(TextComponent.fromLegacyText(HexUtils.colorify("&e, ")), FormatRetention.NONE);
            first = false;

            String updateVersion = data.updateVersion();
            String website = data.website();

            List<Text> content = new ArrayList<>();
            content.add(new Text(TextComponent.fromLegacyText(HexUtils.colorify("&eVersion: &b" + data.version()))));
            content.add(new Text(TextComponent.fromLegacyText(HexUtils.colorify("\n&eRoseGarden Version: &b" + data.roseGardenVersion()))));
            if (updateVersion != null)
                content.add(new Text(TextComponent.fromLegacyText(HexUtils.colorify("\n&eAn update (&bv" + updateVersion + "&e) is available! Click to open the Spigot page."))));

            TextComponent pluginName = new TextComponent(TextComponent.fromLegacyText(HexUtils.colorify(RoseGardenUtils.GRADIENT + data.name())));
            pluginName.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, content.toArray(new Text[0])));

            if (website != null)
                pluginName.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, data.website()));

            builder.append(pluginName);
        }

        context.getSender().spigot().sendMessage(builder.create());
    }

}
