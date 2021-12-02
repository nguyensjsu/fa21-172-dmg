CREATE USER 'cmpe172'@'%' IDENTIFIED BY 'Abc@123$';
GRANT ALL ON `cmpe172`.* to 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `springpayments`;
GRANT ALL ON `springpayments`.* TO 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `springusers`;
GRANT ALL ON `springusers`.* TO 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `springbooks`;
GRANT ALL ON `springbooks`.* TO 'cmpe172'@'%';