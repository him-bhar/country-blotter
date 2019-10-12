CREATE TABLE IF NOT EXISTS `country`(
    `name` VARCHAR(100) NOT NULL PRIMARY KEY,
    `code` VARCHAR(10) NOT NULL,
    `capital` VARCHAR(100) NOT NULL
);

insert into country (`name`, `code`, `capital`) values ('India', 'IN', 'New Delhi');
insert into country (`name`, `code`, `capital`) values ('United States of America', 'US', 'Washington D.C.');