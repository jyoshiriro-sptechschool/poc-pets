delete from authorities;
delete from users;

insert into users(username, password, enabled) values('zeruela','$2y$10$6dTqSV.qrw57Q0nmdpbH6erfctaysvPzllUdQOatqVAOGOZsqn.7K',true);
insert into authorities(username,authority) values('zeruela','ROLE_usuario');
 
insert into users(username, password, enabled) values('zebuduia','$2y$10$72f1mI5qDEaaKPPfnJA.UuAtMMevkncZcfxiGi3RttyDViziGbqGK',true);
insert into authorities(username,authority) values('zebuduia','ROLE_admin');    
