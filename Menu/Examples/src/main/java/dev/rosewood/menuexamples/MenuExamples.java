package dev.rosewood.menuexamples;

import dev.rosewood.menuexamples.manager.CommandManager;
import dev.rosewood.menuexamples.manager.GuiManager;
import dev.rosewood.menuexamples.manager.LocaleManager;
import dev.rosewood.menuexamples.manager.TitleManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import java.util.List;

public class MenuExamples extends RosePlugin {

    public MenuExamples() {
        super(-1, -1, null, LocaleManager.class, CommandManager.class, GuiManager.class);
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(TitleManager.class);
    }

}
