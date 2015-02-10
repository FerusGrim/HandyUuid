package com.gmail.ferusgrim;

import com.gmail.ferusgrim.util.NameGrabber;

import java.util.*;

public class UuidUpdater implements Runnable {

    private final UuidStorage storage;
    private final HandyUuid plugin;

    public UuidUpdater(UuidStorage storage, HandyUuid plugin) {
        this.storage = storage;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Map<UUID, String> usernameMap = storage.getUsernameMap();

        for (UUID uuid : usernameMap.keySet()) {
            if (plugin.getGame().getServer().get().getPlayer(uuid).get().hasJoinedBefore()) {
                usernameMap.remove(uuid);
            }
        }

        NameGrabber grabber = new NameGrabber(new ArrayList<UUID>(usernameMap.keySet()));
        Map<UUID, String> response = null;

        try {
            response = grabber.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response == null) {
            return;
        }

        usernameMap = new HashMap<UUID, String>(response);

        storage.getUsernameMap().putAll(usernameMap);
    }
}
