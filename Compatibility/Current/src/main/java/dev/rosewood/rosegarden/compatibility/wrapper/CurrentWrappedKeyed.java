package dev.rosewood.rosegarden.compatibility.wrapper;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class CurrentWrappedKeyed implements WrappedKeyed {

    private final Keyed keyed;

    public <T extends Keyed> CurrentWrappedKeyed(T keyed) {
        this.keyed = keyed;
    }

    @Override
    public NamespacedKey getKey() {
        return this.keyed.getKey();
    }

}
