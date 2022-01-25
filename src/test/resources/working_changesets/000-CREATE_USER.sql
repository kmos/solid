-- liquibase formatted sql
-- changeset user:create-user

CREATE TABLE USER (
    ID bigint(20) NOT NULL AUTO_INCREMENT,
    NAME varchar(30) NOT NULL,
    PRIMARY KEY (ID)
);

-- rollback DROP TABLE USER
