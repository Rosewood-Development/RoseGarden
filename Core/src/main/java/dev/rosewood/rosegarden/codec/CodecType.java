package dev.rosewood.rosegarden.codec;

/**
 * Represents a type of codec.
 *
 * @param <C> The container type
 */
public interface CodecType<C> {

    Class<C> getContainerType();

}
