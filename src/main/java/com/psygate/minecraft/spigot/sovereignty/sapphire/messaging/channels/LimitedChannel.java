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

package com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels;

import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.Message;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import org.bukkit.Location;

/**
 * Created by psygate on 13.06.2016.
 */
public class LimitedChannel extends UnlimitedChannel {
    private double range;

    public LimitedChannel(String name, ChannelType type, String template, double range) {
        super(name, type, template);
        this.range = range;
    }

    @Override
    public void send(Message message) {
        if (message.getSource() instanceof PlayerMessenger) {
            PlayerMessenger source = (PlayerMessenger) message.getSource();
            sendLocatedMessage(source.getLocation(), bindAndRenderMessage(message), message.getSource());
        } else {
            super.sendMessage(bindAndRenderMessage(message), message.getSource());
        }
    }

    protected void sendLocatedMessage(Location loc, String message, Messenger source) {
        subscribers.stream()
                .filter(c -> filterByRange(c, loc))
                .forEach(m -> m.sendMessage(message, source));
    }

    private boolean filterByRange(Messenger messenger, Location loc) {
        if (messenger instanceof PlayerMessenger) {
            PlayerMessenger mes = (PlayerMessenger) messenger;
            if (mes.getLocation().getWorld().equals(loc.getWorld())) {
                return mes.getLocation().distanceSquared(loc) <= range * range;
            } else {
                return false;
            }
        }

        return true;
    }
}
