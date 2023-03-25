package dev.rosewood.rosegarden.locale;

import java.util.Map;

public interface Locale {

    /**
     * @return the name of the locale (e.g. en_US), this will be used to determine the locale file name
     */
    String getLocaleName();

    /**
     * @return the locale message strings in a key -> value pair
     */
    Map<String, Object> getLocaleValues();

}
