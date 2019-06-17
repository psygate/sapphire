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

package com.psygate.minecraft.spigot.sovereignty.sapphire.messaging;

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.Channel;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.channels.IvoryChannel;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.ChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.Message;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.WhisperMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.MessengerState;
import org.bukkit.ChatColor;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;

/**
 * Created by psygate on 12.06.2016.
 */
public class ChatManager {
    private final static Logger LOG = Logger.getLogger("sapphire.chatmanager");

    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.ALL);
        List<Handler> handlers = Arrays.asList(LOG.getHandlers());

        if (handlers.stream().noneMatch(h -> h instanceof FileHandler)) {
            try {
                File logdir = new File("logs/nucleus_logs/sapphire/chatlogs");
                if (!logdir.exists()) {
                    logdir.mkdirs();
                }
                FileHandler fh = new FileHandler(
                        "logs/nucleus_logs/sapphire/chatlogs/chatmanager.%u.%g.log",
                        8 * 1024 * 1024,
                        12,
                        true
                );
                fh.setLevel(Level.ALL);
                fh.setEncoding("UTF-8");
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                LOG.addHandler(fh);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ChatManager instance;

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    private Map<Messenger, MessengerState> states = new HashMap<>();
    private Channel defaultChannel;

    private ChatManager() {
        defaultChannel = Sapphire.getConfiguration().getDefaultChannel();
    }

    public void setDefaultChannel(Channel channel) {
        this.defaultChannel = Objects.requireNonNull(channel);
    }

    public MessengerState getState(Messenger messenger) {
        MessengerState state = states.computeIfAbsent(messenger, m -> {
//            System.out.println("New state for " + messenger);
            return m.createState();
        });
        return state;
    }

//    public void sendMessage(Message message) {
//        Channel chan = Objects.requireNonNull(defaultChannel, () -> "Default channel undefined.");
//        chan.send(message);
//    }

    public void sendMessage(WhisperMessage message) {
        log(message);
        ST to = new ST(Sapphire.getConfiguration().getWhisperRecipientFormat());
        ST from = new ST(Sapphire.getConfiguration().getWhisperSenderFormat());
        bindColors(to);
        bindColors(from);
        to.add("sender_name", message.getSource().getName());
        from.add("sender_name", message.getSource().getName());
        to.add("recipient_name", message.getTo().getName());
        from.add("recipient_name", message.getTo().getName());
        to.add("msg", message.getText());
        from.add("msg", message.getText());

        message.getTo().sendMessage(to.render(), message.getSource());
        message.getSource().sendMessage(from.render(), message.getSource());
        message.postProcess();
    }

    public void sendMessage(ChannelMessage message) {
        log(message);
        message.getChannel().send(message);
        message.postProcess();
    }

    private void log(Message message) {
        LOG.info(() -> message.getClass().getSimpleName() + ": " + message.toString());
        Sapphire.getInstance().getLogger().info(() -> message.toString());
    }

    public void addParticipant(Messenger playerMessenger) {
        defaultChannel.addSubscriber(playerMessenger);
    }

    public void removeParticipant(Messenger playerMessenger) {
        defaultChannel.removeSubscriber(playerMessenger);
    }

    public Channel getDefaultChannel() {
        return defaultChannel;
    }

    public void bindColors(ST tmplt) {
        for (ChatColor color : ChatColor.values()) {
            tmplt.add(color.name().toUpperCase(), color.toString());
            tmplt.add(color.name().toLowerCase(), color.toString());
            tmplt.add(color.name(), color.toString());
        }
    }

    public Optional<Channel> getChannel(String channel) {
        Optional<? extends Group> group = GroupManager.getInstance().getGroup(channel);
        return group.map(g -> new IvoryChannel(g));
    }
}
