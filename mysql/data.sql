CREATE DATABASE concurrency_db;

USE concurrency_db;

CREATE TABLE client (

 ID BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 ACCOUNT VARCHAR(10) NOT NULL,
 NAME VARCHAR(100) NOT NULL,
 BALANCE VARCHAR(100) NOT NULL,
 CREATED TIMESTAMP NOT NULL,
 PRIMARY KEY (ID),
 UNIQUE INDEX (ACCOUNT)
);

CREATE TABLE transaction (

 ID BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 ACCOUNT VARCHAR(10) NOT NULL,
 DEAL DECIMAL(15,3) NOT NULL,
 CREATED TIMESTAMP NOT NULL,
 PRIMARY KEY (ID)
);

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'mdymen_pass';

INSERT INTO 
	client(ACCOUNT, NAME, BALANCE, CREATED) 
VALUES	
	("00454", "Richard Thompson", 0.0, NOW()),
	("00132", "Peter Vusef", 0.0, NOW()),
	("00222", "Samy Goldstein", 0.0, NOW());
