package dev.rosewood.rosegarden.gui.provider;

import org.bukkit.configuration.ConfigurationSection;

/**
 * An implementable {@linkplain Provider provider} class with the required constructor.
 * @param <S> The object type that this provider provides.
 */
public abstract class AbstractProvider<S> implements Provider<S> {

    public AbstractProvider(String id, ConfigurationSection section) {

    }

}
