CREATE USER 'springuser'@'%' IDENTIFIED BY 'Abc@123$';
GRANT ALL ON `cmpe172`.* to 'springuser'@'%';
CREATE DATABASE IF NOT EXISTS `springpayments`;
GRANT ALL ON `springpayments`.* TO 'springuser'@'%';
CREATE DATABASE IF NOT EXISTS `springusers`;
GRANT ALL ON `springusers`.* TO 'springuser'@'%';
CREATE DATABASE IF NOT EXISTS `springbooks`;
GRANT ALL ON `springbooks`.* TO 'springuser'@'%';