package dev.rosewood.rosegarden.scheduler.wrapper;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.scheduler.task.BukkitScheduledTask;
import dev.rosewood.rosegarden.scheduler.task.ScheduledTask;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class BukkitSchedulerWrapper implements SchedulerWrapper {

    private final RosePlugin rosePlugin;
    private final BukkitScheduler scheduler;

    public BukkitSchedulerWrapper(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
        this.scheduler = Bukkit.getScheduler();
    }

    @Override
    public boolean isEntityThread(Entity entity) {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public boolean isLocationThread(Location location) {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public ScheduledTask runTask(Runnable runnable) {
        return wrap(this.scheduler.runTask(this.rosePlugin, runnable));
    }

    @Override
    public ScheduledTask runTaskAsync(Runnable runnable) {
        return wrap(this.scheduler.runTaskAsynchronously(this.rosePlugin, runnable));
    }

    @Override
    public ScheduledTask runTaskLater(Runnable runnable, long delay) {
        return wrap(this.scheduler.runTaskLater(this.rosePlugin, runnable, delay));
    }

    @Override
    public ScheduledTask runTaskLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        return wrap(this.scheduler.runTaskLater(this.rosePlugin, runnable, RoseGardenUtils.timeUnitToTicks(delay, timeUnit)));
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Runnable runnable, long delay) {
        return wrap(this.scheduler.runTaskLaterAsynchronously(this.rosePlugin, runnable, delay));
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Runnable runnable, long delay, TimeUnit timeUnit) {
        return wrap(this.scheduler.runTaskLaterAsynchronously(this.rosePlugin, runnable, RoseGardenUtils.timeUnitToTicks(delay, timeUnit)));
    }

    @Override
    public ScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
        return wrapRepeating(this.scheduler.runTaskTimer(this.rosePlugin, runnable, delay, period));
    }

    @Override
    public ScheduledTask runTaskTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return wrapRepeating(this.scheduler.runTaskTimer(this.rosePlugin, runnable, RoseGardenUtils.timeUnitToTicks(delay, timeUnit), RoseGardenUtils.timeUnitToTicks(period, timeUnit)));
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Runnable runnable, long delay, long period) {
        return wrapRepeating(this.scheduler.runTaskTimerAsynchronously(this.rosePlugin, runnable, delay, period));
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return wrapRepeating(this.scheduler.runTaskTimerAsynchronously(this.rosePlugin, runnable, RoseGardenUtils.timeUnitToTicks(delay, timeUnit), RoseGardenUtils.timeUnitToTicks(period, timeUnit)));
    }

    @Override
    public ScheduledTask runTaskAtLocation(Location location, Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    public ScheduledTask runTaskAtLocationLater(Location location, Runnable runnable, long delay) {
        return this.runTaskLater(runnable, delay);
    }

    @Override
    public ScheduledTask runTaskAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit timeUnit) {
        return this.runTaskLater(runnable, delay, timeUnit);
    }

    @Override
    public ScheduledTask runTaskTimerAtLocation(Location location, Runnable runnable, long delay, long period) {
        return this.runTaskTimer(runnable, delay, period);
    }

    @Override
    public ScheduledTask runTaskTimerAtLocation(Location location, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return this.runTaskTimer(runnable, delay, period, timeUnit);
    }

    @Override
    public ScheduledTask runTaskAtEntity(Entity entity, Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    public ScheduledTask runTaskAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return this.runTaskLater(runnable, delay);
    }

    @Override
    public ScheduledTask runTaskAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit timeUnit) {
        return this.runTaskLater(runnable, delay, timeUnit);
    }

    @Override
    public ScheduledTask runTaskTimerAtEntity(Entity entity, Runnable runnable, long delay, long period) {
        return this.runTaskTimer(runnable, delay, period);
    }

    @Override
    public ScheduledTask runTaskTimerAtEntity(Entity entity, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return this.runTaskTimer(runnable, delay, period, timeUnit);
    }

    @Override
    public void cancelAllTasks() {
        this.scheduler.cancelTasks(this.rosePlugin);
    }

    private static ScheduledTask wrap(BukkitTask task) {
        return new BukkitScheduledTask(task, false);
    }

    private static ScheduledTask wrapRepeating(BukkitTask task) {
        return new BukkitScheduledTask(task, true);
    }

}
