/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.5.12 : Database - chatyou_wei
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`chatyou_wei` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `chatyou_wei`;

/*Table structure for table `t_activity` */

DROP TABLE IF EXISTS `t_activity`;

CREATE TABLE `t_activity` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_activity_name` varchar(100) DEFAULT NULL,
  `t_activity_number` int(11) DEFAULT NULL,
  `t_join_term` int(11) DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.禁用',
  `t_begin_time` datetime DEFAULT NULL,
  `t_end_time` datetime DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_activity_detail` */

DROP TABLE IF EXISTS `t_activity_detail`;

CREATE TABLE `t_activity_detail` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_activity_id` int(11) DEFAULT NULL,
  `t_prize_name` varchar(100) DEFAULT NULL,
  `t_prize_size` varchar(20) DEFAULT NULL,
  `t_prize_number` int(11) DEFAULT NULL,
  `t_surplus_number` int(11) DEFAULT NULL,
  `t_is_join` int(11) DEFAULT NULL COMMENT '0.参与\r\n1.不参与',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_admin` */

DROP TABLE IF EXISTS `t_admin`;

CREATE TABLE `t_admin` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_name` varchar(255) DEFAULT NULL,
  `t_pass_word` varchar(255) DEFAULT NULL,
  `t_role_id` int(11) DEFAULT NULL,
  `t_is_disable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.禁用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Table structure for table `t_album` */

DROP TABLE IF EXISTS `t_album`;

CREATE TABLE `t_album` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_title` varchar(100) DEFAULT NULL,
  `t_fileId` varchar(100) DEFAULT NULL,
  `t_video_img` varchar(255) DEFAULT NULL,
  `t_addres_url` varchar(150) DEFAULT NULL,
  `t_file_type` int(11) DEFAULT NULL COMMENT '0.图片\r\n1.视频',
  `t_is_private` int(11) DEFAULT NULL COMMENT '0.否\r\n1.是',
  `t_see_count` int(11) DEFAULT NULL,
  `t_money` decimal(7,2) DEFAULT NULL,
  `t_is_del` int(11) DEFAULT NULL COMMENT '0.否\r\n1.是',
  `t_auditing_type` int(11) DEFAULT NULL COMMENT '0.未审核\r\n1.已审核\r\n2.审核失败',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

/*Table structure for table `t_alipay_setup` */

DROP TABLE IF EXISTS `t_alipay_setup`;

CREATE TABLE `t_alipay_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_alipay_appid` varchar(255) DEFAULT NULL,
  `t_alipay_public_key` varchar(3000) DEFAULT NULL,
  `t_alipay_private_key` varchar(3000) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `t_anchor` */

DROP TABLE IF EXISTS `t_anchor`;

CREATE TABLE `t_anchor` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_state` int(11) DEFAULT NULL COMMENT '0.空闲1.忙碌2.离线',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=860 DEFAULT CHARSET=utf8;

/*Table structure for table `t_anchor_devote` */

DROP TABLE IF EXISTS `t_anchor_devote`;

CREATE TABLE `t_anchor_devote` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_anchor_id` int(11) DEFAULT NULL,
  `t_devote_value` decimal(11,2) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Table structure for table `t_anchor_guild` */

DROP TABLE IF EXISTS `t_anchor_guild`;

CREATE TABLE `t_anchor_guild` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_guild_id` int(11) DEFAULT NULL,
  `t_anchor_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Table structure for table `t_anchor_setup` */

DROP TABLE IF EXISTS `t_anchor_setup`;

CREATE TABLE `t_anchor_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_video_gold` decimal(7,2) DEFAULT NULL,
  `t_text_gold` decimal(7,2) DEFAULT NULL,
  `t_phone_gold` decimal(7,2) DEFAULT NULL,
  `t_weixin_gold` decimal(7,2) DEFAULT NULL,
  `t_private_photo_gold` decimal(7,2) DEFAULT NULL,
  `t_private_video_gold` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

/*Table structure for table `t_authority` */

DROP TABLE IF EXISTS `t_authority`;

CREATE TABLE `t_authority` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_role_id` int(11) DEFAULT NULL,
  `t_menu_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2214 DEFAULT CHARSET=utf8;

/*Table structure for table `t_award_record` */

DROP TABLE IF EXISTS `t_award_record`;

CREATE TABLE `t_award_record` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_activity_id` int(11) DEFAULT NULL,
  `t_prizedetai_id` int(11) DEFAULT NULL,
  `t_award_gold` int(11) DEFAULT NULL,
  `t_award_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_balance` */

DROP TABLE IF EXISTS `t_balance`;

CREATE TABLE `t_balance` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_recharge_money` decimal(10,2) DEFAULT NULL,
  `t_profit_money` decimal(10,2) DEFAULT NULL,
  `t_share_money` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=860 DEFAULT CHARSET=utf8;

/*Table structure for table `t_bank_card_data` */

DROP TABLE IF EXISTS `t_bank_card_data`;

CREATE TABLE `t_bank_card_data` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `t_place` varchar(255) DEFAULT NULL COMMENT '地点',
  `t_bank` varchar(100) DEFAULT NULL COMMENT '银行',
  `t_branch_bank` varchar(255) DEFAULT NULL COMMENT '分行',
  `t_bank_card` varchar(255) DEFAULT NULL COMMENT '卡号',
  `t_create_time` datetime DEFAULT NULL,
  `t_nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_banned_setup` */

DROP TABLE IF EXISTS `t_banned_setup`;

CREATE TABLE `t_banned_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_count` int(11) DEFAULT NULL,
  `t_hours` decimal(5,2) DEFAULT NULL,
  `t_createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `t_banner` */

DROP TABLE IF EXISTS `t_banner`;

CREATE TABLE `t_banner` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_img_url` varchar(255) DEFAULT NULL,
  `t_link_url` varchar(100) DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用1.禁用',
  `t_create_time` datetime DEFAULT NULL,
  `t_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_browse` */

DROP TABLE IF EXISTS `t_browse`;

CREATE TABLE `t_browse` (
  `t_browse_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_browse_user` int(11) DEFAULT NULL,
  `t_cover_browse` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_browse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4154 DEFAULT CHARSET=utf8;

/*Table structure for table `t_call_log` */

DROP TABLE IF EXISTS `t_call_log`;

CREATE TABLE `t_call_log` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_callout_user` int(11) DEFAULT NULL,
  `t_answer_user` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_call_time` int(11) DEFAULT NULL,
  `t_room_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1737 DEFAULT CHARSET=utf8;

/*Table structure for table `t_certification` */

DROP TABLE IF EXISTS `t_certification`;

CREATE TABLE `t_certification` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_user_photo` varchar(100) DEFAULT NULL,
  `t_user_video` varchar(100) DEFAULT NULL,
  `t_nam` varchar(24) DEFAULT NULL,
  `t_id_card` varchar(18) DEFAULT NULL,
  `t_certification_type` int(11) DEFAULT NULL COMMENT '0.未审核\r\n1.审核成功\r\n2.审核失败',
  `t_describe` varchar(100) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_com_user_id` int(11) DEFAULT NULL,
  `t_cover_user_id` int(11) DEFAULT NULL,
  `t_com_type` int(11) DEFAULT NULL COMMENT '0.评论\r\n1.回复评论',
  `t_comment` varchar(500) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_is_del` int(11) DEFAULT NULL COMMENT '0.未删除\r\n1.已删除',
  `t_dynamic_id` int(11) DEFAULT NULL,
  `t_is_examine` int(11) DEFAULT NULL,
  `t_is_read` int(11) DEFAULT NULL COMMENT '0.未读1.已读',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Table structure for table `t_comment_count` */

DROP TABLE IF EXISTS `t_comment_count`;

CREATE TABLE `t_comment_count` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_mesg_count` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_coordinate` */

DROP TABLE IF EXISTS `t_coordinate`;

CREATE TABLE `t_coordinate` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_lng` double DEFAULT NULL,
  `t_lat` double DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=483 DEFAULT CHARSET=utf8;

/*Table structure for table `t_cover_examine` */

DROP TABLE IF EXISTS `t_cover_examine`;

CREATE TABLE `t_cover_examine` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_img_url` varchar(150) DEFAULT NULL,
  `t_user_id` int(11) DEFAULT NULL,
  `t_first` int(11) DEFAULT NULL COMMENT '是否设置为第一涨\r\n0.是\r\n1.否',
  `t_is_examine` int(11) DEFAULT NULL COMMENT '0.未审核1.已审核',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=858 DEFAULT CHARSET=utf8;

/*Table structure for table `t_cps` */

DROP TABLE IF EXISTS `t_cps`;

CREATE TABLE `t_cps` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_real_name` varchar(20) DEFAULT NULL,
  `t_settlement_type` int(11) DEFAULT NULL COMMENT '0.支付宝\r\n1.中国工商银行\r\n2.中国农业银行\r\n3.中国银行\r\n4.中国建设银行',
  `t_bank` varchar(50) DEFAULT NULL,
  `t_phone` varchar(11) DEFAULT NULL,
  `t_cps_name` varchar(50) DEFAULT NULL,
  `t_cps` varchar(100) DEFAULT NULL,
  `t_active_user` int(11) DEFAULT NULL,
  `t_proportions` int(11) DEFAULT NULL,
  `t_audit_status` int(11) DEFAULT NULL COMMENT '0.申请中\r\n1.已通过\r\n2.已下架',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `t_cps_devote` */

DROP TABLE IF EXISTS `t_cps_devote`;

CREATE TABLE `t_cps_devote` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_cps_id` int(11) DEFAULT NULL,
  `t_user_id` int(11) DEFAULT NULL,
  `t_devote_value` decimal(7,2) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_ratio` int(11) DEFAULT NULL,
  `t_recharge_money` decimal(7,2) DEFAULT NULL,
  `t_source_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cps_settlement` */

DROP TABLE IF EXISTS `t_cps_settlement`;

CREATE TABLE `t_cps_settlement` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_cps_id` int(11) DEFAULT NULL,
  `t_settlement_amount` decimal(9,2) DEFAULT NULL,
  `t_order_no` varchar(100) DEFAULT NULL,
  `t_settlement_type` int(11) DEFAULT NULL COMMENT '0.结算成功\r\n1.结算失败',
  `t_createt_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_device` */

DROP TABLE IF EXISTS `t_device`;

CREATE TABLE `t_device` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_phone_type` varchar(200) DEFAULT NULL,
  `t_system_version` varchar(200) DEFAULT NULL,
  `t_ip_address` varchar(255) DEFAULT NULL,
  `t_referee_id` int(11) DEFAULT NULL,
  `t_is_use` int(11) DEFAULT NULL COMMENT '0.未使用\r\n1.已使用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1180 DEFAULT CHARSET=utf8;

/*Table structure for table `t_device_login_log` */

DROP TABLE IF EXISTS `t_device_login_log`;

CREATE TABLE `t_device_login_log` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_device_number` varchar(255) DEFAULT NULL,
  `t_user_id` int(11) DEFAULT NULL,
  `t_user_ip` varchar(255) DEFAULT NULL,
  `t_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `t_disable` */

DROP TABLE IF EXISTS `t_disable`;

CREATE TABLE `t_disable` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_context` varchar(255) DEFAULT NULL,
  `t_disable_time` int(11) DEFAULT NULL,
  `t_start_time` datetime DEFAULT NULL,
  `t_end_time` datetime DEFAULT NULL,
  `t_state` int(11) DEFAULT NULL COMMENT '0.生效中\r\n1.已失效',
  `t_operate_user` varchar(255) DEFAULT NULL COMMENT '操作人',
  `t_create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `t_discount` */

DROP TABLE IF EXISTS `t_discount`;

CREATE TABLE `t_discount` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_num` int(11) DEFAULT NULL,
  `t_money` float DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.禁用',
  `t_crate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_discuss_record` */

DROP TABLE IF EXISTS `t_discuss_record`;

CREATE TABLE `t_discuss_record` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_label_id` int(11) DEFAULT NULL,
  `t_evaluation_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

/*Table structure for table `t_domainnamepool` */

DROP TABLE IF EXISTS `t_domainnamepool`;

CREATE TABLE `t_domainnamepool` (
  `t_id` int(11) NOT NULL,
  `t_domain_name` varchar(100) DEFAULT NULL,
  `t_effect_type` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_dynamic` */

DROP TABLE IF EXISTS `t_dynamic`;

CREATE TABLE `t_dynamic` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_title` varchar(100) DEFAULT NULL,
  `t_content` varchar(250) CHARACTER SET utf8mb4 DEFAULT NULL,
  `t_see_count` int(11) DEFAULT NULL,
  `t_is_del` int(11) DEFAULT NULL COMMENT '0.否\r\n1.是',
  `t_auditing_type` int(11) DEFAULT NULL COMMENT '0.未审核\r\n1.已审核\r\n2.审核失败',
  `t_is_visible` int(11) DEFAULT NULL COMMENT '0.公开 1.关注',
  `t_address` varchar(255) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

/*Table structure for table `t_dynamic_file` */

DROP TABLE IF EXISTS `t_dynamic_file`;

CREATE TABLE `t_dynamic_file` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_dynamic_id` int(11) DEFAULT NULL,
  `t_file_type` int(11) DEFAULT NULL COMMENT '0.图片\r\n1.视频',
  `t_cover_img_url` varchar(255) DEFAULT NULL COMMENT '封面地址',
  `t_file_url` varchar(255) DEFAULT NULL,
  `t_gold` decimal(11,2) DEFAULT NULL,
  `t_file_id` varchar(200) DEFAULT NULL,
  `t_video_time` varchar(20) DEFAULT NULL,
  `t_see_count` int(11) DEFAULT NULL COMMENT '查看数',
  `t_is_private` int(11) DEFAULT NULL COMMENT '0.否\r\n1.是',
  `t_is_del` int(11) DEFAULT NULL COMMENT '0.未删除\r\n1.已删除',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

/*Table structure for table `t_enroll` */

DROP TABLE IF EXISTS `t_enroll`;

CREATE TABLE `t_enroll` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_sex` int(11) DEFAULT NULL,
  `t_gold` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `t_extract` */

DROP TABLE IF EXISTS `t_extract`;

CREATE TABLE `t_extract` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_project_type` int(11) DEFAULT NULL COMMENT '0.平台抽成比例\r\n1.一级主播推广比例\r\n2.二级主播推广比例\r\n3.一级用户推广比例\r\n4.二级用户推广比例\r\n5.视频聊天\r\n6.文字聊天\r\n7.查看手机号\r\n8.查看微信号\r\n9.查看私密照片\r\n10.查看私密视频',
  `t_extract_ratio` varchar(100) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `t_feedback` */

DROP TABLE IF EXISTS `t_feedback`;

CREATE TABLE `t_feedback` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_phone` varchar(20) DEFAULT NULL,
  `t_content` varchar(255) DEFAULT NULL,
  `t_img_url` varchar(500) DEFAULT NULL,
  `t_user_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_handle_res` varchar(500) DEFAULT NULL,
  `t_is_handle` int(11) DEFAULT NULL COMMENT '0.未处理 1.已处理',
  `t_handle_time` datetime DEFAULT NULL,
  `t_handle_img` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `t_follow` */

DROP TABLE IF EXISTS `t_follow`;

CREATE TABLE `t_follow` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_follow_id` int(11) DEFAULT NULL,
  `t_cover_follow` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=640 DEFAULT CHARSET=utf8;

/*Table structure for table `t_free_anthor` */

DROP TABLE IF EXISTS `t_free_anthor`;

CREATE TABLE `t_free_anthor` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_is_free` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_gift` */

DROP TABLE IF EXISTS `t_gift`;

CREATE TABLE `t_gift` (
  `t_gift_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_gift_name` varchar(100) DEFAULT NULL,
  `t_gift_gif_url` varchar(100) DEFAULT NULL,
  `t_gift_still_url` varchar(150) DEFAULT NULL,
  `t_gift_gold` decimal(15,2) DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.停用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_gift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Table structure for table `t_gift_record` */

DROP TABLE IF EXISTS `t_gift_record`;

CREATE TABLE `t_gift_record` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_gift_type` int(11) DEFAULT NULL COMMENT '0.图片礼物\r\n1.红包礼物',
  `t_gift_id` int(11) DEFAULT NULL,
  `t_gift_num` int(11) DEFAULT NULL,
  `t_gift_gold` int(11) DEFAULT NULL,
  `t_record_user` int(11) DEFAULT NULL,
  `t_cover_user` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_give_gold_notice` */

DROP TABLE IF EXISTS `t_give_gold_notice`;

CREATE TABLE `t_give_gold_notice` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_gold` decimal(5,2) DEFAULT NULL,
  `t_is_read` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=834 DEFAULT CHARSET=utf8;

/*Table structure for table `t_guild` */

DROP TABLE IF EXISTS `t_guild`;

CREATE TABLE `t_guild` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_guild_name` varchar(50) DEFAULT NULL,
  `t_admin_name` varchar(20) DEFAULT NULL,
  `t_idcard` varchar(20) DEFAULT NULL,
  `t_hand_img` varchar(100) DEFAULT NULL,
  `t_admin_phone` varchar(20) DEFAULT NULL,
  `t_anchor_number` int(11) DEFAULT NULL,
  `t_extract` int(11) DEFAULT NULL,
  `t_examine` int(11) DEFAULT NULL COMMENT '0.未审核\r\n1.已审核',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `t_guild_invite` */

DROP TABLE IF EXISTS `t_guild_invite`;

CREATE TABLE `t_guild_invite` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_guild_id` int(11) DEFAULT NULL,
  `t_anchor_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

/*Table structure for table `t_help_center` */

DROP TABLE IF EXISTS `t_help_center`;

CREATE TABLE `t_help_center` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_title` varchar(255) DEFAULT NULL,
  `t_content` varchar(5000) DEFAULT NULL,
  `t_sort` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_im_log` */

DROP TABLE IF EXISTS `t_im_log`;

CREATE TABLE `t_im_log` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_send_user_id` int(11) DEFAULT NULL,
  `t_accept_user_id` int(11) DEFAULT NULL,
  `t_content` varchar(255) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_label` */

DROP TABLE IF EXISTS `t_label`;

CREATE TABLE `t_label` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_label_name` varchar(100) DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.停用',
  `t_sex` int(11) DEFAULT NULL COMMENT '0.女\r\n1.男',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

/*Table structure for table `t_log_time` */

DROP TABLE IF EXISTS `t_log_time`;

CREATE TABLE `t_log_time` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_login_time` datetime DEFAULT NULL,
  `t_logout_time` datetime DEFAULT NULL,
  `t_duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=790 DEFAULT CHARSET=utf8;

/*Table structure for table `t_login_setup` */

DROP TABLE IF EXISTS `t_login_setup`;

CREATE TABLE `t_login_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_app_id` varchar(100) DEFAULT NULL,
  `t_app_secret` varchar(255) DEFAULT NULL,
  `t_type` int(11) DEFAULT NULL COMMENT '0.QQ互联\r\n1.微信互联',
  `t_state` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.禁用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_menu_name` varchar(100) DEFAULT NULL,
  `t_menu_url` varchar(255) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_father_id` int(11) DEFAULT NULL,
  `t_icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

/*Table structure for table `t_message` */

DROP TABLE IF EXISTS `t_message`;

CREATE TABLE `t_message` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_message_content` varchar(255) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_is_see` int(11) DEFAULT NULL COMMENT '0.未查看\r\n1.以查看',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9221 DEFAULT CHARSET=utf8;

/*Table structure for table `t_object_storage` */

DROP TABLE IF EXISTS `t_object_storage`;

CREATE TABLE `t_object_storage` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_app_id` varchar(100) DEFAULT NULL,
  `t_secret_id` varchar(100) DEFAULT NULL,
  `t_secret_key` varchar(255) DEFAULT NULL,
  `t_bucket` varchar(100) DEFAULT NULL,
  `t_region` varchar(100) DEFAULT NULL,
  `t_img_url` varchar(255) DEFAULT NULL,
  `t_state` int(11) DEFAULT NULL COMMENT '0.正常1.禁用',
  `t_type` int(11) DEFAULT NULL COMMENT '0.腾讯云1.阿里云2.网易云',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `t_order` */

DROP TABLE IF EXISTS `t_order`;

CREATE TABLE `t_order` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_consume` int(11) DEFAULT NULL,
  `t_cover_consume` int(11) DEFAULT NULL,
  `t_consume_type` int(11) DEFAULT NULL COMMENT '0.充值\r\n1.聊天\r\n2.视频\r\n3.私密照片\r\n4.私密视频\r\n5.查看手机\r\n6.查看微信\r\n7.红包\r\n8.VIP\r\n9.礼物\r\n10.提现\r\n11.推荐分成 \r\n12.提现失败返回\r\n13.注册赠送\r\n14.公会收入',
  `t_consume_score` int(11) DEFAULT NULL COMMENT '消费来源ID(视频聊天和文字聊天为0)',
  `t_amount` decimal(10,2) DEFAULT NULL,
  `t_room_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_log_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1575 DEFAULT CHARSET=utf8;

/*Table structure for table `t_platform_income` */

DROP TABLE IF EXISTS `t_platform_income`;

CREATE TABLE `t_platform_income` (
  `t_id` int(11) NOT NULL,
  `t_gold` decimal(18,2) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_praise` */

DROP TABLE IF EXISTS `t_praise`;

CREATE TABLE `t_praise` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_praise_user` int(11) DEFAULT NULL,
  `t_cover_praise_user` int(11) DEFAULT NULL,
  `t_dynamic_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8;

/*Table structure for table `t_private_collection` */

DROP TABLE IF EXISTS `t_private_collection`;

CREATE TABLE `t_private_collection` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_album_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_profit_detail` */

DROP TABLE IF EXISTS `t_profit_detail`;

CREATE TABLE `t_profit_detail` (
  `t_profit_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_profit_type` int(11) DEFAULT NULL COMMENT '1.聊天\r\n2.视频\r\n3.私密照片\r\n4.私密视频\r\n5.查看手机\r\n6.查看微信\r\n7.红包\r\n8.VIP\r\n9.礼物\r\n',
  `t_profit_gold` decimal(10,2) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_profit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1252 DEFAULT CHARSET=utf8;

/*Table structure for table `t_put_forward` */

DROP TABLE IF EXISTS `t_put_forward`;

CREATE TABLE `t_put_forward` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_order_no` varchar(100) DEFAULT NULL,
  `t_money` decimal(10,2) DEFAULT NULL,
  `t_setmeal_id` int(11) DEFAULT NULL,
  `t_describe` varchar(100) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_order_state` int(11) DEFAULT NULL COMMENT '0.待审核\r\n1.已审核待打款\r\n2.已打款\r\n3.打款失败',
  `t_handle_time` datetime DEFAULT NULL,
  `t_income_gold` decimal(10,2) DEFAULT NULL,
  `t_share_gold` decimal(10,2) DEFAULT NULL,
  `t_data_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `t_put_forward_data` */

DROP TABLE IF EXISTS `t_put_forward_data`;

CREATE TABLE `t_put_forward_data` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_real_name` varchar(20) DEFAULT NULL,
  `t_nick_name` varchar(100) DEFAULT NULL,
  `t_head_img` varchar(255) DEFAULT NULL,
  `t_account_number` varchar(100) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_type` int(11) DEFAULT NULL COMMENT '0.支付宝\r\n1.微信 2银行卡',
  `t_place` varchar(255) DEFAULT NULL COMMENT '地点',
  `t_bank` varchar(255) DEFAULT NULL COMMENT '银行',
  `t_branch_bank` varchar(255) DEFAULT NULL COMMENT '分行',
  `t_remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `t_ranking_control` */

DROP TABLE IF EXISTS `t_ranking_control`;

CREATE TABLE `t_ranking_control` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_charm_number` int(11) DEFAULT NULL,
  `t_consumption_number` int(11) DEFAULT NULL,
  `t_courtesy_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `t_reception_rate` */

DROP TABLE IF EXISTS `t_reception_rate`;

CREATE TABLE `t_reception_rate` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_count` int(11) DEFAULT NULL,
  `t_reception_count` int(11) DEFAULT NULL,
  `t_refuse_count` int(11) DEFAULT NULL,
  `t_reception` float DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

/*Table structure for table `t_recharge` */

DROP TABLE IF EXISTS `t_recharge`;

CREATE TABLE `t_recharge` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_recharge_money` decimal(10,2) DEFAULT NULL,
  `t_order_no` varchar(100) DEFAULT NULL,
  `t_tripartite_order` varchar(100) DEFAULT NULL,
  `t_recharge_type` int(11) DEFAULT NULL COMMENT '0.VIP\r\n1.金币',
  `t_payment_type` int(11) DEFAULT NULL COMMENT '0.支付宝\r\n1.微信',
  `t_setmeal_id` int(11) DEFAULT NULL COMMENT '充值VIP不存在此值',
  `t_order_state` int(11) DEFAULT NULL COMMENT '0.创建订单\r\n1.订单已支付\r\n2.放弃支付',
  `t_create_time` datetime DEFAULT NULL,
  `t_fulfil_time` datetime DEFAULT NULL,
  `t_gold_type` int(11) DEFAULT NULL COMMENT '1人民币 2新台币',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=847 DEFAULT CHARSET=utf8;

/*Table structure for table `t_redpacket_log` */

DROP TABLE IF EXISTS `t_redpacket_log`;

CREATE TABLE `t_redpacket_log` (
  `t_redpacket_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_hair_userId` int(11) DEFAULT NULL,
  `t_receive_userId` int(11) DEFAULT NULL,
  `t_redpacket_content` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `t_redpacket_gold` decimal(10,2) DEFAULT NULL,
  `t_redpacket_draw` int(11) DEFAULT NULL COMMENT '0.未领取\r\n1.已领取',
  `t_redpacket_type` int(11) DEFAULT NULL COMMENT '0.赠送红包\r\n1.贡献红包 \r\n2.主播认证红包\r\n3.后台添加红包',
  `t_create_time` datetime DEFAULT NULL,
  `t_order_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_redpacket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8;

/*Table structure for table `t_report` */

DROP TABLE IF EXISTS `t_report`;

CREATE TABLE `t_report` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_cover_user_id` int(11) DEFAULT NULL,
  `t_comment` varchar(255) DEFAULT NULL,
  `t_img` varchar(500) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_is_handle` int(11) DEFAULT NULL COMMENT '0.未处理\r\n1.已处理\r\n',
  `t_handle_comment` varchar(255) DEFAULT NULL,
  `t_handle_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_role_name` varchar(50) DEFAULT NULL,
  `t_enable` int(11) DEFAULT NULL COMMENT '0.启用1.禁用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `t_room_time` */

DROP TABLE IF EXISTS `t_room_time`;

CREATE TABLE `t_room_time` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_room_id` int(11) DEFAULT NULL,
  `t_call_user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `t_answer_user_id` int(11) DEFAULT NULL COMMENT '主播编号',
  `t_an_vi_gold` decimal(11,2) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `t_set_meal` */

DROP TABLE IF EXISTS `t_set_meal`;

CREATE TABLE `t_set_meal` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_project_type` int(11) DEFAULT NULL COMMENT '1.充值\r\n2.提现',
  `t_gold` int(11) DEFAULT NULL,
  `t_set_meal_name` varchar(255) DEFAULT NULL,
  `t_money` decimal(7,2) DEFAULT NULL,
  `t_end_type` int(11) DEFAULT NULL COMMENT '0.Android 1.phone 2.pepay 3.google 4.phone',
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.停用',
  `t_create_time` datetime DEFAULT NULL,
  `t_describe` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

/*Table structure for table `t_share_notes` */

DROP TABLE IF EXISTS `t_share_notes`;

CREATE TABLE `t_share_notes` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_target` int(11) DEFAULT NULL COMMENT '0.QQ群\r\n1.微信群',
  `t_date` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_simulation` */

DROP TABLE IF EXISTS `t_simulation`;

CREATE TABLE `t_simulation` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_centent` varchar(255) DEFAULT NULL,
  `t_sex` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Table structure for table `t_sms_steup` */

DROP TABLE IF EXISTS `t_sms_steup`;

CREATE TABLE `t_sms_steup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` varchar(100) DEFAULT NULL,
  `appkey` varchar(255) DEFAULT NULL,
  `templateId` varchar(200) DEFAULT NULL,
  `smsSign` varchar(50) DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用1.停用',
  `t_platform_type` int(11) DEFAULT NULL COMMENT '0.腾讯云1.阿里云3.网易云',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_speed_manage` */

DROP TABLE IF EXISTS `t_speed_manage`;

CREATE TABLE `t_speed_manage` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_begin_time` varchar(10) DEFAULT NULL,
  `t_end_time` varchar(10) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_speeddating` */

DROP TABLE IF EXISTS `t_speeddating`;

CREATE TABLE `t_speeddating` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_begin_time` datetime DEFAULT NULL,
  `t_end_time` datetime DEFAULT NULL,
  `t_duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=utf8;

/*Table structure for table `t_spread` */

DROP TABLE IF EXISTS `t_spread`;

CREATE TABLE `t_spread` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_is_nominate` int(11) DEFAULT NULL,
  `t_sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `t_spread_award` */

DROP TABLE IF EXISTS `t_spread_award`;

CREATE TABLE `t_spread_award` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_gold` int(11) DEFAULT NULL,
  `t_sex` int(11) DEFAULT NULL COMMENT '0.女\r\n1.男',
  `t_rank` int(11) DEFAULT NULL COMMENT '1.一级推广\r\n2.二级推广',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `t_spread_channel` */

DROP TABLE IF EXISTS `t_spread_channel`;

CREATE TABLE `t_spread_channel` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_real_name` varchar(20) DEFAULT NULL,
  `t_settlement_type` int(11) DEFAULT NULL COMMENT '0.支付宝\r\n1.中国工商银行\r\n2.中国农业银行\r\n3.中国银行\r\n4.中国建设银行',
  `t_bank` varchar(50) DEFAULT NULL,
  `t_weixin` varchar(100) DEFAULT NULL,
  `t_qq` varchar(20) DEFAULT NULL,
  `t_phone` varchar(11) DEFAULT NULL,
  `t_gold_proportions` int(11) DEFAULT NULL,
  `t_vip_proportions` int(11) DEFAULT NULL,
  `t_spread_id` int(11) DEFAULT NULL,
  `t_spread_type` int(11) DEFAULT NULL COMMENT '0.正常 1.已下架',
  `t_create_time` datetime DEFAULT NULL,
  `t_short_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_spread_login` */

DROP TABLE IF EXISTS `t_spread_login`;

CREATE TABLE `t_spread_login` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_name` varchar(255) DEFAULT NULL,
  `t_pass_word` varchar(255) DEFAULT NULL,
  `t_role_id` int(11) DEFAULT NULL,
  `t_user_id` int(11) DEFAULT NULL,
  `t_is_disable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.禁用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_spreed_img` */

DROP TABLE IF EXISTS `t_spreed_img`;

CREATE TABLE `t_spreed_img` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_img_path` varchar(255) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `t_style_setup` */

DROP TABLE IF EXISTS `t_style_setup`;

CREATE TABLE `t_style_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_style_name` varchar(100) DEFAULT NULL,
  `t_mark` varchar(100) DEFAULT NULL,
  `t_state` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `t_system_setup` */

DROP TABLE IF EXISTS `t_system_setup`;

CREATE TABLE `t_system_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_channel` varchar(255) DEFAULT NULL,
  `t_union` varchar(255) DEFAULT NULL,
  `t_take_out_count` int(11) DEFAULT NULL,
  `t_scope` int(11) DEFAULT NULL COMMENT '范围(公里)',
  `t_android_download` varchar(255) DEFAULT NULL,
  `t_ios_download` varchar(255) DEFAULT NULL,
  `t_award_rules` varchar(500) DEFAULT NULL,
  `t_system_lang_girl` varchar(500) DEFAULT NULL,
  `t_system_lang_male` varchar(500) DEFAULT NULL,
  `t_default_text` decimal(11,2) DEFAULT NULL,
  `t_default_video` decimal(11,2) DEFAULT NULL,
  `t_default_phone` decimal(11,2) DEFAULT NULL,
  `t_default_weixin` decimal(11,2) DEFAULT NULL,
  `t_service_qq` varchar(255) DEFAULT NULL,
  `t_nickname_filter` varchar(255) DEFAULT NULL,
  `t_video_hint` varchar(255) DEFAULT NULL,
  `t_spreed_hint` varchar(255) DEFAULT NULL,
  `t_zuidi` decimal(11,2) DEFAULT NULL,
  `t_zhekou` decimal(11,2) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_idcard` int(11) DEFAULT NULL,
  `t_nickName` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `t_sex` int(11) DEFAULT NULL COMMENT '0.女\r\n1.男',
  `t_age` int(11) DEFAULT NULL,
  `t_handImg` varchar(200) DEFAULT NULL,
  `t_cover_img` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `t_phone` varchar(20) DEFAULT NULL,
  `t_height` int(11) DEFAULT NULL,
  `t_weight` float DEFAULT NULL,
  `t_constellation` varchar(50) DEFAULT NULL,
  `t_city` varchar(50) DEFAULT NULL,
  `t_synopsis` varchar(200) DEFAULT NULL,
  `t_autograph` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `t_vocation` varchar(40) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_referee` int(11) DEFAULT NULL,
  `t_role` int(11) DEFAULT NULL COMMENT '0.普通用户\r\n1.主播',
  `t_disable` int(11) DEFAULT NULL COMMENT '0.正常\r\n1.封号\r\n2.禁用',
  `t_login_time` datetime DEFAULT NULL,
  `t_open_id` varchar(200) DEFAULT NULL,
  `t_qq_open_id` varchar(200) DEFAULT NULL,
  `t_pass_wrod` varchar(200) DEFAULT NULL,
  `t_is_vip` int(11) DEFAULT NULL COMMENT '0.是\r\n1.否',
  `t_is_not_disturb` int(11) DEFAULT NULL COMMENT '0.是\r\n1.否',
  `t_browse_sum` int(11) DEFAULT NULL,
  `t_onLine` int(11) DEFAULT NULL COMMENT '0.在线\r\n1.离线',
  `t_weixin` varchar(255) DEFAULT NULL,
  `t_alipay_id` varchar(100) DEFAULT NULL,
  `t_online_setup` int(11) DEFAULT NULL,
  `t_camera_switch` int(11) DEFAULT '0' COMMENT '1打开 0关闭',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=906 DEFAULT CHARSET=utf8;

/*Table structure for table `t_user_evaluation` */

DROP TABLE IF EXISTS `t_user_evaluation`;

CREATE TABLE `t_user_evaluation` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_user_hand` varchar(255) DEFAULT NULL,
  `t_user_nick` varchar(100) DEFAULT NULL,
  `t_anchor_id` int(11) DEFAULT NULL,
  `t_comment` varchar(255) DEFAULT NULL COMMENT '评论内容',
  `t_score` int(11) DEFAULT NULL COMMENT '评价分数',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

/*Table structure for table `t_user_label` */

DROP TABLE IF EXISTS `t_user_label`;

CREATE TABLE `t_user_label` (
  `t_user_lable_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_lable_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_user_lable_id`)
) ENGINE=InnoDB AUTO_INCREMENT=286 DEFAULT CHARSET=utf8;

/*Table structure for table `t_user_laud` */

DROP TABLE IF EXISTS `t_user_laud`;

CREATE TABLE `t_user_laud` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_laud_user_id` int(11) DEFAULT NULL,
  `t_cover_user_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `t_vague_check` */

DROP TABLE IF EXISTS `t_vague_check`;

CREATE TABLE `t_vague_check` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_img_url` varchar(200) DEFAULT NULL,
  `t_data_type` int(11) DEFAULT NULL COMMENT '0.头像图片\r\n1.封面图片\r\n2.相册图片',
  `t_user_id` int(11) DEFAULT NULL,
  `t_gold` int(11) DEFAULT NULL COMMENT '0.否\r\n1.是',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_version` */

DROP TABLE IF EXISTS `t_version`;

CREATE TABLE `t_version` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_download_url` varchar(255) DEFAULT NULL,
  `t_is_new` int(11) DEFAULT NULL,
  `t_version` varchar(100) DEFAULT NULL,
  `t_version_depict` varchar(500) DEFAULT NULL,
  `t_version_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Table structure for table `t_video_record` */

DROP TABLE IF EXISTS `t_video_record`;

CREATE TABLE `t_video_record` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_dial_user_id` int(11) DEFAULT NULL,
  `t_answer_user_id` int(11) DEFAULT NULL,
  `t_type` int(11) DEFAULT NULL COMMENT '0.收入\r\n1.支出',
  `t_time_length` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_vip` */

DROP TABLE IF EXISTS `t_vip`;

CREATE TABLE `t_vip` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_openUp_time` datetime DEFAULT NULL,
  `t_end_time` datetime DEFAULT NULL,
  `t_auto_renew` int(11) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `t_vip_setmeal` */

DROP TABLE IF EXISTS `t_vip_setmeal`;

CREATE TABLE `t_vip_setmeal` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_setmeal_name` varchar(20) DEFAULT NULL,
  `t_cost_price` decimal(10,2) DEFAULT NULL,
  `t_money` decimal(10,2) DEFAULT NULL,
  `t_gold` int(11) DEFAULT NULL,
  `t_pay_type` int(11) DEFAULT NULL COMMENT '0.支付宝1.微信2.pepay3.google pay4.iPhone',
  `t_set_meal_name` varchar(255) DEFAULT NULL,
  `t_duration` int(11) DEFAULT NULL,
  `t_is_enable` int(11) DEFAULT NULL COMMENT '0.启用\r\n1.停用',
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Table structure for table `t_virtual` */

DROP TABLE IF EXISTS `t_virtual`;

CREATE TABLE `t_virtual` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Table structure for table `t_wallet_detail` */

DROP TABLE IF EXISTS `t_wallet_detail`;

CREATE TABLE `t_wallet_detail` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_change_type` int(11) DEFAULT NULL COMMENT '0.收入\r\n1.支出',
  `t_change_category` int(11) DEFAULT NULL COMMENT '0.充值\r\n1.聊天\r\n2.视频\r\n3.私密照片\r\n4.私密视频\r\n5.查看手机\r\n6.查看微信\r\n7.红包\r\n8.VIP\r\n9.礼物\r\n10.提现\r\n11.推荐分成\r\n12.提现失败原路退回\r\n13.注册赠送\r\n14.公会收入\r\n',
  `t_change_front` decimal(11,2) DEFAULT NULL COMMENT '注意：\r\n如果为收益时，此结果为分项单独金额(如:充值，收益，分享)\r\n如果为支出时，此项结果为总金额\r\n',
  `t_value` decimal(11,2) DEFAULT NULL,
  `t_change_after` decimal(11,2) DEFAULT NULL COMMENT '注意：\r\n如果为收益时，此结果为分项金额(充值，收益，分享)\r\n如果为支出时，此项结果为总金额',
  `t_change_time` datetime DEFAULT NULL,
  `t_sorece_id` int(11) DEFAULT NULL COMMENT '消费为t_order主键\r\n充值为t_recharge 主键\r\n提现为t_put_forward 主键',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4062 DEFAULT CHARSET=utf8;

/*Table structure for table `t_weixinpay_setup` */

DROP TABLE IF EXISTS `t_weixinpay_setup`;

CREATE TABLE `t_weixinpay_setup` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `appId` varchar(100) DEFAULT NULL,
  `t_mchid` varchar(100) DEFAULT NULL,
  `t_mchid_key` varchar(255) DEFAULT NULL,
  `t_certificate_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `t_yellowing_error` */

DROP TABLE IF EXISTS `t_yellowing_error`;

CREATE TABLE `t_yellowing_error` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_user_id` int(11) DEFAULT NULL,
  `t_room_msg` varchar(1000) DEFAULT NULL,
  `t_content` varchar(1000) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
