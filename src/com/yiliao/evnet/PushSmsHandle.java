package com.yiliao.evnet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.yiliao.util.SendSMSUtil;

/**
 * 短信发送
 * @author Administrator
 *
 */
@Component(value="PushSmsHandle")
public class PushSmsHandle implements ApplicationListener<PushSms> {
 
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onApplicationEvent(PushSms event) {
		 
		@SuppressWarnings("unchecked")
		Map<String, String> smsPhone  = (Map<String, String>) event.getSource();
		logger.info("异步发送短信验证码");
		
		if(null == smsPhone.get("sms_type")) {
			
			logger.info("发送验证码的手机号-->{}",smsPhone.get("phone"));
			//发送短信
			if(smsPhone.get("phone").indexOf("886") == 0) { //发送台湾短信
				SendSMSUtil.sendInternationalSms(smsPhone.get("phone").toString().substring(3), smsPhone.get("smsCode").toString());
				return;
			}else if(smsPhone.get("phone").indexOf("+886") == 0) {
				SendSMSUtil.sendInternationalSms(smsPhone.get("phone").toString().substring(4), smsPhone.get("smsCode").toString());
				return;
			}
			SendSMSUtil.sendSms(smsPhone.get("phone").toString(), smsPhone.get("smsCode").toString());
		}else 
		//腾讯短信
		if(smsPhone.get("sms_type").equals("0")) {
			SendSMSUtil.sendQQSMS(smsPhone.get("appid").toString(), smsPhone.get("appkey").toString(), Integer.parseInt(smsPhone.get("templateId")), smsPhone.get("smsSign").toString(), smsPhone.get("phone").toString(), smsPhone.get("smsCode").toString());
		//阿里云
		}else if(smsPhone.get("sms_type").equals("1")) {
			SendSMSUtil.sendAliPaySMS(smsPhone.get("appid"), smsPhone.get("appkey"), smsPhone.get("templateId"), smsPhone.get("smsSign"), smsPhone.get("phone"), smsPhone.get("smsCode"));
		//网易云
		}else if(smsPhone.get("sms_type").equals("2")) {
			SendSMSUtil.sendNetEaseSMS(smsPhone.get("appid"), smsPhone.get("appkey"), Integer.parseInt(smsPhone.get("templateId")), smsPhone.get("smsSign"), smsPhone.get("phone"), smsPhone.get("smsCode"));
		}
		
	}

}
