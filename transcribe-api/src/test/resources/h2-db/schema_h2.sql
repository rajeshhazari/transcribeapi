-- Thanks to Amir Kibbar and Peter Rietzler for contributing the schema for H2 database,
-- and verifying that it works with Quartz's StdJDBCDelegate
--
-- Note, Quartz depends on row-level locking which means you must use the MVCC=TRUE
-- setting on your H2 database, or you will experience dead-locks
--
--
-- In your Quartz properties file, you'll need to set
-- org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate


DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;
--ALTER TABLE USERSTRANSCRIPTIONS DROP CONSTRAINT IF EXISTS FK_Users_Transcriptions_users;
--ALTER TABLE REGISTEREDAPPUSERS DROP CONSTRAINT IF EXISTS FK_REGUSERS_USERNAME_EMAIL;
DROP TABLE IF EXISTS APPUSERS;
DROP TABLE IF EXISTS AUTHORITIES;
DROP TABLE IF EXISTS USERREGVERIFYLOGDETIALS;
DROP TABLE IF EXISTS USERREGISTRATIONSLOGDETIALS;
DROP TABLE IF EXISTS USERSTRANSCRIPTIONS;
DROP TABLE IF EXISTS TRANSCRIBEFILELOG;
DROP TABLE IF EXISTS REGISTEREDAPPUSERS;

--ALTER TABLE USER_SESSIONS_ATTRIBUTES DROP CONSTRAINT IF EXISTS USER_SESSIONS_ATTRIBUTES_FK;
DROP TABLE IF EXISTS USER_SESSIONS;
DROP TABLE IF EXISTS USER_SESSIONS_ATTRIBUTES;
--DROP TABLE IF EXISTS USER;


CREATE TABLE QRTZ_CALENDARS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  CALENDAR_NAME VARCHAR (200)  NOT NULL ,
  CALENDAR IMAGE NOT NULL
);

CREATE TABLE QRTZ_CRON_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  CRON_EXPRESSION VARCHAR (120)  NOT NULL ,
  TIME_ZONE_ID VARCHAR (80)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  ENTRY_ID VARCHAR (95)  NOT NULL ,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  INSTANCE_NAME VARCHAR (200)  NOT NULL ,
  FIRED_TIME BIGINT NOT NULL ,
  SCHED_TIME BIGINT NOT NULL ,
  PRIORITY INTEGER NOT NULL ,
  STATE VARCHAR (16)  NOT NULL,
  JOB_NAME VARCHAR (200)  NULL ,
  JOB_GROUP VARCHAR (200)  NULL ,
  IS_NONCONCURRENT BOOLEAN  NULL ,
  REQUESTS_RECOVERY BOOLEAN  NULL
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL
);

CREATE TABLE QRTZ_SCHEDULER_STATE (
  SCHED_NAME VARCHAR(120) NOT NULL,
  INSTANCE_NAME VARCHAR (200)  NOT NULL ,
  LAST_CHECKIN_TIME BIGINT NOT NULL ,
  CHECKIN_INTERVAL BIGINT NOT NULL
);

CREATE TABLE QRTZ_LOCKS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  LOCK_NAME VARCHAR (40)  NOT NULL
);

CREATE TABLE QRTZ_JOB_DETAILS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  JOB_NAME VARCHAR (200)  NOT NULL ,
  JOB_GROUP VARCHAR (200)  NOT NULL ,
  DESCRIPTION VARCHAR (250) NULL ,
  JOB_CLASS_NAME VARCHAR (250)  NOT NULL ,
  IS_DURABLE BOOLEAN  NOT NULL ,
  IS_NONCONCURRENT BOOLEAN  NOT NULL ,
  IS_UPDATE_DATA BOOLEAN  NOT NULL ,
  REQUESTS_RECOVERY BOOLEAN  NOT NULL ,
  JOB_DATA IMAGE NULL
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  REPEAT_COUNT BIGINT NOT NULL ,
  REPEAT_INTERVAL BIGINT NOT NULL ,
  TIMES_TRIGGERED BIGINT NOT NULL
);

CREATE TABLE qrtz_simprop_triggers
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INTEGER NULL,
    INT_PROP_2 INTEGER NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 BOOLEAN NULL,
    BOOL_PROP_2 BOOLEAN NULL
);

CREATE TABLE QRTZ_BLOB_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  BLOB_DATA IMAGE NULL
);

CREATE TABLE QRTZ_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  JOB_NAME VARCHAR (200)  NOT NULL ,
  JOB_GROUP VARCHAR (200)  NOT NULL ,
  DESCRIPTION VARCHAR (250) NULL ,
  NEXT_FIRE_TIME BIGINT NULL ,
  PREV_FIRE_TIME BIGINT NULL ,
  PRIORITY INTEGER NULL ,
  TRIGGER_STATE VARCHAR (16)  NOT NULL ,
  TRIGGER_TYPE VARCHAR (8)  NOT NULL ,
  START_TIME BIGINT NOT NULL ,
  END_TIME BIGINT NULL ,
  CALENDAR_NAME VARCHAR (200)  NULL ,
  MISFIRE_INSTR SMALLINT NULL ,
  JOB_DATA IMAGE NULL
);

ALTER TABLE QRTZ_CALENDARS  ADD
  CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY
  (
    SCHED_NAME,
    CALENDAR_NAME
  );

ALTER TABLE QRTZ_CRON_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_CRON_TRIGGERS PRIMARY KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_FIRED_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_FIRED_TRIGGERS PRIMARY KEY
  (
    SCHED_NAME,
    ENTRY_ID
  );

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS  ADD
  CONSTRAINT PK_QRTZ_PAUSED_TRIGGER_GRPS PRIMARY KEY
  (
    SCHED_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_SCHEDULER_STATE  ADD
  CONSTRAINT PK_QRTZ_SCHEDULER_STATE PRIMARY KEY
  (
    SCHED_NAME,
    INSTANCE_NAME
  );

ALTER TABLE QRTZ_LOCKS  ADD
  CONSTRAINT PK_QRTZ_LOCKS PRIMARY KEY
  (
    SCHED_NAME,
    LOCK_NAME
  );

ALTER TABLE QRTZ_JOB_DETAILS  ADD
  CONSTRAINT PK_QRTZ_JOB_DETAILS PRIMARY KEY
  (
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP
  );

ALTER TABLE QRTZ_SIMPLE_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_SIMPLE_TRIGGERS PRIMARY KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_SIMPROP_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_SIMPROP_TRIGGERS PRIMARY KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_TRIGGERS PRIMARY KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_CRON_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) REFERENCES QRTZ_TRIGGERS (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) ON DELETE CASCADE;


ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) REFERENCES QRTZ_TRIGGERS (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) ON DELETE CASCADE;

ALTER TABLE QRTZ_SIMPROP_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) REFERENCES QRTZ_TRIGGERS (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) ON DELETE CASCADE;


ALTER TABLE QRTZ_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS FOREIGN KEY
  (
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP
  ) REFERENCES QRTZ_JOB_DETAILS (
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP
  );

  --ALTER SEQUENCE Cast1_id_seq RESTART WITH 100;
  CREATE TABLE APPUSERS(
  userid identity not null auto_increment,
  username VARCHAR(100) not null unique ,
  password VARCHAR(100) not null,
  first_name VARCHAR(50) not null unique ,
  last_name VARCHAR(50) not null,
  zipcode VARCHAR(50) not null,
  email VARCHAR(100) not null unique ,
  phone_number VARCHAR(10),
  active boolean default false,
  disabled boolean default false,
  verified boolean default false,
  locked boolean default false,
  registered_date timestamp default CURRENT_TIMESTAMP,
  last_modified timestamp default CURRENT_TIMESTAMP,
  primary key (userid)
);


--CREATE TABLE USER(
--   id identity not null auto_increment,
--   username VARCHAR(100) not null,
--   password VARCHAR(50) not null,
--   primary key (id)
-- );

CREATE TABLE REGISTEREDAPPUSERS(
  id identity not null auto_increment,
  username VARCHAR(100) not null,
   email VARCHAR(100) not null,
  primary key (id),
  CONSTRAINT FK_REGUSERS_USERNAME_EMAIL foreign key (username,email) references APPUSERS(username,email)
);


 CREATE TABLE AUTHORITIES (
  auth_id identity not null auto_increment,
  username VARCHAR(256),
  authority VARCHAR(256),
  primary key (auth_id)
);

CREATE TABLE USERREGVERIFYLOGDETIALS (
  id identity not null auto_increment,
  username VARCHAR(100) not null,  
  disabled boolean default false,
  verified boolean default false,
  verification_email_to VARCHAR(100) not null,
  verified_reg_ip VARCHAR(50) ,
  verification_email_sent boolean default false,
  verification_email_code VARCHAR(50) not null,
  email_sent_date  timestamp default CURRENT_TIMESTAMP,
  verfication_date timestamp , primary key (id)
);

CREATE TABLE USERREGISTRATIONSLOGDETIALS (
  id identity not null auto_increment,
  username VARCHAR(100) not null,
  firstName VARCHAR(50) not null,
  lastName VARCHAR(50) not null,
  zipcode VARCHAR(50) not null,
  email VARCHAR(100) not null,
  password VARCHAR(50) not null,
  old_username VARCHAR(100) ,
  old_firstName VARCHAR(50) ,
  old_lastName VARCHAR(50) ,
  old_zipcode VARCHAR(50) ,
  registered_ip VARCHAR(50) ,
  old_email VARCHAR(100) ,
  old_password VARCHAR(50) not null,
  registration_log_date timestamp default CURRENT_TIMESTAMP,
  primary key (id)
);

CREATE TABLE USERSTRANSCRIPTIONS (
  log_id identity not null auto_increment,
  username VARCHAR(100) not null,
  email VARCHAR(100) not null,
  transcription_req_id VARCHAR(100) not null,
  transcribe_res_type VARCHAR(20) default 'application/json',
  file_name VARCHAR(100) not null,
  session_id VARCHAR(100) not null,
  userid Long,
  transcribed boolean default false,
  downloaded boolean default true,
  transcribe_res_available_format VARCHAR(20) default 'application/json',
  transcribe_res_downloaded_format VARCHAR(20) default 'application/json',
  transcribed_date timestamp default CURRENT_TIMESTAMP,
  uploaded_date timestamp default CURRENT_TIMESTAMP,
  primary key (log_id)
);

--
--CREATE TABLE UsersTranscriptionsDetails (
--  did identity not null auto_increment,
--  transcription_req_id  VARCHAR(100) not null,
--  fileName  VARCHAR(100) not null,
--  transcription_date TIMESTAMP default now(),
--  primary key (did)
--);

ALTER TABLE USERSTRANSCRIPTIONS ADD
  CONSTRAINT FK_Users_Transcriptions_users FOREIGN KEY
  (
    username,
    email
  ) REFERENCES APPUSERS(
    username,
    email
  );

  CREATE TABLE TRANSCRIBEFILELOG (
  log_id identity not null auto_increment,
  username VARCHAR(100),
  file_name VARCHAR(256),
  session_id VARCHAR(256),
  transcribe_req_id Long,
  transcribe_res_type VARCHAR(20),
  
  primary key (log_id)
);
  
  CREATE TABLE USER_SESSIONS (
  PRIMARY_ID CHAR(36) NOT NULL,
  SESSION_ID CHAR(36) NOT NULL,
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  EXPIRY_TIME BIGINT NOT NULL,
  PRINCIPAL_NAME VARCHAR(100),
  CONSTRAINT USER_SESSIONS_PK PRIMARY KEY (PRIMARY_ID)
);
 
CREATE UNIQUE INDEX USER_SESSIONS_IX1 ON USER_SESSIONS (SESSION_ID);
CREATE INDEX USER_SESSIONS_IX2 ON USER_SESSIONS (EXPIRY_TIME);
CREATE INDEX USER_SESSIONS_IX3 ON USER_SESSIONS (PRINCIPAL_NAME);
 
CREATE TABLE USER_SESSIONS_ATTRIBUTES (
  SESSION_PRIMARY_ID CHAR(36) NOT NULL,
  ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
  ATTRIBUTE_BYTES LONGVARBINARY NOT NULL,
  CONSTRAINT USER_SESSIONS_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
  CONSTRAINT USER_SESSIONS_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES USER_SESSIONS(PRIMARY_ID) ON DELETE CASCADE
);
 
CREATE INDEX USER_SESSIONS_ATTRIBUTES_IX1 ON USER_SESSIONS_ATTRIBUTES (SESSION_PRIMARY_ID);


  
COMMIT;
