package dev.rosewood.rosegarden.gui.provider.animation;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.Provider;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Optional;

public class MenuTicker extends BukkitRunnable {

    private final RosePlugin rosePlugin;
    private final MenuView view;
    private final Player player;

    public MenuTicker(RosePlugin rosePlugin, MenuView view, Player player, int speed) {
        this.rosePlugin = rosePlugin;
        this.view = view;
        this.player = player;
        this.runTaskTimer(rosePlugin, 0L, speed);
    }

    @Override
    public void run() {
        for (int slot : this.view.getActiveIcons().keySet()) {
            Icon icon = this.view.getActiveIcons().get(slot);

            Context context = Context.of(Parameters.SLOT, slot)
                    .add(Parameters.MENU, this.view.getMenu())
                    .add(Parameters.VIEW, this.view)
                    .add(Parameters.PLAYER, this.player)
                    .add(Parameters.ICON, icon)
                    .addAll(this.view.getInitContext());

            Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
            RoseItem item = itemProvider.isPresent() ? itemProvider.get().get(context) : RoseItem.empty();
            context.add(Parameters.ITEM, item);

            for (Provider<?> provider : icon.getProviders().values()) {
                if (provider instanceof Tickable)
                    ((Tickable) provider).run(context);

                if (provider.getKey().equalsIgnoreCase(Providers.TICK.getKey()))
                    icon.call(Providers.TICK.getKey(), context);
            }
        }

        Context context = Context.of(Parameters.MENU, this.view.getMenu())
                .add(Parameters.VIEW, this.view)
                .add(Parameters.PLAYER, this.player)
                .add(Parameters.PLUGIN, this.rosePlugin);
        this.view.run(context);
    }

}
