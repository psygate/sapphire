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

import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.Channel;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

/**
 * Created by psygate on 13.06.2016.
 */
public class PlayerChannelMessage implements ChannelMessage {
    private Channel channel;
    private PlayerMessenger source;
    private String text;

    public PlayerChannelMessage(Player source, String text, Channel target) {
        this.source = new PlayerMessenger(Objects.requireNonNull(source));
        this.text = Objects.requireNonNull(text);
        this.channel = target;
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
    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "(" + source.getName() + ", " + source.getUUID() + ") > [" + channel.getName() + "]: " + text;
    }
}
