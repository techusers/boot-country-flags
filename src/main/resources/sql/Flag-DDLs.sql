create database flags;

use flags;

create table continent (continentid int not null auto_increment,
name varchar(15) not null,
addedby varchar(20) not null,
addeddate timestamp not null,
modifiedby varchar(20) not null,
modifieddate timestamp not null,
primary key (continentid)
);

create table country (countryid int not null auto_increment,
name varchar(30) not null,
flag varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
continentid int references continent(continentid),
addedby varchar(20) not null,
addeddate timestamp not null,
modifiedby varchar(20) not null,
modifieddate timestamp not null,
primary key (countryid)
);

create user 'searchserviceuser'@'%' identified by 'Search@12#';

use flags;

grant select, insert, update, delete, create, drop on flags.* to 'searchserviceuser'@'%';

