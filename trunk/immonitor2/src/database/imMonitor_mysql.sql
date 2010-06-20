CREATE TABLE IF NOT EXISTS ofMonitorUser(
   username          varchar(50)  ,
   isEnable          TINYINT(1) NOT NULL,
   PRIMARY KEY(username)
);


CREATE TABLE IF NOT EXISTS ofMsn(
   username          varchar(50)  ,
   msn          varchar(255)  ,
   PRIMARY KEY(username)
);

CREATE TABLE IF NOT EXISTS ofSms(
   username          varchar(50)  ,
   cellphone    varchar(20)  ,
   PRIMARY KEY(username)
);

CREATE TABLE IF NOT EXISTS ofMonitorGroup
(
  `groupname` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`groupName`)
);

CREATE TABLE IF NOT EXISTS ofMonitorGroupUser
(
groupname   varchar(50) NOT NULL,
username    varchar(50) NOT NULL,
PRIMARY KEY(groupname,username)
);

INSERT INTO ofVersion(name,version) values('imMonitor',1);