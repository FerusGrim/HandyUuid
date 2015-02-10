/*
Copyright (c) 2015 Nicholas Badger / FerusGrim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall
be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gmail.ferusgrim;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Subscribe;

import java.util.UUID;

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

    public static Optional<UUID> getUuid(String playerName) {
        return getInstance().getUuidStorage().getUuid(playerName);
    }

    public static Optional<UUID> retrieveUuid(String playerName) {
        return getInstance().getUuidStorage().retrieveUuid(playerName);
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
