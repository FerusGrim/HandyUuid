package com.gmail.ferusgrim;

import com.gmail.ferusgrim.util.AsyncThenSync;
import com.gmail.ferusgrim.util.Grab;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;
import java.util.UUID;

public class CommandExample implements CommandCallable {

    private final Plugin plugin;
    private final Server server;

    public CommandExample(Plugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public boolean call(final CommandSource source, String arguments, List<String> parents) throws CommandException {
        String[] args = arguments.split(" ");
        if (args.length < 1) {
            source.sendMessage("Some message to state that a player name is required, or something.");
        }

        final String playerName = args[0];

        // doStuff, because we can safely get the UUID from a previously logged in player.
        if (this.server.getPlayer(playerName).isPresent()) {
            return doStuff(this.server.getPlayer(playerName).get().getUniqueId());
        }

        // Attempt to get UUID from HandyUuid cache.
        UUID uuid = HandyUuid.getUuid(playerName);

        // doStuff, because this player hasn't logged in before, but has been cached by HandyUuid from a previous lookup.
        if (uuid != null) {
            return doStuff(uuid);
        }

        source.sendMessage("UUID isn't in cache - looking up...");
        // AsyncThenSync is a utility class that allows you to complete a task off of the Game/Server thread,
        // and then execute the results ON the Game/Server thread.
        new AsyncThenSync(plugin, true) {
            private UUID uuid = null;
            // Asynchronously lookup the UUID.
            @Override
            protected void async() {
                try {
                    uuid = HandyUuid.retrieveUuid(playerName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Use the information gathered asynchronously synchronously.
            @Override
            protected void sync() {
                if (uuid == null) {
                    source.sendMessage("This username isn't registered! Whoops~");
                    return;
                }

                // Now that the UUID has been retrieved and is cached, doStuff.
                doStuff(uuid);
            }
        };

        return true;
    }

    private boolean doStuff(UUID uniqueId) {
        // Do some thangs.
        return true;
    }

    /*
        IGNORE EVERYTHING BELOW THIS POINT - EXAMPLE COMMAND IS EXAMPLE.
     */

    @Override
    public boolean testPermission(CommandSource commandSource) {
        return false;
    }

    @Override
    public Optional<String> getShortDescription() {
        return null;
    }

    @Override
    public Optional<String> getHelp() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
        return null;
    }
}
