package dev.rosewood.menuexamples.command.argument;

import dev.rosewood.menuexamples.manager.GuiManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;

public class MenuArgumentHandler extends ArgumentHandler<RoseMenuWrapper> {

    public static final ArgumentHandler<RoseMenuWrapper> INSTANCE = new MenuArgumentHandler();

    private MenuArgumentHandler() {
        super(RoseMenuWrapper.class);
    }

    @Override
    public RoseMenuWrapper handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();

        RoseMenuWrapper menu = RosePlugin.instance().getManager(GuiManager.class).getActiveMenus().get(input);
        if (menu == null)
            throw new HandledArgumentException("command-menu-invalid", StringPlaceholders.of("menu", input));

        return menu;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return new ArrayList<>(RosePlugin.instance().getManager(GuiManager.class).getActiveMenus().keySet());
    }

}
