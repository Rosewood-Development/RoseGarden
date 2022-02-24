package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.EnumArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.utils.ClassUtils;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("rawtypes")
public abstract class AbstractCommandManager extends Manager {

    private static final String ARGUMENT_PACKAGE = "dev.rosewood.rosegarden.command.argument";
    private final Map<Class<? extends RoseCommandArgumentHandler>, RoseCommandArgumentHandler<?>> argumentHandlers;
    private List<RoseCommandWrapper> commandWrappers;

    public AbstractCommandManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.argumentHandlers = new HashMap<>();
    }

    @Override
    public final void reload() {
        if (this.commandWrappers == null) {
            this.commandWrappers = new ArrayList<>();
            for (Class<? extends RoseCommandWrapper> commandWrapperClass : this.getRootCommands()) {
                try {
                    Constructor<? extends RoseCommandWrapper> constructor = commandWrapperClass.getConstructor(RosePlugin.class);
                    RoseCommandWrapper commandWrapper = constructor.newInstance(this.rosePlugin);
                    this.commandWrappers.add(commandWrapper);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            // Load argument handlers
            List<Class<RoseCommandArgumentHandler>> argumentHandlerClasses = new ArrayList<>(ClassUtils.getClassesOf(this.rosePlugin, ARGUMENT_PACKAGE, RoseCommandArgumentHandler.class));
            this.getArgumentHandlerPackages().stream().map(x -> ClassUtils.getClassesOf(this.rosePlugin, x, RoseCommandArgumentHandler.class)).forEach(argumentHandlerClasses::addAll);

            for (Class<RoseCommandArgumentHandler> argumentHandlerClass : argumentHandlerClasses) {
                // Ignore abstract/interface classes
                if (Modifier.isAbstract(argumentHandlerClass.getModifiers()) || Modifier.isInterface(argumentHandlerClass.getModifiers()))
                    continue;

                RoseCommandArgumentHandler<?> argumentHandler = argumentHandlerClass.getConstructor(RosePlugin.class).newInstance(this.rosePlugin);
                this.argumentHandlers.put(argumentHandlerClass, argumentHandler);
            }
        } catch (Exception e) {
            this.rosePlugin.getLogger().severe("Fatal error initializing command argument handlers");
            e.printStackTrace();
        }

        this.commandWrappers.forEach(RoseCommandWrapper::register);
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }

    @Override
    public final void disable() {
        this.argumentHandlers.clear();
        this.commandWrappers.forEach(RoseCommandWrapper::unregister);
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }

    public RoseCommandArgumentHandler<?> resolveArgumentHandler(Class<?> handledParameterClass) {
        if (Enum.class.isAssignableFrom(handledParameterClass))
            return this.argumentHandlers.get(EnumArgumentHandler.class);

        // Map primitive types to their wrapper handlers
        if (handledParameterClass.isPrimitive())
            handledParameterClass = RoseGardenUtils.getPrimitiveAsWrapper(handledParameterClass);

        Class<?> finalHandledParameterClass = handledParameterClass;
        return this.argumentHandlers.values()
                .stream()
                .filter(x -> x.getHandledType() != null && x.getHandledType() == finalHandledParameterClass)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tried to resolve a RoseCommandArgumentHandler for an unhandled type"));
    }

    public abstract List<Class<? extends RoseCommandWrapper>> getRootCommands();

    public abstract List<String> getArgumentHandlerPackages();

}
