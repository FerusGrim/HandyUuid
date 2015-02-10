package com.gmail.ferusgrim.util;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class UuidGrabber implements Callable<Map<String, UUID>> {

    private final List<String> namesToLookup;

    public UuidGrabber(List<String> namesToLookup) {
        this.namesToLookup = ImmutableList.copyOf(namesToLookup);
    }

    public Map<String, UUID> call() throws Exception {
        JSONParser jsonParser = new JSONParser();
        Map<String, UUID> responseMap = new HashMap<String, UUID>();

        URL url = new URL("https://api.mojang.com/profiles/minecraft");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        String body = JSONArray.toJSONString(this.namesToLookup);

        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        stream.close();

        JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));

        for (Object profile : array) {
            JSONObject jsonProfile = (JSONObject) profile;
            String id = (String) jsonProfile.get("id");
            String name = (String) jsonProfile.get("name");
            UUID uuid = Grab.uuidFromResult(id);
            responseMap.put(name, uuid);
        }

        return responseMap;
    }
}
