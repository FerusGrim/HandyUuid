package com.gmail.ferusgrim.util;

import java.util.Arrays;
import java.util.UUID;

public class Grab {

    public static UUID uuidFromResult(String uuid) {
        return UUID.fromString(uuid.substring(0, 8) + "-"
                + uuid.substring(8, 12) + "-"
                + uuid.substring(12, 16) + "-"
                + uuid.substring(16, 20) + "-"
                + uuid.substring(20, 32));
    }

    public static UUID uuidOf(String username) throws Exception {
        return new UuidGrabber(Arrays.asList(username)).call().get(username);
    }

    public static String nameOf(UUID uuid) throws Exception {
        return new NameGrabber(Arrays.asList(uuid)).call().get(uuid);
    }
}
