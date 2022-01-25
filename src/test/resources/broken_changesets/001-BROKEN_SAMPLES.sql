-- liquibase formatted sql
-- changeset user:create-broken-sample

CREATE TABLE BROKEN_SAMPLES (
    ID bigint(20) NOT NULL AUTO_INCREMENT,
    NAME varchar(30) NOT NULL
    PRIMARY KEY (ID)
);

-- rollback DROP TABLE SAMPLES