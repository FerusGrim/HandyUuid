package com.gmail.ferusgrim;

import com.google.common.base.Optional;
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
        String[] args = arguments.split("\\s+");

        if (args.length < 1) {
            source.sendMessage("You forgot a username!");
            return true;
        }

        final String player = args[0];

        if (this.server.getPlayer(player).isPresent()) {
            this.doStuff(this.server.getPlayer(player).get().getUniqueId());
            return true;
        }

        if (HandyUuid.getUuid(player).isPresent()) {
            this.doStuff(HandyUuid.getUuid(player).get());
            return true;
        }

        source.sendMessage("We have to lookup the UUID of this player! It may take a moment!");

        HandyUuid.getInstance().getGame().getAsyncScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                Optional<UUID> uuid = HandyUuid.retrieveUuid(player);

                if (!uuid.isPresent()) {
                    source.sendMessage("Username isn't registered!");
                } else {
                    doStuff(uuid.get());
                }
            }
        });

        return true;
    }

    private void doStuff(UUID uniqueId) {
        // Do some thangs.
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
