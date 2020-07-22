CREATE TABLE user (
  id int(11) AUTO_INCREMENT,
  account_id varchar(100) NULL,
  name varchar(50) NULL,
  token char(36) NULL,
  gmt_create bigint(20) NULL,
  gmt_modified bigint(20) NULL,
  constraint user_pk
  PRIMARY KEY (`id`)
);
