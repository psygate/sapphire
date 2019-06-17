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
import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerWhisperMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.MessengerState;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.stringtemplate.v4.ST;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by psygate on 10.06.2016.
 */
public class ReplyCommand extends NucleusPlayerCommand {

    public ReplyCommand() {
        super(1, Integer.MAX_VALUE);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        MessengerState state = ChatManager.getInstance().getState(new PlayerMessenger(player));

        if (!state.hasReplyMessenger()) {
            throw new CommandException("No one to reply to.");
        }

        Messenger reply = state.getReplyMessenger();

        String text = buildMessage(strings);

        ChatManager.getInstance().sendMessage(new PlayerWhisperMessage(player, text, reply));
    }

    private String buildMessage(String[] strings) {
        return Arrays.stream(strings).reduce((a, b) -> a + " " + b).orElseGet(() -> "");
    }

    @Override
    protected String[] getName() {
        return new String[]{"reply"};
    }
}
