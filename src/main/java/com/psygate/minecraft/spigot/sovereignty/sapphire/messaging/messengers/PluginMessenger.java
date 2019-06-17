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

import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.DefaultMessengerState;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.Messenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.MessengerState;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Created by psygate on 14.06.2016.
 */
public class PluginMessenger implements Messenger {
    private JavaPlugin plugin;

    public PluginMessenger(JavaPlugin instance) {
        this.plugin = Objects.requireNonNull(instance);
    }

    @Override
    public void sendMessage(String message, Messenger source) {

    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public MessengerState createState() {
        return new DefaultMessengerState();
    }
}
