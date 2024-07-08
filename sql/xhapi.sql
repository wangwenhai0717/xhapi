/*
Navicat MySQL Data Transfer

Source Server         : lhdbmysql-e80dmps9/MySQL-81Vv
Source Server Version : 80022
Source Host           : gz-cynosdbmysql-grp-o1qshfxv.sql.tencentcdb.com:29501
Source Database       : xhapi

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2024-07-08 14:07:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for interface_info
-- ----------------------------
DROP TABLE IF EXISTS `interface_info`;
CREATE TABLE `interface_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口名称',
  `description` varchar(256) NOT NULL COMMENT '描述',
  `url` varchar(512) NOT NULL COMMENT '接口地址',
  `request_header` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求头',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '请求参数',
  `request_response` varchar(256) NOT NULL COMMENT '响应头',
  `status` int NOT NULL DEFAULT '0' COMMENT '接口状态（0-下线，1-上线）',
  `method` varchar(256) NOT NULL COMMENT '请求类型',
  `user_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_del` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除（0-未删，1-已删）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口信息表';

-- ----------------------------
-- Records of interface_info
-- ----------------------------
INSERT INTO `interface_info` VALUES ('1', 'GetUsernameByPost', '获取用户名', 'http://106.54.193.109:8111/api/name/user', '{\"Content-Type\":\"application/json\"}', '{\"name\":\"name\",\"type\":\"String\"}', '{\"Content-Type\":\"application/json\"}', '1', 'POST', null, '2024-04-10 10:58:36', '2024-04-10 10:58:36', '0');
INSERT INTO `interface_info` VALUES ('2', '田锦程', '郝鸿煊', 'www.contessa-boehm.name', '209.184.88.13', '1', '20.53.138.110', '1', '徐健柏', '尹伟祺', '2022-07-20 20:55:23', '2022-12-02 06:58:31', '0');
INSERT INTO `interface_info` VALUES ('3', '郭思源', '郭建辉', 'www.janell-feest.info', '198.25.23.109', null, '101.128.230.18', '0', '阎峻熙', '田驰', '2022-01-18 13:38:40', '2022-05-30 14:39:38', '0');
INSERT INTO `interface_info` VALUES ('4', '廖鸿煊', '莫鹏涛', 'www.deandre-pollich.com', '129.71.105.17', null, '202.37.210.63', '0', '刘鹭洋', '张凯瑞', '2022-06-23 16:16:57', '2022-08-11 16:08:38', '0');
INSERT INTO `interface_info` VALUES ('5', '丁弘文', '金文轩', 'www.bella-ohara.io', '193.177.186.138', null, '205.232.206.154', '0', '钱天翊', '谭楷瑞', '2022-10-02 08:14:36', '2022-06-29 16:02:48', '0');
INSERT INTO `interface_info` VALUES ('6', '周健柏', '胡立诚', 'www.pamula-schmeler.biz', '45.109.205.55', null, '59.43.66.199', '0', '何明哲', '黎浩然', '2022-09-06 10:05:44', '2022-09-03 04:25:16', '0');
INSERT INTO `interface_info` VALUES ('7', '曹鹏', '吕昊天', 'www.salvador-greenfelder.org', '102.48.164.227', null, '209.100.30.194', '0', '金晓博', '陈志泽', '2022-03-15 04:13:47', '2022-08-22 19:09:14', '0');
INSERT INTO `interface_info` VALUES ('8', '于昊天', '孟熠彤', 'www.warner-morissette.biz', '24.116.209.5', null, '151.54.4.2', '0', '谭博超', '何皓轩', '2022-06-02 13:43:53', '2022-02-02 01:38:24', '0');
INSERT INTO `interface_info` VALUES ('9', '邓思', '毛子轩', 'www.larisa-halvorson.net', '251.214.243.224', null, '20.3.204.117', '0', '郭果', '贾峻熙', '2022-04-01 06:31:30', '2022-01-10 08:27:38', '0');
INSERT INTO `interface_info` VALUES ('10', '杜煜祺', '陶荣轩', 'www.cedrick-stehr.io', '196.71.53.242', null, '127.223.55.33', '0', '白炫明', '邵凯瑞', '2022-10-23 17:03:46', '2022-02-16 18:23:36', '0');
INSERT INTO `interface_info` VALUES ('17', '查询', '测试1', 'www.contessa-boehm.name', '测试1', null, '测试', '0', 'GET', null, '2024-04-10 10:06:14', '2024-04-10 10:06:14', '0');
INSERT INTO `interface_info` VALUES ('18', '马昊焱', '袁明轩', 'www.eulalia-nader.com', '83.114.182.176', null, '153.152.178.126', '0', '周擎苍', '尹钰轩', '2022-11-19 18:43:40', '2022-12-03 15:18:37', '0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `secretKey` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'secretKey',
  `accessKey` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'accessKey',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1777965126163435523 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1777879825894207490', 'admin', '686cf144692de2d1ab8737c3978351f8', null, null, '讯号', 'https://lowcode-3gjxs6di23304e1a-1327162105.tcloudbaseapp.com/resources/2024-07/lowcode-1873502', null, 'admin', 'qwertyui', 'xunhao', '2024-04-10 10:02:36', '2024-07-07 15:27:01', '0');
INSERT INTO `user` VALUES ('1777965126163435522', 'user', '170d443bfcc76948ad0997cefde0bae7', null, null, null, null, null, 'admin', '26aa1bca03f59bfbbb7a0358631acfea', '402f0baab7dd3d27db8c4e5a8aa6d3ee', '2024-04-10 15:41:33', '2024-04-10 15:41:45', '0');

-- ----------------------------
-- Table structure for user_interface_info
-- ----------------------------
DROP TABLE IF EXISTS `user_interface_info`;
CREATE TABLE `user_interface_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '调用用户 id',
  `interface_info_id` bigint NOT NULL COMMENT '接口 id',
  `total_num` int NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `left_num` int NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
  `status` int NOT NULL DEFAULT '0' COMMENT '0-正常，1-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删，1-己删)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户调用接口关系';

-- ----------------------------
-- Records of user_interface_info
-- ----------------------------
INSERT INTO `user_interface_info` VALUES ('1', '1777879825894207490', '1', '16', '-2', '0', '2024-05-10 10:34:29', '2024-05-10 10:34:29', '0');
INSERT INTO `user_interface_info` VALUES ('2', '1777879825894207490', '2', '4', '1', '0', '2024-06-08 17:17:00', '2024-06-08 17:17:00', '0');
INSERT INTO `user_interface_info` VALUES ('3', '1777879825894207490', '3', '5', '1', '0', '2024-06-08 17:17:12', '2024-06-08 17:17:12', '0');
INSERT INTO `user_interface_info` VALUES ('4', '1777879825894207490', '4', '7', '1', '0', '2024-06-08 17:17:22', '2024-06-08 17:17:22', '0');
INSERT INTO `user_interface_info` VALUES ('5', '1777879825894207490', '5', '11', '1', '0', '2024-06-08 17:17:29', '2024-06-08 17:17:29', '0');
