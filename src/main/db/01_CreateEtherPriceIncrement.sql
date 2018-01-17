create database if not exists crypto;

use crypto;

drop table if exists EtherPriceIncrement cascade;

create table EtherPriceIncrement (
	Id bigint not null auto_increment,
    Increment bigint not null,
    Timestamp datetime,
    primary key (Id)
);

create index ix_EtherPriceIncrement_Increment on EtherPriceIncrement (Increment);

insert into EtherPriceIncrement (Increment, Timestamp) values
(100, now()),
(200, now()),
(300, now()),
(400, now()),
(500, now()),
(600, now()),
(700, now()),
(800, now()),
(900, now());