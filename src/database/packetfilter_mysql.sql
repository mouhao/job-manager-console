CREATE TABLE ofPfRules (
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

CREATE TABLE ofMsn(
   id           BIGINT          NOT NULL AUTO_INCREMENT,
   jid          varchar(255)  ,
   msn          varchar(255)  ,
   enable       boolean
);

CREATE TABLE ofSms(
   id           BIGINT          NOT NULL AUTO_INCREMENT,
   jid          varchar(255)  ,
   cellphone    varchar(20)  ,
   enable       boolean
);

INSERT INTO ofVersion(name,version) values('packetfilter',2);