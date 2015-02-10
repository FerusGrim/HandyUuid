package com.gmail.ferusgrim;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Subscribe;

@Plugin(id = "handyuuid", name = "HandyUuid", version = "1.0-SNAPSHOT")
public class HandyUuid {

    @Subscribe
    public void whileServerStarting(ServerStartingEvent event) {
        instance = this;
        this.uuidStorage = new UuidStorage(this);
        this.game = event.getGame();
    }

    private static HandyUuid instance = null;
    public static HandyUuid getInstance() {
        return instance;
    }

    @Inject
    private Logger logger;
    public Logger getLogger() {
        return this.logger;
    }

    private UuidStorage uuidStorage;
    public UuidStorage getUuidStorage() {
        return this.uuidStorage;
    }

    private Game game;
    public Game getGame() {
        return this.game;
    }
}
