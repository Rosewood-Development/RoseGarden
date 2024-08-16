package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommandManager extends Manager {

    private final List<RoseCommandWrapper> commandWrappers;

    public AbstractCommandManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.commandWrappers = new ArrayList<>();
    }

    @Override
    public void reload() {
        this.getRootCommands().stream()
                .map(x -> x.apply(this.rosePlugin))
                .map(x -> new RoseCommandWrapper(this.rosePlugin, x))
                .forEach(this.commandWrappers::add);

        this.commandWrappers.forEach(RoseCommandWrapper::register);
    }

    @Override
    public void disable() {
        this.commandWrappers.forEach(RoseCommandWrapper::unregister);
        this.commandWrappers.clear();
    }

    @NotNull
    public abstract List<Function<RosePlugin, BaseRoseCommand>> getRootCommands();

    public List<RoseCommandWrapper> getActiveCommands() {
        return Collections.unmodifiableList(this.commandWrappers);
    }

}
