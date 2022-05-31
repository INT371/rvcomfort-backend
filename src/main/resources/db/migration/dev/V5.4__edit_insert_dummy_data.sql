TRUNCATE TABLE `rvcomfort`.`reservation`;
TRUNCATE TABLE `rvcomfort`.`room`;
TRUNCATE TABLE `rvcomfort`.`room_type`;
TRUNCATE TABLE `rvcomfort`.`room_type_image`;

INSERT INTO `rvcomfort`.`room_type` (type_id, type_name, description, price, max_capacity, policy, created_at,
                                     updated_at)
VALUES (0, 'Standard', 'Standard Room', 400.00, 2, 'benefits', NOW(), null),
       (0, 'Standard River View', 'Standard River View Room', 500.00, 2, 'benefits', NOW(), null),
       (0, 'Family', 'Family Room', 550.00, 2, 'benefits', NOW(), null),
       (0, 'Deluxe', 'Deluxe room', 800.00, 2, 'benefits', NOW(), null),
       (0, 'Luxurious', 'Luxurious room', 1500.00, 4, 'benefits', NOW(), null),
       (0, 'Queen', 'Queen room', 2000.00, 4, 'benefits', NOW(), null),
       (0, 'King', 'King room', 3500.00, 8, 'benefits', NOW(), null)
;

INSERT INTO `rvcomfort`.`room` (room_id, room_name, room_type, created_at, updated_at)
VALUES (0, 'S101', 1, NOW(), null),
       (0, 'S102', 1, NOW(), null),
       (0, 'S103', 1, NOW(), null),
       (0, 'SR111', 2, NOW(), null),
       (0, 'SR112', 2, NOW(), null),
       (0, 'SR113', 2, NOW(), null),
       (0, 'F121', 3, NOW(), null),
       (0, 'F122', 3, NOW(), null),
       (0, 'D201', 4, NOW(), null),
       (0, 'D202', 4, NOW(), null),
       (0, 'D203', 4, NOW(), null),
       (0, 'D204', 4, NOW(), null),
       (0, 'L301', 5, NOW(), null),
       (0, 'L302', 5, NOW(), null),
       (0, 'Q401', 6, NOW(), null),
       (0, 'Q402', 6, NOW(), null),
       (0, 'K501', 7, NOW(), null),
       (0, 'K502', 7, NOW(), null)
;

INSERT INTO `rvcomfort`.`room_type_image` (id, type_id, image, created_at, updated_at)
VALUES (0, 1, 'regular_a1.jpg', NOW(), null),
       (0, 1, 'regular_a2.jpg', NOW(), null),
       (0, 1, 'regular_a3.jpg', NOW(), null),
       (0, 2, 'regular_b1.jpg', NOW(), null),
       (0, 2, 'regular_b2.jpg', NOW(), null),
       (0, 3, 'regular_c.jpg', NOW(), null),
       (0, 4, 'deluxe_1.jpg', NOW(), null),
       (0, 4, 'deluxe_2.jpg', NOW(), null),
       (0, 4, 'deluxe_3.jpg', NOW(), null),
       (0, 5, 'luxurious_1.jpg', NOW(), null),
       (0, 5, 'luxurious_2.jpg', NOW(), null),
       (0, 6, 'queen_1.jpg', NOW(), null),
       (0, 6, 'queen_2.jpg', NOW(), null),
       (0, 6, 'queen_3.jpg', NOW(), null),
       (0, 7, 'king_1.jpg', NOW(), null),
       (0, 7, 'king_2.jpg', NOW(), null),
       (0, 7, 'king_3.jpg', NOW(), null)
;

INSERT INTO `rvcomfort`.`reservation`
    (id, user_id, room_id, check_in_date, check_out_date, reserved_name, num_of_guest, total_price, created_at, updated_at, status)
VALUES (0, 1, 1, NOW(), NOW(), 'John Doe', 2, 1000.00, NOW(), null, 'reserved')
;
