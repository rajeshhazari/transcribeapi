

--insert into authorities_master (rolename,roleDesc) values ('ROLE_BASIC_USER','Default role for any user, This supports limited file size transcription, fo ex: 20 mb and one file at a time, no batch Transcription');
--insert into authorities_master (rolename,roledesc)  values ('ROLE_ADMIN','Default admin role for limited admin privileges, like new features released testing, unlimited tranribitions for sphin4x and deepspeech and unlimited file size and unlimited files');
--insert into authorities_master (rolename,roleDesc)  values ('ROLE_SUPER_ADMIN','Super admin role with complete auth, this will be mostly backend and complete access to all features, endpoints');
--insert into authorities_master (rolename,roleDesc)  values ('ROLE_PREMIUM_USER','Super role with complete transcription endpoints access like spinx4 transribtion with unlimited size, for ex at max 100MB(system limit), unlimited Transcriptions at anytime, access to deepspeech Transcriptions endpoints ');

insert into APPUSERS (username, password, active, first_name, last_name, email, zipcode) values ('rajeshhazari', 'admin321', true,'Rajesh', 'Hazari', 'rajeshhazari@gmail.com','27560');
insert into APPUSERS (username, password, active, first_name, last_name, email, zipcode) values ('rajeshh', '$2a$10$JuqFvWlOf/AIbBvrhvkvfuNuCnnwudxDxTzeuqc3Gr3n6sTLniHsy', true,'rajesh','hazare','rajesh_hazari@yahoo.com','27560');

--insert into appusers_auth (userid,username,email,role_id) values (1,'rajeshhazari','rajeshhazari@gmail.com',1);
--insert into appusers_auth (userid,username,email,role_id) values (2,'rajeshh','rajesh_hazari@yahoo.com',3);



--insert into USER (username, password) values ('rajeshhazari', 'admin111' );
insert into APPUSERS_TRANSCRIPTIONS   (userid,username,email,transcription_req_id,FILE_NAME,session_id,transcribed) values (2,'rajeshh','rajesh_hazari@yahoo.com', '1111','sample1.wav',  'sessionid1', true);
insert into APPUSERS_TRANSCRIPTIONS   (userid,username,email,transcription_req_id,FILE_NAME,session_id,transcribed) values (2,'rajeshh','rajesh_hazari@yahoo.com', '1112','sample2.wav',  'sessionid1', true);
select * from TRANSCRIBEFILELOG;
insert into TRANSCRIBEFILELOG   (LOG_ID,email,FILE_NAME,TRANSCRIption_REQ_ID,TRANSCRIBE_RES) values (1,'rajesh_hazari@yahoo.com', 'sample.wav',  '1111',  'sample text2 ');
insert into TRANSCRIBEFILELOG   (LOG_ID,email,FILE_NAME,TRANSCRIption_REQ_ID,TRANSCRIBE_RES) values (2,'rajesh_hazari@yahoo.com', 'sample2.wav',  '1112', 'sample text1');

--insert into usertranscriptions   (LOG_ID,USERNAME,EMAIl, TRANSCRIBE_REQ_ID,FILE_NAME,SESSION_ID,USER_ID,TRANSCRIBED, DOWNLOADED, TRANSCRIBE_RES_AVAILABLE_FORMAT,TRANSCRIBE_RES_TYPE) values (1,'rajeshhazari', 'sample.wav',  '1234', '3314', 'JSON')
