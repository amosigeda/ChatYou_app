package com.yiliao.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import com.yiliao.domain.Balance;
import com.yiliao.domain.MessageEntity;
import com.yiliao.domain.NewRedPacketRes;
import com.yiliao.domain.UserIoSession;
import com.yiliao.domain.WalletDetail;
import com.yiliao.evnet.PushMesgEvnet;
import com.yiliao.service.ConsumeService;
import com.yiliao.service.GoldComputeService;
import com.yiliao.timer.SimulationVideoTimer;
import com.yiliao.timer.VideoTiming;
import com.yiliao.util.DateUtils;
import com.yiliao.util.Md5Util;
import com.yiliao.util.MessageUtil;
import com.yiliao.util.Mid;
import com.yiliao.util.PayUtil;
import com.yiliao.util.SpringConfig;

import net.sf.json.JSONObject;

/**
 * 消费服务实现累
 * 
 * @author Administrator
 * 
 */
@Service("consumeService")
public class ConsumeServiceImpl extends ICommServiceImpl implements ConsumeService {

	private MessageUtil mu = null;

	private GoldComputeService goldComputeService = (GoldComputeService) SpringConfig.getInstance()
			.getBean("goldComputeService");

	/*
	 * 用户查看照片 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#SeeImgConsume(int, int)
	 */
	@Override
	public MessageUtil seeImgConsume(int consumeUserId, int coverConsumeUserId, int photoId) {
		try {

			// 更新查看次数
			String uSql = " UPDATE t_album SET t_see_count = t_see_count+1 WHERE t_id = ?  ";
			this.executeSQL(uSql, photoId);

			// 获取改照片是否已经消费过了
			if (queryOrderExits(consumeUserId, coverConsumeUserId, photoId,
					WalletDetail.CHANGE_CATEGORY_PRIVATE_PHOTO)) {

				// 查看该资料是否还需需要进行存储
				savePrivate(consumeUserId, photoId);

				mu = new MessageUtil();
				mu.setM_istatus(2);
				return mu;
			}

			/* 判断用户是否是VIP VIP用户无需付费 */
			if (!getUserIsVip(consumeUserId)) {

				String sql = "SELECT t_money FROM t_album WHERE t_id = ? AND t_file_type = 0 ";
				Map<String, Object> price = this.getMap(sql, photoId);

				int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, photoId,
						WalletDetail.CHANGE_CATEGORY_PRIVATE_PHOTO, new BigDecimal(price.get("t_money").toString()));
				// 扣除消费者需要消费的金币
				if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGORY_PRIVATE_PHOTO,
						new BigDecimal(price.get("t_money").toString()), orderId)) {
					// 分配用户的消费的金币
					goldComputeService.distribution(new BigDecimal(price.get("t_money").toString()), consumeUserId,
							coverConsumeUserId, WalletDetail.CHANGE_CATEGORY_PRIVATE_PHOTO, orderId);
					// 查看该资料是否还需需要进行存储
					savePrivate(consumeUserId, photoId);

					mu = new MessageUtil();
					mu.setM_istatus(1);
					mu.setM_strMessage("消费成功!");
				} else {
					this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
					mu = new MessageUtil();
					mu.setM_istatus(-1);
					mu.setM_strMessage("余额不足!请充值.");
				}
			} else {
				mu = new MessageUtil();
				mu.setM_istatus(2);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查看图片消费异常!", e);
			mu = new MessageUtil(0, "程序异常");
		}
		return mu;
	}

	/*
	 * 查看私密视频 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#seeVideoConsume(int, int)
	 */
	@Override
	public MessageUtil seeVideoConsume(int consumeUserId, int coverConsumeUserId, int videoId) {
		try {

			// 更新查看次数
			String uSql = " UPDATE t_album SET t_see_count = t_see_count+1 WHERE t_id = ?  ";
			this.executeSQL(uSql, videoId);

			// 获取该视频是否已经消费过了
			if (queryOrderExits(consumeUserId, coverConsumeUserId, videoId,
					WalletDetail.CHANGE_CATEGORY_PRIVATE_VIDEO)) {
				// 查看该资料是否还需需要进行存储
				savePrivate(consumeUserId, videoId);
				mu = new MessageUtil();
				mu.setM_istatus(2);
				return mu;
			}
			/* 如果是VIP 直接返回数据 */
			if (getUserIsVip(consumeUserId)) {
				mu = new MessageUtil();
				mu.setM_istatus(2);
				return mu;
			}

			String sql = "SELECT t_money FROM t_album WHERE t_id = ? AND t_file_type = 1";
			Map<String, Object> price = this.getMap(sql, videoId);

			// 保存消费记录 切返回订单编号
			int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, videoId,
					WalletDetail.CHANGE_CATEGORY_PRIVATE_VIDEO, new BigDecimal(price.get("t_money").toString()));

			// 扣除消费者需要消费的金币
			if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGORY_PRIVATE_VIDEO,
					new BigDecimal(price.get("t_money").toString()), orderId)) {

				// 分配用户的消费的金币
				goldComputeService.distribution(new BigDecimal(price.get("t_money").toString()), consumeUserId,
						coverConsumeUserId, WalletDetail.CHANGE_CATEGORY_PRIVATE_VIDEO, orderId);

				// 查看该资料是否还需需要进行存储
				savePrivate(consumeUserId, videoId);

				mu = new MessageUtil();
				mu.setM_istatus(1);
				mu.setM_strMessage("消费成功!");
			} else {
				this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
				mu = new MessageUtil();
				mu.setM_istatus(-1);
				mu.setM_strMessage("余额不足!请充值.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查看私密视频异常!", e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/*
	 * 用户查看手机号 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#seePhoneConsume(int, int)
	 */
	@Override
	public MessageUtil seePhoneConsume(int consumeUserId, int coverConsumeUserId) {
		try {

			// 优先获取主播用户的查看手机号收费设置
			String sql = " SELECT t_phone_gold FROM t_anchor_setup WHERE t_user_id = ? ";
			Map<String, Object> price = this.getMap(sql, coverConsumeUserId);
			// if compere phone query gold 0 return compere not public
			if (0 == new BigDecimal(price.get("t_phone_gold").toString()).intValue()) {
				mu = new MessageUtil();
				mu.setM_istatus(-2);
				return mu;
			}
			// 获取该用户是否已经消费过了查看主播手机号
			if (queryOrderExits(consumeUserId, coverConsumeUserId, 0, WalletDetail.CHANGE_CATEGORY_PHONE)) {
				mu = new MessageUtil();
				mu.setM_istatus(2);
				return mu;
			}
			// 保存消费记录
			int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, 0, WalletDetail.CHANGE_CATEGORY_PHONE,
					new BigDecimal(price.get("t_phone_gold").toString()));
			// 扣除消费者需要消费的金币
			if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGORY_PHONE,
					new BigDecimal(price.get("t_phone_gold").toString()), orderId)) {

				// 分配用户的消费的金币
				goldComputeService.distribution(new BigDecimal(price.get("t_phone_gold").toString()), consumeUserId,
						coverConsumeUserId, WalletDetail.CHANGE_CATEGORY_PHONE, orderId);

				mu = new MessageUtil();
				mu.setM_istatus(1);
				mu.setM_strMessage("消费成功!");
			} else {
				this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
				mu = new MessageUtil();
				mu.setM_istatus(-1);
				mu.setM_strMessage("余额不足!请充值.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查看用户手机号异常!", e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/*
	 * 查看微信号 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#seeWeiXinConsume(int, int)
	 */
	@Override
	public MessageUtil seeWeiXinConsume(int consumeUserId, int coverConsumeUserId) {
		try {

			// 获取该用户是否消费过了查看主播微信号
			if (queryOrderExits(consumeUserId, coverConsumeUserId, 0, WalletDetail.CHANGE_CATEGORY_WEIXIN)) {
				mu = new MessageUtil();
				mu.setM_istatus(2);
				return mu;
			}

			String sql = " SELECT t_weixin_gold FROM t_anchor_setup WHERE t_user_id = ? ";

			List<Map<String, Object>> sqlList = getQuerySqlList(sql, coverConsumeUserId);

			if (!sqlList.isEmpty()) {
				// 保存消费记录
				int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, 0, WalletDetail.CHANGE_CATEGORY_WEIXIN,
						new BigDecimal(sqlList.get(0).get("t_weixin_gold").toString()));

				// 扣除消费者需要消费的金币
				if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGORY_WEIXIN,
						new BigDecimal(sqlList.get(0).get("t_weixin_gold").toString()), orderId)) {

					// 分配用户的消费的金币
					goldComputeService.distribution(new BigDecimal(sqlList.get(0).get("t_weixin_gold").toString()),
							consumeUserId, coverConsumeUserId,

							WalletDetail.CHANGE_CATEGORY_WEIXIN, orderId);
					mu = new MessageUtil();
					mu.setM_istatus(1);
					mu.setM_strMessage("消费成功!");
				} else {
					this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
					mu = new MessageUtil();
					mu.setM_istatus(-1);
					mu.setM_strMessage("余额不足!请充值.");
				}
			} else {
				mu = new MessageUtil();
				mu.setM_istatus(-1);
				mu.setM_strMessage("主播资料未完善");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查看用户微信异常!,查看人【{}】,被查看人【{}】", consumeUserId, coverConsumeUserId, e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/*
	 * 非VIP发送文本消息 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#sendTextConsume(int, int)
	 */
	@Override
	public synchronized MessageUtil sendTextConsume(int consumeUserId, int coverConsumeUserId) {
		try {

			
			if(getUserIsZhuBo(consumeUserId)){
				mu = new MessageUtil(2, "无需支付!");
			}else{
				// 获取当前用户角色
				List<Map<String, Object>> user = this.getQuerySqlList(
//						"SELECT * FROM t_user WHERE t_role = 1 AND t_id = ? AND t_is_vip = 1 ", consumeUserId);//0.是  1.否 vip
				"SELECT * FROM t_user WHERE t_role = 1 AND t_id = ? ", consumeUserId);//0.是  1.否 vip
				

				if (!getUserIsVip(consumeUserId) && user.size()>0) {
					// 获取对方收费设置
					String sql = " SELECT t_text_gold FROM t_anchor_setup WHERE t_user_id = ? ";
					Map<String, Object> price = this.getMap(sql, coverConsumeUserId);
					// 保存消费记录 并返回消费记录编号
					int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, 0, WalletDetail.CHANGE_CATEGORY_TEXT,
							new BigDecimal(price.get("t_text_gold").toString()));
					// 扣除消费者需要消费的金币
					if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGORY_TEXT,
							new BigDecimal(price.get("t_text_gold").toString()), orderId)) {

						// 分配用户的消费的金币
						goldComputeService.distribution(new BigDecimal(price.get("t_text_gold").toString()), consumeUserId,
								coverConsumeUserId, WalletDetail.CHANGE_CATEGORY_TEXT, orderId);

						mu = new MessageUtil();
						mu.setM_istatus(1);
						mu.setM_strMessage("消费成功!");
					} else {
						this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
						mu = new MessageUtil();
						mu.setM_istatus(-1);
						mu.setM_strMessage("余额不足!请充值.");
					}
				} else {
					mu = new MessageUtil(2, "无需支付!");
				}
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送私密聊天信息异常;发送人[{}],接收人[{}]", consumeUserId, coverConsumeUserId, e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/**
	 * 查询本次订单是否已经存在支付过的类型
	 * 
	 * @param consume
	 * @param cover_consume
	 * @param consume_score
	 * @param consume_type
	 * @return fals没有记录 true已有记录
	 */
	private boolean queryOrderExits(int consume, int cover_consume, int consume_score, int consume_type) {
		String sql = "SELECT * FROM t_order WHERE t_consume = ?  AND t_cover_consume = ? AND t_consume_type = ? ";

		if (consume_type == WalletDetail.CHANGE_CATEGORY_PRIVATE_PHOTO
				|| consume_type == WalletDetail.CHANGE_CATEGORY_PRIVATE_VIDEO) {
			sql = sql + " AND t_consume_score =  " + consume_score;
		}

		List<Map<String, Object>> dataMap = this.getQuerySqlList(sql, consume, cover_consume, consume_type);

		return null == dataMap ? false : dataMap.isEmpty() ? false : true;
	}

	/**
	 * 存储订单记录
	 * 
	 * @param consume       消费者
	 * @param cover_consume 被消费者
	 * @param consume_score 消费资源数据编号
	 * @param consume_type  消费类型
	 * @param amount        消费金额
	 */
	private int saveOrder(int consume, int cover_consume, int consume_score, int consume_type, BigDecimal amount) {

		String sql = "INSERT INTO t_order (t_consume, t_cover_consume, t_consume_type, t_consume_score, t_amount, t_create_time) VALUES (?, ?, ?, ?, ?, ?)";

		return this.getFinalDao().getIEntitySQLDAO().saveData(sql, consume, cover_consume, consume_type, consume_score,
				amount, DateUtils.format(new Date(), DateUtils.FullDatePattern));
	}

	/**
	 * 存储用户的私藏
	 * 
	 * @param consume   消费者
	 * @param dynamicId 动态编号
	 */
	private void savePrivate(int consume, int dynamicId) {
		// 查询需要保存的数据是否已经存在了
		String sql = "SELECT * FROM t_private_collection WHERE t_user_id = ? AND t_album_id = ?";
		List<Map<String, Object>> dataLsit = this.getQuerySqlList(sql, consume, dynamicId);
		// 判断该数据是否已经存在
		if (null == dataLsit || !dataLsit.isEmpty()) {
			// 保存数据
			sql = " INSERT INTO t_private_collection (t_user_id,t_album_id) VALUES ( ?, ?)";
			this.executeSQL(sql, consume, dynamicId);
		}
	}

	/*
	 * VIP 查看私密照片或者视频 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#vipSeeData(int, int)
	 */
	@Override
	public MessageUtil vipSeeData(int vipUserId, int sourceId) {
		try {
			savePrivate(vipUserId, sourceId);

			mu = new MessageUtil(1, "查看成功!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("vip查看私密视频或者照片", e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/*
	 * 消费者给被消费者发红包 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#sendRedEnvelope(int, int, int)
	 */
	@SuppressWarnings("null")
	@Override
	public MessageUtil sendRedEnvelope(int consumeUserId, int coverConsumeUserId, int gold) {
		try {

			Map<String, Integer> map = VideoTiming.timingUser.get(consumeUserId);

			int consumeGold = 0;

			// 如果用户正在进行视频聊天中
			if (null != map && !map.isEmpty()) {
				// 计算出用户已经消费了多少金币
				int timing = map.get("timing") / 60 + 2;
				consumeGold = map.get("deplete") * timing;

				// 判断用户发送的红包金币加上已经视频聊天消费了的金币 是否足够
				if (map.get("gold") < (consumeGold + gold)) {
					mu = new MessageUtil(-1, "余额不足!,请充值.");
					return mu;
				} else {
					// 更新视频聊天中的总金币数
					map.put("gold", map.get("gold") - gold);
				}
			}
			// 保存消费记录
			int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, 0, WalletDetail.CHANGE_CATEGORY_RED_PACKET,
					new BigDecimal(gold));
			// 扣除消费者需要消费的金币
			if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGORY_RED_PACKET,
					new BigDecimal(gold), orderId)) {

				// 分配用户的消费的金币
				goldComputeService.distribution(new BigDecimal(gold), consumeUserId, coverConsumeUserId,
						WalletDetail.CHANGE_CATEGORY_RED_PACKET, orderId);

				mu = new MessageUtil();
				mu.setM_istatus(1);
				mu.setM_strMessage("发送红包成功!");

				Map<String, Object> consumeUser = goldComputeService.queryUserData(consumeUserId);

				String message = "收到来自" + consumeUser.get("t_nickName") + "的红包";

				// socket推送
				NewRedPacketRes newRedP = new NewRedPacketRes();
				newRedP.setMid(Mid.noticeNewRedPacketRes);

				IoSession ioSession = UserIoSession.getInstance().getMapIoSession(coverConsumeUserId);

				if (null != ioSession) {
					ioSession.write(JSONObject.fromObject(newRedP).toString());
				}
				// 异步通知
				this.applicationContext
						.publishEvent(new PushMesgEvnet(new MessageEntity(coverConsumeUserId, message, 0, new Date())));

			} else {
				this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
				mu = new MessageUtil();
				mu.setM_istatus(-1);
				mu.setM_strMessage("余额不足!请充值.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送红包异常!", e);
			mu = new MessageUtil(0, "程序异常!");
		}

		return mu;
	}

	/*
	 * 礼物 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#getGiftList()
	 */
	@Override
	public MessageUtil getGiftList() {

		try {

			String sql = "SELECT t_gift_id,t_gift_name,t_gift_gif_url,t_gift_still_url,t_gift_gold FROM t_gift WHERE t_is_enable = 0 ORDER BY t_gift_gold ASC ";

			List<Map<String, Object>> data = this.getQuerySqlList(sql);

			mu = new MessageUtil();
			mu.setM_istatus(1);
			mu.setM_object(data);

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("获取礼物列表异常!", e);
			mu = new MessageUtil(0, "程序异常!");
		}

		return mu;
	}

	/*
	 * 用户赠送礼物 (non-Javadoc)
	 * 
	 * @see com.yiliao.service.app.ConsumeService#userGiveGift(int, int, int, int)
	 */
	@Override
	public MessageUtil userGiveGift(int consumeUserId, int coverConsumeUserId, int giftId, int giftNum) {

		try {
			// 根据编号查询出礼物数据
			Map<String, Object> giftMap = this.getMap("SELECT * FROM t_gift WHERE  t_gift_id = ?", giftId);

			// 得到用户消费的金币数
			BigDecimal totalGold = new BigDecimal(giftMap.get("t_gift_gold").toString())
					.multiply(new BigDecimal(giftNum)).setScale(2, BigDecimal.ROUND_DOWN);

			Map<String, Integer> map = VideoTiming.timingUser.get(consumeUserId);

			int consumeGold = 0;

			// 如果用户正在进行视频聊天中
			if (null != map && !map.isEmpty()) {
				// 计算出用户已经消费了多少金币
				int timing = map.get("timing") / 60 + 2;
				consumeGold = map.get("deplete") * timing;

				// 判断用户消费金币加上已经视频聊天消费了的金币 是否足够
				if (map.get("gold") < (consumeGold + totalGold.intValue())) {
					mu = new MessageUtil(-1, "余额不足!,请充值.");
					return mu;
				} else {
					// 更新视频聊天中的总金币数
					map.put("gold", map.get("gold") - totalGold.intValue());
				}
			}

			// 保存消费记录
			int orderId = this.saveOrder(consumeUserId, coverConsumeUserId, giftId, WalletDetail.CHANGE_CATEGOR_GIFT,
					totalGold);

			// 扣除消费者需要消费的金币
			if (goldComputeService.userConsume(consumeUserId, WalletDetail.CHANGE_CATEGOR_GIFT, totalGold, orderId)) {

				// 分配用户的消费的金币
				goldComputeService.distribution(totalGold, consumeUserId, coverConsumeUserId,
						WalletDetail.CHANGE_CATEGOR_GIFT, orderId);

				mu = new MessageUtil();
				mu.setM_istatus(1);
				mu.setM_strMessage("赠送成功!");
			} else {
				this.executeSQL("DELETE FROM t_order WHERE t_id = ? ", orderId);
				mu = new MessageUtil();
				mu.setM_istatus(-1);
				mu.setM_strMessage("余额不足!请充值.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("赠送礼物异常!", e);
			mu = new MessageUtil(0, "程序异常!");
		}

		return mu;
	}

	/**
	 * 获取用户是否是VIP用户
	 * 
	 * @param userId
	 * @return
	 */
	private boolean getUserIsVip(int userId) {
		try {

			String vSql = "SELECT t_is_vip FROM t_user WHERE t_id = ?";

			Map<String, Object> vipMap = this.getMap(vSql, userId);

			if ("0".equals(vipMap.get("t_is_vip").toString())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户是否是VIP异常", e);
		}

		return false;
	}
	
	/**
	 * 获取用户是否主播
	 * 
	 * @param userId
	 * @return
	 */
	private boolean getUserIsZhuBo(int userId) {
		try {

			String vSql = "SELECT t_role FROM t_user WHERE t_id = ?";

			Map<String, Object> vipMap = this.getMap(vSql, userId);

			if ("1".equals(vipMap.get("t_role").toString())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户是否是VIP异常", e);
		}

		return false;
	}

	/**
	 * 用户拆红包
	 */
	@Override
	public synchronized MessageUtil receiveRedPacket(Integer t_id, int userId) {
		try {

			List<Map<String, Object>> findBySQLTOMap = null;
			// 拆开指定红包
			if (null != t_id && 0 != t_id) {
				String sql = "SELECT t_redpacket_id,t_hair_userId,t_receive_userId,t_redpacket_content,t_redpacket_gold,t_redpacket_draw,t_redpacket_type,DATE_FORMAT(t_create_time,'%Y-%m-%d %T') AS t_create_time FROM t_redpacket_log WHERE t_redpacket_id = ? AND t_redpacket_draw = 0";
				findBySQLTOMap = this.getQuerySqlList(sql, t_id);
			} else { // 红包列表
				String sql = "SELECT t_redpacket_id,t_hair_userId,t_receive_userId,t_redpacket_content,t_redpacket_gold,t_redpacket_draw,t_redpacket_type,DATE_FORMAT(t_create_time,'%Y-%m-%d %T') AS t_create_time FROM t_redpacket_log WHERE t_receive_userId = ? AND t_redpacket_draw = 0 ;";
				findBySQLTOMap = this.getQuerySqlList(sql, userId);
			}

			if (null == findBySQLTOMap || findBySQLTOMap.isEmpty()) {
				mu = new MessageUtil(-1, "暂无红包!");
			} else {

				BigDecimal totalGold = new BigDecimal(0);

				for (Map<String, Object> m : findBySQLTOMap) {

					// 统计用户变动前的金额
					String banSql = "SELECT * FROM t_balance WHERE t_user_id = ? ";

					Map<String, Object> balance = this.getMap(banSql, m.get("t_receive_userId"));

					BigDecimal originalGold = null;

					int chageType = WalletDetail.CHANGE_CATEGORY_RED_PACKET;
					int goldType = Balance.GOLD_TYPE_PROFIT;
					/**
					 * 0.赠送红包 1.贡献红包 2.主播认证红包 3.后台赠送红包
					 */
					if ("0".equals(m.get("t_redpacket_type").toString())
							|| "2".equals(m.get("t_redpacket_type").toString())
							|| "3".equals(m.get("t_redpacket_type").toString())) {
						originalGold = new BigDecimal(balance.get("t_profit_money").toString());
					} else {
						chageType = WalletDetail.CHANGE_CATEGOR_RECOMMENDATION;
						goldType = Balance.GOLD_TYPE_SHARE;
						originalGold = new BigDecimal(balance.get("t_share_money").toString());
					}
					// 入账
					goldComputeService.saveChangeRecord(userId, originalGold,
							new BigDecimal(m.get("t_redpacket_gold").toString()), WalletDetail.CHANGE_TYPE_INCOME,
							chageType, goldType, (Integer) m.get("t_redpacket_id"));
					// 修改当前记录
					this.executeSQL("UPDATE t_redpacket_log SET t_redpacket_draw = 1 WHERE t_redpacket_id = ? ",
							m.get("t_redpacket_id"));

					totalGold = totalGold.add(new BigDecimal(m.get("t_redpacket_gold").toString()));
				}

				Map<String, Object> re_m = new HashMap<String, Object>();
				re_m.put("totalGold", totalGold.add(new BigDecimal(0)));
				re_m.put("redpachageCount", findBySQLTOMap.size());

				return new MessageUtil(1, re_m);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("拆红包异常!", e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/*
	 * 充值VIP(non-Javadoc)
	 * 
	 * @see com.yiliao.service.ConsumeService#vipStoreValue(int, int)
	 * //0.支付宝 1.微信 2.pepay 3.Google pay  4. iPhone 内购
	 */
	@Override
	public MessageUtil vipStoreValue(int userId, int setMealId, int payType) {
		
		try {
			// 根据套餐编号得到用户需要支付的金额

			String setMealSql = "SELECT  t_money,t_set_meal_name FROM t_vip_setmeal WHERE t_id = ? AND t_is_enable = 0 ";

			Map<String, Object> setMealMap = this.getMap(setMealSql, setMealId);

			String orderNo = "";

			Map<String, String> map = null;

			String aliPay = null;

			Map<String, Object> res_map = null;

			if (payType == 0) {
				orderNo = orderNo + "zf_" + userId + "_" + System.currentTimeMillis();
				// 获取支付包配置信息
				List<Map<String, Object>> alipaySetUp = this.getQuerySqlList(
						"SELECT t_alipay_appid,t_alipay_private_key,t_alipay_public_key FROM t_alipay_setup");

				if (alipaySetUp.isEmpty()) {
					return new MessageUtil(-2, "支付信息未配置!");
				}

				aliPay = PayUtil.alipayCreateOrder(orderNo,
						new BigDecimal(setMealMap.get("t_money").toString()).setScale(2, BigDecimal.ROUND_DOWN),
						"VIP充值", alipaySetUp.get(0).get("t_alipay_appid").toString(),
						alipaySetUp.get(0).get("t_alipay_private_key").toString(),
						alipaySetUp.get(0).get("t_alipay_public_key").toString());

			} else if (payType == 1) {
				orderNo = orderNo + "wx_" + userId + "_" + System.currentTimeMillis();

				// 计算需要支付的金额
				int money = new BigDecimal(setMealMap.get("t_money").toString()).setScale(2, BigDecimal.ROUND_DOWN)
						.multiply(new BigDecimal(100)).intValue();

				// 查询出微信支付设置
				List<Map<String, Object>> weixinPay = this
						.getQuerySqlList("SELECT appId,t_mchid,t_mchid_key FROM t_weixinpay_setup");

				if (weixinPay.isEmpty()) {
					return new MessageUtil(-2, "支付信息未配置!");
				}

				map = PayUtil.wxPay("VIP充值", orderNo, money, weixinPay.get(0).get("appId").toString(),
						weixinPay.get(0).get("t_mchid").toString(), weixinPay.get(0).get("t_mchid_key").toString());
			} else if (payType == 2) {
				orderNo = orderNo + "pepay_" + userId + "_" + System.currentTimeMillis();

				int money = new BigDecimal(setMealMap.get("t_money").toString()).intValue();
				logger.info("金额-->{}", money);
				// 创建返回数据
				res_map = new HashMap<>();
				res_map.put("SHOP_ID", "PPS_170500");
				res_map.put("ORDER_ID", orderNo);
				res_map.put("ORDER_ITEM", URLEncoder.encode("VIP充值", "UTF-8"));
				res_map.put("AMOUNT", money);
				res_map.put("SESS_ID", "");
				res_map.put("PROD_ID", "");
				res_map.put("SYS_TRUST_CODE", "qPER2Hfjic");
				res_map.put("SHOP_TRUST_CODE", "at6sc8l3av");
				res_map.put("CHECK_CODE",
						Md5Util.md5(res_map.get("SYS_TRUST_CODE").toString() + "#" + res_map.get("SHOP_ID").toString()
								+ "#" + res_map.get("ORDER_ID").toString() + "#" + res_map.get("AMOUNT").toString()
								+ "#at6sc8l3av"));
				// ios跳转地址
				res_map.put("iosUrl",
						"https://gate.pepay.com.tw/pepay/payselect_amt.php?SHOP_ID=" + res_map.get("SHOP_ID")
								+ "&ORDER_ID=" + res_map.get("ORDER_ID") + "&ORDER_ITEM=" + res_map.get("ORDER_ITEM")
								+ "&AMOUNT=" + res_map.get("AMOUNT") + "&CHECK_CODE=" + res_map.get("CHECK_CODE")
								+ "&SESS_ID=&PROD_ID=");
			}else if(payType == 3) { //Google pay
				orderNo = orderNo + "google_" + userId + "_" + System.currentTimeMillis();
				res_map = new HashMap<>(); 
				res_map.put("goodsName", setMealMap.get("t_set_meal_name"));
				res_map.put("serviceOrderId", orderNo);
			}else  if(payType == 4) { //苹果支付预留
				// 生成苹果支付相关构建代码和回传给app的数据信息
			}

			if (StringUtils.isNotBlank(aliPay) || (null != map && !map.isEmpty()) || (null !=res_map && !res_map.isEmpty())) {
//0 1 4人民币 2 3新台币
				int goldType = 1;
				if(payType == 2 || payType == 3){
					goldType = 2;//1人民币 2新台币
				}
				// 生产订单记录
				String orderSql = "INSERT INTO t_recharge ( t_user_id, t_recharge_money, t_order_no, t_recharge_type, t_payment_type, t_setmeal_id, t_order_state, t_create_time, t_gold_type) VALUES (?,?,?,?,?,?,?,?,?);";

				this.executeSQL(orderSql, userId, setMealMap.get("t_money").toString(), orderNo, 0, payType, setMealId,
						0, DateUtils.format(new Date(), DateUtils.FullDatePattern),goldType);

				mu = new MessageUtil(1, "订单创建成功!");

				switch (payType) {
				case 0: //支付宝
					mu.setM_object(aliPay);
					break;
				case 1: //微信
					mu.setM_object(map);
					break;
				case 2: //pepay
					mu.setM_object(res_map);
					break;
				case 3: //google
					mu.setM_object(res_map);
					break;
				case 4: //Apple pay
					
					break;
				}
			} else {
				mu = new MessageUtil(-1, "创建订单失败!");
			}

			logger.info("订单跳转链接-->{}", mu.getM_object());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户{}进行VIP充值异常", userId, e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/**
	 * 用户充值金币
	 */
	@Override
	public MessageUtil goldStoreValue(int userId, int setMealId, int payType) {
		try {
			String smlSql = "SELECT t_money,t_set_meal_name FROM t_set_meal WHERE t_id = ? AND t_is_enable = 0 ";

			Map<String, Object> smlMap = this.getMap(smlSql, setMealId);

			String orderNo = "";

			Map<String, String> map = null;

			String alipay = null;

			Map<String, Object> resMap = null;

			if (payType == 0) {
				orderNo = orderNo + "zf_" + userId + "_" + System.currentTimeMillis();

				// 获取支付包配置信息
				List<Map<String, Object>> alipaySetUp = this.getQuerySqlList(
						"SELECT t_alipay_appid,t_alipay_private_key,t_alipay_public_key FROM t_alipay_setup");

				if (alipaySetUp.isEmpty()) {
					return new MessageUtil(-2, "支付信息未配置!");
				}

				alipay = PayUtil.alipayCreateOrder(orderNo, new BigDecimal(smlMap.get("t_money").toString()), "金币充值",
						alipaySetUp.get(0).get("t_alipay_appid").toString(),
						alipaySetUp.get(0).get("t_alipay_private_key").toString(),
						alipaySetUp.get(0).get("t_alipay_public_key").toString());

//				alipay = PayUtil.alipayCreateOrder(orderNo, new BigDecimal(smlMap.get("t_money").toString()), "金币充值");

			} else if (payType == 1) {
				orderNo = orderNo + "wx_" + userId + "_" + System.currentTimeMillis();

				// 计算需要支付的金额
				int money = new BigDecimal(smlMap.get("t_money").toString()).multiply(new BigDecimal(100)).intValue();

				// 查询出微信支付设置
				List<Map<String, Object>> weixinPay = this
						.getQuerySqlList("SELECT appId,t_mchid,t_mchid_key FROM t_weixinpay_setup");

				if (weixinPay.isEmpty()) {
					return new MessageUtil(-2, "支付信息未配置!");
				}

				map = PayUtil.wxPay("金币充值", orderNo, money, weixinPay.get(0).get("appId").toString(),
						weixinPay.get(0).get("t_mchid").toString(), weixinPay.get(0).get("t_mchid_key").toString());
			} else if (payType == 2) {
				orderNo = orderNo + "pepay_" + userId + "_" + System.currentTimeMillis();
				
				int payMoney = new BigDecimal(smlMap.get("t_money").toString()).intValue();
				
				logger.info("pepay支付金额-->{}",payMoney);
				// 创建返回数据
				resMap = new HashMap<>();
				resMap.put("SHOP_ID", "PPS_170500");
				resMap.put("ORDER_ID", orderNo);
				resMap.put("ORDER_ITEM", URLEncoder.encode("金币充值", "UTF-8"));
				resMap.put("AMOUNT", payMoney);
				resMap.put("SESS_ID", "");
				resMap.put("PROD_ID", "");
				resMap.put("SYS_TRUST_CODE", "qPER2Hfjic");
				resMap.put("SHOP_TRUST_CODE", "at6sc8l3av");
				
				resMap.put("CHECK_CODE",
						Md5Util.md5(resMap.get("SYS_TRUST_CODE").toString() + "#" + resMap.get("SHOP_ID").toString()
								+ "#" + resMap.get("ORDER_ID").toString() + "#" + resMap.get("AMOUNT").toString()
								+ "#at6sc8l3av"));
				// ios跳转地址
				resMap.put("iosUrl","https://gate.pepay.com.tw/pepay/payselect_amt.php?SHOP_ID=" + resMap.get("SHOP_ID")
								+ "&ORDER_ID=" + resMap.get("ORDER_ID") + "&ORDER_ITEM=" + resMap.get("ORDER_ITEM")
								+ "&AMOUNT=" + resMap.get("AMOUNT") + "&CHECK_CODE=" + resMap.get("CHECK_CODE")
								+ "&SESS_ID=&PROD_ID=");
			}else  if(payType == 3) {
				orderNo = orderNo + "google_" + userId + "_" + System.currentTimeMillis();
				// 创建返回数据
				resMap = new HashMap<>();
				resMap.put("goodsName", smlMap.get("t_set_meal_name"));
				resMap.put("serviceOrderId", orderNo);
			}else if(payType == 4) { //苹果支付
				// 生成苹果支付相关构建代码和回传给app的数据信息
			}

			if (StringUtils.isNotBlank(alipay) || (null != map && !map.isEmpty()) || (null != resMap && !resMap.isEmpty())) {	
				
				// 生产订单记录
				int goldType = 1;
				if(payType == 2 || payType == 3){
					goldType = 2;//1人民币 2新台币
				}
				
				String orderSql = "INSERT INTO t_recharge ( t_user_id, t_recharge_money, t_order_no, t_recharge_type, t_payment_type, t_setmeal_id, t_order_state, t_create_time,t_gold_type) VALUES (?,?,?,?,?,?,?,?,?);";

				this.executeSQL(orderSql, userId, smlMap.get("t_money").toString(), orderNo, 1, payType, setMealId, 0,
						DateUtils.format(new Date(), DateUtils.FullDatePattern),goldType);

				mu = new MessageUtil(1, "订单创建成功!");

				switch (payType) {
				case 0: //支付宝
					mu.setM_object(alipay);
					break;
				case 1: //微信
					mu.setM_object(map);
					break;
				case 2: //pepay
					mu.setM_object(resMap);
					break;
				case 3: //google pay
					mu.setM_object(resMap);
					break;
				case 4 : //Apple pay
					
					break;
				}
			} else {
				mu = new MessageUtil(-1, "创建订单失败!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户充值金币异常", e);
			mu = new MessageUtil(0, "程序异常!");
		}
		return mu;
	}

	/*
	 * 支付成功回调(non-Javadoc)
	 * 
	 * @see com.yiliao.service.ConsumeService#wxPayNotify(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public synchronized void payNotify(String t_order_no, String t_tripartite_order,int type) {

		try {
			logger.info("--进入了支付回调--");
			logger.info("当前接收到的订单号->{},第三方订单号->{}", t_order_no, t_tripartite_order);

			String qSql = "SELECT * FROM t_recharge WHERE t_order_no = ?  AND  t_order_state = 1";

			List<Map<String, Object>> list = this.getQuerySqlList(qSql, t_order_no);
			// 如果已经修改了 那么暂停通知
			if (!list.isEmpty()) {
				logger.info("已经回调过了，订单号->{}", t_order_no);
				return;
			}

			// 根据订单号更新订单信息
			String upSql = "UPDATE t_recharge SET t_order_state= 1 , t_tripartite_order=?,t_fulfil_time=? WHERE t_order_no= ?";
			this.executeSQL(upSql, t_tripartite_order, DateUtils.format(new Date(), DateUtils.FullDatePattern),
					t_order_no);

			// 查询当前支付订单的用户编号
			String sql = "SELECT t_id,t_user_id,t_setmeal_id,t_recharge_type,t_recharge_money FROM t_recharge WHERE t_order_no= ? ";
			if(StringUtils.isNotBlank(t_tripartite_order)) {
				sql = sql + " AND t_tripartite_order = '"+t_tripartite_order+"'";
			}

			Map<String, Object> userData = this.getMap(sql, t_order_no);

			// 删除模拟呼叫视频消息
			SimulationVideoTimer.callUser.remove(Integer.parseInt(userData.get("t_id").toString()));
			// 推送挂断呼叫
			IoSession session = UserIoSession.getInstance()
					.getMapIoSession(Integer.parseInt(userData.get("t_id").toString()));

			if (null != session) {
				com.yiliao.domain.Mid mid = new com.yiliao.domain.Mid();
				mid.setMid(Mid.brokenLineRes);

				session.write(JSONObject.fromObject(mid));
			}

			/*********** 处理CPS联盟逻辑 ************/
			// 用户是否是CPS联盟推广用户
			qSql = " SELECT t_id,t_proportions FROM t_cps WHERE t_user_id =(SELECT t_referee FROM t_user WHERE t_id= ?) AND t_audit_status = 1 ";

			List<Map<String, Object>> cpsList = this.getQuerySqlList(qSql, userData.get("t_user_id"));

			// 如果用户是CPS联盟推广的用户
			if (!cpsList.isEmpty()) {
				// 计算本次应该给CPS联盟主分配多少提成
				BigDecimal money = new BigDecimal(userData.get("t_recharge_money").toString())
						.multiply(new BigDecimal(cpsList.get(0).get("t_proportions").toString())//t_proportions  这个是提成比例
								.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN))
						.setScale(2, BigDecimal.ROUND_DOWN);
				
				/*
				 * 支付宝X1   微信 X1  pepayX0.2  Google payX0.2X0.7   iPhoneX0.7
				 *         0            1            2                   3  
				 * */
				
				if(type == 1){
					money = new BigDecimal(money+"")
							.multiply(new BigDecimal("20")//t_proportions  这个是提成比例 谷歌也是新台币  目前按照 1：5 那就是百分之20   weitechao
									.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN))
							.setScale(2, BigDecimal.ROUND_DOWN);
				}
				if(type == 2){
					money = new BigDecimal(money+"")
							.multiply(new BigDecimal("14")//t_proportions  这个是提成比例 谷歌也是新台币  目前按照 1：5 那就是百分之20   weitechao
									.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN))
							.setScale(2, BigDecimal.ROUND_DOWN);
				}
				if(type == 3){
					money = new BigDecimal(money+"")
							.multiply(new BigDecimal("70")//t_proportions  这个是提成比例 谷歌也是新台币  目前按照 1：5 那就是百分之20   weitechao
									.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN))
							.setScale(2, BigDecimal.ROUND_DOWN);
				}
				String inSql = " INSERT INTO t_cps_devote (t_cps_id, t_user_id, t_devote_value, t_create_time, t_ratio, t_recharge_money, t_source_id) VALUES (?,?,?,?,?,?,?)";

				this.executeSQL(inSql, cpsList.get(0).get("t_id"), userData.get("t_user_id"), money,
						DateUtils.format(new Date(), DateUtils.FullDatePattern), cpsList.get(0).get("t_proportions"),
						userData.get("t_recharge_money"), userData.get("t_id"));

			}
			/************ CPS联盟处理逻辑完成 ************/
			// 判断用户是充值金币还是VIP充值
			// 0 VIP充值 1.金币充值
			if ("0".equals(userData.get("t_recharge_type").toString())) {
				// 根据套餐编号得到套餐数据时长
				String smalSql = "SELECT t_duration FROM t_vip_setmeal WHERE t_id = ?";
				Map<String, Object> smalMap = this.getMap(smalSql, userData.get("t_setmeal_id"));

				// 根据用户编号得到用户的VIP数据
				String vipSql = "SELECT * FROM t_vip WHERE t_user_id = ? ";
				List<Map<String, Object>> vipData = this.getQuerySqlList(vipSql, userData.get("t_user_id"));

				// 得到VIP结束时间
				Date time = null;

				// 从来没有开通过VIP
				if (null == vipData || vipData.isEmpty()) {

					time = DateUtils.addMonth(new Date(), Integer.parseInt(smalMap.get("t_duration").toString()));

					// 新增VIP记录
					String inSql = "INSERT INTO t_vip (t_user_id, t_openUp_time, t_end_time) VALUES (?,?,?)";

					this.executeSQL(inSql, userData.get("t_user_id"),
							DateUtils.format(new Date(), DateUtils.FullDatePattern),
							DateUtils.format(time, DateUtils.FullDatePattern));

				} else {

					Date endTime = DateUtils.parse(vipData.get(0).get("t_end_time").toString(),
							DateUtils.FullDatePattern);
					// 开通过 但是VIP已经过期了
					if (endTime.getTime() < System.currentTimeMillis()) {

						time = DateUtils.addMonth(new Date(), Integer.parseInt(smalMap.get("t_duration").toString()));
						// 修改用户VIP信息
						String upVipSql = "UPDATE t_vip SET  t_openUp_time=?, t_end_time=? WHERE t_id=? ";
						this.executeSQL(upVipSql, DateUtils.format(new Date(), DateUtils.FullDatePattern),
								DateUtils.format(time, DateUtils.FullDatePattern), vipData.get(0).get("t_id"));

					} else { // 开通过切VIP没有过期

						time = DateUtils.addMonth(endTime, Integer.parseInt(smalMap.get("t_duration").toString()));

						// 修改用户的的VIP到期时间
						String upVipSql = "UPDATE t_vip SET t_end_time=? WHERE t_id=? ";

						this.executeSQL(upVipSql, DateUtils.format(time, DateUtils.FullDatePattern),
								vipData.get(0).get("t_id"));
					}

				}

				// 修改用户为VIP
				String upVipSql = "UPDATE t_user SET  t_is_vip = ? WHERE t_id = ? ";

				this.executeSQL(upVipSql, 0, userData.get("t_user_id"));

				/************* 设置用户消费流水 ******************/

				sql = "INSERT INTO t_wallet_detail (t_user_id, t_change_type, t_change_category, t_change_front, t_value, t_change_after, t_change_time,t_sorece_id)"
						+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";

				this.executeSQL(sql, userData.get("t_user_id"), WalletDetail.CHANGE_TYPE_PAY, // 类别 支出
						WalletDetail.CHANGE_CATEGOR_VIP, // 类型 购买VIP
						0, 0, 0, DateUtils.format(new Date(), DateUtils.FullDatePattern), userData.get("t_id"));

				/************* 设置用户消费流水完 ******************/

				// 推送消息
				String message = "恭喜您VIP开通成功!VIP到期时间为:" + DateUtils.format(time, DateUtils.FullDatePattern);

				// 异步通知
				this.applicationContext.publishEvent(new PushMesgEvnet(new MessageEntity(
						Integer.parseInt(userData.get("t_user_id").toString()), message, 0, new Date())));

//				GoogleCacheUtil.userCache.invalidate(Integer.parseInt(userData.get("t_user_id").toString()));
			} else { // 金币充值

				// 查询套餐
				String smalSql = "SELECT t_gold FROM t_set_meal WHERE t_id = ? ";

				Map<String, Object> smlMap = this.getMap(smalSql, userData.get("t_setmeal_id"));

				// 查询用户的充值金额
				String banSql = "SELECT  t_recharge_money FROM t_balance WHERE t_user_id = ? ";
				Map<String, Object> userBanMap = this.getMap(banSql, userData.get("t_user_id"));

				// 调用用户账户变动记录
				this.goldComputeService.saveChangeRecord(Integer.parseInt(userData.get("t_user_id").toString()),
						new BigDecimal(userBanMap.get("t_recharge_money").toString()),
						new BigDecimal(smlMap.get("t_gold").toString()), WalletDetail.CHANGE_TYPE_INCOME,
						WalletDetail.CHANGE_CATEGORY_RECHARGE, Balance.GOLD_TYPE_RECHARGE,
						(Integer) userData.get("t_id"));

				String message = "恭喜!本次成功充值" + smlMap.get("t_gold") + "个金币";

				// 异步通知
				this.applicationContext.publishEvent(new PushMesgEvnet(new MessageEntity(
						Integer.parseInt(userData.get("t_user_id").toString()), message, 0, new Date())));

				/** 获取用户是否正在进行视频聊天 */
				Map<String, Integer> map = VideoTiming.timingUser
						.get(Integer.parseInt(userData.get("t_user_id").toString()));
				// 如果用户正在进行视频聊天中
				if (null != map && !map.isEmpty()) {
					// 更新视频聊天中的总金币数
					map.put("gold", map.get("gold") + Integer.parseInt(smlMap.get("t_gold").toString()));
				}
				/***** 完成操作 *******/

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("支付成功回调异常;当前第三方订单号：{},系统内部订单号:{}", t_tripartite_order, t_order_no, e);
		}
	}

	/**
	 * 获取订单Id
	 * 
	 * @return
	 */
	public int getOrderId() {
		List<Map<String, Object>> arr = this.getQuerySqlList("SELECT t_id FROM t_order ORDER BY t_id DESC LIMIT 1;");
		return arr.isEmpty() ? 1 : (Integer) arr.get(0).get("t_id") + 1;
	}

	/**
	 * 获取支付宝公钥
	 */
	@Override
	public String getAlipayPublicKey() {
		try {
			String qSql = "SELECT t_alipay_public_key FROM t_alipay_setup";

			return this.getFinalDao().getIEntitySQLDAO().findBySQLUniqueResultToMap(qSql).get("t_alipay_public_key")
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAlipayAppId() {
		try {
			String qSql = "SELECT t_alipay_appid FROM t_alipay_setup";
			return this.getFinalDao().getIEntitySQLDAO().findBySQLUniqueResultToMap(qSql).get("t_alipay_appid")
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean checkMoney(String orderId, BigDecimal money) {
		try {

			List<Map<String, Object>> sqlList = this
					.getQuerySqlList("SELECT * FROM t_recharge WHERE t_order_no = ?  AND  t_order_state = 0 ", orderId);

			if (sqlList.isEmpty()) {
				return false;
			} else {
				if (money.compareTo(new BigDecimal(sqlList.get(0).get("t_recharge_money").toString())) == 0) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
