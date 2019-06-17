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
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerWhisperMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.MessengerState;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by psygate on 10.06.2016.
 */
public class ReplyChannelCommand extends NucleusPlayerCommand {

    public ReplyChannelCommand() {
        super(1, Integer.MAX_VALUE);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        MessengerState state = ChatManager.getInstance().getState(new PlayerMessenger(player));

        if (!state.hasReplyChannel()) {
            throw new CommandException("No channel to reply to.");
        }

        Channel reply = state.getReplyChannel();

        String text = buildMessage(strings);

        ChatManager.getInstance().sendMessage(new PlayerChannelMessage(player, text, reply));
    }

    private String buildMessage(String[] strings) {
        return Arrays.stream(strings).reduce((a, b) -> a + " " + b).orElseGet(() -> "");
    }

    @Override
    protected String[] getName() {
        return new String[]{"replychannel"};
    }
}
