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

package com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages;

import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.Message;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Created by psygate on 13.06.2016.
 */
public abstract class PlayerMessage implements Message {
    private PlayerMessenger source;
    private String text;

    public PlayerMessage(Player source, String text) {
        this.source = new PlayerMessenger(Objects.requireNonNull(source));
        this.text = Objects.requireNonNull(text);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public PlayerMessenger getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "(" + source.getName() + ", " + source.getUUID() + ") > [global]: " + text;
    }
}
