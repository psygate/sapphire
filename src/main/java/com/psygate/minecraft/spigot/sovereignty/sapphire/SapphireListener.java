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

package com.psygate.minecraft.spigot.sovereignty.sapphire;

import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.sapphire.data.TowerManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.ChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.MessengerState;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by psygate on 18.04.2016.
 */
public class SapphireListener implements Listener {

    public SapphireListener() {
        Bukkit.getOnlinePlayers().forEach(p -> ChatManager.getInstance().addParticipant(new PlayerMessenger(p)));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent ev) {
        ev.setCancelled(true);
        MessengerState state = ChatManager.getInstance().getState(new PlayerMessenger(ev.getPlayer()));

        if (state.isInChannelChatMode()) {
            ChannelMessage message = new PlayerChannelMessage(ev.getPlayer(), ev.getMessage(), state.getLockedChannel());
            ChatManager.getInstance().sendMessage(message);
        } else {
            ChannelMessage message = new PlayerChannelMessage(ev.getPlayer(), ev.getMessage(), ChatManager.getInstance().getDefaultChannel());
            ChatManager.getInstance().sendMessage(message);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent ev) {
        ChatManager.getInstance().addParticipant(new PlayerMessenger(ev.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent ev) {
        ChatManager.getInstance().removeParticipant(new PlayerMessenger(ev.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent ev) {
        TowerManager.getInstance().breakTower(new BlockKey(ev.getBlock()));
    }
}
