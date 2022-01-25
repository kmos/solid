-- liquibase formatted sql
-- changeset gpanice:create-order_info

CREATE TABLE ORDER_INFO (
      ID bigint(20) NOT NULL AUTO_INCREMENT,
      NAME varchar(30) NOT NULL,
      PRIMARY KEY (ID)
);

-- rollback DROP TABLE ORDER_INFO