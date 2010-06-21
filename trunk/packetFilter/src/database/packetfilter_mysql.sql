CREATE TABLE `ofMsn` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `jid` VARCHAR(255) DEFAULT NULL,
  `msn` VARCHAR(255) DEFAULT NULL,
  `enable` TINYINT(1) DEFAULT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `jid` (`jid`)
)DEFAULT CHARSET=utf8 ;

CREATE TABLE `ofSms` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `jid` VARCHAR(255) DEFAULT NULL,
  `cellphone` VARCHAR(20) DEFAULT NULL,
  `enable` TINYINT(1) DEFAULT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `jid` (`jid`)
) DEFAULT CHARSET=utf8 ;

INSERT INTO ofVersion(name,version) values('packetfilter',1);