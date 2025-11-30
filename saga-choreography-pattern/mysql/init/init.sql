-- Initialize databases and create a service user for the compose stack
CREATE DATABASE IF NOT EXISTS orderServiceDb;
CREATE DATABASE IF NOT EXISTS paymentServiceDb;

-- Create a limited user for the services (safer than using root)
CREATE USER IF NOT EXISTS 'springuser'@'%' IDENTIFIED BY 'springpwd';
GRANT ALL PRIVILEGES ON orderServiceDb.* TO 'springuser'@'%';
GRANT ALL PRIVILEGES ON paymentServiceDb.* TO 'springuser'@'%';
FLUSH PRIVILEGES;

