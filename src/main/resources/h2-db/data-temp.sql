insert into APPUSERS (username, password, active, first_name, last_name, email, zipcode) values ('rajeshhazari', 'admin321', true,'Rajesh', 'Hazari', 'rajeshhazari@gmail.com','27560');
insert into APPUSERS (username, password, active, first_name, last_name, email, zipcode) values ('rajeshh', '$2a$10$JuqFvWlOf/AIbBvrhvkvfuNuCnnwudxDxTzeuqc3Gr3n6sTLniHsy', true,'dev','madvedi','rajesh_hazari@yahoo.com','27560');
insert into APPUSERS (username, password, active, first_name, last_name, email, zipcode) values ('devuser', '$2a$10$JuqFvWlOf/AIbBvrhvkvfuNuCnnwudxDxTzeuqc3Gr3n6sTLniHsy', true,'devappuser','devapp','transcriibedevappuser@yahoo.com','27560');

--insert into USER (username, password) values ('rajeshhazari', 'admin111' );

insert into TRANSCRIBEFILELOG   (LOG_ID,USEREMAIL,FILE_NAME,SESSION_ID,TRANSCRIBE_REQ_ID,TRANSCRIBE_RES_TYPE) values (1,'transcribedevuser@yahoo.com', 'sample.wav',  '1234', '3214', 'JSON');
insert into TRANSCRIBEFILELOG   (LOG_ID,USEREMAIL,FILE_NAME,SESSION_ID,TRANSCRIBE_REQ_ID,TRANSCRIBE_RES_TYPE) values (2,'transcribedevuser@yahoo.com', 'sample.wav',  '1234', '3314', 'JSON');

--insert into usertranscriptions   (LOG_ID,USERNAME,EMAIl, TRANSCRIBE_REQ_ID,FILE_NAME,SESSION_ID,USER_ID,TRANSCRIBED, DOWNLOADED, TRANSCRIBE_RES_AVAILABLE_FORMAT,TRANSCRIBE_RES_TYPE) values (1,'rajeshhazari', 'sample.wav',  '1234', '3314', 'JSON')
