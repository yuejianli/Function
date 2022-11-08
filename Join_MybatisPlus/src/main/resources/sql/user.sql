# 临时指定字符集 防中文乱码
/*!40101 SET NAMES utf8 */;
CREATE TABLE `user`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'id编号',
    `name`        varchar(15)  DEFAULT NULL COMMENT '名称',
    `age`         int(4)       DEFAULT NULL COMMENT '年龄',
    `description` varchar(50)  DEFAULT NULL COMMENT '描述',
    `sex`         varchar(255) DEFAULT NULL COMMENT '性别',
    `role_id`     int(11)      DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8 COMMENT ='用户表';

CREATE TABLE `role`
(
    `id`          int(11) NOT NULL COMMENT 'id编号',
    `name`        varchar(255) DEFAULT NULL COMMENT '名称',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='角色表';


INSERT INTO `role`(`id`, `name`, `description`)
VALUES (1, '超级管理员', '超级管理员');
INSERT INTO `role`(`id`, `name`, `description`)
VALUES (2, '普通用户', '普通用户');


INSERT INTO `user`(`id`, `name`, `age`, `description`, `sex`, `role_id`)
VALUES (2, '张三', 25, '张三', '男', 1);
INSERT INTO `user`(`id`, `name`, `age`, `description`, `sex`, `role_id`)
VALUES (3, '李四', 26, '李四', '男', 1);
INSERT INTO `user`(`id`, `name`, `age`, `description`, `sex`, `role_id`)
VALUES (5, '岳泽霖', 27, '岳泽霖', '男', 2);
INSERT INTO `user`(`id`, `name`, `age`, `description`, `sex`, `role_id`)
VALUES (6, '岳建立', 28, '两个蝴蝶飞', '男', 2);
