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

INSERT INTO ofVersion(name,version) values('packetfilter',1);