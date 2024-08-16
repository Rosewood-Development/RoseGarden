package dev.rosewood.rosegarden.locale.provider;

import dev.rosewood.rosegarden.locale.Locale;
import dev.rosewood.rosegarden.locale.YamlFileLocale;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarResourceLocaleProvider implements LocaleProvider {

    private final String resourcePath;

    /**
     * @param resourcePath The path to the resource directory, with a trailing slash
     */
    public JarResourceLocaleProvider(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public Collection<Locale> getLocales() {
        try {
            // Find all the *.yml files in the resource path directory
            List<Locale> locales = new ArrayList<>();

            URL url = JarResourceLocaleProvider.class.getClassLoader().getResource(this.resourcePath);
            if (url == null)
                throw new IllegalArgumentException("JarResourceLocaleProvider could not find resource path: " + this.resourcePath);

            URLConnection connection = url.openConnection();
            if (!(connection instanceof JarURLConnection))
                throw new IllegalArgumentException("JarResourceLocaleProvider can only handle jar URLs: " + url);

            JarURLConnection jarConnection = (JarURLConnection) connection;
            JarFile jarFile = jarConnection.getJarFile();
            String prefix = jarConnection.getEntryName() + "/";

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(prefix) || entry.isDirectory() || !name.endsWith(".yml"))
                    continue;

                String localeName = name.substring(prefix.length(), name.length() - 4);
                try (InputStream inputStream = jarFile.getInputStream(entry);
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
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
