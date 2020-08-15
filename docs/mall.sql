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
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '新鲜水果', 0, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (2, '海鲜水产', 0, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (3, '精选肉类', 0, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (4, '冷饮冻食', 0, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (5, '蔬菜食品', 0, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (6, '草莓', 1, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (7, '水蜜桃', 1, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (8, '车厘子', 1, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (9, '海鲜礼盒', 2, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (10, '鱼类', 2, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (11, '贝类', 2, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (12, '鸡翅', 3, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (13, '猪肋排', 3, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (14, '牛排', 3, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (15, '酸奶', 4, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (16, '冰淇淋', 4, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (17, '牛奶', 4, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (18, '水培蔬菜', 5, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (19, '玉米', 5, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);
INSERT INTO `category` VALUES (20, '蛋品', 5, '2020-08-08 00:00:00', '2020-08-08 00:00:00', 0);

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
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '订单状态：0-8，默认0',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '草莓', '草莓', 'http://qiniu.littleredhat1997.com/草莓.jpg', 1.00, 100, 6, 1, 0);
INSERT INTO `product` VALUES (2, '水蜜桃', '水蜜桃', 'http://qiniu.littleredhat1997.com/水蜜桃.jpg', 1.00, 100, 7, 1, 0);
INSERT INTO `product` VALUES (3, '车厘子', '车厘子', 'http://qiniu.littleredhat1997.com/车厘子.jpg', 1.00, 100, 8, 1, 0);
INSERT INTO `product` VALUES (4, '海鲜礼盒', '海鲜礼盒', 'http://qiniu.littleredhat1997.com/海鲜礼盒.jpg', 1.00, 100, 9, 1, 0);
INSERT INTO `product` VALUES (5, '鱼类', '鱼类', 'http://qiniu.littleredhat1997.com/鱼类.jpg', 1.00, 100, 10, 1, 0);
INSERT INTO `product` VALUES (6, '贝类', '贝类', 'http://qiniu.littleredhat1997.com/贝类.jpg', 1.00, 100, 11, 1, 0);
INSERT INTO `product` VALUES (7, '鸡翅', '鸡翅', 'http://qiniu.littleredhat1997.com/鸡翅.jpg', 1.00, 100, 12, 1, 0);
INSERT INTO `product` VALUES (8, '猪肋排', '猪肋排', 'http://qiniu.littleredhat1997.com/猪肋排.jpg', 1.00, 100, 13, 1, 0);
INSERT INTO `product` VALUES (9, '牛排', '牛排', 'http://qiniu.littleredhat1997.com/牛排.jpg', 1.00, 100, 14, 1, 0);
INSERT INTO `product` VALUES (10, '酸奶', '酸奶', 'http://qiniu.littleredhat1997.com/酸奶.jpg', 1.00, 100, 15, 1, 0);
INSERT INTO `product` VALUES (11, '冰淇淋', '冰淇淋', 'http://qiniu.littleredhat1997.com/冰淇淋.jpg', 1.00, 100, 16, 1, 0);
INSERT INTO `product` VALUES (12, '牛奶', '牛奶', 'http://qiniu.littleredhat1997.com/牛奶.jpg', 1.00, 100, 17, 1, 0);
INSERT INTO `product` VALUES (13, '水培蔬菜', '水培蔬菜', 'http://qiniu.littleredhat1997.com/水培蔬菜.jpg', 1.00, 100, 18, 1, 0);
INSERT INTO `product` VALUES (14, '玉米', '玉米', 'http://qiniu.littleredhat1997.com/玉米.jpg', 1.00, 100, 19, 1, 0);
INSERT INTO `product` VALUES (15, '蛋品', '蛋品', 'http://qiniu.littleredhat1997.com/蛋品.jpg', 1.00, 100, 20, 1, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `gender` tinyint(3) NULL DEFAULT 0 COMMENT '性别：0未知、1男性、2女性，默认0',
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
