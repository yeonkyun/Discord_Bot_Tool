DROP DATABASE IF EXISTS Discord;
DROP USER IF EXISTS Discord@localhost;
create user Discord@localhost identified WITH mysql_native_password by 'project7';
create database Discord;
grant all privileges on Discord.* to Discord@localhost with grant option;
commit;

use Discord;

CREATE TABLE Users (
                       username VARCHAR(35),
                       email VARCHAR(254),
                       user_id BIGINT(20),
                       authordate DATE,
                       PRIMARY KEY (user_id)
);

CREATE TABLE Admin (
                       admin_id VARCHAR(20) NOT NULL,
                       admin_pw VARCHAR(45) NOT NULL,
                       PRIMARY KEY (admin_id)
);