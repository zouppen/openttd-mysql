CREATE TABLE `chat` ( `id` int(11) NOT NULL auto_increment, `time`
  timestamp NOT NULL default CURRENT_TIMESTAMP, `nick` varchar(20) NOT
  NULL, `msg` text NOT NULL, PRIMARY KEY (`id`), KEY `nick` (`nick`),
  KEY `time` (`time`));

CREATE TABLE `action` ( `id` int(11) NOT NULL auto_increment, `time`
  timestamp NOT NULL default CURRENT_TIMESTAMP, `nick` varchar(20) NOT
  NULL, `action` enum('join','leave') NOT NULL, `extra` text, PRIMARY
  KEY (`id`), KEY `nick` (`nick`), KEY `time` (`time`), KEY `action`
  (`action`));

CREATE TABLE `company` ( `id` int(11) NOT NULL auto_increment, `time`
  timestamp NOT NULL default CURRENT_TIMESTAMP, `company_id` int(11)
  NOT NULL, `name` varchar(40) default NULL, `colour` varchar(20)
  default NULL, `founded` smallint(6) default NULL, PRIMARY KEY
  (`id`), UNIQUE KEY `block` (`company_id`,`name`,`colour`,`founded`),
  KEY `company_id` (`company_id`) );

CREATE TABLE `company_stats` ( `id` int(11) NOT NULL auto_increment,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP, `company_id`
  int(11) NOT NULL, `money` bigint(20) default NULL, `loan` bigint(20)
  default NULL, `value` bigint(20) default NULL, `trains` smallint(6)
  default NULL, `roadvs` smallint(6) default NULL, `planes`
  smallint(6) default NULL, `ships` smallint(6) default NULL, PRIMARY
  KEY (`id`), KEY `time` (`time`), KEY `company_id` (`company_id`) );
