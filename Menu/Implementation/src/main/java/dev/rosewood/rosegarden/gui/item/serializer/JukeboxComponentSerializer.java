package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;

@SuppressWarnings("UnstableApiUsage")
public class JukeboxComponentSerializer implements MetaSerializer {

    public static final String JUKEBOX = "jukebox";
    public static final String SONG = JUKEBOX + ".song";
    public static final String SHOW_IN_TOOLTIP = JUKEBOX + ".show-in-tooltip";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(JUKEBOX))
            return;

        if (NMSUtil.getVersionNumber() < 21) {
            Bukkit.getLogger().warning("The " + JUKEBOX + " item meta is only available on 1.21+!");
            return;
        }

        JukeboxPlayableComponent jukeboxPlayableComponent = item.getJukeboxPlayableComponent();

        if (section.contains(SONG)) {
            NamespacedKey key = ItemSerializer.getKey(section.getString(SONG), JUKEBOX);
            if (key != null)
                jukeboxPlayableComponent.setSongKey(key);
        }

        if (section.contains(SHOW_IN_TOOLTIP))
            jukeboxPlayableComponent.setShowInTooltip(section.getBoolean(SHOW_IN_TOOLTIP));

        item.setJukeboxPlayableComponent(jukeboxPlayableComponent);
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasJukeboxPlayableComponent())
            return;

        JukeboxPlayableComponent jukeboxPlayableComponent = item.getJukeboxPlayableComponent();

        if (jukeboxPlayableComponent.getSongKey() != null)
            section.set(SONG, jukeboxPlayableComponent.getSongKey().toString());

        if (jukeboxPlayableComponent.isShowInTooltip())
            section.set(SHOW_IN_TOOLTIP, jukeboxPlayableComponent.isShowInTooltip());
    }

}
