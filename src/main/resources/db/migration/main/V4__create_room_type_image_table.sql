-- -----------------------------------------------------
-- Table `rvcomfort`.`room_type_image`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rvcomfort`.`room_type_image` ;

CREATE TABLE IF NOT EXISTS `rvcomfort`.`room_type_image`
(
    `id`            INT             NOT NULL    AUTO_INCREMENT,
    `type_id`       INT             NOT NULL    REFERENCES `rvcomfort`.`room_type_image`
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    `image`         VARCHAR(2000)   NOT NULL,
    `created_at`    TIMESTAMP       NULL,
    `updated_at`    TIMESTAMP       NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `fk_room_type_image_type_id_idx` ON `rvcomfort`.`room_type_image` (`type_id`);
