DROP DATABASE IF EXISTS `e-invoice`;
CREATE DATABASE IF NOT EXISTS `e-invoice` ;
USE `e-invoice`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_invoice_file
-- ----------------------------
DROP TABLE IF EXISTS `t_invoice_file`;
CREATE TABLE `t_invoice_file`  (
                              `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
                              `invoice_id` bigint UNSIGNED NOT NULL,
                              `file_type` tinyint(1) UNSIGNED NOT NULL,
                              `content` longblob NOT NULL,
                              PRIMARY KEY (`id` DESC),
                              UNIQUE INDEX `id`(`id`) USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_invoice_file
-- ----------------------------

-- ----------------------------
-- Table structure for t_invoice
-- ----------------------------
DROP TABLE IF EXISTS `t_invoice`;
CREATE TABLE `t_invoice`  (
                              `invoice_id` bigint UNSIGNED NOT NULL,
                              `user_id` bigint UNSIGNED NOT NULL,
                              `file_name` varchar(255) NOT NULL,
                              `pdf_flag` tinyint(1) UNSIGNED NULL DEFAULT 0,
                              `json_flag` tinyint(1) UNSIGNED NULL DEFAULT 0,
                              `xml_flag` tinyint(1) UNSIGNED NULL DEFAULT 0,
                              `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`invoice_id` DESC, `user_id`),
                              UNIQUE INDEX `invoiceId`(`invoice_id`) USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_invoice
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
                                 `file_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                 `lock_flag` tinyint(0) NULL DEFAULT NULL,
                                 `login_fail_count` int(0) NULL DEFAULT NULL,
                                 `login_lock_begin_time` datetime(0) NULL DEFAULT NULL,
                                 `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
                           `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `login_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           `user_id` bigint(0) NOT NULL AUTO_INCREMENT,
                           PRIMARY KEY (`user_id`) USING BTREE,
                           INDEX `username`(`user_name`) USING BTREE,
                           INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;