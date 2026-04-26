/*
 ============================================
 电影票务系统数据库优化脚本
 优化内容：
 1. 添加索引提升查询性能
 2. 优化表结构
 3. 添加适当的约束
 ============================================
*/

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. user表优化
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `user_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `user_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
    `user_pwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
    `user_email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
    `user_role` int(1) NOT NULL DEFAULT 0 COMMENT '用户权限：0普通会员，1管理员',
    `user_headImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像地址',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE KEY `uk_user_name` (`user_name`) USING BTREE,
    KEY `idx_user_email` (`user_email`) USING BTREE,
    KEY `idx_user_role` (`user_role`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ============================================
-- 2. movie表优化
-- ============================================
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
    `movie_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '电影ID',
    `movie_cn_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影名称（中文）',
    `movie_fg_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电影名称（外语）',
    `movie_actor` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影演职人员',
    `movie_director` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影导演',
    `movie_detail` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影详情',
    `movie_duration` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影时长',
    `movie_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影类型',
    `movie_score` float(10, 1) NULL DEFAULT 0.0 COMMENT '电影评分',
    `movie_boxOffice` float(12, 4) NULL DEFAULT 0.0000 COMMENT '电影票房（单位：亿）',
    `movie_commentCount` bigint(10) NULL DEFAULT 0 COMMENT '电影评论数',
    `movie_releaseDate` date NOT NULL COMMENT '电影上映时间',
    `movie_country` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影制片地区',
    `movie_picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影海报地址',
    `movie_state` int(1) NOT NULL DEFAULT 1 COMMENT '电影状态：1在线，0下架',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`movie_id`) USING BTREE,
    KEY `idx_movie_state` (`movie_state`) USING BTREE,
    KEY `idx_movie_releaseDate` (`movie_releaseDate`) USING BTREE,
    KEY `idx_movie_score` (`movie_score`) USING BTREE,
    KEY `idx_movie_commentCount` (`movie_commentCount`) USING BTREE,
    KEY `idx_movie_boxOffice` (`movie_boxOffice`) USING BTREE,
    KEY `idx_movie_cn_name` (`movie_cn_name`) USING BTREE,
    KEY `idx_movie_type` (`movie_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电影表' ROW_FORMAT = Dynamic;

-- ============================================
-- 3. cinema表优化
-- ============================================
DROP TABLE IF EXISTS `cinema`;
CREATE TABLE `cinema` (
    `cinema_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '影院ID',
    `cinema_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '影院名称',
    `cinema_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '影院地址',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`cinema_id`) USING BTREE,
    UNIQUE KEY `uk_cinema_name` (`cinema_name`) USING BTREE,
    KEY `idx_cinema_address` (`cinema_address`(100)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '影院表' ROW_FORMAT = Dynamic;

-- ============================================
-- 4. hall表优化
-- ============================================
DROP TABLE IF EXISTS `hall`;
CREATE TABLE `hall` (
    `hall_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '放映厅ID',
    `hall_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '放映厅名称',
    `hall_capacity` int(10) NOT NULL DEFAULT 144 COMMENT '放映厅容量',
    `cinema_id` bigint(10) NOT NULL COMMENT '所属影院ID',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`hall_id`) USING BTREE,
    KEY `idx_hall_cinema_id` (`cinema_id`) USING BTREE,
    KEY `idx_hall_name` (`hall_name`) USING BTREE,
    CONSTRAINT `fk_hall_cinema` FOREIGN KEY (`cinema_id`) REFERENCES `cinema` (`cinema_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '放映厅表' ROW_FORMAT = Dynamic;

-- ============================================
-- 5. schedule表优化
-- ============================================
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
    `schedule_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '场次ID',
    `hall_id` bigint(10) NOT NULL COMMENT '所属放映厅ID',
    `movie_id` bigint(10) NOT NULL COMMENT '放映的电影ID',
    `schedule_startTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电影放映时间',
    `schedule_price` int(10) NOT NULL COMMENT '票价',
    `schedule_remain` int(10) NOT NULL COMMENT '剩余座位数',
    `schedule_state` int(1) NOT NULL DEFAULT 1 COMMENT '场次状态：1上映中，0下架',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`schedule_id`) USING BTREE,
    KEY `idx_schedule_hall_id` (`hall_id`) USING BTREE,
    KEY `idx_schedule_movie_id` (`movie_id`) USING BTREE,
    KEY `idx_schedule_state` (`schedule_state`) USING BTREE,
    KEY `idx_schedule_startTime` (`schedule_startTime`) USING BTREE,
    KEY `idx_schedule_movie_state` (`movie_id`, `schedule_state`) USING BTREE,
    CONSTRAINT `fk_schedule_hall` FOREIGN KEY (`hall_id`) REFERENCES `hall` (`hall_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_schedule_movie` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '场次表' ROW_FORMAT = Dynamic;

-- ============================================
-- 6. orderinfo表优化
-- ============================================
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo` (
    `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单ID',
    `user_id` bigint(10) NOT NULL COMMENT '所属用户ID',
    `schedule_id` bigint(10) NOT NULL COMMENT '所属场次ID',
    `order_position` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '座位信息',
    `order_state` int(1) NOT NULL DEFAULT 1 COMMENT '订单状态：0退票中，1已支付，2退票成功',
    `order_price` int(10) NOT NULL COMMENT '订单价格',
    `order_time` datetime(0) NOT NULL COMMENT '订单支付时间',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`order_id`) USING BTREE,
    KEY `idx_order_user_id` (`user_id`) USING BTREE,
    KEY `idx_order_schedule_id` (`schedule_id`) USING BTREE,
    KEY `idx_order_state` (`order_state`) USING BTREE,
    KEY `idx_order_time` (`order_time`) USING BTREE,
    KEY `idx_order_user_state` (`user_id`, `order_state`) USING BTREE,
    CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_order_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ============================================
-- 7. comment表优化
-- ============================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `comment_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `user_id` bigint(10) NOT NULL COMMENT '所属用户ID',
    `comment_content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
    `movie_id` bigint(10) NOT NULL COMMENT '所属电影ID',
    `comment_time` datetime(0) NOT NULL COMMENT '评论时间',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`comment_id`) USING BTREE,
    KEY `idx_comment_user_id` (`user_id`) USING BTREE,
    KEY `idx_comment_movie_id` (`movie_id`) USING BTREE,
    KEY `idx_comment_time` (`comment_time`) USING BTREE,
    KEY `idx_comment_movie_time` (`movie_id`, `comment_time`) USING BTREE,
    CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_movie` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 索引优化说明
-- ============================================
/*
 【优化原则】：
 1. 为经常用于WHERE条件、JOIN条件、ORDER BY、GROUP BY的字段添加索引
 2. 为高选择性的字段添加单列索引
 3. 为组合查询添加组合索引（最左前缀原则）
 4. 避免在索引列上进行函数操作或类型转换

 【具体优化】：
 1. user表：
    - 为user_name添加唯一索引（登录、注册高频查询）
    - 为user_email添加普通索引（找回密码等）
    - 为user_role添加普通索引（权限过滤）

 2. movie表：
    - 为movie_state添加普通索引（状态过滤）
    - 为movie_releaseDate添加普通索引（时间排序）
    - 为movie_score添加普通索引（评分排序）
    - 为movie_commentCount添加普通索引（热门排序）
    - 为movie_boxOffice添加普通索引（票房排序）
    - 为movie_cn_name添加普通索引（名称模糊查询）
    - 为movie_type添加普通索引（类型过滤）

 3. schedule表（多表关联高频表）：
    - 为hall_id添加普通索引（影院筛选）
    - 为movie_id添加普通索引（电影筛选）
    - 为schedule_state添加普通索引（状态过滤）
    - 为schedule_startTime添加普通索引（时间排序）
    - 为(movie_id, schedule_state)添加组合索引（电影+状态筛选）

 4. orderinfo表：
    - 为user_id添加普通索引（用户订单查询）
    - 为schedule_id添加普通索引（场次订单查询）
    - 为order_state添加普通索引（状态过滤）
    - 为order_time添加普通索引（时间排序）
    - 为(user_id, order_state)添加组合索引（用户+状态筛选）

 5. comment表：
    - 为user_id添加普通索引（用户评论查询）
    - 为movie_id添加普通索引（电影评论查询）
    - 为comment_time添加普通索引（时间排序）
    - 为(movie_id, comment_time)添加组合索引（电影评论时间排序）

 【外键约束】：
 添加适当的外键约束保证数据完整性：
 - hall.cinema_id -> cinema.cinema_id
 - schedule.hall_id -> hall.hall_id
 - schedule.movie_id -> movie.movie_id
 - orderinfo.user_id -> user.user_id
 - orderinfo.schedule_id -> schedule.schedule_id
 - comment.user_id -> user.user_id
 - comment.movie_id -> movie.movie_id

 【性能提升预估】：
 1. 登录查询：从全表扫描 → 唯一索引扫描，性能提升100x+
 2. 电影列表查询：从全表扫描 → 索引扫描，性能提升10x+
 3. 订单查询：从全表扫描 → 组合索引扫描，性能提升20x+
 4. 多表关联查询：通过外键索引优化JOIN性能
*/
