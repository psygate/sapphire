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

package com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers;

import com.psygate.minecraft.spigot.sovereignty.ivory.data.PlayerSettings;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by psygate on 13.06.2016.
 */
public class PlayerMessenger implements Messenger {
    private static Location DEFAULT_LOCATION;
    private UUID player;
    private String name;

    public PlayerMessenger(Player p) {
        this.player = Objects.requireNonNull(p).getUniqueId();
        this.name = Objects.requireNonNull(p.getName());
    }

    public PlayerMessenger(UUID source) {
        this.player = Objects.requireNonNull(source);
    }

    @Override
    public void sendMessage(String message, Messenger source) {
        if (source instanceof PlayerMessenger) {
            if (GroupManager.getInstance().getPlayerMutes(player).contains(((PlayerMessenger) source).getUUID())) {
                return;
            }
        }
        Optional.ofNullable(Bukkit.getPlayer(player)).ifPresent(p -> p.sendMessage(message));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MessengerState createState() {
        return new DefaultMessengerState();
    }

    public Location getLocation() {
        return Optional.ofNullable(Bukkit.getPlayer(player)).map(Player::getLocation).orElseGet(() -> getDefaultLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerMessenger that = (PlayerMessenger) o;

        return player != null ? player.equals(that.player) : that.player == null;

    }

    @Override
    public int hashCode() {
        return player != null ? player.hashCode() : 0;
    }

    public UUID getUUID() {
        return player;
    }

    @Override
    public String toString() {
        return player.toString();
    }

    public Location getDefaultLocation() {
        if (DEFAULT_LOCATION == null) {
            DEFAULT_LOCATION = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        }
        return DEFAULT_LOCATION;
    }
}
