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
