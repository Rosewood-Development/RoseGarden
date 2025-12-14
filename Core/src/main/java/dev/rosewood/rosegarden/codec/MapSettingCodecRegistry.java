package dev.rosewood.rosegarden.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapSettingCodecRegistry implements SettingCodecRegistry {

    private final Map<RegistryKey, SettingCodec<?, ?>> codecMap;

    public MapSettingCodecRegistry() {
        this.codecMap = new HashMap<>();
    }

    @Override
    public <C, T> void register(CodecType<C> codecType, SettingCodec<C, T> codec) {
        RegistryKey key = new RegistryKey(codecType, codec.getSettingType());
        if (this.codecMap.containsKey(key))
            throw new IllegalArgumentException(codecType.toString() + " already registered for type " + codec.getSettingType().getType().getTypeName());
        this.codecMap.put(key, codec);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C, T> SettingCodec<C, T> get(CodecType<C> codecType, SettingType<T> type) {
        SettingCodec<C, T> codec = (SettingCodec<C, T>) this.codecMap.get(new RegistryKey(codecType, type));
        if (codec == null)
            throw new IllegalArgumentException("No " + codecType.toString() + " found applicable for type " + type.getType().getTypeName());
        return codec;
    }

    @Override
    public <C, T> boolean has(CodecType<C> codecType, SettingType<T> type) {
        return this.codecMap.containsKey(new RegistryKey(codecType, type));
    }

    private static class RegistryKey {

        private final CodecType<?> codecType;
        private final SettingType<?> type;

        public RegistryKey(CodecType<?> codecType, SettingType<?> type) {
            this.codecType = codecType;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            RegistryKey that = (RegistryKey) o;
            return this.codecType.equals(that.codecType) && this.type.equals(that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.codecType, this.type);
        }

    }

}
