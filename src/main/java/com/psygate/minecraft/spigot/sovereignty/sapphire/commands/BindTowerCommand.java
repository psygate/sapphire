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

import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.ReinforcementManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.sapphire.data.Tower;
import com.psygate.minecraft.spigot.sovereignty.sapphire.data.TowerManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.data.TowerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by psygate on 11.06.2016.
 */
public class BindTowerCommand extends NucleusPlayerCommand {

    public BindTowerCommand() {
        super(0, 0);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        Block search = player.getTargetBlock((Set<Material>) null, 5);

        if (!ReinforcementManager.getInstance().isReinforced(new BlockKey(search))) {
            throw new CommandException("Block is not reinforced, tower cannot be bound to a group.");
        }

        if (TowerManager.getInstance().hasTower(search)) {
            throw new CommandException("This is already a tower.");
        }
        Tower tower = TowerUtil.getTower(search).filter(Tower::isValid).orElseThrow(() -> new CommandException("No tower template found."));
        TowerManager.getInstance().createTower(tower);

        if (tower.getTowerID() == -1) {
            throw new CommandException("Tower creation failed.");
        }
        player.sendMessage(new String[]{
                ChatColor.GREEN + "Tower found and bound to group.",
                ChatColor.YELLOW + "Participant Capacity: " + tower.getMaxParticipants(),
                ChatColor.YELLOW + "Range: " + tower.getRange()
        });
    }

    @Override
    protected String[] getName() {
        return new String[]{"bindtower"};
    }
}
