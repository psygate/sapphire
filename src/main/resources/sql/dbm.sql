-- SCRIPT INFORMATION --
-- Types: mysql mariadb
-- Version: 1
-- Upgrades: 0
-- SCRIPT INFORMATION --

START TRANSACTION;
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS sapphire_tower CASCADE;
DROP TABLE IF EXISTS sapphire_tower_block CASCADE;

CREATE TABLE sapphire_tower (
  tower_id         BIGINT     NOT NULL AUTO_INCREMENT,
  group_id         BIGINT     NOT NULL,
  x                INTEGER    NOT NULL,
  y                INTEGER    NOT NULL,
  z                INTEGER    NOT NULL,
  world_uuid       BINARY(16) NOT NULL,
  participant_size INTEGER    NOT NULL,
  range_size       INTEGER    NOT NULL,
  FOREIGN KEY (group_id) REFERENCES ivory_groups (group_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  PRIMARY KEY (tower_id)
);

CREATE TABLE sapphire_tower_block (
  tower_id   BIGINT     NOT NULL,
  x          INTEGER    NOT NULL,
  y          INTEGER    NOT NULL,
  z          INTEGER    NOT NULL,
  world_uuid BINARY(16) NOT NULL,
  material   INTEGER    NOT NULL,
  FOREIGN KEY (tower_id) REFERENCES sapphire_tower (tower_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  PRIMARY KEY (x, y, z, world_uuid),
  UNIQUE (tower_id, x, y, z, world_uuid)
);

SET foreign_key_checks = 1;
COMMIT;