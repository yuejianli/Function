/*!40101 SET NAMES utf8 */;
CREATE TABLE `compensation`
(
    `id`                int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
    `bus_key`           varchar(255)     NOT NULL COMMENT '标识使用的key值，是 类名+方法名+参数 构成的md5加密值',
    `class_name`        varchar(255)     NOT NULL COMMENT '完全限定类名',
    `bean_name`         varchar(255)     NOT NULL COMMENT '使用的 bean name,不填写默认为 类名首字母小写',
    `method_name`       varchar(255)     NOT NULL COMMENT '方法名',
    `param_value`       varchar(255)          DEFAULT NULL COMMENT '参数值',
    `param_type`        varchar(255)          DEFAULT NULL COMMENT '参数类型',
    `param_real_type`   varchar(255)          DEFAULT NULL COMMENT '真实参数类型',
    `revoke_type`       tinyint(1)       NOT NULL COMMENT '执行类型 0为自动 1为手动',
    `revoke_status`     tinyint(1)       NOT NULL COMMENT '执行状态  0为重试期间 1为成功 2为失败',
    `retry_count`       tinyint(2)       NOT NULL COMMENT '最大重试次数,默认为3次',
    `has_retry_count`   tinyint(2)            DEFAULT NULL COMMENT '已经重试次数',
    `exception_message` varchar(255)          DEFAULT NULL COMMENT '异常信息',
    `create_date`       timestamp        NULL DEFAULT NULL COMMENT '创建时间',
    `update_date`       timestamp        NULL DEFAULT NULL COMMENT '更新时间',
    `flag`              tinyint(1)       NOT NULL COMMENT '1为正常 0为删除记录',
    PRIMARY KEY (`id`),
    KEY `idx_compensation_1` (`bus_key`) USING BTREE COMMENT '标识键'
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  DEFAULT CHARSET = utf8 COMMENT ='服务补偿表';