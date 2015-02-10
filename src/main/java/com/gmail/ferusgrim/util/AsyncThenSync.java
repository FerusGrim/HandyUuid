package com.gmail.ferusgrim.util;

import com.gmail.ferusgrim.HandyUuid;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.Plugin;

public abstract class AsyncThenSync {

    private final Plugin plugin;
    private boolean ran;

    public AsyncThenSync(Plugin plugin) {
        this(plugin, false);
    }

    public AsyncThenSync(Plugin plugin, boolean runNow) {
        this.plugin = plugin;
        this.ran = false;
        if (runNow) {
            run();
        }
    }

    void run() {
        if (ran) {
            throw new IllegalStateException("Can only run once.");
        }

        ran = true;
        HandyUuid.getInstance().getGame().getAsyncScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                async();
                HandyUuid.getInstance().getGame().getSyncScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        sync();
                    }
                });
            }
        });
    }

    protected abstract void async();

    protected abstract void sync();
}
