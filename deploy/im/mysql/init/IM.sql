/*
Navicat MySQL Data Transfer

Source Server         : JFC
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : im

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-05-14 22:34:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for im_group_announcement
-- ----------------------------
DROP TABLE IF EXISTS `im_group_announcement`;
CREATE TABLE `im_group_announcement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `group_id` bigint(20) NOT NULL COMMENT '群id,im_group_info.id',
  `title` varchar(30) NOT NULL DEFAULT '' COMMENT '公告名称',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT '公告内容',
  `create_user` bigint(20) NOT NULL COMMENT '公告编写人,im_user.id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '公告创建时间',
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群公告';

-- ----------------------------
-- Table structure for im_group_info
-- ----------------------------
DROP TABLE IF EXISTS `im_group_info`;
CREATE TABLE `im_group_info` (
  `id` bigint(20) NOT NULL COMMENT '群id(账号)',
  `profile_photo` varchar(300) NOT NULL COMMENT '群头像',
  `name` varchar(30) NOT NULL COMMENT '群名称',
  `create_user` bigint(20) NOT NULL COMMENT '创建人:im_user.id',
  `introduction` varchar(100) NOT NULL DEFAULT '' COMMENT '简介',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '群创建时间',
  PRIMARY KEY (`id`),
  KEY `group_name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群基本信息';

-- ----------------------------
-- Table structure for im_group_msg
-- ----------------------------
DROP TABLE IF EXISTS `im_group_msg`;
CREATE TABLE `im_group_msg` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `group_id` bigint(20) NOT NULL COMMENT '群id,im_group_info.id',
  `from_id` bigint(20) NOT NULL COMMENT '发送人id,im_user.id',
  `msg_type` tinyint(4) NOT NULL COMMENT '消息类型,0:文字消息,1:图片,2:语音,3:视频,4:文件',
  `content` varchar(1000) NOT NULL COMMENT '消息内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
  PRIMARY KEY (`id`),
  KEY `from_id` (`from_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群消息';

-- ----------------------------
-- Table structure for im_group_user
-- ----------------------------
DROP TABLE IF EXISTS `im_group_user`;
CREATE TABLE `im_group_user` (
  `group_id` bigint(20) NOT NULL COMMENT '群id,im_group_info.id',
  `role` tinyint(4) NOT NULL COMMENT '群角色,0:群主,1:管理员,2:群员',
  `user_id` bigint(20) NOT NULL COMMENT '用户id,im_user.id',
  `remarks` varchar(40) NOT NULL COMMENT '群备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户入群时间',
  PRIMARY KEY (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群-用户 关联表';

-- ----------------------------
-- Table structure for im_msg
-- ----------------------------
DROP TABLE IF EXISTS `im_msg`;
CREATE TABLE `im_msg` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `from_id` bigint(20) NOT NULL COMMENT '发送人id,im_user.id',
  `to_id` bigint(20) NOT NULL COMMENT '接受者id,im_user.id',
  `msg_type` bigint(20) NOT NULL COMMENT '消息类型,0:文字消息,1:图片,2:语音,3:视频,4:文件',
  `content` varchar(1000) NOT NULL COMMENT '消息内容',
  `delivered` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否到达,0:未到达，1:已到达,2:发送失败',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
  PRIMARY KEY (`id`),
  KEY `to_id` (`to_id`,`from_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单聊消息';

-- ----------------------------
-- Table structure for im_notify
-- ----------------------------
DROP TABLE IF EXISTS `im_notify`;
CREATE TABLE `im_notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` tinyint(4) NOT NULL COMMENT '通知类型,0:添加好友，1:邀请入群，2:其他通知(不需要用户进行操作)',
  `from_id` bigint(20) NOT NULL COMMENT '如果时添加好友或者邀请入群的通知，则为请求用户id或群id',
  `to_id` bigint(20) NOT NULL COMMENT '接收通知的用户id',
  `content` varchar(100) NOT NULL DEFAULT '' COMMENT '通知内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知创建时间',
  PRIMARY KEY (`id`),
  KEY `to_id` (`to_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- ----------------------------
-- Table structure for im_offline_msg
-- ----------------------------
DROP TABLE IF EXISTS `im_offline_msg`;
CREATE TABLE `im_offline_msg` (
  `id` bigint(20) NOT NULL COMMENT 'id,应该和im_msg.id保持一致',
  `from_id` bigint(20) NOT NULL COMMENT '发送人id',
  `to_id` bigint(20) NOT NULL COMMENT '接收人id',
  `msg_type` bigint(20) NOT NULL COMMENT '消息类型,0:文字消息,1:图片,2:语音,3:视频,4:文件',
  `content` varchar(1000) NOT NULL COMMENT '消息内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
  PRIMARY KEY (`id`),
  KEY `to_id` (`to_id`,`from_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='离线消息单聊消息表';

-- ----------------------------
-- Table structure for im_permission
-- ----------------------------
DROP TABLE IF EXISTS `im_permission`;
CREATE TABLE `im_permission` (
  `id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL COMMENT '资源id',
  `operation` varchar(30) NOT NULL COMMENT '操作类型:*、create、update、delete、view',
  `resource_name` varchar(100) NOT NULL COMMENT '资源名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- Table structure for im_recently_chat_friends
-- ----------------------------
DROP TABLE IF EXISTS `im_recently_chat_friends`;
CREATE TABLE `im_recently_chat_friends` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '自己id',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '来源,0:好友，1:群',
  `from_id` bigint(20) NOT NULL COMMENT '好友/群id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '好友添加时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for im_recently_notify
-- ----------------------------
DROP TABLE IF EXISTS `im_recently_notify`;
CREATE TABLE `im_recently_notify` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `notify_id` bigint(20) NOT NULL COMMENT '通知id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for im_relation
-- ----------------------------
DROP TABLE IF EXISTS `im_relation`;
CREATE TABLE `im_relation` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id,im_user.id',
  `friend_id` bigint(20) NOT NULL COMMENT '好友id,im_user.id',
  `remarks` varchar(30) NOT NULL COMMENT '好友备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加好友的时间',
  PRIMARY KEY (`user_id`,`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友关系，好友关系是单向的，所以一个关系需要存两记录';

-- ----------------------------
-- Table structure for im_sync_group_msg
-- ----------------------------
DROP TABLE IF EXISTS `im_sync_group_msg`;
CREATE TABLE `im_sync_group_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` bigint(20) NOT NULL COMMENT '群id,im_group_info.id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id,im_user.id',
  `last_msg_id` bigint(20) NOT NULL COMMENT '已经同步的最后一条消息id,im_group_msg.id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次消息拉时间',
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群消息同步表';

-- ----------------------------
-- Table structure for im_user
-- ----------------------------
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user` (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `account` varchar(40) NOT NULL COMMENT '用户名(登陆用)',
  `nick_name` varchar(40) NOT NULL COMMENT '昵称',
  `sex` tinyint(4) NOT NULL COMMENT '性别',
  `birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生日',
  `profile_photo` varchar(300) NOT NULL COMMENT '头像',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `signature` varchar(200) NOT NULL DEFAULT '' COMMENT '签名',
  `phone` varchar(15) NOT NULL DEFAULT '' COMMENT '电话',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮箱',
  `school` varchar(50) NOT NULL DEFAULT '' COMMENT '学校',
  `country` varchar(20) NOT NULL DEFAULT '' COMMENT '国家',
  `city` varchar(20) NOT NULL DEFAULT '' COMMENT '城市',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `account_index` (`account`(10),`phone`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- ----------------------------
-- Table structure for im_user_permission
-- ----------------------------
DROP TABLE IF EXISTS `im_user_permission`;
CREATE TABLE `im_user_permission` (
  `user_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知创建时间',
  PRIMARY KEY (`user_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色资源关联表';
