create database ApiDB;
use ApiDB;
create table ApiResults (id integer PRIMARY KEY IDENTITY(1,1),web_pages VARCHAR (100),
state_province VARCHAR (100),alpha_two_code VARCHAR (100), name VARCHAR (100),country VARCHAR (100),
domins  VARCHAR (100));
  