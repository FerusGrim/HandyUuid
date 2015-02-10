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

import com.gmail.ferusgrim.util.Grab;
import com.google.common.base.Optional;
import com.sun.istack.internal.NotNull;

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

    @NotNull
    public Optional<UUID> getUuid(String username) {
        for (UUID uuid : this.usernameMap.keySet()) {
            if (this.usernameMap.get(uuid).equalsIgnoreCase(username)) {
                return Optional.of(uuid);
            }
        }

        return Optional.absent();
    }

    @NotNull
    public Optional<UUID> retrieveUuid(String username) {
        UUID uuid = null;

        try {
            uuid = Grab.uuidOf(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (uuid == null) {
            return Optional.absent();
        }

        this.usernameMap.put(uuid, username);
        return Optional.of(uuid);
    }
}
