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

package com.psygate.minecraft.spigot.sovereignty.sapphire;

import com.psygate.minecraft.spigot.sovereignty.nucleus.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.nucleus.managment.NucleusPlugin;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.DatabaseInterface;
import com.psygate.minecraft.spigot.sovereignty.sapphire.configuration.Configuration;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

/**
 * Created by psygate on 18.04.2016.
 */
public class Sapphire extends JavaPlugin implements NucleusPlugin {
    private final static Logger LOG = Logger.getLogger(Sapphire.class.getName());
    private static Sapphire instance;
    private Configuration configuration;
    private DatabaseInterface dbi;

    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.ALL);
        List<Handler> handlers = Arrays.asList(LOG.getHandlers());

        if (handlers.stream().noneMatch(h -> h instanceof FileHandler)) {
            try {
                File logdir = new File("logs/nucleus_logs/sapphire/");
                if (!logdir.exists()) {
                    logdir.mkdirs();
                }
                FileHandler fh = new FileHandler(
                        "logs/nucleus_logs/sapphire/sapphire.%u.%g.log",
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

    public static Logger getLogger(String name) {
        Logger log = Logger.getLogger(name);
        log.setParent(LOG);
        log.setUseParentHandlers(true);
        log.setLevel(Level.ALL);
        return log;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
//            new File(getDataFolder(), "config.yml").delete();
            subEnable();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void subEnable() {

        saveDefaultConfig();
        configuration = new Configuration(getConfig());
        Nucleus.getInstance().register(this);
        ChatManager.getInstance();
    }

    public static Sapphire getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin not initialized.");
        }

        return instance;
    }

    public static Configuration getConfiguration() {
        return getInstance().configuration;
    }

    public static DatabaseInterface DBI() {
        return getInstance().dbi;
    }

    @Override
    public int getWantedDBVersion() {
        return 1;
    }

    @Override
    public void fail() {
        System.err.println("Unable to load sapphire.");
        Bukkit.shutdown();
    }

    @Override
    public void setLogger(Logger logger) {

    }

    @Override
    public Logger getPluginLogger() {
        return LOG;
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(new SapphireListener());
    }

    @Override
    public void setDatabaseInterface(DatabaseInterface databaseInterface) {
        this.dbi = databaseInterface;
    }

    public void reload() {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(c -> c.sendMessage(ChatColor.YELLOW + "Reloading Sapphire."));
        subEnable();
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(c -> c.sendMessage(ChatColor.GREEN + "Reloaded Sapphire."));
    }
}
