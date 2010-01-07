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

CREATE TABLE `company` (
  `id` int(11) NOT NULL auto_increment,
  `game_id` int(11) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `company_id` int(11) NOT NULL,
  `name` varchar(40) default NULL,
  `colour` varchar(20) default NULL,
  `founded` smallint(6) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `block` (`company_id`,`name`,`colour`,`founded`),
  KEY `company_id` (`company_id`),
  KEY `game_id` (`game_id`)
);

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
  PRIMARY KEY  (`id`),
  KEY `company_id` (`company_id`),
  KEY `gamedate` (`gamedate`),
  KEY `game_id` (`game_id`)
);

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
  PRIMARY KEY  (`id`),
  KEY `company_id` (`company_id`),
  KEY `year` (`year`),
  KEY `game_id` (`game_id`)
);

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
