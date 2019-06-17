/*
 * Copyright (C) 2016 psygate (https://github.com/psygate)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 */

package com.psygate.minecraft.spigot.sovereignty.sapphire.commands;

import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.Channel;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.ChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by psygate on 12.06.2016.
 */
public class ChannelCommand extends NucleusPlayerCommand {
    public ChannelCommand() {
        super(0, 1000);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {

        if (strings.length == 0) {
            ChatManager.getInstance().getState(new PlayerMessenger(player)).setLockedChannel(null);
            player.sendMessage(ChatColor.YELLOW + "Channel reset to default channel.");
        } else {
            String channel = strings[0];
            if (strings.length <= 1) {
                Channel chan = ChatManager.getInstance().getChannel(channel).orElseThrow(() -> new CommandException("Channel \"" + channel + "\" not found."));
                ChatManager.getInstance().getState(new PlayerMessenger(player)).setLockedChannel(chan);
                player.sendMessage(ChatColor.YELLOW + "Now redirecting all chat messages to " + chan.getName());
            } else {
                String msg = Arrays.stream(strings, 1, strings.length).reduce("", (a, b) -> a + " " + b);
                Channel chan = ChatManager.getInstance().getChannel(channel).orElseThrow(() -> new CommandException("Channel \"" + channel + "\" not found."));
                ChannelMessage message = new PlayerChannelMessage(player, msg, chan);
                ChatManager.getInstance().sendMessage(message);
            }
        }
    }

    @Override
    protected String[] getName() {
        return new String[]{"channel"};
    }
}
