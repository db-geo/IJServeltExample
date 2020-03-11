CREATE DATABASE tst
CHARACTER SET utf8
COLLATE utf8_general_ci;
CREATE USER 'tst'@'localhost' IDENTIFIED BY 'tst';
GRANT ALL PRIVILEGES ON tst.* TO 'tst'@'localhost';
CREATE TABLE tst.utilisateurs (
  id int(11) NOT NULL AUTO_INCREMENT,
  nom varchar(20) NOT NULL,
  login varchar(20) NOT NULL,
  password varchar(20) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
