package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

/**
 * item:
 *   type: goat_horn
 *   goat-horn:
 *     instrument: ponder_goat_horn
 */
@SuppressWarnings("deprecation")
public class MusicInstrumentMetaSerializer implements MetaSerializer {

    public static final String MUSIC_INSTRUMENT = "music-instrument";
    public static final String INSTRUMENT = MUSIC_INSTRUMENT + ".instrument";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(MUSIC_INSTRUMENT) || !section.contains(INSTRUMENT))
            return;

        if (NMSUtil.getVersionNumber() < 19) {
            this.invalidateVersion(MUSIC_INSTRUMENT, "1.19");
            return;
        }

        if (!section.isString(INSTRUMENT))
            return;

        String keyStr = section.getString(INSTRUMENT);
        if (keyStr == null)
            return;

        NamespacedKey key = ItemSerializer.getKey(keyStr, MUSIC_INSTRUMENT);
        if (key == null)
            return;

        MusicInstrument instrument = MusicInstrument.getByKey(key);
        if (instrument == null)
            return;

        item.setMusicInstrument(instrument);
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (item.getMusicInstrument() == null)
            return;

        section.set(INSTRUMENT, ((MusicInstrument) item.getMusicInstrument()).getKey().toString());
    }

}
