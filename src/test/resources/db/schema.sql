CREATE TABLE IF NOT EXISTS `country`(
    `name` VARCHAR(100) NOT NULL PRIMARY KEY,
    `capital` VARCHAR(100) NOT NULL
);

insert into country (`name`, `capital`) values ('India', 'New Delhi');
insert into country (`name`, `capital`) values ('United States of America', 'Washington D.C.');