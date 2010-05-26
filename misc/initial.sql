CREATE DATABASE openttd;

USE openttd;

CREATE TABLE `chat` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `nick` varchar(20) NOT NULL,
  `msg` text NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `nick` (`nick`),
  KEY `time` (`time`),
  KEY `game_id` (`game_id`)
);

CREATE TABLE `action` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `nick` varchar(20) NOT NULL,
  `action` enum('join','leave') NOT NULL,
  `extra` text,
  PRIMARY KEY  (`id`),
  KEY `nick` (`nick`),
  KEY `time` (`time`),
  KEY `action` (`action`),
  KEY `game_id` (`game_id`)
);

CREATE TABLE `nature_stats` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `gamedate` date default NULL,
  `water` int(11) default NULL,
  `city_tiles` int(11) default NULL,
  `trees` int(11) default NULL,
  `needle` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `gamedate` (`gamedate`),
  KEY `game_id` (`game_id`),
  KEY `needle` (`needle`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `company` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `company_id` int(11) NOT NULL,
  `name` varchar(40) default NULL,
  `colour` varchar(20) default NULL,
  `founded` smallint(6) default NULL,
  PRIMARY KEY  (`id`),
  KEY `company_id` (`company_id`),
  KEY `game_id` (`game_id`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `company_stats` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `gamedate` date default NULL,
  `company_id` int(11) NOT NULL,
  `money` bigint(20) default NULL,
  `loan` bigint(20) default NULL,
  `value` bigint(20) default NULL,
  `trains` smallint(6) default NULL,
  `roadvs` smallint(6) default NULL,
  `planes` smallint(6) default NULL,
  `ships` smallint(6) default NULL,
  `income` bigint(20) default NULL,
  `expenses` bigint(20) default NULL,
  `cargo` int(11) default NULL,
  `tiles` int(11) default NULL,
  `needle` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `company_id` (`company_id`),
  KEY `gamedate` (`gamedate`),
  KEY `game_id` (`game_id`),
  KEY `needle` (`needle`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `company_annual` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `company_id` int(11) NOT NULL,
  `year` smallint(6) NOT NULL,
  `cost_construction` bigint(20) default NULL,
  `cost_new_vs` bigint(20) default NULL,
  `cost_train` bigint(20) default NULL,
  `cost_road` bigint(20) default NULL,
  `cost_air` bigint(20) default NULL,
  `cost_ship` bigint(20) default NULL,
  `cost_property` bigint(20) default NULL,
  `income_train` bigint(20) default NULL,
  `income_road` bigint(20) default NULL,
  `income_air` bigint(20) default NULL,
  `income_ship` bigint(20) default NULL,
  `loan_interest` bigint(20) default NULL,
  `other` bigint(20) default NULL,
  `needle` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `company_id` (`company_id`),
  KEY `year` (`year`),
  KEY `game_id` (`game_id`),
  KEY `needle` (`needle`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `gamedate` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `ingame` date NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `time` (`time`),
  KEY `ingame` (`ingame`),
  KEY `game_id` (`game_id`)
);

CREATE TABLE `game` (
  `id` int(11) NOT NULL auto_increment,
  `name` text,
  PRIMARY KEY  (`id`)
);

DELIMITER |
CREATE FUNCTION update_company (`in_game_id` int,
       		       	       `in_company_id` int,
		       	       `in_name` varchar(40),
		       	       `in_colour` varchar(20),
		       	       `in_founded` smallint)
RETURNS boolean
BEGIN
	DECLARE last_id int;
	DECLARE res boolean;
	SELECT id INTO last_id
  	       FROM company
  	       WHERE game_id=in_game_id AND company_id=in_company_id
	       ORDER BY id DESC
	       LIMIT 1;
	SELECT in_name=name AND  in_colour=colour AND in_founded=founded
	       INTO res
	       FROM company
	       WHERE id=last_id;		
	IF res THEN RETURN FALSE; END IF;
	INSERT company (game_id,company_id,name,colour,founded)
	       VALUES(in_game_id,in_company_id,in_name,in_colour,in_founded);
	RETURN TRUE;
END|
DELIMITER ;

DELIMITER |
-- Returns the "importance" of an integer. It counts successive LSB
-- zero-bits. Used in triggers and it helps picking just a fraction of
-- all data .
CREATE FUNCTION msb_zeros (`n` int)
RETURNS int
BEGIN
	DECLARE res INT DEFAULT 0;
	WHILE (n & 1 = 0 AND res < 32) DO
	      SET n = n >> 1;
	      SET res = res + 1;
	END WHILE;
	RETURN res;
END|
DELIMITER ;

-- Calculates the optimal needle value. You need to know the number of
-- samples and the minimum number of samples you need. You can use
-- this function inside an SQL query. See README for examples.
CREATE FUNCTION optimal_limit (`n` int, `minimum` int)
RETURNS int
RETURN FLOOR(LOG2(n/minimum));

-- Fills needle for company quarter year stats. A needle consists from
-- quarter number (0-3) and a year.
CREATE TRIGGER sew_company_stats BEFORE INSERT ON company_stats FOR EACH ROW
SET NEW.needle=msb_zeros(4*YEAR(NEW.gamedate)+(MONTH(NEW.gamedate)-1)/3);

-- Fills needle for company annual stats.
CREATE TRIGGER sew_company_annual BEFORE INSERT ON company_annual FOR EACH ROW
SET NEW.needle=msb_zeros(NEW.year);

-- Fills needle for nature stats.
CREATE TRIGGER sew_nature BEFORE INSERT ON nature_stats FOR EACH ROW
SET NEW.needle=msb_zeros(4*YEAR(NEW.gamedate)+(MONTH(NEW.gamedate)-1)/3);

DELIMITER |
-- Fills needle values. Used to fill needles to old data. Not used in
-- the code, used only for converting from legacy format. NOTE:
-- There's only company_stats and company_annual needles because
-- needles were implemented when there was not nature etc.
CREATE PROCEDURE fill_needles ()
BEGIN
	UPDATE company_stats
	       SET needle=msb_zeros(4*YEAR(gamedate)+(MONTH(gamedate)-1)/3)
	       WHERE needle IS NULL;
	UPDATE company_annual
	       SET needle=msb_zeros(year) 
	       WHERE needle IS NULL;
END|
DELIMITER ;
