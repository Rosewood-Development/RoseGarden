package dev.rosewood.rosegarden.gui.provider.slot;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a single slot in an inventory.<br>
 * Serializes as:<br>
 * <pre>
 *     {@code
 *     slot: 0
 *     }
 * </pre>
 */
public class SingleSlotProvider extends AbstractSlotProvider {

    public static final String ID = "slot";

    // Code Constructors

    public SingleSlotProvider(int slot) {
        super(ID, null);

        this.slots.add(slot);
    }

    // Config Constructors

    public SingleSlotProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.slots.add(section.getInt(this.getKey()));
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(this.getKey(), this.slots.get(0));
    }

    // Static Constructors

    public static SingleSlotProvider of(int slot) {
        return new SingleSlotProvider(slot);
    }

}
