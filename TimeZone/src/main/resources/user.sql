SET NAMES utf8mb4;

drop table if exists `user`;
CREATE TABLE `user`
(
    `id`          int(11)   NOT NULL AUTO_INCREMENT COMMENT '用户编号',
    `name`        varchar(100)   DEFAULT NULL COMMENT '用户名称',
    `sex`         varchar(10)    DEFAULT NULL COMMENT '用户性别',
    `age`         int(4)         DEFAULT NULL COMMENT '用户年龄',
    `description` varchar(100)   DEFAULT NULL COMMENT '描述',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
    `merchant_id` bigint(20)     DEFAULT NULL COMMENT '商家编号',
    `country`     varchar(255)   DEFAULT NULL COMMENT '商家时区',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8 COMMENT ='用户表';


INSERT INTO `user`(`id`, `name`, `sex`, `age`, `description`, `create_time`, `update_time`, `merchant_id`, `country`)
VALUES (1, '张三', '男', 24, '张三在8时区', '2022-10-31 12:00:00', '2022-10-31 12:00:00', 1, '+8:00');
INSERT INTO `user`(`id`, `name`, `sex`, `age`, `description`, `create_time`, `update_time`, `merchant_id`, `country`)
VALUES (2, '李四', '女', 24, '李四在9时区', '2022-10-31 12:00:00', '2022-10-31 12:00:00', 2, '+9:00');
INSERT INTO `user`(`id`, `name`, `sex`, `age`, `description`, `create_time`, `update_time`, `merchant_id`, `country`)
VALUES (3, '王二', '男', 24, '在7时区', '2022-10-31 12:00:00', '2022-10-31 12:00:00', 3, '+7:00');
