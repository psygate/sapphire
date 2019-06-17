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

import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.Message;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import org.bukkit.ChatColor;
import org.stringtemplate.v4.ST;

import java.util.*;

/**
 * Created by psygate on 13.06.2016.
 */
public class UnlimitedChannel implements Channel {
    private String name;
    private double range;
    private ChannelType type;
    private String template;
    protected Set<Messenger> subscribers = new HashSet<>();

    public UnlimitedChannel(String name, ChannelType type, String template) {
        this.name = Objects.requireNonNull(name);
        this.range = Objects.requireNonNull(range);
        this.type = Objects.requireNonNull(type);
        this.template = Objects.requireNonNull(template);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addSubscriber(Messenger sub) {
        subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(Messenger sub) {
        subscribers.remove(sub);
    }

    @Override
    public Collection<Messenger> getSubscribers() {
        return Collections.unmodifiableCollection(subscribers);
    }

    @Override
    public ChannelType getChannelType() {
        return type;
    }

    @Override
    public void send(Message message) {
        sendMessage(bindAndRenderMessage(message), message.getSource());
    }

    protected void sendMessage(String message, Messenger source) {
        subscribers.forEach(s -> s.sendMessage(message, source));
    }

    protected String bindAndRenderMessage(Message message) {
        ST tmplt = new ST(template);

        tmplt.add("channel_name", name);
        tmplt.add("source_name", message.getSource().getName());
        tmplt.add("msg", message.getText());
        ChatManager.getInstance().bindColors(tmplt);
        return tmplt.render();
    }
}
