package dev.rosewood.rosegarden.locale.provider;

import dev.rosewood.rosegarden.locale.Locale;
import dev.rosewood.rosegarden.locale.YamlFileLocale;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class JarResourceLocaleProvider implements LocaleProvider {

    private final String resourcePath;

    public JarResourceLocaleProvider(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public Collection<Locale> getLocales() {
        try {
            // Find all the *.lang files in the resource path directory
            List<Locale> locales = new ArrayList<>();

            Enumeration<URL> paths = JarResourceLocaleProvider.class.getClassLoader().getResources(this.resourcePath);
            while (paths.hasMoreElements()) {
                URL url = paths.nextElement();
                String name = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
                if (!name.endsWith(".yml"))
                    continue;

                try (InputStream inputStream = url.openStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    String localeName = name.substring(0, name.length() - 4);
                    Locale locale = new YamlFileLocale(localeName, inputStreamReader);
                    locales.add(locale);
                }
            }

            return locales;
        } catch (IOException e) {
            throw new IllegalArgumentException("JarResourceLocaleProvider could not load locales from resource path: " + this.resourcePath, e);
        }
    }

}
