package dev.rosewood.rosegarden.codec.pdc;

import dev.rosewood.rosegarden.codec.CodecType;
import org.bukkit.persistence.PersistentDataContainer;

public class PdcCodecType implements CodecType<PersistentDataContainer> {

    public static final PdcCodecType INSTANCE = new PdcCodecType();

    private PdcCodecType() {

    }

    @Override
    public Class<PersistentDataContainer> getContainerType() {
        return PersistentDataContainer.class;
    }

    @Override
    public String toString() {
        return "PdcCodec";
    }

}
