CREATE TABLE IF NOT EXISTS ofPfRules (
   id           BIGINT          NOT NULL AUTO_INCREMENT,
   ruleorder    BIGINT      ,
   type         varchar(255)     ,
   tojid       varchar(255)    ,
   fromjid     varchar(255)    ,
   rulef         varchar(255)   ,
   disabled     boolean,
   log          boolean,
   description  varchar(255),
   sourcetype   varchar(255),
   desttype     varchar(255),
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ofMsn(
   id           BIGINT          NOT NULL AUTO_INCREMENT,
   jid          varchar(255)  ,
   msn          varchar(255)  ,
   enable       boolean,
   PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS ofSms(
   id           BIGINT          NOT NULL AUTO_INCREMENT,
   jid          varchar(255)  ,
   cellphone    varchar(20)  ,
   enable       boolean,
   PRIMARY KEY(id)

);

INSERT INTO ofVersion(name,version) values('imMonitor',1);