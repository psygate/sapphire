/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.daos;


import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.SapphireTower;
import com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.records.SapphireTowerRecord;

import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Configuration;
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
public class SapphireTowerDao extends DAOImpl<SapphireTowerRecord, com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower, Long> {

	/**
	 * Create a new SapphireTowerDao without any configuration
	 */
	public SapphireTowerDao() {
		super(SapphireTower.SAPPHIRE_TOWER, com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower.class);
	}

	/**
	 * Create a new SapphireTowerDao with an attached configuration
	 */
	public SapphireTowerDao(Configuration configuration) {
		super(SapphireTower.SAPPHIRE_TOWER, com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower object) {
		return object.getTowerId();
	}

	/**
	 * Fetch records that have <code>tower_id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByTowerId(Long... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.TOWER_ID, values);
	}

	/**
	 * Fetch a unique record that has <code>tower_id = value</code>
	 */
	public com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower fetchOneByTowerId(Long value) {
		return fetchOne(SapphireTower.SAPPHIRE_TOWER.TOWER_ID, value);
	}

	/**
	 * Fetch records that have <code>group_id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByGroupId(Long... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.GROUP_ID, values);
	}

	/**
	 * Fetch records that have <code>x IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByX(Integer... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.X, values);
	}

	/**
	 * Fetch records that have <code>y IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByY(Integer... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.Y, values);
	}

	/**
	 * Fetch records that have <code>z IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByZ(Integer... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.Z, values);
	}

	/**
	 * Fetch records that have <code>world_uuid IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByWorldUuid(UUID... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.WORLD_UUID, values);
	}

	/**
	 * Fetch records that have <code>participant_size IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByParticipantSize(Integer... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.PARTICIPANT_SIZE, values);
	}

	/**
	 * Fetch records that have <code>range_size IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.sapphire.db.model.tables.pojos.SapphireTower> fetchByRangeSize(Integer... values) {
		return fetch(SapphireTower.SAPPHIRE_TOWER.RANGE_SIZE, values);
	}
}
