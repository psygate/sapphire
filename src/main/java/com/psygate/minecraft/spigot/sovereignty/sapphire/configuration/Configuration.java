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

package com.psygate.minecraft.spigot.sovereignty.sapphire.configuration;

import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.Channel;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.ChannelType;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.LimitedChannel;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.UnlimitedChannel;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.stringtemplate.v4.ST;

/**
 * Created by psygate on 01.06.2016.
 */
public class Configuration {
    private String groupMessageTemplate;
    private String whisperRecipientFormat;
    private String whisperSenderFormat;
    private double participantMul;
    private double rangeMul;
    private Channel defaultchannel;

    public Configuration(FileConfiguration conf) {
        defaultchannel = new LimitedChannel(conf.getString("global_channel.name"), ChannelType.PLAYER, conf.getString("global_channel.message_template"), conf.getDouble("global_channel.range"));

        whisperRecipientFormat = conf.getString("whisper.recipient_format");
        whisperSenderFormat = conf.getString("whisper.sender_format");
        participantMul = conf.getDouble("towers.participant_multiplier");
        rangeMul = conf.getDouble("towers.range_multiplier");
        groupMessageTemplate = conf.getString("group_channels.message_template");
    }

    public String getWhisperRecipientFormat() {
        return whisperRecipientFormat;
    }

    public String getWhisperSenderFormat() {
        return whisperSenderFormat;
    }

    public double getParticipantMul() {
        return participantMul;
    }

    public double getRangeMul() {
        return rangeMul;
    }

    public Channel getDefaultChannel() {
        return defaultchannel;
    }

    public String getGroupMessageTemplate() {
        return groupMessageTemplate;
    }
}
