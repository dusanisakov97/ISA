SET NAMES utf8mb4;

INSERT INTO `app_user` (`id`, `country`, `email`, `name`, `password`, `penals`, `phone`, `role`, `surname`, `town`, `work`) VALUES
(101,	'Serbia',	'dulekos@gmail.com',	'Dusan',	'$2a$10$FFfeiSVPFYPAyDxHe90jzOXpcUioajMJWlj33JJVPs6c7E/7SoNCC',	2,	'123-123',	'ROLE_USER',	'Isakov',	'Novi Sad',	'Software'),
(102,	'Serbia',	'jpjerlol@test.com',	'tester',	'$2a$10$7y5BxTxLDEWb2tpOIQOZo.zZK0r5NKi920yUTeRJNuJV9R48X2osW',	0,	'123-456',	'ROLE_USER',	'Test',	'Novi Sad',	'Test'),
(103,	'Serbia',	'sadmin@test.com',	'Name',	'$2a$10$e50NrW..hX/kkBg3oFbBUO.oJCjl1aX1uo6nrcR6Adh4oanCY.xCW',	0,	'123-567',	'ROLE_SYS_ADMIN',	'Surname',	'Novi Sad',	'sysadmin'),
(104,	'Serbia',	'cadmin@test.com',	'cadmin@test.com',	'$2a$10$aPCn92qbaF5UilcKlNneqOeKB0RGA8LTHz7oS2fdlMVgUdcwj8aWi',	0,	'123-789',	'ROLE_CPY_ADMIN',	'cadmin@test.com',	'Novi Sad',	'CPY admin');

INSERT INTO `verification_model` (`id`, `confirmed`, `hash`, `user_id`) VALUES
(101,	CONV('1', 2, 10) + 0,	'JH9QgtFst1ST9Lim72a8mLcm1JH6yChr_GP9yrXdHg0',	101),
(102,	CONV('1', 2, 10) + 0,	'4IiSHGji481JrldF7T0A88OvnHH1PE5ANlXu0jfi0NE',	102),
(103,	CONV('1', 2, 10) + 0,	'nDo5gAC8Ym7LvkvjhmQnXLLBTQPXvv2wOrOJWXI2v54', 103),
(104,	CONV('1', 2, 10) + 0,	'O1TYbdG-ozCLcYaWSLlDb-vUtcoTUp9Qnv_fi5d67T8',	104);

INSERT INTO `company_model` (`id`, `address`, `description`, `name`) VALUES
(101,	'Novosadska 1',	'Prodaja slusnih uredjaja',	'Slusni uredjaji'),
(102,	'Beogradska 1',	'Prodaja skenera',	'Prodaja skenera');

INSERT INTO `product_model` (`id`, `lager`, `name`, `price`, `company_id`) VALUES
(101,	3,	'Slusni uredjaj A',	350,	101),
(102,	5,	'Slusni uredjaj B',	250,	101),
(103,	4,	'Slusni uredjaj C',	150,	101),
(104,	3,	'PET skener',	5000,	102),
(105,	2,	'CT skener',	10000,	102);

INSERT INTO `time_slot_model` (`id`, `date_time`, `company_id`, `cpy_admin_id`) VALUES
(101,	'2024-09-10 00:00:00.000000',	101,	104),
(102,	'2024-09-09 00:00:00.000000',	101,	104),
(103,	'2024-10-15 00:00:00.000000',	102,	104),
(104,	'2024-10-16 00:00:00.000000',	102,	104),
(105,	'2024-10-17 00:00:00.000000',	102,	104);