CREATE USER IF NOT EXISTS 'cmpe172'@'%' IDENTIFIED BY 'Abc@123$';
CREATE USER IF NOT EXISTS 'springuser'@'%' IDENTIFIED BY 'ThePassword';
CREATE USER IF NOT EXISTS 'springbook'@'%' IDENTIFIED BY 'ThePassword';
CREATE USER IF NOT EXISTS 'springpayment'@'%' IDENTIFIED BY 'ThePassword';
GRANT ALL PRIVILEGES ON `cmpe172`.* to 'cmpe172'@'%';
CREATE DATABASE IF NOT EXISTS `springpayments`;
GRANT ALL PRIVILEGES ON `springpayments`.* TO 'cmpe172'@'%';
GRANT ALL PRIVILEGES ON `springpayments`.* TO 'springuser'@'%';
CREATE DATABASE IF NOT EXISTS `springusers`;
GRANT ALL PRIVILEGES ON `springusers`.* TO 'cmpe172'@'%';
GRANT ALL PRIVILEGES ON `springusers`.* TO 'springuser'@'%';
CREATE DATABASE IF NOT EXISTS `springbooks`;
GRANT ALL PRIVILEGES ON `springbooks`.* TO 'cmpe172'@'%';
GRANT ALL PRIVILEGES ON `springusers`.* TO 'springuser'@'%';