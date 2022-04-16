ALTER TABLE `rvcomfort`.`user`
    ADD COLUMN `first_name`    VARCHAR(100)  NOT NULL AFTER `email`,
    ADD COLUMN `last_name`     VARCHAR(100)  NOT NULL AFTER `first_name`,
    ADD COLUMN `date_of_birth` TIMESTAMP     NOT NULL AFTER `last_name`,
    ADD COLUMN `address`       VARCHAR(2000) NOT NULL AFTER `date_of_birth`,
    ADD COLUMN `tel_no`        VARCHAR(13)   NOT NULL AFTER `address`,
    ADD COLUMN `is_non_locked` TINYINT(1)    NOT NULL DEFAULT 1 AFTER `updated_at`,
    ADD COLUMN `is_enabled`    TINYINT(1)    NOT NULL DEFAULT 1 AFTER `is_non_locked`
;