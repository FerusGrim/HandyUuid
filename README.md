# HandyUuid
A simple, easy-to-use UUID cache system for Sponge.

### Information
HandyUuid stores a Map of UUIDs and their related Usernames. How you go about utilizing HandyUuid is completely up to you.

In the example below, you'll see me go through 3 stages. Server storage, HandyUuid storage, and Lookup. However, for the most accurate use, you could always skip straight to step 3. Or, maybe you want the accuracy, but don't want to do a lookup for every request, you could skip straight to step 2. HandyUuid's cache is updated every hour (asynchronously), so you can safely query from Step 2 (followed by Step 3 if Step 2 fails).

### Example Usage
For this complete example, take a look at the [CommandExample](src/test/java/com/gmail/ferusgrim/CommandExample.java).

```java
// We start off with a username. Simple enough.
String username = "FerusGrim";
```

With this username, there are three potential stages.

Stage 1, we check if the user that goes by the given name has played on the server, before.
```java
if (this.server.getPlayer(username).isPresent()) {
    this.doStuff(this.server.getPlayer(username).get().getUniqueId());
}
```

If you succeed in Stage 1, it means that the player has played on the server before, and therefore has their UUID stored. If you don't succeed, we now need to check if the username is in HandyUuid's cache.

```java
if (HandyUuid.getUuid(username).isPresent()) {
    this.doStuff(HandyUuid.getUuid(username).get());
}
```

If you succeed in Stage 2, it means that the player _hasn't_ played on the server before, but a lookup has been done, and therefore is stored in HandyUuid's cache. If you don't succeed, we now need to do a lookup for this username.

Keep in mind that looking up a UUID is a blocking call, and should therefore be done Asynchronously. I'll allow you to decide your own method for doing this. Previously, there was an AsyncThenSync utility I employed, if you want to look through the commit history for such a tool.

```java
Optional<UUID> uuid = HandyUuid.retrieveUuid(username);

if (!uuid.isPresent()) {
    commandSource.sendMessage("Username isn't registered!");
} else {
    doStuff(uuid.get());
}
```

At this point, if the Username does not return a UUID to match it, the Username hasn't been registered. Or, you don't have an internet connection. I'll assume you can troubleshoot those types of issues on your own.
