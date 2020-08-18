package dev.rosewood.rosegarden.config;

public class RoseSettingValue {

    private final String key;
    private final Object defaultValue;
    private final String[] comments;

    public RoseSettingValue(String key, Object defaultValue, String... comments) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    public String getKey() {
        return this.key;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public String[] getComments() {
        return this.comments;
    }

}
