package dev.rosewood.rosegarden.locale;

import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import java.io.File;
import java.io.Reader;
import java.util.Map;

public class YamlFileLocale implements Locale {

    private final String localeName;
    private final Map<String, Object> configuration;

    public YamlFileLocale(File file) {
        this.localeName = file.getName();
        this.configuration = CommentedFileConfiguration.loadConfiguration(file).getValues(true);
    }

    public YamlFileLocale(String localeName, Reader reader) {
        this.localeName = localeName;
        this.configuration = CommentedFileConfiguration.loadConfiguration(reader).getValues(true);
    }

    @Override
    public String getLocaleName() {
        return this.localeName;
    }

    @Override
    public Map<String, Object> getLocaleValues() {
        return this.configuration;
    }

}
