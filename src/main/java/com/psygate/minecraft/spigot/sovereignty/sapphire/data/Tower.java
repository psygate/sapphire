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


import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.records.SapphireTowerRecord;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.ChatManager;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messages.PlayerChannelMessage;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PlayerMessenger;
import com.psygate.minecraft.spigot.sovereignty.sapphire.messaging.messengers.PluginMessenger;
import com.psygate.spatial.primitives.IntAABB2D;
import com.psygate.spatial.primitives.IntBoundable2D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by psygate on 11.06.2016.
 */
public class Tower implements IntBoundable2D {
    private int participantSize;
    private int rangeSize;
    private Set<TowerBlock> blocks = new HashSet<>();
    private long groupID = -1;
    private long towerID = -1;
    private int x, y, z;
    private UUID world;
    private IntAABB2D bounds;

    public Tower(Block anchor) {
        this.x = anchor.getX();
        this.y = anchor.getY();
        this.z = anchor.getZ();
        this.world = anchor.getWorld().getUID();
    }

    public Tower(SapphireTowerRecord r) {
        this.participantSize = r.getParticipantSize();
        this.rangeSize = r.getRangeSize();
        this.x = r.getX();
        this.y = r.getY();
        this.z = r.getZ();
        this.groupID = r.getGroupId();
        this.towerID = r.getTowerId();
        this.world = r.getWorldUuid();
    }

    public UUID getWorld() {
        return world;
    }

    public long getTowerID() {
        return towerID;
    }

    public void setTowerID(long towerID) {
        this.towerID = towerID;
    }

    public int getParticipantSize() {
        return participantSize;
    }

    public int getRangeSize() {
        return rangeSize;
    }

    public boolean isValid() {
        return matchBlocks();
    }

    private boolean matchBlocks() {
        return blocks.stream().allMatch(TowerBlock::matches);
    }

    public void setParticipantSize(int participantSize) {
        this.participantSize = participantSize;
    }

    public void setRangeSize(int rangeSize) {
        this.rangeSize = rangeSize;
    }

    public void setBlocks(Set<TowerBlock> blocks) {
        this.blocks = blocks;
    }

    public void addBlock(TowerBlock block) {
        blocks.add(block);
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public Set<TowerBlock> getBlocks() {
        return blocks;
    }

    public long getGroupID() {
        return groupID;
    }

    public int getMaxParticipants() {
        return (int) Sapphire.getConfiguration().getParticipantMul() * participantSize;
    }

    public double getRange() {
        return Sapphire.getConfiguration().getRangeMul() * rangeSize;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setBounds(IntAABB2D bounds) {
        this.bounds = bounds;
    }

    @Override
    public IntAABB2D getBounds() {
        if (bounds == null) {
            int range = (int) getRange();
            bounds = new IntAABB2D(x - range, z - range, x + range, z + range);
        }

        return bounds;
    }

    public boolean sendMessage(PlayerChannelMessage message, String render) {
        return GroupManager.getInstance().getGroup(groupID).map(group -> {
            if (group.getMembers().size() > getMaxParticipants()) {
                message.getSource().sendMessage(ChatColor.RED + "Tower member capacity exceeded.", new PluginMessenger(Sapphire.getInstance()));
                return false;
            } else {
                group.getMembers().keySet().stream()
                        .map(PlayerMessenger::new)
                        .filter(m -> distSqr(m.getLocation(), message.getSource().getLocation()) <= getRange() * getRange())
                        .forEach(v -> {
                            v.sendMessage(render, message.getSource());
                            ChatManager.getInstance().getState(v).setReplyChannel(message.getChannel());
                        });
                return true;
            }
        })
                .orElseGet(() -> Boolean.FALSE);
    }

    private double distSqr(Location m, Location l) {
        return (m.getX() - l.getX()) * (m.getX() - l.getX()) + (m.getY() - l.getY()) * (m.getY() - l.getY()) + (m.getZ() - l.getZ()) * (m.getZ() - l.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tower tower = (Tower) o;

        if (x != tower.x) return false;
        if (y != tower.y) return false;
        if (z != tower.z) return false;
        return world != null ? world.equals(tower.world) : tower.world == null;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (world != null ? world.hashCode() : 0);
        return result;
    }
}
