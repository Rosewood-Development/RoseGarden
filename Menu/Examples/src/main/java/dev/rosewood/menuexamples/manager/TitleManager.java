package dev.rosewood.menuexamples.manager;

import dev.rosewood.menuexamples.gui.model.Title;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import java.util.LinkedHashMap;
import java.util.Map;

public class TitleManager extends Manager {

    private final Map<String, Title> titles;

    public TitleManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.titles = new LinkedHashMap<>();

        // Just adding these by default for an example.
        this.titles.put("example1",
                new Title("example1", "&7[&aExample 1&7]", "basic"));
        this.titles.put("example2",
                new Title("example2", "&7[&aExample 2&7]", "basic"));
        this.titles.put("example3",
                new Title("example3", "&7[&aExample 3&7]", "basic"));
        this.titles.put("example4",
                new Title("example4", "&7[&aExample 4&7]", "basic"));
        this.titles.put("example5",
                new Title("example5", "&7[&aExample 5&7]", "rare"));
        this.titles.put("example6",
                new Title("example6", "&7[&aExample 6&7]", "rare"));
        this.titles.put("example7",
                new Title("example7", "&7[&aExample 7&7]", "rare"));
        this.titles.put("example8",
                new Title("example8", "&7[&aExample 8&7]", "rare"));
        this.titles.put("example9",
                new Title("example9", "&7[&aExample 9&7]", "meow"));
        this.titles.put("example10",
                new Title("example10", "&7[&aExample 10&7]", "meow"));
        this.titles.put("example11",
                new Title("example11", "&7[&aExample 11&7]", "meow"));
        this.titles.put("example12",
                new Title("example12", "&7[&aExample 12&7]", "meow"));
    }

    @Override
    public void reload() {
        // Get data from config.
    }

    @Override
    public void disable() {

    }

    public Map<String, Title> getTitles() {
        return this.titles;
    }

}
