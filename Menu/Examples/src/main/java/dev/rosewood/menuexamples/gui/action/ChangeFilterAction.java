package dev.rosewood.menuexamples.gui.action;

import dev.rosewood.menuexamples.gui.model.Filter;
import dev.rosewood.menuexamples.gui.model.Title;
import dev.rosewood.menuexamples.manager.GuiManager;
import dev.rosewood.menuexamples.manager.TitleManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChangeFilterAction extends AbstractAction {

    public static final String ID = "change-filter";

    private int currentFilter;

    // Code Constructor

    public ChangeFilterAction() {
        super(ID);
    }

    // Config Constructor

    public ChangeFilterAction(ConfigurationSection section) {
        super(ID, section);
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(ID, true);
    }

    @Override
    public void run(Context context) {
        // Called when the action is run
        Optional<MenuView> view = context.get(Parameters.VIEW);
        if (!view.isPresent())
            return;

        // Increase the filter ID. If it goes over the amount of filters, reset it to 0
        this.currentFilter++;
        if (this.currentFilter >= Filter.values().length)
            this.currentFilter = 0;

        // Grab the filter from the ID.
        Filter filter = Filter.values()[this.currentFilter];

        TitleManager titleManager = RosePlugin.instance().getManager(TitleManager.class);
        List<Title> titles;

        // Sort the titles based on the filter!
        if (filter == Filter.NAME) {
            titles = titleManager.getTitles().values().stream().sorted(Comparator.comparing(Title::title)).collect(Collectors.toList());
        } else if (filter == Filter.TYPE) {
            titles = titleManager.getTitles().values().stream().sorted(Comparator.comparing(Title::type)).collect(Collectors.toList());
        } else {
            titles = new ArrayList<>(titleManager.getTitles().values());
        }

        // Now we should update the context for what we have just changed.
        // We use the init context, as we pass the filter and titles through when the menu is created.
        view.get().getInitContext().add(GuiManager.FILTER, filter);
        view.get().getInitContext().add(GuiManager.FILTERED_TITLES, titles);

        // Refresh the view so that the items change.
        view.get().refresh();
    }

}
