package dev.rosewood.rosegarden.locale.provider;

import dev.rosewood.rosegarden.locale.Locale;
import java.util.Collection;

public interface LocaleProvider {

    /**
     * Fetches the locales from the provider.
     * This could be a file, database, webservice, or any other source.
     * TODO: This should be a CompletableFuture, but that currently causes issues where the locales are not loaded in time
     *
     * @return the locales
     */
    Collection<Locale> getLocales();

}
