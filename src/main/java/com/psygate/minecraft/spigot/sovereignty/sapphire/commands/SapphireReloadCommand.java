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

package com.psygate.minecraft.spigot.sovereignty.sapphire.commands;

import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusOPConsoleCommand;
import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by psygate on 16.06.2016.
 */
public class SapphireReloadCommand extends NucleusOPConsoleCommand {

    public SapphireReloadCommand() {
        super(0, 0);
    }

    @Override
    protected void subOnCommandOP(CommandSender commandSender, Command command, String s, String[] strings) throws Exception {
        Sapphire.getInstance().reload();
    }

    @Override
    protected String[] getName() {
        return new String[]{"reloadsapphire"};
    }
}
