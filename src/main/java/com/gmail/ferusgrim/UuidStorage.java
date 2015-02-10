package com.gmail.ferusgrim;

import com.gmail.ferusgrim.util.Grab;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UuidStorage {

    private final Map<UUID, String> usernameMap;
    private final UuidUpdater updater;

    public UuidStorage(HandyUuid plugin) {
        this.usernameMap = new HashMap<UUID, String>();
        this.updater = new UuidUpdater(this, plugin);

        plugin.getGame().getAsyncScheduler().runRepeatingTask(plugin, this.updater, TimeUnit.MINUTES, 60);
    }

    public Map<UUID, String> getUsernameMap() {
        return this.usernameMap;
    }

    public UuidUpdater getUpdater() {
        return this.updater;
    }

    @Nullable
    public UUID getUuid(String username) {
        for (UUID uuid : this.usernameMap.keySet()) {
            if (this.usernameMap.get(uuid).equalsIgnoreCase(username)) {
                return uuid;
            }
        }

        return null;
    }

    @Nullable
    public UUID retrieveUuid(String username) {
        UUID uuid = null;

        try {
            uuid = Grab.uuidOf(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (uuid == null) {
            return null;
        }

        this.usernameMap.put(uuid, username);
        return uuid;
    }
}
