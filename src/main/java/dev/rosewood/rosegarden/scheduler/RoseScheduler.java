package dev.rosewood.rosegarden.scheduler;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.scheduler.task.ScheduledTask;
import dev.rosewood.rosegarden.scheduler.wrapper.BukkitSchedulerWrapper;
import dev.rosewood.rosegarden.scheduler.wrapper.FoliaSchedulerWrapper;
import dev.rosewood.rosegarden.scheduler.wrapper.SchedulerWrapper;
import dev.rosewood.rosegarden.utils.NMSUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class RoseScheduler implements SchedulerWrapper {

    private static RoseScheduler instance;

    private final AtomicInteger runningTasks;
    private final SchedulerWrapper scheduler;

    private RoseScheduler(RosePlugin rosePlugin) {
        if (instance != null)
            throw new IllegalStateException("An instance of RoseScheduler already exists");

        instance = this;
        this.runningTasks = new AtomicInteger();

        if (NMSUtil.isFolia()) {
            this.scheduler = new FoliaSchedulerWrapper(rosePlugin);
        } else {
            this.scheduler = new BukkitSchedulerWrapper(rosePlugin);
        }
    }

    @Override
    public boolean isEntityThread(Entity entity) {
        return this.scheduler.isEntityThread(entity);
    }

    @Override
    public boolean isLocationThread(Location location) {
        return this.scheduler.isLocationThread(location);
    }

    @Override
    public ScheduledTask runTask(Runnable runnable) {
        return this.scheduler.runTask(this.wrap(runnable));
    }

    @Override
    public ScheduledTask runTaskAsync(Runnable runnable) {
        return this.scheduler.runTaskAsync(this.wrap(runnable));
    }

    @Override
    public ScheduledTask runTaskLater(Runnable runnable, long delay) {
        return this.scheduler.runTaskLater(this.wrap(runnable), delay);
    }

    @Override
    public ScheduledTask runTaskLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        return this.scheduler.runTaskLater(this.wrap(runnable), delay, timeUnit);
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Runnable runnable, long delay) {
        return this.scheduler.runTaskLaterAsync(this.wrap(runnable), delay);
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Runnable runnable, long delay, TimeUnit timeUnit) {
        return this.scheduler.runTaskLaterAsync(this.wrap(runnable), delay, timeUnit);
    }

    @Override
    public ScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
        return this.scheduler.runTaskTimer(this.wrap(runnable), delay, period);
    }

    @Override
    public ScheduledTask runTaskTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return this.scheduler.runTaskTimer(this.wrap(runnable), delay, period, timeUnit);
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Runnable runnable, long delay, long period) {
        return this.scheduler.runTaskTimerAsync(this.wrap(runnable), delay, period);
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return this.scheduler.runTaskTimerAsync(this.wrap(runnable), delay, period, timeUnit);
    }

    @Override
    public ScheduledTask runTaskAtLocation(Location location, Runnable runnable) {
        return this.scheduler.runTaskAtLocation(location, this.wrap(runnable));
    }

    @Override
    public ScheduledTask runTaskAtLocationLater(Location location, Runnable runnable, long delay) {
        return this.scheduler.runTaskAtLocationLater(location, this.wrap(runnable), delay);
    }

    @Override
    public ScheduledTask runTaskAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit timeUnit) {
        return this.scheduler.runTaskAtLocationLater(location, this.wrap(runnable), delay, timeUnit);
    }

    @Override
    public ScheduledTask runTaskTimerAtLocation(Location location, Runnable runnable, long delay, long period) {
        return this.scheduler.runTaskTimerAtLocation(location, this.wrap(runnable), delay, period);
    }

    @Override
    public ScheduledTask runTaskTimerAtLocation(Location location, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return this.scheduler.runTaskTimerAtLocation(location, this.wrap(runnable), delay, period, timeUnit);
    }

    @Override
    public ScheduledTask runTaskAtEntity(Entity entity, Runnable runnable) {
        return this.scheduler.runTaskAtEntity(entity, this.wrap(runnable));
    }

    @Override
    public ScheduledTask runTaskAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return this.scheduler.runTaskAtEntityLater(entity, this.wrap(runnable), delay);
    }

    @Override
    public ScheduledTask runTaskAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit timeUnit) {
        return this.scheduler.runTaskAtEntityLater(entity, this.wrap(runnable), delay, timeUnit);
    }

    @Override
    public ScheduledTask runTaskTimerAtEntity(Entity entity, Runnable runnable, long delay, long period) {
        return this.scheduler.runTaskTimerAtEntity(entity, this.wrap(runnable), delay, period);
    }

    @Override
    public ScheduledTask runTaskTimerAtEntity(Entity entity, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return this.scheduler.runTaskTimerAtEntity(entity, this.wrap(runnable), delay, period, timeUnit);
    }

    @Override
    public void cancelAllTasks() {
        this.scheduler.cancelAllTasks();
    }

    public int getRunningTaskCount() {
        return this.runningTasks.get();
    }

    private Runnable wrap(Runnable runnable) {
        return () -> {
            this.runningTasks.incrementAndGet();
            try {
                runnable.run();
            } finally {
                this.runningTasks.decrementAndGet();
            }
        };
    }

    public static RoseScheduler getInstance(RosePlugin rosePlugin) {
        if (instance == null)
            instance = new RoseScheduler(rosePlugin);
        return instance;
    }

}
