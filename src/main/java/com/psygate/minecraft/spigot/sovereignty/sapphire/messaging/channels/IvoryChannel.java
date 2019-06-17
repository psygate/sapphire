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

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import com.psygate.minecraft.spigot.sovereignty.sapphire.data.Tower;
import com.psygate.minecraft.spigot.sovereignty.sapphire.data.TowerManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.Message;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PluginMessenger;
import com.psygate.spatial.trees.IntAreaQuadTree;
import org.bukkit.ChatColor;
import org.stringtemplate.v4.ST;

import java.util.*;

/**
 * Created by psygate on 14.06.2016.
 */
public class IvoryChannel implements Channel {
    private Group group;
    private Set<Messenger> subscribers = new HashSet<>();

    public IvoryChannel(Group group) {
        this.group = group;
    }

    @Override
    public String getName() {
        return group.getName();
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
        return ChannelType.PLAYER;
    }

    @Override
    public void send(Message message) {
//        System.out.println(message);
        if (message instanceof PlayerChannelMessage) {
            PlayerChannelMessage msg = (PlayerChannelMessage) message;
            if (!group.hasMember(msg.getSource().getUUID())) {
                message.getSource().sendMessage(ChatColor.RED + "Insufficient permission to send to channel.", new PluginMessenger(Sapphire.getInstance()));
                return;
            }
        }

        if (message instanceof PlayerChannelMessage) {
            sendMessageTowered((PlayerChannelMessage) message);
        } else {
            sendToAll(message);
        }
    }

    private void sendMessageTowered(PlayerChannelMessage message) {

        IntAreaQuadTree<Tower> towers = TowerManager.getInstance().getTowersTree(group.getGroupID());
        int x = message.getSource().getLocation().getBlockX(), z = message.getSource().getLocation().getBlockZ();

        if (!towers.hasValuesContaining(x, z)) {
            message.getSource().sendMessage(ChatColor.RED + "Transmission failed, no tower in range.", new PluginMessenger(Sapphire.getInstance()));
        } else {
//            Set<Tower> visited = new HashSet<>();
            LinkedList<Tower> stack = new LinkedList<>(towers.getValuesContaining(x, z));

//            for (Tower tower : stack) {
//                if (!visited.contains(tower)) {
//                    visited.add(tower);
//                    for (Tower candidate : towers.getValuesIntersecting(tower.getBounds())) {
//                        if (!visited.contains(candidate) && tower.getBounds().contains(candidate.getX(), candidate.getZ())) {
//                            stack.add(candidate);
//                        }
//                    }
//                    if (!tower.sendMessage(message, render(message))) {
//                        break;
//                    }
//                }
//            }

            for (Tower tower : stack) {
                if (!tower.sendMessage(message, render(message))) {
                    break;
                }
            }
        }
    }

    private void sendToAll(Message message) {
        String text = render(message);
        for (UUID uuid : group.getMembers().keySet()) {
            if (message.getSource() instanceof PlayerMessenger) {
                if (GroupManager.getInstance().getGroupMutes(uuid).contains(group.getGroupID())) {
                    continue;
                }
            }
            PlayerMessenger m = new PlayerMessenger(uuid);
            m.sendMessage(text, message.getSource());
            ChatManager.getInstance().getState(m).setReplyChannel(this);
        }
    }

    private String render(Message message) {
        ST st = new ST(Sapphire.getConfiguration().getGroupMessageTemplate());
        st.add("channel_name", group.getName());
        st.add("source_name", message.getSource().getName());
        st.add("msg", message.getText());
        ChatManager.getInstance().bindColors(st);
        String text = st.render();
        return text;
    }
}
