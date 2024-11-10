/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : school_system_v12-27.2

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 28/12/2023 02:30:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_num` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '编号',
  `password` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '123456' COMMENT '密码',
  `name` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '男' COMMENT '性别',
  `phone` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '手机',
  PRIMARY KEY (`admin_num`) USING BTREE,
  CONSTRAINT `admin_sex_check` CHECK ((`sex` = _utf8mb3'男') or (`sex` = _utf8mb3'女'))
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1001', '010202', '刘小贰', '男', '4286');
INSERT INTO `admin` VALUES ('1002', '010206', '梅老陆', '男', '42866');
INSERT INTO `admin` VALUES ('1003', '010214', '黄拾肆', '女', '43166');
INSERT INTO `admin` VALUES ('1004', '010218', '龙拾捌', '男', '431666');
INSERT INTO `admin` VALUES ('1005', '010229', '袁贰玖', '男', '428666');
INSERT INTO `admin` VALUES ('1006', '010231', '森叁壹', '男', '42886');

-- ----------------------------
-- Table structure for advice
-- ----------------------------
DROP TABLE IF EXISTS `advice`;
CREATE TABLE `advice`  (
  `stu_num` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `info` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '详细信息',
  INDEX `advice_FK_stunum`(`stu_num` ASC) USING BTREE,
  CONSTRAINT `advice_FK_stunum` FOREIGN KEY (`stu_num`) REFERENCES `student` (`stu_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of advice
-- ----------------------------
INSERT INTO `advice` VALUES ('202108010202', '建议全天24小时提供热水。');
INSERT INTO `advice` VALUES ('202108010202', '和上一条建议一样');
INSERT INTO `advice` VALUES ('202108010202', '和上一条建议不一样，但是和第一条建议一样');
INSERT INTO `advice` VALUES ('202108010206', '袁神!');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `cno` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '课程号',
  `cname` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '课程名',
  `score` smallint NULL DEFAULT NULL COMMENT '学分',
  `tno` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '授课教师',
  PRIMARY KEY (`cno`) USING BTREE,
  INDEX `teach`(`tno` ASC) USING BTREE,
  CONSTRAINT `teach` FOREIGN KEY (`tno`) REFERENCES `teacher` (`tno`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `score_range` CHECK ((`score` >= 0) and (`score` <= 100))
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('0001', '程序设计', 5, '2001');
INSERT INTO `course` VALUES ('0002', '高等数学', 5, '2002');
INSERT INTO `course` VALUES ('0003', '线性代数', 3, '2003');
INSERT INTO `course` VALUES ('0004', '大学物理', 3, '2004');
INSERT INTO `course` VALUES ('0005', '电子电路', 5, '2005');
INSERT INTO `course` VALUES ('0006', '计算机系统', 3, '2006');
INSERT INTO `course` VALUES ('0007', '数据库系统', 3, '2007');

-- ----------------------------
-- Table structure for dormitory
-- ----------------------------
DROP TABLE IF EXISTS `dormitory`;
CREATE TABLE `dormitory`  (
  `floor_num` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '楼号',
  `layer` int NOT NULL COMMENT '楼层',
  `room_num` int NOT NULL COMMENT '宿舍号',
  `bed_total` int NOT NULL DEFAULT 4 COMMENT '总床位数',
  `bed_surplus` int NOT NULL DEFAULT 4 COMMENT '剩余床位数',
  `price` int NOT NULL COMMENT '单价',
  PRIMARY KEY (`floor_num`, `layer`, `room_num`) USING BTREE,
  INDEX `floor_num`(`floor_num` ASC) USING BTREE,
  INDEX `layer`(`layer` ASC) USING BTREE,
  INDEX `room_num`(`room_num` ASC) USING BTREE,
  INDEX `floor_num_2`(`floor_num` ASC, `room_num` ASC) USING BTREE,
  CONSTRAINT `dormitory_FK_floornum` FOREIGN KEY (`floor_num`) REFERENCES `floor` (`floor_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bed_total_range` CHECK (`bed_total` > 0),
  CONSTRAINT `layer_range` CHECK (`layer` > 0),
  CONSTRAINT `price_range` CHECK (`price` > 0),
  CONSTRAINT `room_num_range` CHECK (`room_num` > 0)
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dormitory
-- ----------------------------
INSERT INTO `dormitory` VALUES ('TM25', 1, 428, 4, 4, 300);
INSERT INTO `dormitory` VALUES ('TM25', 2, 429, 4, 4, 310);
INSERT INTO `dormitory` VALUES ('TM25', 3, 430, 4, 4, 320);
INSERT INTO `dormitory` VALUES ('TM25', 4, 431, 4, 4, 330);
INSERT INTO `dormitory` VALUES ('TM25', 5, 432, 4, 4, 340);
INSERT INTO `dormitory` VALUES ('TM316', 3, 104, 4, 4, 110);
INSERT INTO `dormitory` VALUES ('TM316', 4, 103, 4, 4, 120);
INSERT INTO `dormitory` VALUES ('TM316', 5, 102, 4, 4, 130);
INSERT INTO `dormitory` VALUES ('TM316', 7, 433, 4, 4, 150);
INSERT INTO `dormitory` VALUES ('TM39', 1, 105, 2, 2, 50);
INSERT INTO `dormitory` VALUES ('TM39', 2, 106, 2, 2, 50);

-- ----------------------------
-- Table structure for floor
-- ----------------------------
DROP TABLE IF EXISTS `floor`;
CREATE TABLE `floor`  (
  `floor_num` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '楼号',
  `layer_amount` int NOT NULL COMMENT '层数',
  `room_amount` int NOT NULL COMMENT '房间数',
  `category` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类别',
  `sex` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '男' COMMENT '居住性别',
  `admin_num` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '宿管编号',
  PRIMARY KEY (`floor_num`) USING BTREE,
  INDEX `floor_fk_admin`(`admin_num` ASC) USING BTREE,
  CONSTRAINT `floor_fk_admin` FOREIGN KEY (`admin_num`) REFERENCES `admin` (`admin_num`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `layer_amount_range` CHECK (`layer_amount` > 0),
  CONSTRAINT `room_amount_range` CHECK (`room_amount` > 0),
  CONSTRAINT `sex_ran` CHECK ((`sex` = _utf8mb3'男') or (`sex` = _utf8mb3'女') or (`sex` = _utf8mb3'混合'))
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of floor
-- ----------------------------
INSERT INTO `floor` VALUES ('TM11', 8, 320, '普通', '男', '1004');
INSERT INTO `floor` VALUES ('TM25', 7, 140, '豪华', '混合', '1001');
INSERT INTO `floor` VALUES ('TM316', 7, 280, '普通', '男', '1003');
INSERT INTO `floor` VALUES ('TM39', 6, 240, '简朴', '女', '1002');
INSERT INTO `floor` VALUES ('TM47', 7, 210, '豪华', '女', '1005');
INSERT INTO `floor` VALUES ('TM66', 5, 100, '巨豪华', '男', '1006');

-- ----------------------------
-- Table structure for in_out
-- ----------------------------
DROP TABLE IF EXISTS `in_out`;
CREATE TABLE `in_out`  (
  `stu_num` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `floor_num` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '楼号',
  `category` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类别（出/入）',
  `time` datetime NOT NULL COMMENT '时间',
  INDEX `inout_FK_stunum`(`stu_num` ASC) USING BTREE,
  INDEX `inout_FK_floornum`(`floor_num` ASC) USING BTREE,
  CONSTRAINT `inout_FK_floornum` FOREIGN KEY (`floor_num`) REFERENCES `floor` (`floor_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `inout_FK_stunum` FOREIGN KEY (`stu_num`) REFERENCES `student` (`stu_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `category_range` CHECK ((`category` = _utf8mb3'出') or (`category` = _utf8mb3'入'))
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of in_out
-- ----------------------------
INSERT INTO `in_out` VALUES ('202108010202', 'TM25', '出', '2021-09-30 15:30:21');
INSERT INTO `in_out` VALUES ('202108010202', 'TM25', '入', '2021-10-07 10:45:01');
INSERT INTO `in_out` VALUES ('202108010214', 'TM316', '出', '2021-11-13 09:25:00');
INSERT INTO `in_out` VALUES ('202108010202', 'TM25', '出', '2023-12-27 08:56:13');
INSERT INTO `in_out` VALUES ('202108010206', 'TM25', '出', '2023-12-27 08:56:29');

-- ----------------------------
-- Table structure for repair
-- ----------------------------
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair`  (
  `stu_num` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `floor_num` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '楼号',
  `layer` int NOT NULL COMMENT '楼层',
  `room_num` int NOT NULL COMMENT '宿舍号',
  `info` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '报修详细信息',
  `yes_no` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否处理（是/否）',
  INDEX `repair_FK_stunum`(`stu_num` ASC) USING BTREE,
  INDEX `repair_FK_floornum`(`floor_num` ASC) USING BTREE,
  INDEX `repair_FK_layer`(`layer` ASC) USING BTREE,
  INDEX `repair_FK_dormitory`(`room_num` ASC, `floor_num` ASC, `layer` ASC) USING BTREE,
  CONSTRAINT `repair-fk_roomnum` FOREIGN KEY (`room_num`) REFERENCES `dormitory` (`room_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `repair_fk_floornum` FOREIGN KEY (`floor_num`) REFERENCES `dormitory` (`floor_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `repair_fk_layer` FOREIGN KEY (`layer`) REFERENCES `dormitory` (`layer`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `repair_fk_stunum` FOREIGN KEY (`stu_num`) REFERENCES `student` (`stu_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `yes_no_range` CHECK ((`yes_no` = _utf8mb3'是') or (`yes_no` = _utf8mb3'否'))
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of repair
-- ----------------------------
INSERT INTO `repair` VALUES ('202108010214', 'TM316', 7, 433, '空调损坏。', '是');
INSERT INTO `repair` VALUES ('202008010218', 'TM39', 2, 106, '水龙头损坏。', '否');

-- ----------------------------
-- Table structure for select_course
-- ----------------------------
DROP TABLE IF EXISTS `select_course`;
CREATE TABLE `select_course`  (
  `sno` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `cno` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '课程号',
  `mark` smallint NULL DEFAULT NULL COMMENT '成绩',
  PRIMARY KEY (`sno`, `cno`) USING BTREE,
  INDEX `select_cou`(`cno` ASC) USING BTREE,
  CONSTRAINT `select_cou` FOREIGN KEY (`cno`) REFERENCES `course` (`cno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `select_stu` FOREIGN KEY (`sno`) REFERENCES `student` (`stu_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of select_course
-- ----------------------------
INSERT INTO `select_course` VALUES ('202008010206', '0001', 1);
INSERT INTO `select_course` VALUES ('202008010206', '0002', 100);
INSERT INTO `select_course` VALUES ('202108010202', '0001', 0);
INSERT INTO `select_course` VALUES ('202108010202', '0002', 50);
INSERT INTO `select_course` VALUES ('202108010202', '0003', 0);
INSERT INTO `select_course` VALUES ('202108010202', '0004', 0);
INSERT INTO `select_course` VALUES ('202108010202', '0006', 0);
INSERT INTO `select_course` VALUES ('202108010202', '0007', 0);
INSERT INTO `select_course` VALUES ('202108010206', '0001', 0);
INSERT INTO `select_course` VALUES ('202108010206', '0002', 90);
INSERT INTO `select_course` VALUES ('202108010206', '0003', 0);
INSERT INTO `select_course` VALUES ('202108010206', '0004', 0);
INSERT INTO `select_course` VALUES ('202108010206', '0007', 30);
INSERT INTO `select_course` VALUES ('202108010214', '0002', NULL);
INSERT INTO `select_course` VALUES ('202108010214', '0003', 90);
INSERT INTO `select_course` VALUES ('202108010218', '0001', 0);
INSERT INTO `select_course` VALUES ('202108010218', '0002', 0);

-- ----------------------------
-- Table structure for stayinfo
-- ----------------------------
DROP TABLE IF EXISTS `stayinfo`;
CREATE TABLE `stayinfo`  (
  `stu_num` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `floor_num` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '楼号',
  `layer` int NOT NULL COMMENT '楼层',
  `room_num` int NOT NULL COMMENT '宿舍号',
  `time` date NOT NULL COMMENT '入住时间',
  PRIMARY KEY (`stu_num`) USING BTREE,
  INDEX `stayinfo_FK_layer`(`layer` ASC) USING BTREE,
  INDEX `stayinfo_FK_roomnum`(`room_num` ASC) USING BTREE,
  INDEX `stayinfo_FK_dormitory`(`floor_num` ASC, `layer` ASC, `room_num` ASC) USING BTREE,
  CONSTRAINT `stayinfo_floornum` FOREIGN KEY (`floor_num`) REFERENCES `dormitory` (`floor_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stayinfo_layer` FOREIGN KEY (`layer`) REFERENCES `dormitory` (`layer`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stayinfo_roomnum` FOREIGN KEY (`room_num`) REFERENCES `dormitory` (`room_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stayinfo_stunum` FOREIGN KEY (`stu_num`) REFERENCES `student` (`stu_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stayinfo
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `stu_num` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `password` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '123456' COMMENT '密码',
  `name` char(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '男' COMMENT '性别',
  `birth` int NOT NULL COMMENT '出生日期（用于计算年龄）',
  `grade` int NOT NULL COMMENT '年级',
  `faculty` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '院系',
  `class` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '班级',
  `phone` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '手机',
  `yes_no` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否入住（是/否）',
  PRIMARY KEY (`stu_num`) USING BTREE,
  CONSTRAINT `birth_range` CHECK (`birth` > 0),
  CONSTRAINT `grade_range` CHECK (`grade` > 0),
  CONSTRAINT `stu_sex_range` CHECK ((`sex` = _utf8mb3'男') or (`sex` = _utf8mb3'女'))
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('202008010206', '0206', '阿梅', '男', 2001, 2020, '信息科学与工程学院', '计科2002', '15651516155', '否');
INSERT INTO `student` VALUES ('202008010214', '0214', '阿政', '女', 2002, 2020, '信息科学与工程学院', '计科2001', '13692875100', '否');
INSERT INTO `student` VALUES ('202008010218', '0218', '小强', '男', 2002, 2020, '信息科学与工程学院', '计科2102', '13692873211', '否');
INSERT INTO `student` VALUES ('202108010202', '0202', '小刘', '男', 2002, 2021, '信息科学与工程学院', '计科2102', '13692871011', '否');
INSERT INTO `student` VALUES ('202108010206', '0206', '小梅', '男', 2002, 2021, '信息科学与工程学院', '计科2102', '13692871022', '否');
INSERT INTO `student` VALUES ('202108010214', '0214', '小黄', '女', 2003, 2021, '信息科学与工程学院', '计科2102', '13692871033', '否');
INSERT INTO `student` VALUES ('202108010218', '0218', '小龙', '女', 2003, 2021, '信息科学与工程学院', '计科2102', '13692875111', '否');
INSERT INTO `student` VALUES ('202108010229', '0229', '小袁', '男', 2002, 2021, '信息科学与工程学院', '计科2102', '13692873222', '否');
INSERT INTO `student` VALUES ('202108010231', '0231', '小森', '男', 2002, 2021, '信息科学与工程学院', '计科2102', '13692873033', '否');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `tno` char(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '职工号',
  `tname` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '教师名',
  `tsex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '男' COMMENT '性别',
  `password` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '123456' COMMENT '密码',
  PRIMARY KEY (`tno`) USING BTREE,
  CONSTRAINT `tsex_range` CHECK ((`tsex` = _utf8mb3'男') or (`tsex` = _utf8mb3'女'))
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('2001', '波波', '男', '200101');
INSERT INTO `teacher` VALUES ('2002', '圆周率', '男', '200202');
INSERT INTO `teacher` VALUES ('2003', '易学军', '男', '200303');
INSERT INTO `teacher` VALUES ('2004', '李美姮', '女', '200404');
INSERT INTO `teacher` VALUES ('2005', '凌纯清', '女', '200505');
INSERT INTO `teacher` VALUES ('2006', '黄丽达', '女', '200606');
INSERT INTO `teacher` VALUES ('2007', '杨超', '女', '200707');

-- ----------------------------
-- View structure for adminview_advice
-- ----------------------------
DROP VIEW IF EXISTS `adminview_advice`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `adminview_advice` AS select `advice`.`stu_num` AS `stu_num`,`student`.`name` AS `name`,`advice`.`info` AS `info`,`floor`.`admin_num` AS `admin_num` from (((`advice` join `student` on((`advice`.`stu_num` = `student`.`stu_num`))) join `stayinfo` on((`advice`.`stu_num` = `stayinfo`.`stu_num`))) join `floor` on((`stayinfo`.`floor_num` = `floor`.`floor_num`)));

-- ----------------------------
-- View structure for adminview_student
-- ----------------------------
DROP VIEW IF EXISTS `adminview_student`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `adminview_student` AS select `student`.`stu_num` AS `stu_num`,`student`.`password` AS `password`,`student`.`name` AS `name`,`student`.`sex` AS `sex`,`student`.`birth` AS `birth`,`student`.`grade` AS `grade`,`student`.`faculty` AS `faculty`,`student`.`class` AS `class`,`student`.`phone` AS `phone`,`student`.`yes_no` AS `yes_no`,`stayinfo`.`floor_num` AS `floor_num` from (`student` join `stayinfo` on((`student`.`stu_num` = `stayinfo`.`stu_num`)));

-- ----------------------------
-- View structure for student_advice
-- ----------------------------
DROP VIEW IF EXISTS `student_advice`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_advice` AS select `advice`.`stu_num` AS `stu_num`,`student`.`name` AS `name`,`advice`.`info` AS `info` from (`student` join `advice` on((`student`.`stu_num` = `advice`.`stu_num`)));

-- ----------------------------
-- View structure for student_inout
-- ----------------------------
DROP VIEW IF EXISTS `student_inout`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_inout` AS select `in_out`.`stu_num` AS `stu_num`,`student`.`name` AS `name`,`in_out`.`floor_num` AS `floor_num`,`in_out`.`category` AS `category`,`in_out`.`time` AS `time` from (`student` join `in_out` on((`student`.`stu_num` = `in_out`.`stu_num`)));

-- ----------------------------
-- View structure for student_repair
-- ----------------------------
DROP VIEW IF EXISTS `student_repair`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_repair` AS select `repair`.`stu_num` AS `stu_num`,`student`.`name` AS `name`,`repair`.`floor_num` AS `floor_num`,`repair`.`layer` AS `layer`,`repair`.`room_num` AS `room_num`,`repair`.`info` AS `info`,`repair`.`yes_no` AS `yes_no` from (`student` join `repair` on((`student`.`stu_num` = `repair`.`stu_num`)));

-- ----------------------------
-- View structure for student_stayinfo
-- ----------------------------
DROP VIEW IF EXISTS `student_stayinfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_stayinfo` AS select `student`.`stu_num` AS `stu_num`,`student`.`name` AS `name`,`stayinfo`.`floor_num` AS `floor_num`,`stayinfo`.`layer` AS `layer`,`stayinfo`.`room_num` AS `room_num`,`stayinfo`.`time` AS `time` from (`student` join `stayinfo` on((`student`.`stu_num` = `stayinfo`.`stu_num`)));

SET FOREIGN_KEY_CHECKS = 1;
