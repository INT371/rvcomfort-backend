-- -----------------------------------------------------
-- Table `rvcomfort`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rvcomfort`.`user` ;

CREATE TABLE IF NOT EXISTS `rvcomfort`.`user` (
  `id` INT NOT NULL,
  `username` VARCHAR(20) NOT NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(100) NOT NULL,
  `user_type` VARCHAR(45) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `username_UNIQUE` ON `rvcomfort`.`user` (`username`);
CREATE UNIQUE INDEX `email_UNIQUE` ON `rvcomfort`.`user` (`email`);


-- -----------------------------------------------------
-- Table `rvcomfort`.`room_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rvcomfort`.`room_type` ;

CREATE TABLE IF NOT EXISTS `rvcomfort`.`room_type` (
   `type_id` INT NOT NULL,
   `type_name` VARCHAR(100) NOT NULL,
   `description` VARCHAR(2000) NULL,
   `price` DECIMAL(10,2) NOT NULL,
   `max_capacity` INT NULL,
   `policy` VARCHAR(2000) NULL,
   `created_at` TIMESTAMP NOT NULL,
   `updated_at` TIMESTAMP NULL,
   PRIMARY KEY (`type_id`)
);

-- -----------------------------------------------------
-- Table `rvcomfort`.`room`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rvcomfort`.`room` ;

CREATE TABLE IF NOT EXISTS `rvcomfort`.`room` (
    `room_id` INT NOT NULL,
    `room_name` VARCHAR(100) NOT NULL,
    `room_type` INT NOT NULL     REFERENCES `rvcomfort`.`room_type` (`type_id`)
                            ON DELETE NO ACTION
                            ON UPDATE NO ACTION,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NULL,
    PRIMARY KEY (`room_id`)
);

CREATE UNIQUE INDEX `room_name_UNIQUE` ON `rvcomfort`.`room` (`room_name`);
CREATE INDEX `fk_room_room_type_idx` ON `rvcomfort`.`room` (`room_type`);

-- -----------------------------------------------------
-- Table `rvcomfort`.`report`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rvcomfort`.`report` ;

CREATE TABLE IF NOT EXISTS `rvcomfort`.`report`(
    `id`          INT           NOT NULL,
    `issue`       VARCHAR(2000) NOT NULL,
    `status`      VARCHAR(45)   NULL,
    `created_at`  TIMESTAMP     NULL,
    `updated_at`  TIMESTAMP     NULL,
    `resolved_at` TIMESTAMP     NULL,
    `user_id`     INT           NOT NULL REFERENCES `rvcomfort`.`user` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    PRIMARY KEY (`id`)
);

CREATE INDEX `fk_report_user1_idx` ON `rvcomfort`.`report` (`user_id`);


-- -----------------------------------------------------
-- Table `rvcomfort`.`reservation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rvcomfort`.`reservation` ;

CREATE TABLE IF NOT EXISTS `rvcomfort`.`reservation` (
     `id` INT NOT NULL,
     `user_id` INT NOT NULL      REFERENCES `rvcomfort`.`user` (`id`)
                             ON DELETE NO ACTION
                             ON UPDATE NO ACTION,
     `room_id` INT NOT NULL      REFERENCES `rvcomfort`.`room` (`room_id`)
                             ON DELETE NO ACTION
                             ON UPDATE NO ACTION,
     `check_in_date` TIMESTAMP NULL,
    `check_out_date` TIMESTAMP NULL,
    `reserved_name` VARCHAR(45) NULL,
    `num_of_guest` INT NULL,
    `total_price` DECIMAL(10,2) NULL,
    `created_at` TIMESTAMP NULL,
    `updated_at` TIMESTAMP NULL,
    `status` VARCHAR(45) NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `fk_reservation_user1_idx` ON `rvcomfort`.`reservation` (`user_id`);
CREATE INDEX `fk_reservation_room1_idx` ON `rvcomfort`.`reservation` (`room_id`);



