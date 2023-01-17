Use ApiDB;
Create Table DummyDataFromAp(
id integer PRIMARY KEY IDENTITY(1,1),
cioc VARCHAR (100) not null,
region VARCHAR (100) not null,
startOfWeek VARCHAR (100) not null,
common VARCHAR (100) not null,
f  VARCHAR (100) not null,
symbol VARCHAR (100) not null,
googleMaps VARCHAR (100) not null,
unMember  bit not null,
landlocked bit not null,
area smallint not null
);