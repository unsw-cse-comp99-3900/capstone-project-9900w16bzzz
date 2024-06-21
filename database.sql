/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : e-invoice

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 21/06/2024 14:13:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`  (
                           `file_id` int(0) NOT NULL,
                           `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `user_id` bigint(0) NOT NULL,
                           `file_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
                           `file_validation` tinyint(0) NULL DEFAULT NULL,
                           PRIMARY KEY (`file_id`, `user_id`) USING BTREE,
                           INDEX `file`(`user_id`) USING BTREE,
                           CONSTRAINT `file` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_file
-- ----------------------------

-- ----------------------------
-- Table structure for t_login_fail
-- ----------------------------
DROP TABLE IF EXISTS `t_login_fail`;
CREATE TABLE `t_login_fail`  (
                                 `login_fail_id` bigint(0) NOT NULL,
                                 `user_id` bigint(0) NULL DEFAULT NULL,
                                 `user_type` int(0) NULL DEFAULT NULL,
                                 `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                 `lock_flag` tinyint(0) NULL DEFAULT NULL,
                                 `login_fail_count` int(0) NULL DEFAULT NULL,
                                 `login_lock_begin_time` datetime(0) NULL DEFAULT NULL,
                                 `update_time` datetime(0) NULL DEFAULT NULL,
                                 `create_time` datetime(0) NULL DEFAULT NULL,
                                 PRIMARY KEY (`login_fail_id`) USING BTREE,
                                 INDEX `login_fail`(`user_id`) USING BTREE,
                                 CONSTRAINT `login_fail` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_login_fail
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
                           `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `firstname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `lastname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `update_time` datetime(0) NULL DEFAULT NULL,
                           `create_time` datetime(0) NULL DEFAULT NULL,
                           `user_id` bigint(0) NOT NULL,
                           PRIMARY KEY (`user_id`) USING BTREE,
                           INDEX `username`(`username`) USING BTREE,
                           INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
