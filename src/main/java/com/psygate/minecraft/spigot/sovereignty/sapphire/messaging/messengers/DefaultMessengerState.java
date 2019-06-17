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

import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.Channel;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * Created by psygate on 14.06.2016.
 */
public class DefaultMessengerState implements MessengerState {

    private Channel replyChannel;
    private PlayerMessenger replyMessenger;
    private Channel lockedChannel;

    @Override
    public boolean isInChannelChatMode() {
        return lockedChannel != null;
    }

    @Override
    public Channel getLockedChannel() {
        return lockedChannel;
    }

    @Override
    public void setReplyChannel(Channel channel) {
        this.replyChannel = channel;
    }

    @Override
    public Messenger getReplyMessenger() {
        return replyMessenger;
    }

    @Override
    public boolean hasReplyMessenger() {
        return replyMessenger != null;
    }

    @Override
    public void setLockedChannel(Channel channel) {
        this.lockedChannel = channel;
    }

    @Override
    public Channel getReplyChannel() {
        return replyChannel;
    }

    @Override
    public boolean hasReplyChannel() {
        return replyChannel != null;
    }

    @Override
    public void setReplyMessenger(PlayerMessenger source) {
        this.replyMessenger = source;
    }
}
