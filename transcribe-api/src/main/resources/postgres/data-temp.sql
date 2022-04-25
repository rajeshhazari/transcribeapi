insert into users (username, password, active, fName, lName, email, zipcode, role) values ('rajeshhazari', 'admin111', true,'Rajesh', 'Hazari', 'rajeshhazari@gmail.com','27560','ROLE_ADMIN');
insert into users (username, password, active, fName, lName, email, zipcode, role) values ('dev', 'user111', true,'dev','madvedi','rajeshhazari@gmail.com','27560','ROLE_USER');

insert into authorities (username, authority) values ('rajeshhazari', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('rajeshhazari', 'ADMIN_POWERUSER');