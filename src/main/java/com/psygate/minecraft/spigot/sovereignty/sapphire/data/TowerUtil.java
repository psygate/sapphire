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

package com.psygate.minecraft.spigot.sovereignty.sapphire.data;

import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.ReinforcementManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.bukkit.Material.*;

/**
 * Created by psygate on 11.06.2016.
 */
public class TowerUtil {
    public static Optional<Tower> getTower(Block redstoneLamp) {
        if (isTower(redstoneLamp)) {
            Tower tower = new Tower(redstoneLamp);
            tower.addBlock(new TowerBlock(redstoneLamp));
            tower.addBlock(new TowerBlock(redstoneLamp.getRelative(BlockFace.DOWN)));
            tower.addBlock(new TowerBlock(redstoneLamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH)));
            tower.addBlock(new TowerBlock(redstoneLamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH)));
            tower.addBlock(new TowerBlock(redstoneLamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST)));
            tower.addBlock(new TowerBlock(redstoneLamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST)));
            appendHead(tower, appendAntennas(tower, redstoneLamp));
            try {
                tower.setGroupID(ReinforcementManager.getInstance().getReinforcement(new BlockKey(redstoneLamp)).getGroupID().longValue());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

            return Optional.of(tower);
        } else {
            return Optional.empty();
        }
    }

    private static void appendHead(Tower tower, Block block) {
        tower.addBlock(new TowerBlock(block));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.UP)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.UP, 2)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.NORTH)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.EAST)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.SOUTH)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.WEST)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH)));
        tower.addBlock(new TowerBlock(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST)));
    }

    private static Block appendAntennas(Tower tower, Block redstoneLamp) {
        int fence = 0;
        for (int i = 1; i < redstoneLamp.getWorld().getMaxHeight() - redstoneLamp.getY(); i++) {
            Block block = redstoneLamp.getRelative(BlockFace.UP, i);
            tower.addBlock(new TowerBlock(block));
            if (block.getType() == IRON_FENCE) {
                fence++;
            } else if (block.getType() == DIAMOND_BLOCK) {
                tower.setParticipantSize(fence);
                fence = 0;
            } else if (block.getType() == GOLD_BLOCK) {
                tower.setRangeSize(fence);
                fence = 0;
            } else if (block.getType() == IRON_BLOCK) {
                return block;
            }
        }

        throw new IllegalStateException();
    }

    public static boolean isTower(Block redstonelamp) {
        return isReinforced(redstonelamp) && isBase(redstonelamp) && checkUp(redstonelamp);
    }

    private static boolean isReinforced(Block redstonelamp) {
        try {
            return ReinforcementManager.getInstance().isReinforced(new BlockKey(redstonelamp));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkUp(Block redstonelamp) {
        boolean hasGold = false, hasDiamond = false;

        for (int i = 1; i < redstonelamp.getWorld().getMaxHeight() - redstonelamp.getY(); i++) {
            Block block = redstonelamp.getRelative(BlockFace.UP, i);

            if (block.getType() == IRON_FENCE) {
                //This is ignored
            } else if (block.getType() == Material.DIAMOND_BLOCK) {
                if (hasDiamond) {
                    return false;
                }
                hasDiamond = true;
            } else if (block.getType() == Material.GOLD_BLOCK) {
                if (hasGold) {
                    return false;
                }
                hasGold = true;
            } else if (block.getType() == Material.IRON_BLOCK) {
                return hasDiamond && hasGold && checkHead(block);
            } else {
                return false;
            }
        }

        return false;
    }

    private static boolean checkHead(Block block) {
        return isHeadFan(block) && isHeadFan(block.getRelative(BlockFace.UP)) && block.getRelative(BlockFace.UP, 2).getType() == Material.REDSTONE_TORCH_ON;
    }

    private static boolean isHeadFan(Block block) {
        return block.getType() == Material.IRON_BLOCK
                && block.getRelative(BlockFace.NORTH).getType() == Material.IRON_TRAPDOOR
                && block.getRelative(BlockFace.SOUTH).getType() == Material.IRON_TRAPDOOR
                && block.getRelative(BlockFace.EAST).getType() == Material.IRON_TRAPDOOR
                && block.getRelative(BlockFace.WEST).getType() == Material.IRON_TRAPDOOR;
    }

    public static boolean isBase(Block redstonelamp) {
        return redstonelamp.getType() == Material.REDSTONE_LAMP_ON
                && redstonelamp.getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK
                && redstonelamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getType() == Material.OBSIDIAN
                && redstonelamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getType() == Material.OBSIDIAN
                && redstonelamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getType() == Material.OBSIDIAN
                && redstonelamp.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getType() == Material.OBSIDIAN;
    }
}
