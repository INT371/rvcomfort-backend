INSERT INTO `rvcomfort`.`user` (id, username, email, first_name, last_name, date_of_birth, address, tel_no, user_type,
                                password, created_at, updated_at, is_non_locked, is_enabled)
VALUES (1, 'dummy01', 'duis.elementum.dui@outlook.edu', 'Ignacia', 'Rocha', '2019-12-18', 'Ap #134-2329 Leo. Rd.',
        '024957871', 'user', 'FFK76PLV7EI28rV', '2021-01-20 10:28:08', '2020-09-22 04:31:35', 1, 1),
       (2, 'dummy02', 'integer.vitae.nibh@aol.net', 'Camden', 'Wilkerson', '1988-06-25', '9149 Arcu Rd.', '0688071936',
        'user', 'LNQ14SND7GH56rK', '2020-06-29 04:16:35', '2021-01-29 15:46:23', 1, 1),
       (3, 'dummy03', 'integer.tincidunt@outlook.org', 'Kermit', 'Bradley', '2008-09-08', '4565 Quisque St.',
        '0863919233', 'user', 'IHL67DVW3SC26nL', '2020-07-26 16:57:21', '2021-02-13 03:07:45', 1, 1),
       (4, 'dummy04', 'rutrum.magna@yahoo.ca', 'Melvin', 'Gonzalez', '1998-01-29', '800-9992 Ut, Road', '021558587',
        'user', 'OWU31WPO2LA84fO', '2019-12-04 01:51:00', '2021-03-05 21:48:23', 1, 1),
       (5, 'dummy05', 'donec@protonmail.com', 'Rosalyn', 'Skinner', '2011-12-04', 'Ap #952-3814 Nibh. Rd.', '026416721',
        'user', 'WHJ78MJY4WS64rJ', '2020-03-07 00:27:11', '2021-01-05 05:25:50', 1, 1)
;

INSERT INTO `rvcomfort`.`room_type` (type_id, type_name, description, price, max_capacity, policy, created_at,
                                     updated_at)
VALUES (0, 'Regular', 'Regular room', 500.00, 2, 'mai mee', NOW(), null),
       (0, 'Supreme', 'Supreme room', 700.00, 2, 'mai mee', NOW(), null),
       (0, 'Gorgeous', 'Gorgeous room', 1000.00, 4, 'mai mee', NOW(), null),
       (0, 'Luxurious', 'Luxurious room', 1200.00, 4, 'mai mee', NOW(), null),
       (0, 'Queen', 'Queen room', 2000.00, 4, 'mai mee', NOW(), null),
       (0, 'King', 'King room', 3500.00, 6, 'mai mee', NOW(), null)
;

INSERT INTO `rvcomfort`.`room` (room_id, room_name, room_type, created_at, updated_at)
VALUES (0, 'R1', 1, NOW(), null),
       (0, 'R2', 1, NOW(), null),
       (0, 'R3', 1, NOW(), null),
       (0, 'R4', 1, NOW(), null),
       (0, 'R5', 1, NOW(), null),
       (0, 'S1', 2, NOW(), null),
       (0, 'S2', 2, NOW(), null),
       (0, 'S3', 2, NOW(), null),
       (0, 'S4', 2, NOW(), null),
       (0, 'G1', 3, NOW(), null),
       (0, 'G2', 3, NOW(), null),
       (0, 'L1', 4, NOW(), null),
       (0, 'L2', 4, NOW(), null),
       (0, 'Q1', 5, NOW(), null),
       (0, 'K1', 6, NOW(), null)
;

INSERT INTO `rvcomfort`.`reservation`
VALUES (0, 1, 1, NOW(), NOW(), 'customer1', 2, 1000.00, NOW(), null, 'reserved')
;

INSERT INTO `rvcomfort`.`report`
VALUES (0, 'Bed is bad', 'in progress', NOW(), null, null, 1)
;