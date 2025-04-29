package dev.rosewood.rosegarden.locale.provider;

import dev.rosewood.rosegarden.locale.Locale;
import java.util.Collection;

public interface LocaleProvider {

    /**
     * Fetches the locales from the provider synchronously.
     * This could be a file, database, webservice, or any other source.
     *
     * @return the locales
     */
    Collection<Locale> getLocales();

}
