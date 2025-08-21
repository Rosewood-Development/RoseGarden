package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.BookMeta;

public class BookMetaSerializer implements MetaSerializer {

    public static final String BOOK = "book";
    public static final String TITLE = BOOK + ".title";
    public static final String AUTHOR = BOOK + ".author";
    public static final String GENERATION = BOOK + ".generation";
    public static final String PAGES = BOOK + ".pages";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(BOOK))
            return;

        if (section.contains(TITLE))
            item.setBookTitle(section.getString(TITLE));

        if (section.contains(AUTHOR))
            item.setBookAuthor(section.getString(AUTHOR));

        if (section.contains(GENERATION)) {
            BookMeta.Generation generation = ItemSerializer.getGeneration(section.getString(GENERATION), BOOK);
            if (generation != null)
                item.setBookGeneration(generation);
        }

        if (section.contains(PAGES)) {
            for (String page : section.getStringList(PAGES))
                item.addBookPage(page);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (item.hasBookTitle())
            section.set(TITLE, item.getBookTitle());

        if (item.hasBookAuthor())
            section.set(AUTHOR, item.getBookAuthor());

        if (item.hasBookGeneration())
            section.set(GENERATION, item.getBookGeneration().toString().toLowerCase());

        if (item.hasBookPages())
            section.set(PAGES, item.getBookPages());
    }

}
