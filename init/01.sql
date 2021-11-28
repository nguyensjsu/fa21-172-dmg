CREATE USER 'cmpe172'@'%' IDENTIFIED BY 'Abc@123$';
GRANT ALL ON `cmpe172`.* to 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `spring-payments`;
GRANT ALL ON `spring-payments`.* TO 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `spring-users`;
GRANT ALL ON `spring-users`.* TO 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `spring-books`;
GRANT ALL ON `spring-books`.* TO 'cmpe172'@'%';