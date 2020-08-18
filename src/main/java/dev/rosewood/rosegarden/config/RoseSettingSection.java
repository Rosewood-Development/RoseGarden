package dev.rosewood.rosegarden.config;

import java.util.Arrays;
import java.util.List;

public class RoseSettingSection {

    private final List<RoseSettingValue> values;

    public RoseSettingSection(RoseSettingValue... values) {
        this.values = Arrays.asList(values);
    }

    public List<RoseSettingValue> getValues() {
        return this.values;
    }

}
