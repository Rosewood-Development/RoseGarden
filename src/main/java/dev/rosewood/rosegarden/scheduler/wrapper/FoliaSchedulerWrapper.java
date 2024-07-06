package dev.rosewood.rosegarden.scheduler.wrapper;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.scheduler.task.FoliaScheduledTask;
import dev.rosewood.rosegarden.scheduler.task.ScheduledTask;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class FoliaSchedulerWrapper implements SchedulerWrapper {

    private final RosePlugin rosePlugin;
    private final RegionScheduler regionScheduler;
    private final GlobalRegionScheduler globalRegionScheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaSchedulerWrapper(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
        this.regionScheduler = Bukkit.getRegionScheduler();
        this.globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
        this.asyncScheduler = Bukkit.getAsyncScheduler();
    }

    @Override
    public boolean isEntityThread(Entity entity) {
        return Bukkit.isOwnedByCurrentRegion(entity);
    }

    @Override
    public boolean isLocationThread(Location location) {
        return Bukkit.isOwnedByCurrentRegion(location);
    }

    @Override
    public ScheduledTask runTask(Runnable runnable) {
        return wrap(this.globalRegionScheduler.run(this.rosePlugin, task -> runnable.run()));
    }

    @Override
    public ScheduledTask runTaskAsync(Runnable runnable) {
        return wrap(this.asyncScheduler.runNow(this.rosePlugin, task -> runnable.run()));
    }

    @Override
    public ScheduledTask runTaskLater(Runnable runnable, long delay) {
        return wrap(this.globalRegionScheduler.runDelayed(this.rosePlugin, task -> runnable.run(), fix(delay)));
    }

    @Override
    public ScheduledTask runTaskLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        return wrap(this.globalRegionScheduler.runDelayed(this.rosePlugin, task -> runnable.run(), fix(delay)));
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Runnable runnable, long delay) {
        return wrap(this.asyncScheduler.runDelayed(this.rosePlugin, task -> runnable.run(), fix(ticksToMillis(delay)), TimeUnit.MILLISECONDS));
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Runnable runnable, long delay, TimeUnit timeUnit) {
        return wrap(this.asyncScheduler.runDelayed(this.rosePlugin, task -> runnable.run(), fix(delay), timeUnit));
    }

    @Override
    public ScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
        return wrap(this.globalRegionScheduler.runAtFixedRate(this.rosePlugin, task -> runnable.run(), fix(delay), fix(period)));
    }

    @Override
    public ScheduledTask runTaskTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return wrap(this.globalRegionScheduler.runAtFixedRate(this.rosePlugin, task -> runnable.run(), fix(RoseGardenUtils.timeUnitToTicks(delay, timeUnit)), fix(RoseGardenUtils.timeUnitToTicks(period, timeUnit))));
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Runnable runnable, long delay, long period) {
        return wrap(this.asyncScheduler.runAtFixedRate(this.rosePlugin, task -> runnable.run(), fix(ticksToMillis(delay)), fix(ticksToMillis(period)), TimeUnit.MILLISECONDS));
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return wrap(this.asyncScheduler.runAtFixedRate(this.rosePlugin, task -> runnable.run(), fix(delay), fix(period), timeUnit));
    }

    @Override
    public ScheduledTask runTaskAtLocation(Location location, Runnable runnable) {
        return wrap(this.regionScheduler.run(this.rosePlugin, location, task -> runnable.run()));
    }

    @Override
    public ScheduledTask runTaskAtLocationLater(Location location, Runnable runnable, long delay) {
        return wrap(this.regionScheduler.runDelayed(this.rosePlugin, location, task -> runnable.run(), fix(delay)));
    }

    @Override
    public ScheduledTask runTaskAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit timeUnit) {
        return wrap(this.regionScheduler.runDelayed(this.rosePlugin, location, task -> runnable.run(), fix(RoseGardenUtils.timeUnitToTicks(delay, timeUnit))));
    }

    @Override
    public ScheduledTask runTaskTimerAtLocation(Location location, Runnable runnable, long delay, long period) {
        return wrap(this.regionScheduler.runAtFixedRate(this.rosePlugin, location, task -> runnable.run(), fix(delay), fix(period)));
    }

    @Override
    public ScheduledTask runTaskTimerAtLocation(Location location, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return wrap(this.regionScheduler.runAtFixedRate(this.rosePlugin, location, task -> runnable.run(), fix(RoseGardenUtils.timeUnitToTicks(delay, timeUnit)), fix(RoseGardenUtils.timeUnitToTicks(period, timeUnit))));
    }

    @Override
    public ScheduledTask runTaskAtEntity(Entity entity, Runnable runnable) {
        return wrap(entity.getScheduler().run(this.rosePlugin, task -> runnable.run(), null));
    }

    @Override
    public ScheduledTask runTaskAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return wrap(entity.getScheduler().runDelayed(this.rosePlugin, task -> runnable.run(), null, fix(delay)));
    }

    @Override
    public ScheduledTask runTaskAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit timeUnit) {
        return wrap(entity.getScheduler().runDelayed(this.rosePlugin, task -> runnable.run(), null, fix(RoseGardenUtils.timeUnitToTicks(delay, timeUnit))));
    }

    @Override
    public ScheduledTask runTaskTimerAtEntity(Entity entity, Runnable runnable, long delay, long period) {
        return wrap(entity.getScheduler().runAtFixedRate(this.rosePlugin, task -> runnable.run(), null, fix(delay), fix(period)));
    }

    @Override
    public ScheduledTask runTaskTimerAtEntity(Entity entity, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return wrap(entity.getScheduler().runAtFixedRate(this.rosePlugin, task -> runnable.run(), null, fix(RoseGardenUtils.timeUnitToTicks(delay, timeUnit)), fix(RoseGardenUtils.timeUnitToTicks(period, timeUnit))));
    }

    @Override
    public void cancelAllTasks() {
        this.globalRegionScheduler.cancelTasks(this.rosePlugin);
        this.asyncScheduler.cancelTasks(this.rosePlugin);
    }

    private static ScheduledTask wrap(io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask) {
        return new FoliaScheduledTask(foliaTask);
    }

    private static long fix(long delay) {
        return delay > 0 ? delay : 1;
    }

    private static long ticksToMillis(long ticks) {
        return ticks * 50L;
    }

}
