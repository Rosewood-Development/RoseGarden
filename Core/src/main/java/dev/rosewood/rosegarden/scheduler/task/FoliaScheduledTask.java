package dev.rosewood.rosegarden.scheduler.task;

import dev.rosewood.rosegarden.RosePlugin;

public class FoliaScheduledTask implements ScheduledTask {

    private final io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask;

    public FoliaScheduledTask(io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask) {
        this.foliaTask = foliaTask;
    }

    @Override
    public void cancel() {
        this.foliaTask.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.foliaTask.isCancelled();
    }

    @Override
    public RosePlugin getOwningPlugin() {
        return (RosePlugin) this.foliaTask.getOwningPlugin();
    }

    @Override
    public boolean isRunning() {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState state = this.foliaTask.getExecutionState();
        return state == io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState.RUNNING
                || state == io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    @Override
    public boolean isRepeating() {
        return this.foliaTask.isRepeatingTask();
    }

}
