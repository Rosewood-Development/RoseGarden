package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;

public class BlockDataMetaSerializer implements MetaSerializer {

    public static final String BLOCK_DATA = "block-data";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(BLOCK_DATA))
            return;

        String blockStateStr = section.getString(BLOCK_DATA);
        if (blockStateStr == null)
            return;

        BlockData data = Bukkit.createBlockData(blockStateStr);
        item.setBlockData(data);
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasBlockData())
            return;

        BlockData blockData = item.getBlockData(item.getType());
        section.set(BLOCK_DATA, blockData.getAsString());
    }

}
