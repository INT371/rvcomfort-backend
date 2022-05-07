TRUNCATE TABLE `rvcomfort`.`user`;

ALTER TABLE `rvcomfort`.`user`
    ADD COLUMN `image`    VARCHAR(100)  NULL AFTER `tel_no`
;