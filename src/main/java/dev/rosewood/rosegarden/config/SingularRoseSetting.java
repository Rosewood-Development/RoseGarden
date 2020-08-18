package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import java.util.List;

public class SingularRoseSetting implements RoseSetting {

    private final RosePlugin rosePlugin;
    private final String key;
    private final Object defaultValue;
    private final String[] comments;
    private Object value = null;

    public SingularRoseSetting(RosePlugin rosePlugin, String key, Object defaultValue, String... comments) {
        this.rosePlugin = rosePlugin;
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments != null ? comments : new String[0];
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String[] getComments() {
        return this.comments;
    }

    @Override
    public Object getCachedValue() {
        return this.value;
    }

    @Override
    public boolean getBoolean() {
        this.loadValue();
        return (boolean) this.value;
    }

    @Override
    public int getInt() {
        this.loadValue();
        return (int) this.getNumber();
    }

    @Override
    public long getLong() {
        this.loadValue();
        return (long) this.getNumber();
    }

    @Override
    public double getDouble() {
        this.loadValue();
        return this.getNumber();
    }

    @Override
    public float getFloat() {
        this.loadValue();
        return (float) this.getNumber();
    }

    @Override
    public String getString() {
        this.loadValue();
        return (String) this.value;
    }

    private double getNumber() {
        if (this.value instanceof Integer) {
            return (int) this.value;
        } else if (this.value instanceof Short) {
            return (short) this.value;
        } else if (this.value instanceof Byte) {
            return (byte) this.value;
        } else if (this.value instanceof Float) {
            return (float) this.value;
        }

        return (double) this.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getStringList() {
        this.loadValue();
        return (List<String>) this.value;
    }

    @Override
    public boolean isSection() {
        return this.defaultValue == null;
    }

    @Override
    public void loadValue() {
        if (this.value != null)
            return;

        this.value = this.rosePlugin.getManager(AbstractConfigurationManager.class).getConfig().get(this.key);
    }

    @Override
    public void reset() {
        this.value = null;
    }

}
