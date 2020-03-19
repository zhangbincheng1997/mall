SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '父ID',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '小说', 0, '2020-03-19 03:34:46', '2020-03-19 04:21:25');
INSERT INTO `category` VALUES (2, '文学', 0, '2020-03-19 03:34:50', '2020-03-19 04:21:27');
INSERT INTO `category` VALUES (3, '励志与成功', 0, '2020-03-19 03:38:37', '2020-03-19 04:21:29');
INSERT INTO `category` VALUES (4, '青春文学', 0, '2020-03-19 04:05:27', '2020-03-19 04:21:31');
INSERT INTO `category` VALUES (5, '管理', 0, '2020-03-19 04:05:28', '2020-03-19 04:21:32');
INSERT INTO `category` VALUES (6, '武侠', 1, '2020-03-19 04:22:05', '2020-03-19 04:27:13');
INSERT INTO `category` VALUES (7, '网络小说', 1, '2020-03-19 04:22:08', '2020-03-19 04:23:49');
INSERT INTO `category` VALUES (8, '都市', 1, '2020-03-19 04:22:09', '2020-03-19 04:23:53');
INSERT INTO `category` VALUES (9, '戏剧曲艺', 2, '2020-03-19 04:23:08', '2020-03-19 04:23:59');
INSERT INTO `category` VALUES (10, '诗词歌曲', 2, '2020-03-19 04:23:11', '2020-03-19 04:24:01');
INSERT INTO `category` VALUES (11, '外国文学', 2, '2020-03-19 04:23:13', '2020-03-19 04:24:04');
INSERT INTO `category` VALUES (12, '智慧格言', 3, '2020-03-19 04:24:22', '2020-03-19 04:24:49');
INSERT INTO `category` VALUES (13, '人际与社交', 3, '2020-03-19 04:24:23', '2020-03-19 04:24:57');
INSERT INTO `category` VALUES (14, '经典著作', 3, '2020-03-19 04:24:24', '2020-03-19 04:25:08');
INSERT INTO `category` VALUES (15, '校园', 4, '2020-03-19 04:24:26', '2020-03-19 04:26:02');
INSERT INTO `category` VALUES (16, '悬疑惊悚', 4, '2020-03-19 04:24:27', '2020-03-19 04:26:08');
INSERT INTO `category` VALUES (17, '韩国青春文学', 4, '2020-03-19 04:24:28', '2020-03-19 04:26:18');
INSERT INTO `category` VALUES (18, '领导者', 5, '2020-03-19 04:24:30', '2020-03-19 04:26:28');
INSERT INTO `category` VALUES (19, '财务管理', 5, '2020-03-19 04:24:31', '2020-03-19 04:26:32');
INSERT INTO `category` VALUES (20, '生产与运作管理', 5, '2020-03-19 04:24:33', '2020-03-19 04:26:44');

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '（当前）商品名称',
  `product_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '（当前）商品图标',
  `product_price` decimal(8, 2) NOT NULL COMMENT '（当前）商品价格',
  `product_quantity` int(11) NOT NULL COMMENT '商品数量',
  PRIMARY KEY (`id`) USING BTREE,
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_master
-- ----------------------------
DROP TABLE IF EXISTS `order_master`;
CREATE TABLE `order_master`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `amount` decimal(8, 2) NOT NULL COMMENT '总金额',
  `status` smallint(3) NOT NULL DEFAULT 0 COMMENT '订单状态：0-6，默认0',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_timeline
-- ----------------------------
DROP TABLE IF EXISTS `order_timeline`;
CREATE TABLE `order_timeline`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `status` smallint(3) NOT NULL DEFAULT 0 COMMENT '订单状态',
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '生成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pid` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图标',
  `price` decimal(8, 2) NOT NULL COMMENT '价格',
  `stock` int(11) NOT NULL COMMENT '库存',
  `category` bigint(20) NOT NULL COMMENT '分类ID',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0关、1开，默认0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category`(`category`) USING BTREE,
  CONSTRAINT `category` FOREIGN KEY (`category`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '天龙八部', '天龙八部', 'http://qiniu.littleredhat1997.com/FsctutU1f2OzOhpfnbO6vuNloHF5', 91.90, 100, 6, 1);
INSERT INTO `product` VALUES (2, '倚天屠龙记', '倚天屠龙记', 'http://qiniu.littleredhat1997.com/FpmgFRT8LOvnl2cwzrZItPuUGIAO', 79.00, 100, 6, 1);
INSERT INTO `product` VALUES (3, '射雕英雄传', '射雕英雄传', 'http://qiniu.littleredhat1997.com/Fv1ILmhqS9F12n-jKAdRNk2-olX9', 72.90, 100, 6, 1);
INSERT INTO `product` VALUES (4, '哈利波特', '哈利波特', 'http://qiniu.littleredhat1997.com/FkNobMJbD572cpVQ07iCm8SYsEmV', 49.50, 100, 7, 1);
INSERT INTO `product` VALUES (5, '三生三世', '三生三世', 'http://qiniu.littleredhat1997.com/FlQeKOk4G0t4F1o-ddG7G295ySZU', 63.50, 100, 7, 1);
INSERT INTO `product` VALUES (6, '橘生淮南', '橘生淮南', 'http://qiniu.littleredhat1997.com/FtI1aoAm6wgyW7ssckO7W9F7DzvC', 45.00, 100, 7, 1);
INSERT INTO `product` VALUES (7, '张大小姐', '张大小姐', 'http://qiniu.littleredhat1997.com/FudUO4X_aqAg89jJ6Y6uGeRWO5zC', 27.90, 100, 8, 1);
INSERT INTO `product` VALUES (8, '老人与海', '老人与海', 'http://qiniu.littleredhat1997.com/FjzxlHMbKJvB8qkDkcSWAKBp6Z94', 23.84, 100, 8, 1);
INSERT INTO `product` VALUES (9, '新名字的故事', '新名字的故事', 'http://qiniu.littleredhat1997.com/FiPMLb_CovQ1sWnSSyLiLmtoJT0L', 41.20, 100, 8, 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ROLE_ADMIN');
INSERT INTO `role` VALUES (2, 'ROLE_USER');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'http://qiniu.littleredhat1997.com/avatar.png' COMMENT '头像',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '普通用户' COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` smallint(3) NULL DEFAULT 0 COMMENT '性别：0未知、1男性、2女性，默认0',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE COMMENT '用户名'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$.tRtx0egh3PiqJ/auImsQu6RdFysCzl0QV.Q4fVF0Kz24Lh7HoxAC', 'http://qiniu.littleredhat1997.com/FvjJNyWw7hmQFVsp7jm86nDm9Ex_', '超级用户', '1656704949@qq.com', 0, '2020-02-02');
INSERT INTO `user` VALUES (2, '123', '$2a$10$iChB/T3bGkZB0TmGfS8ayun.9WSYtYS0raDyJVzroYK7AZ.IhSqDm', 'http://qiniu.littleredhat1997.com/avatar.png', '普通用户', NULL, 0, NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;
