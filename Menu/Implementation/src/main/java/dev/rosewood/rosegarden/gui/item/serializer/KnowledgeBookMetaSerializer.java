package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeBookMetaSerializer implements MetaSerializer {

    public static final String KNOWLEDGE_BOOK = "knowledge-book";
    public static final String RECIPES = KNOWLEDGE_BOOK + ".recipes";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(KNOWLEDGE_BOOK) || !section.contains(RECIPES))
            return;

        if (!section.isList(RECIPES))
            return;

        for (String s : section.getStringList(RECIPES)) {
            NamespacedKey key = ItemSerializer.getKey(s, KNOWLEDGE_BOOK);
            if (key == null)
                continue;

            item.addKnowledgeBookRecipe(key);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasKnowledgeBookRecipes())
            return;

        List<String> recipes = new ArrayList<>();
        for (NamespacedKey key : item.getKnowledgeBookRecipes())
            recipes.add(key.toString());

        section.set(RECIPES, recipes);
    }

}
