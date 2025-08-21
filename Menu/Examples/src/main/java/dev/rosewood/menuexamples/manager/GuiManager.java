package dev.rosewood.menuexamples.manager;

import dev.rosewood.menuexamples.gui.model.Filter;
import dev.rosewood.menuexamples.gui.ListMenu;
import dev.rosewood.menuexamples.gui.model.Title;
import dev.rosewood.menuexamples.gui.action.ChangeFilterAction;
import dev.rosewood.menuexamples.gui.fill.TitleAreaFill;
import dev.rosewood.menuexamples.gui.item.FilterItemProvider;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.gui.action.Actions;
import dev.rosewood.rosegarden.gui.fill.Fills;
import dev.rosewood.rosegarden.gui.parameter.Parameter;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import dev.rosewood.rosegarden.manager.AbstractGuiManager;
import java.util.List;
import java.util.function.Function;

public class GuiManager extends AbstractGuiManager {

    // Parameters are variables as they need to be used in other classes.
    public static Parameter<Filter> FILTER;
    public static Parameter<List<Title>> FILTERED_TITLES;

    public GuiManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setup() {
        // Used for registering custom parameters, actions, providers, and fills.
        FILTER = Parameters.create("filter", Filter.class);
        FILTERED_TITLES = Parameters.create("filtered_titles", (Class<List<Title>>) ((Class<?>) List.class));

        Actions.create(ChangeFilterAction.ID, ChangeFilterAction::new);
        Providers.create(FilterItemProvider.ID, ItemProvider.ID, FilterItemProvider::new);
        Fills.create(TitleAreaFill.ID, TitleAreaFill::new);
    }

    @Override
    public List<Function<RosePlugin, RoseMenuWrapper>> getMenus() {
        return List.of(ListMenu::new);
    }

}
