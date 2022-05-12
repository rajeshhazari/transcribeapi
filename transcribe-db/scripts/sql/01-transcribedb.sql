--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
--SET COMMIT = ON;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--drop database IF EXISTS transcribeapp_db;
CREATE DATABASE transcribeapp_db;

--DROP TRIGGER last_updated ON transcribeapp_db.users;
--DROP TRIGGER last_updated ON transcribeapp_db.unregistered_users;
DROP SCHEMA if exists  transcribeapp_schema;
CREATE SCHEMA transcribeapp_schema;
ALTER DATABASE transcribeapp_db OWNER TO devuser;

--ALTER TABLE APPUSERS_TRANSCRIPTIONS DROP CONSTRAINT IF EXISTS FK_Users_Transcriptions_users;
--ALTER TABLE REGISTEREDAPPUSERS DROP CONSTRAINT IF EXISTS FK_REGUSERS_USERNAME_EMAIL;


DROP FUNCTION IF EXISTS last_updated CASCADE;
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;
DROP TABLE IF EXISTS APPUSERS CASCADE;
--DROP TABLE IF EXISTS authorities_user CASCADE;
DROP TABLE IF EXISTS APPUSERS_AUTH CASCADE;
DROP TABLE IF EXISTS AUTHORITIES_MASTER cascade;
DROP TABLE IF EXISTS USERREGVERIFYLOGDETAILS;
DROP TABLE IF EXISTS USERREGISTRATIONSLOGDETIALS;
DROP TABLE IF EXISTS APPUSERS_TRANSCRIPTIONS CASCADE;
DROP TABLE IF EXISTS TRANSCRIBEFILELOG;
DROP TABLE IF EXISTS REGISTEREDAPPUSERS;
--ALTER TABLE USER_SESSIONS_ATTRIBUTES DROP CONSTRAINT IF EXISTS USER_SESSIONS_ATTRIBUTES_FK;

DROP TABLE IF EXISTS USER_SESSIONS;
DROP TABLE IF EXISTS USER_SESSIONS_ATTRIBUTES;
DROP TABLE IF EXISTS APPUSERS_ADDRESS;
DROP SEQUENCE IF EXISTS address_address_id_seq;
DROP TABLE IF EXISTS US_STATES;
DROP TABLE IF EXISTS STATES_master CASCADE;
DROP TABLE IF EXISTS CITY_MASTER CASCADE;
DROP SEQUENCE IF EXISTS city_city_id_seq;
DROP TABLE IF EXISTS COUNTRY_MASTER CASCADE;
DROP SEQUENCE IF EXISTS country_country_id_seq;
DROP TABLE IF EXISTS PAYMENT CASCADE;
DROP SEQUENCE IF EXISTS payment_payment_id_seq;
DROP TABLE  IF EXISTS  SPRING_SESSION CASCADE;
DROP TABLE IF EXISTS SPRING_SESSION_ATTRIBUTES CASCADE;

DROP TABLE IF EXISTS REGISTEREDAPPUSERS_ACTIVITY_LOG;
DROP TABLE IF EXISTS CUSTOMERCONTACTMESSAGES;
DROP TABLE IF EXISTS APPUSERS_UPDATE_LOG CASCADE;



CREATE TABLE QRTZ_JOB_DETAILS
(
  SCHED_NAME        text NOT NULL,
  JOB_NAME          text NOT NULL,
  JOB_GROUP         text NOT NULL,
  DESCRIPTION       text NULL,
  JOB_CLASS_NAME    text NOT NULL,
  IS_DURABLE        BOOL         NOT NULL,
  IS_NONCONCURRENT  BOOL         NOT NULL,
  IS_UPDATE_DATA    BOOL         NOT NULL,
  REQUESTS_RECOVERY BOOL         NOT NULL,
  JOB_DATA          BYTEA        NULL,
  PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
(
  SCHED_NAME     text NOT NULL,
  TRIGGER_NAME   text NOT NULL,
  TRIGGER_GROUP  text NOT NULL,
  JOB_NAME       text NOT NULL,
  JOB_GROUP      text NOT NULL,
  DESCRIPTION    text NULL,
  NEXT_FIRE_TIME BIGINT       NULL,
  PREV_FIRE_TIME BIGINT       NULL,
  PRIORITY       INTEGER      NULL,
  TRIGGER_STATE  text  NOT NULL,
  TRIGGER_TYPE   text   NOT NULL,
  START_TIME     BIGINT       NOT NULL,
  END_TIME       BIGINT       NULL,
  CALENDAR_NAME  text NULL,
  MISFIRE_INSTR  SMALLINT     NULL,
  JOB_DATA       BYTEA        NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
  REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  SCHED_NAME      text NOT NULL,
  TRIGGER_NAME    text NOT NULL,
  TRIGGER_GROUP   text NOT NULL,
  REPEAT_COUNT    BIGINT       NOT NULL,
  REPEAT_INTERVAL BIGINT       NOT NULL,
  TIMES_TRIGGERED BIGINT       NOT NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
  SCHED_NAME      text NOT NULL,
  TRIGGER_NAME    text NOT NULL,
  TRIGGER_GROUP   text NOT NULL,
  CRON_EXPRESSION text NOT NULL,
  TIME_ZONE_ID    text,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
  SCHED_NAME    text   NOT NULL,
  TRIGGER_NAME  text   NOT NULL,
  TRIGGER_GROUP text   NOT NULL,
  STR_PROP_1    text   NULL,
  STR_PROP_2    text   NULL,
  STR_PROP_3    text   NULL,
  INT_PROP_1    INT            NULL,
  INT_PROP_2    INT            NULL,
   bigint_PROP_1   BIGINT         NULL,
   bigint_PROP_2   BIGINT         NULL,
  DEC_PROP_1    NUMERIC(13, 4) NULL,
  DEC_PROP_2    NUMERIC(13, 4) NULL,
  BOOL_PROP_1   BOOL           NULL,
  BOOL_PROP_2   BOOL           NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
  SCHED_NAME    text NOT NULL,
  TRIGGER_NAME  text NOT NULL,
  TRIGGER_GROUP text NOT NULL,
  BLOB_DATA     BYTEA        NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
(
  SCHED_NAME    text NOT NULL,
  CALENDAR_NAME text NOT NULL,
  CALENDAR      BYTEA        NOT NULL,
  PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
);


CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
  SCHED_NAME    text NOT NULL,
  TRIGGER_GROUP text NOT NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
  SCHED_NAME        text NOT NULL,
  ENTRY_ID          text  NOT NULL,
  TRIGGER_NAME      text NOT NULL,
  TRIGGER_GROUP     text NOT NULL,
  INSTANCE_NAME     text NOT NULL,
  FIRED_TIME        BIGINT       NOT NULL,
  SCHED_TIME        BIGINT       NOT NULL,
  PRIORITY          INTEGER      NOT NULL,
  STATE             text  NOT NULL,
  JOB_NAME          text NULL,
  JOB_GROUP         text NULL,
  IS_NONCONCURRENT  BOOL         NULL,
  REQUESTS_RECOVERY BOOL         NULL,
  PRIMARY KEY (SCHED_NAME, ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
  SCHED_NAME        text NOT NULL,
  INSTANCE_NAME     text NOT NULL,
  LAST_CHECKIN_TIME BIGINT       NOT NULL,
  CHECKIN_INTERVAL  BIGINT       NOT NULL,
  PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
(
  SCHED_NAME text NOT NULL,
  LOCK_NAME  text  NOT NULL,
  PRIMARY KEY (SCHED_NAME, LOCK_NAME)
);

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY
  ON QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP
  ON QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J
  ON QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG
  ON QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C
  ON QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME
  ON QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE
  ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE
  ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP
  ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP, TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);



--ALTER SEQUENCE Cast1_id_seq RESTART WITH 100;

  CREATE TABLE APPUSERS(
  userid bigserial PRIMARY KEY,
  username text not null unique ,
  password text not null,
  first_name text not null unique ,
  last_name text not null,
  zipcode text not null,
  email text not null unique ,
  phone_number text,
  active boolean default false,
  disabled boolean default false,
  verified boolean default false,
  locked boolean default false,
  superuser boolean default false,
  registered_date timestamp default CURRENT_TIMESTAMP,
  last_modified timestamp default CURRENT_TIMESTAMP
);

ALTER TABLE APPUSERS ADD
CONSTRAINT APPUSERS_USERNAME_EMAIL_KEY UNIQUE (userid,username,email);


CREATE TABLE APPUSERS_UPDATE_LOG(
    id bigserial PRIMARY KEY,
    operation   char(1)   NOT NULL,
    userid int not null,
    username text not null,
    email text not null,
    password text,
    first_name text,
    last_name text,
    phone_number text,
    active boolean,
    disabled boolean,
    verified boolean,
    locked boolean,
    superuser boolean,
    last_updated timestamp default CURRENT_TIMESTAMP
);




CREATE TABLE REGISTEREDAPPUSERS(
  id serial not null PRIMARY KEY,
  username VARCHAR(100) not null,
   email VARCHAR(100) not null,
   confToken VARCHAR(100) not null,
   confEmail VARCHAR(500) not null,
  CONSTRAINT FK_REGUSERS_USERNAME_EMAIL foreign key (email) references APPUSERS(email)
);


CREATE TABLE REGISTEREDAPPUSERS_ACTIVITY_LOG(
  id bigserial PRIMARY KEY,
  user_id int not null,
  username text not null,
  email text not null,
  token text,
   last_loggedin timestamp default CURRENT_TIMESTAMP
);


ALTER TABLE REGISTEREDAPPUSERS_ACTIVITY_LOG ADD CONSTRAINT FK_REGUSERS_USERNAME_EMAIL foreign key (user_id,username,email) references APPUSERS(userid,username,email);




CREATE TABLE AUTHOrities_MASTER (
 id serial PRIMARY KEY ,
 role_id VARCHAR(50) unique,
 roledesc VARCHAR(400),
 max_file_size int not null,
 max_number_files int not null
);



CREATE TABLE APPUSERS_AUTH (
 auth_user_id bigserial not null PRIMARY KEY,
 userid bigserial not null,
 username text,
 email text not null,
 role_id VARCHAR(50) not null,
 updated_time timestamp default CURRENT_TIMESTAMP,

 CONSTRAINT FK_APPUSERS_AUTH_AUTHORITIES_MASTER FOREIGN KEY (role_id) REFERENCES AUTHORITIES_MASTER (role_id),
 CONSTRAINT FK_APPUSERS_AUTH_APPUSER foreign key (userid,username,email) references APPUSERS(userid,username,email)
);


--ALTER TABLE APPUSERS_AUTH ADD CONSTRAINT FK_AUTHROTIES_APPUSER foreign key (userid,username,email) references APPUSERS(userid,username,email);

CREATE TABLE USERREGISTRATIONSLOGDETIALS (
  id bigserial PRIMARY KEY,
  username text not null,
  firstName text not null,
  lastName text not null,
  zipcode text not null,
  email text not null,
  password text not null,
  old_username text ,
  old_firstName text ,
  old_lastName text ,
  old_zipcode text ,
  registered_ip text ,
  old_email text ,
  old_password text not null,
  registration_log_date timestamp default CURRENT_TIMESTAMP
);



CREATE TABLE APPUSERS_TRANSCRIPTIONS (
  log_id bigserial PRIMARY KEY,
  username text not null,
  userid integer not null,
  email text not null,
  transcription_req_id bigint not null,
  transcribe_res_type text default 'application/json',
  file_name text not null,
  session_id varchar(100) not null,
  transcribed boolean default false,
  downloaded boolean default false,
  transcribe_res_available_format text default 'application/json',
  transcribe_res_downloaded_format text default 'application/json',
  transcribed_date timestamp default CURRENT_TIMESTAMP,
  uploaded_date timestamp default CURRENT_TIMESTAMP
);


ALTER TABLE APPUSERS_TRANSCRIPTIONS ADD
CONSTRAINT APPUSERS_TRANSCRIPTIONS_UNIQUE_KEY UNIQUE (log_id,transcription_req_id,email);


--CREATE TABLE UsersTranscriptionsDetails (
--  did bigserial PRIMARY KEY,
--  transcription_req_id  text not null,
--  fileName  text not null,
--  transcription_date TIMESTAMP default now()
--);

ALTER TABLE APPUSERS_TRANSCRIPTIONS ADD CONSTRAINT FK_Users_Transcriptions_users FOREIGN KEY
  ( userid, username,   email  ) REFERENCES APPUSERS(  userid, username, email  );


  CREATE TABLE TRANSCRIBEFILELOG (
  tflog_id bigserial PRIMARY KEY,
  email text,
  file_name text,
  log_id integer,
  transcription_req_id bigint,
  transcribe_res text,
  file_size integer,
  created_at timestamp DEFAULT now() NOT NULL,
  FOREIGN KEY (log_id, transcription_req_id,email) REFERENCES APPUSERS_TRANSCRIPTIONS (log_id,transcription_req_id,email)
);


--CREATE TRIGGER REGISTEREDAPPUSERS_ACTIVITY_LOG_trigger
--    AFTER INSERT ON APPUSERS   REFERENCING NEW TABLE AS X          FOR EACH ROW



  CREATE TABLE USER_SESSIONS (
  PRIMARY_ID CHAR(36) NOT NULL,
  SESSION_ID CHAR(36) NOT NULL,
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  EXPIRY_TIME BIGINT NOT NULL,
  PRINCIPAL_NAME text,
  CONSTRAINT USER_SESSIONS_PK PRIMARY KEY (PRIMARY_ID)
);



CREATE TABLE CUSTOMERCONTACTMESSAGES (
  id  bigserial PRIMARY KEY,
  email VARCHAR(100),
  firstName VARCHAR(100),
  lastName VARCHAR(100),
  message text,
  created_at timestamp DEFAULT now() NOT NULL
  );





CREATE TABLE APPUSERS_ADDRESS (
    address_id SERIAL PRIMARY KEY,
    address1 text NOT NULL,
    address2 text,
    city text NOT NULL,
    city_id smallint NOT NULL,
    postal_code text,
    phone text NOT NULL,
    last_updated timestamp with time zone DEFAULT now() NOT NULL
);


--
 ---
 ---

 CREATE TABLE COUNTRY_MASTER (
    ID SERIAL PRIMARY KEY,
    country_code VARCHAR(3),
    country_name VARCHAR(50),
    last_update timestamp with time zone DEFAULT now() NOT NULL
);


--
-- Name: city_city_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--


CREATE TABLE STATES_MASTER (
	ID serial PRIMARY KEY ,
	STATE_CODE char(2) NOT NULL,
	STATE_NAME varchar(50) NOT NULL
);

--
-- Name: city; Type: TABLE; Schema: public; Owner: postgres
--
drop table if exists CITY_MASTER  cascade;

CREATE TABLE CITY_MASTER (
    city_id serial PRIMARY KEY,
    ID_STATE integer NOT NULL,
    ID_COUNTRY INTEGER NOT NULL,
    city text NOT NULL,
    COUNTY varchar(50) NOT NULL,
	LATITUDE FLOAT(24) NOT NULL,
	LONGITUDE FLOAT(24)NOT NULL,
    FOREIGN KEY (ID_STATE) REFERENCES STATES_MASTER (ID),
    FOREIGN KEY (ID_COUNTRY) REFERENCES COUNTRY_MASTER (ID)
);



--
-- Name: users_list_view; Type: VIEW; Schema: public; Owner: postgres
--

--CREATE VIEW users_list_view AS
--SELECT cu.user_id AS id,
--   (((cu.first_name)::text || ' '::text) || (cu.last_name)::text) AS name,
--   a.address,
--   a.postal_code AS "zip code",
--   a.phone,
--   city_master.city,
--   country_master.country,
--       CASE
--           WHEN cu.activebool THEN 'active'::text
--           ELSE ''::text
--       END AS notes
--  FROM (((APPUSERS cu
--    JOIN address a ON ((cu.address_id = a.address_id)))
--    JOIN city_master ON ((a.city_id = city.city_id)))
--    JOIN country_master ON ((city.country_id = country.country_id)));
--




--
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--
DROP TABLE IF EXISTS PAYMENT CASCADE;

CREATE TABLE PAYMENT (
    payment_id SERIAL,
    user_id smallint NOT NULL,
    transcribtion_id integer NOT NULL,
    amount numeric(5,2) NOT NULL,
    payment_date timestamp with time zone NOT NULL,
    PRIMARY KEY (PAYMENT_ID,payment_date)
) PARTITION BY RANGE(payment_date);



ALTER TABLE PAYMENT drop constraint payment_pkey;
ALTER TABLE PAYMENT  ADD CONSTRAINT payment_pkey UNIQUE (payment_id, payment_date);


    --
    -- Name: idx_fk_address_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
    --

    -- CREATE INDEX idx_fk_address_id ON customer USING btree (address_id);


    --
    -- Name: idx_fk_city_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
    --

    CREATE INDEX idx_fk_city_id ON APPUSERS_ADDRESS USING btree (city_id);



    --
    -- Name: idx_fk_customer_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
    --

    CREATE INDEX idx_fk_user_id ON PAYMENT USING btree (user_id);




CREATE TABLE SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME text,
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME text NOT NULL,
	ATTRIBUTE_BYTES BYTEA NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);



--
-- Name: last_updated(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION last_updated() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.last_update = CURRENT_TIMESTAMP
    RETURN NEW
END $$;


--
-- Name: last_updated; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER last_updated BEFORE UPDATE ON APPUSERS_ADDRESS FOR EACH ROW EXECUTE PROCEDURE last_updated();



CREATE TABLE USERREGVERIFYLOGDETAILS (
  id bigserial PRIMARY KEY,
  username text not null,
  email text not null UNIQUE,
  disabled boolean default false,
  verified boolean default false,
  confEmailUrl text not null,
  verifiedRegClientIp text ,
  verificationEmailSent boolean default false,
  confEmailToken text not null UNIQUE,
  emailSentDate  timestamp default CURRENT_TIMESTAMP,
  verificationDate timestamp not null
);



--ALTER TABLE USERREGVERIFYLOGDETAILS ADD CONSTRAINT FK_REGUSERS_USERREGVERIFYLOGDETAILS_EMAIL foreign key (username,email) references APPUSERS(username,email)


--/

CREATE OR REPLACE FUNCTION process_users_profile_audit() RETURNS TRIGGER
AS $appusers_update_activity_trigger$
    BEGIN
            IF (TG_OP = 'DELETE') THEN
            INSERT INTO APPUSERS_UPDATE_LOG (operation,userid,username,email,password,first_name,last_name,phone_number,active,disabled,verified,locked,superuser)
                SELECT 'D',  n.userid,n.username,n.email,n.password,n.first_name,n.last_name,n.phone_number,n.active,n.disabled,n.verified,n.locked,n.superuser FROM old_table o;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO APPUSERS_UPDATE_LOG (operation,userid,username,email,password,first_name,last_name,phone_number,active,disabled,verified,locked,superuser)
                SELECT 'U',  n.userid,n.username,n.email,n.password,n.first_name,n.last_name,n.phone_number,n.active,n.disabled,n.verified,n.locked,n.superuser FROM old_table n;
        ELSIF (TG_OP = 'INSERT') THEN
            INSERT INTO APPUSERS_UPDATE_LOG (operation,userid,username,email,password,first_name,last_name,phone_number,active,disabled,verified,locked,superuser)
                SELECT 'I',  n.userid,n.username,n.email,n.password,n.first_name,n.last_name,n.phone_number,n.active,n.disabled,n.verified,n.locked,n.superuser  FROM new_table n;
        END IF;
        RETURN NULL;
    END $appusers_update_activity_trigger$  LANGUAGE plpgsql;


CREATE TRIGGER APPUSERS_PROFILE_INS_LOG
    AFTER INSERT ON APPUSERS
    REFERENCING NEW TABLE AS new_table
    FOR EACH STATEMENT EXECUTE FUNCTION process_users_profile_audit();
CREATE TRIGGER APPUSERS_PROFILE_UPDATE_LOG
    AFTER UPDATE ON APPUSERS
    REFERENCING OLD TABLE AS old_table NEW TABLE AS new_table
    FOR EACH STATEMENT EXECUTE FUNCTION process_users_profile_audit();
CREATE TRIGGER APPUSERS_PROFILE_DEL_LOG
    AFTER DELETE ON APPUSERS
    REFERENCING OLD TABLE AS old_table
    FOR EACH STATEMENT EXECUTE FUNCTION process_users_profile_audit();




COMMIT;
