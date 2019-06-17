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

import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.sapphire.Sapphire;
import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.records.SapphireTowerBlockRecord;
import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.records.SapphireTowerRecord;
import com.psygate.spatial.primitives.IntAABB2D;
import com.psygate.spatial.trees.AdaptiveIntAreaQuadTree;
import com.psygate.spatial.trees.IntAreaQuadTree;
import org.bukkit.block.Block;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.stream.Collectors;

import static com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.Tables.SAPPHIRE_TOWER;
import static com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.Tables.SAPPHIRE_TOWER_BLOCK;

/**
 * Created by psygate on 11.06.2016.
 */
public class TowerManager {
    private static TowerManager instance;

    public static TowerManager getInstance() {
        if (instance == null) {
            instance = new TowerManager();
        }
        return instance;
    }

    private final Map<Long, List<Tower>> towerByGroup = new HashMap<>();
    private final Map<UUID, AdaptiveIntAreaQuadTree<Tower>> towerMap = new HashMap<>();
    private final Map<BlockKey, Tower> towerByBlock = new HashMap<>();

    private TowerManager() {
        loadTowers();
    }

    private void loadTowers() {
        Sapphire.DBI().submit((conf) -> {
            DSLContext ctx = DSL.using(conf);

            Map<Long, Tower> towermap = ctx.selectFrom(SAPPHIRE_TOWER).fetch()
                    .map((r) -> new Tower(r))
                    .stream()
                    .collect(Collectors.toMap(t -> t.getTowerID(), t -> t));

            ctx.selectFrom(SAPPHIRE_TOWER_BLOCK).fetch()
                    .forEach(block -> towermap.get(block.getTowerId()).addBlock(new TowerBlock(block)));

            towermap.values().forEach(this::addTower);
        });
    }

    public void createTower(Tower tower) {

        Sapphire.DBI().submit((conf) -> {
            DSLContext ctx = DSL.using(conf);

            for (TowerBlock block : tower.getBlocks()) {
                ctx.deleteFrom(SAPPHIRE_TOWER_BLOCK)
                        .where(SAPPHIRE_TOWER_BLOCK.X.eq(block.getX()))
                        .and(SAPPHIRE_TOWER_BLOCK.Y.eq(block.getY()))
                        .and(SAPPHIRE_TOWER_BLOCK.Z.eq(block.getZ()))
                        .and(SAPPHIRE_TOWER_BLOCK.WORLD_UUID.eq(block.getWorld()))
                        .execute();
            }

            ctx.deleteFrom(SAPPHIRE_TOWER)
                    .where(SAPPHIRE_TOWER.X.eq(tower.getX()))
                    .and(SAPPHIRE_TOWER.Y.eq(tower.getY()))
                    .and(SAPPHIRE_TOWER.Z.eq(tower.getZ()))
                    .and(SAPPHIRE_TOWER.WORLD_UUID.eq(tower.getWorld()))
                    .execute();

            long id = ctx.insertInto(SAPPHIRE_TOWER).set(
                    new SapphireTowerRecord(
                            null,
                            tower.getGroupID(),
                            tower.getX(),
                            tower.getY(),
                            tower.getZ(),
                            tower.getWorld(),
                            tower.getParticipantSize(),
                            tower.getRangeSize()
                    )
            ).returning(SAPPHIRE_TOWER.TOWER_ID).fetchOne().getTowerId();
            ctx.batchInsert(
                    tower.getBlocks().stream()
                            .map(b -> new SapphireTowerBlockRecord(
                                    id,
                                    b.getX(),
                                    b.getY(),
                                    b.getZ(),
                                    b.getWorld(),
                                    b.getMaterial()
                            ))
                            .collect(Collectors.toList())
            ).execute();
            tower.setTowerID(id);
        });

        addTower(tower);
    }

    private void addTower(Tower tower) {
        towerByGroup.putIfAbsent(tower.getGroupID(), new LinkedList<>());
        towerByGroup.get(tower.getGroupID()).add(tower);
        towerMap.putIfAbsent(tower.getWorld(), new AdaptiveIntAreaQuadTree<>(new IntAABB2D(0, 0, 10000, 10000), 8));
        towerMap.get(tower.getWorld()).add(tower);
        for (TowerBlock key : tower.getBlocks()) {
            BlockKey bk = new BlockKey(key.getX(), key.getY(), key.getZ(), key.getWorld());
            towerByBlock.put(bk, tower);
        }
    }

    public void breakTower(BlockKey block) {
        if (towerByBlock.containsKey(block)) {
            Tower tower = towerByBlock.get(block);
            for (TowerBlock key : tower.getBlocks()) {
                BlockKey bk = new BlockKey(key.getX(), key.getY(), key.getZ(), key.getWorld());
                towerByBlock.remove(bk);
            }

            towerMap.get(tower.getWorld()).remove(tower);
            towerByGroup.get(tower.getGroupID()).remove(tower);

            Sapphire.DBI().asyncSubmit((conf) -> {
                DSLContext ctx = DSL.using(conf);
                ctx.deleteFrom(SAPPHIRE_TOWER_BLOCK).where(SAPPHIRE_TOWER_BLOCK.TOWER_ID.eq(tower.getTowerID())).execute();
                ctx.deleteFrom(SAPPHIRE_TOWER).where(SAPPHIRE_TOWER.TOWER_ID.eq(tower.getTowerID())).execute();
            });
        }
    }

    public List<Tower> getTowers(long groupID) {
        return towerByGroup.getOrDefault(groupID, Collections.emptyList());
    }

    public IntAreaQuadTree<Tower> getTowersTree(long groupID) {
        List<Tower> towers = getTowers(groupID);
        IntAreaQuadTree<Tower> tree = new IntAreaQuadTree<Tower>(towers.stream().map(Tower::getBounds).reduce(new IntAABB2D(0, 0, 100, 100), (a, b) -> a.merge(b)));
        tree.addAll(towers);

        return tree;
    }

    public boolean hasTower(Block search) {
        return towerByBlock.containsKey(new BlockKey(search));
    }
}
