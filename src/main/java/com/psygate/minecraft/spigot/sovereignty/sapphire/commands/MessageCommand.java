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
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.WhisperMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.kitteh.vanish.VanishPlugin;
import org.stringtemplate.v4.ST;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by psygate on 10.06.2016.
 */
public class MessageCommand extends NucleusPlayerCommand {

    public MessageCommand() {
        super(2, Integer.MAX_VALUE);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        String recpname = strings[0];
        String text = buildMessage(Arrays.copyOfRange(strings, 1, strings.length));

        Player recp = Optional.ofNullable(Bukkit.getPlayer(recpname)).orElseThrow(() -> new CommandException("Player \"" + recpname + "\" not found."));

        if (Bukkit.getPluginManager().getPlugin("VanishNoPacket") != null) {
            VanishPlugin plug = (VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket");
            if (plug.getManager().isVanished(recp) && !player.hasPermission("vanish.see")) {
                throw new CommandException("Player \"" + recpname + "\" not found.");
            }
        }

        WhisperMessage message = new PlayerWhisperMessage(player, text, new PlayerMessenger(recp));
        ChatManager.getInstance().sendMessage(message);
    }

    private String buildMessage(String[] strings) {
        return Arrays.stream(strings).reduce((a, b) -> a + " " + b).orElseGet(() -> "");
    }

    @Override
    protected String[] getName() {
        return new String[]{"message"};
    }
}
