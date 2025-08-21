package dev.rosewood.rosegarden.gui.persistence;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class InventoryContainerType implements PersistentDataType<PersistentDataContainer, Map<Integer, RoseItem>> {

    private final RosePlugin rosePlugin;

    public InventoryContainerType(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
    }

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<Map<Integer, RoseItem>> getComplexType() {
        return (Class<Map<Integer, RoseItem>>) ((Class<?>) Map.class);
    }

    @Override
    public PersistentDataContainer toPrimitive(Map<Integer, RoseItem> map, PersistentDataAdapterContext context) {
        PersistentDataContainer pdc = context.newPersistentDataContainer();
        for (int slot : map.keySet()) {
            NamespacedKey key = new NamespacedKey(this.rosePlugin, "slot-" + slot);
            pdc.set(key, PersistentDataType.BYTE_ARRAY, ItemSerializer.toBytes(map.get(slot)));
        }

        return pdc;
    }

    @Override
    public Map<Integer, RoseItem> fromPrimitive(PersistentDataContainer pdc, PersistentDataAdapterContext context) {
        Map<Integer, RoseItem> items = new HashMap<>();
        for (int slot = 0; slot < 54; slot++) {
            NamespacedKey key = new NamespacedKey(this.rosePlugin, "slot-" + slot);
            if (!pdc.has(key))
                continue;

            items.put(slot, ItemSerializer.fromBytes(pdc.get(key, PersistentDataType.BYTE_ARRAY)));
        }

        return items;
    }

}
