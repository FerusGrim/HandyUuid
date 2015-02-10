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
