/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.daos;


import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.SapphireTowerBlock;
import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.records.SapphireTowerBlockRecord;

import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.bukkit.Material;
import org.jooq.Configuration;
import org.jooq.Record4;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SapphireTowerBlockDao extends DAOImpl<SapphireTowerBlockRecord, com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock, Record4<Integer, Integer, Integer, UUID>> {

	/**
	 * Create a new SapphireTowerBlockDao without any configuration
	 */
	public SapphireTowerBlockDao() {
		super(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK, com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock.class);
	}

	/**
	 * Create a new SapphireTowerBlockDao with an attached configuration
	 */
	public SapphireTowerBlockDao(Configuration configuration) {
		super(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK, com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record4<Integer, Integer, Integer, UUID> getId(com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock object) {
		return compositeKeyRecord(object.getX(), object.getY(), object.getZ(), object.getWorldUuid());
	}

	/**
	 * Fetch records that have <code>tower_id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock> fetchByTowerId(Long... values) {
		return fetch(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK.TOWER_ID, values);
	}

	/**
	 * Fetch records that have <code>x IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock> fetchByX(Integer... values) {
		return fetch(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK.X, values);
	}

	/**
	 * Fetch records that have <code>y IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock> fetchByY(Integer... values) {
		return fetch(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK.Y, values);
	}

	/**
	 * Fetch records that have <code>z IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock> fetchByZ(Integer... values) {
		return fetch(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK.Z, values);
	}

	/**
	 * Fetch records that have <code>world_uuid IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock> fetchByWorldUuid(UUID... values) {
		return fetch(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK.WORLD_UUID, values);
	}

	/**
	 * Fetch records that have <code>material IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTowerBlock> fetchByMaterial(Material... values) {
		return fetch(SapphireTowerBlock.SAPPHIRE_TOWER_BLOCK.MATERIAL, values);
	}
}
