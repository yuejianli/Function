CREATE TABLE `product`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id编号',
    `title`       varchar(100)     NOT NULL COMMENT '商品名称',
    `price`       decimal(10, 2)        DEFAULT NULL COMMENT '商品价格',
    `create_id`   int(10)               DEFAULT NULL COMMENT '创建人,默认为1',
    `create_time` timestamp        NULL DEFAULT NULL COMMENT '创建时间',
    `update_id`   int(10)               DEFAULT NULL COMMENT '修改人,默认为1',
    `update_time` timestamp        NULL DEFAULT NULL COMMENT '修改时间',
    `flag`        int(1)                DEFAULT NULL COMMENT '标识,1为正常,0为删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8 COMMENT ='商品表';


CREATE TABLE `order_info`
(
    `id`           int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id编号',
    `order_no`     varchar(255)          DEFAULT NULL COMMENT '订单编号,雪花算法生成',
    `title`        varchar(255)          DEFAULT NULL COMMENT '订单标题',
    `product_id`   int(10)               DEFAULT NULL COMMENT '商品id',
    `customer_id`  int(10)               DEFAULT NULL COMMENT '用户id,默认为1',
    `payment_type` int(2)                DEFAULT NULL COMMENT '支付类型 1为微信 2为支付宝 3为银行卡',
    `ip_addr`      varchar(255)          DEFAULT NULL COMMENT '地址信息',
    `total_fee`    decimal(10, 2)        DEFAULT NULL COMMENT '订单金额',
    `order_status` int(2)                DEFAULT NULL COMMENT '订单的状态',
    `code_url`     varchar(255)          DEFAULT NULL COMMENT '订单二维码链接',
    `create_id`    int(11)               DEFAULT NULL COMMENT '创建人',
    `create_time`  timestamp        NULL DEFAULT NULL COMMENT '创建时间',
    `update_id`    int(11)               DEFAULT NULL COMMENT '更新人',
    `update_time`  timestamp        NULL DEFAULT NULL COMMENT '更新时间',
    `flag`         int(1)                DEFAULT NULL COMMENT '标识 1为正常 0为删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_order_info_1` (`order_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8 COMMENT ='订单表';


CREATE TABLE `payment_info`
(
    `id`             int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id编号',
    `order_no`       varchar(30)           DEFAULT NULL COMMENT '订单编号, order_info 中的编号',
    `transaction_id` varchar(50)           DEFAULT NULL COMMENT '支付系统对应的交易编号',
    `payment_type`   int(2)                DEFAULT NULL COMMENT '支付类型 1为微信 2为支付宝',
    `trade_type`     int(2)                DEFAULT NULL COMMENT '交易类型',
    `trade_state`    int(2)                DEFAULT NULL COMMENT '交易状态',
    `payer_total`    decimal(10, 2)        DEFAULT NULL COMMENT '支付金额',
    `content`        text COMMENT '通知参数',
    `create_id`      int(10)               DEFAULT NULL COMMENT '创建人',
    `create_time`    timestamp        NULL DEFAULT NULL COMMENT '创建时间',
    `update_id`      int(10)               DEFAULT NULL COMMENT '更新人',
    `update_time`    timestamp        NULL DEFAULT NULL COMMENT '更新时间',
    `flag`           int(2)                DEFAULT NULL COMMENT '标识 1为正常 0为删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_payment_info_1` (`order_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8 COMMENT ='支付信息表';

CREATE TABLE `refund_info`
(
    `id`             int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id编号',
    `order_no`       varchar(50)      NOT NULL COMMENT '商户订单编号',
    `refund_no`      varchar(50)           DEFAULT NULL COMMENT '商户退款单编号，雪花算法生成',
    `refund_id`      varchar(50)           DEFAULT NULL COMMENT '支付系统退款单号',
    `total_fee`      decimal(10, 2)        DEFAULT NULL COMMENT '原订单金额',
    `refund`         decimal(10, 2)        DEFAULT NULL COMMENT '退款金额',
    `reason`         varchar(255)          DEFAULT NULL COMMENT '退款原因',
    `refund_status`  int(1)                DEFAULT NULL COMMENT '退款状态',
    `content_return` text COMMENT '申请退款返回参数',
    `content_notify` text COMMENT '退款结果通知参数',
    `create_id`      int(11)               DEFAULT NULL COMMENT '创建人',
    `create_time`    timestamp        NULL DEFAULT NULL COMMENT '创建时间',
    `update_id`      int(11)               DEFAULT NULL COMMENT '更新人',
    `update_time`    timestamp        NULL DEFAULT NULL COMMENT '更新时间',
    `flag`           int(11)               DEFAULT NULL COMMENT '标识 1为正常 0为删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8 COMMENT ='退款单信息';
