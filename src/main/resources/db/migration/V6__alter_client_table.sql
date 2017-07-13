ALTER TABLE clients
ADD `role_id` INTEGER DEFAULT NULL,
ADD `converted` bit(1) NOT NULL DEFAULT b'0',
ADD `converted_date` datetime DEFAULT NULL;