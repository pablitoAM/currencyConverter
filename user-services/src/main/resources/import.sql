INSERT IGNORE INTO permissions (description) VALUES ('ROLE_USER');
INSERT IGNORE INTO permissions (description) VALUES ('ROLE_ADMIN');

INSERT IGNORE INTO groups (description) VALUES ('USERS_GROUP');
INSERT IGNORE INTO groups (description) VALUES ('ADMINS_GROUP');

INSERT IGNORE INTO permissions2group (GROUP_ID, PERMISSION_ID) VALUES (1,1);
INSERT IGNORE INTO permissions2group (GROUP_ID, PERMISSION_ID) VALUES (2,1);
INSERT IGNORE INTO permissions2group (GROUP_ID, PERMISSION_ID) VALUES (2,2);
