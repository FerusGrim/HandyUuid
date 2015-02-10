package com.gmail.ferusgrim.util;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class NameGrabber implements Callable<Map<UUID, String>> {

    private final List<UUID> uuidsToLookup;

    public NameGrabber(List<UUID> uuidsToLookup) {
        this.uuidsToLookup = ImmutableList.copyOf(uuidsToLookup);
    }


    @Override
    public Map<UUID, String> call() throws Exception {
        Map<UUID, String> responseMap = new HashMap<UUID, String>();
        JSONParser parser = new JSONParser();

        for (UUID uuid : this.uuidsToLookup) {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/"
                    + uuid.toString().replace("-", "")).openConnection();
            JSONObject response = (JSONObject) parser.parse(new InputStreamReader(connection.getInputStream()));

            String name = (String) response.get("name");

            if (name == null) {
                continue;
            }

            String cause = (String) response.get("cause");
            String message = (String) response.get("errorMessage");

            if (cause != null && cause.length() > 0) {
                throw new IllegalStateException(message);
            }

            responseMap.put(uuid, name);
        }

        return responseMap;
    }
}
