CREATE DATABASE IF NOT EXISTS `z_union` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `z_union`;

CREATE TABLE IF NOT EXISTS `sys_table_info` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '表名',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表，系统管理';

CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `pwd` varchar(255) NOT NULL COMMENT '密码',
  `birthday` datetime NOT NULL COMMENT '生日',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2018.05.22 --