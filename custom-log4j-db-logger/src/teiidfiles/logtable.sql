

CREATE TABLE IF NOT EXISTS TEIID_COMMANDLOG
(
   EVENT_TIME timestamp,  
   VDB varchar(254),
   VERSION integer,
   EVENT_TYPE varchar(50),  
   APPLICATION_NAME varchar(254),
   PRINCIPAL_NAME varchar(100),
   SESSION_ID varchar(100),
   REQUEST_ID varchar(100),
   TRANSACTION_ID varchar(100),
   SOURCE_COMMANDID bigint,
   MODELNAME varchar(100),
   TRANSLATORNAME varchar(100),
   ISSOURCE char,   
   ROW_COUNT integer,
   SQL varchar(4000),
   PLAN varchar(4000)
);

