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

import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.records.SapphireTowerBlockRecord;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.UUID;

/**
 * Created by psygate on 11.06.2016.
 */
public class TowerBlock {
    private final Material material;
    private final int x, y, z;
    private final UUID world;

    public TowerBlock(Material material, int x, int y, int z, UUID world) {
        this.material = material;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public TowerBlock(Block start) {
        this.material = start.getType();
        this.x = start.getX();
        this.y = start.getY();
        this.z = start.getZ();
        this.world = start.getWorld().getUID();
    }

    public TowerBlock(SapphireTowerBlockRecord block) {
        this.material = block.getMaterial();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorldUuid();
    }

    public Material getMaterial() {
        return material;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean matches() {
        return Bukkit.getWorld(world).getBlockAt(x, y, z).getType() == material;
    }

    public UUID getWorld() {
        return world;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TowerBlock that = (TowerBlock) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        return world != null ? world.equals(that.world) : that.world == null;

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
