package com.yiliao.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.yiliao.service.CallBackService;
import com.yiliao.service.ConsumeService;
import com.yiliao.util.Md5Util;
import com.yiliao.util.MessageUtil;
import com.yiliao.util.PrintUtil;
import com.yiliao.util.SystemConfig;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("pay")
public class PayCallbackControl {

	private static Logger logger = LoggerFactory.getLogger(PayCallbackControl.class);

	@Autowired
	private ConsumeService consumeService;

	@Autowired
	private CallBackService callBackService;

	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	String FEATURE = null;

	/**
	 * 微信支付回调
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("wxPayCallBack")
	@ResponseBody
	public void wxPayCallBack(HttpServletRequest request, HttpServletResponse response) {

		BufferedReader reader = null;

		try {

			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);

			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 -
			// http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 -
			// http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);

			// Xerces 1 -
			// http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 -
			// http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);

			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);

			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and
			// Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);

			// 读取微信发送的数据
			reader = request.getReader();
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");

			String line = "";
			String xmlString = null;

			StringBuffer inputString = new StringBuffer();

			while ((line = reader.readLine()) != null) {
				inputString.append(line);
			}
			xmlString = inputString.toString();
			request.getReader().close();

//			System.out.println("回调通知->" + xmlString);

			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
			out.flush();
			out.close();

			// 字符串转为Map对象
			Map<String, String> xmlToMap = WXPayUtil.xmlToMap(xmlString);

			logger.info("return_code-->{},result_code-->{}", xmlToMap.get("return_code"), xmlToMap.get("result_code"));
			// 支付成功!
			if ("SUCCESS".equals(xmlToMap.get("return_code")) && "SUCCESS".equals(xmlToMap.get("result_code"))) {

				this.consumeService.payNotify(xmlToMap.get("out_trade_no"), xmlToMap.get("transaction_id"),0);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 第一步:验证签名,签名通过后进行第二步
	 * 第二步:按一下步骤进行验证
	 * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	 * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
	 * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
	 * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
	 * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
	 * </pre>
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("alipay_callback")
	@ResponseBody
	public void callback(HttpServletRequest request, HttpServletResponse response) {

		final Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
		logger.info("支付宝回调，{}", params);
		try {

			String alipayPublicKey = this.consumeService.getAlipayPublicKey();
			logger.info("alipayPublicKey- >{}", alipayPublicKey);
			// 调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", "RSA2");
			if (signVerified) {
				logger.info("支付宝回调签名认证成功");
				// 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
				this.check(params);
				// 支付成功
				if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
					// 处理支付成功逻辑
					try {
//						this.callBackService.alipayPaymentComplete(param.getOutTradeNo());
						this.consumeService.payNotify(params.get("out_trade_no"), params.get("trade_no"),0);
					} catch (Exception e) {
						logger.error("支付宝回调业务处理报错,params:" + params, e);
					}
				} else {
					logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}", params.get("trade_status"), params);
				}
				// 如果签名验证正确，立即返回success，后续业务另起线程单独处理
				// 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
				PrintUtil.printWriStr("success", response);
			} else {
				logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", params);
				PrintUtil.printWriStr("failure", response);
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", params, e.getMessage());
			PrintUtil.printWriStr("failure", response);
		}
	}

	// 将request中的参数转换成Map
	@SuppressWarnings("unchecked")
	private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<String, String>();

		Set<Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

		for (Entry<String, String[]> entry : entrySet) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			int valLen = values.length;

			if (valLen == 1) {
				retMap.put(name, values[0]);
			} else if (valLen > 1) {
				StringBuilder sb = new StringBuilder();
				for (String val : values) {
					sb.append(",").append(val);
				}
				retMap.put(name, sb.toString().substring(1));
			} else {
				retMap.put(name, "");
			}
		}

		return retMap;
	}

	/**
	 * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）， 3、校验通知中的seller_id（或者seller_email
	 * )是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
	 * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
	 * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
	 * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
	 * 
	 * @param params
	 * @throws AlipayApiException
	 */
	private void check(Map<String, String> params) throws AlipayApiException {

		String outTradeNo = params.get("out_trade_no");

		// 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		Map<String, Object> dataMap = this.callBackService.getOrderByOrderNo(outTradeNo);
		if (null == dataMap) {
			throw new AlipayApiException("out_trade_no错误");
		}

		// 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		BigDecimal payMoney = new BigDecimal(params.get("total_amount"));
		if (payMoney.compareTo(new BigDecimal(dataMap.get("t_recharge_money").toString())) != 0) {
			throw new AlipayApiException("error total_amount");
		}
//		 4、验证app_id是否为该商户本身。
		if (!params.get("app_id").equals(this.consumeService.getAlipayAppId())) {
			throw new AlipayApiException("app_id不一致");
		}
	}

	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null)
			return null;

		Object obj = beanClass.newInstance();

		org.apache.commons.beanutils.BeanUtils.populate(obj, map);

		return obj;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "web_pay_receive02")
	public String webPayCallback(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> parMap = new HashMap<String, Object>() {
			{
				put("SESS_ID", request.getParameter("SESS_ID"));
				put("ORDER_ID", request.getParameter("ORDER_ID"));
				put("BILL_ID", request.getParameter("BILL_ID"));
				put("DATA_ID", request.getParameter("DATA_ID"));
				put("SHOP_ID", request.getParameter("SHOP_ID"));
				put("PAY_TYPE", request.getParameter("PAY_TYPE"));
				put("PROD_ID", request.getParameter("PROD_ID"));
				put("USER_ID", request.getParameter("USER_ID"));
				put("SOURCE_AMOUNT", request.getParameter("SOURCE_AMOUNT"));
				put("AMOUNT", request.getParameter("AMOUNT"));
				put("CURRENCY", request.getParameter("CURRENCY"));
				put("DATA_CODE", request.getParameter("DATA_CODE"));
				put("TRADE_CODE", request.getParameter("TRADE_CODE"));
				put("SHOP_PARA", request.getParameter("SHOP_PARA"));
				put("CDATE", request.getParameter("CDATE"));
				put("CTIME", request.getParameter("CTIME"));
				put("BILL_DATE", request.getParameter("BILL_DATE"));
				put("BILL_TIME", request.getParameter("BILL_TIME"));
				put("DATE", request.getParameter("DATE"));
				put("TIME", request.getParameter("TIME"));
				put("CHECK_CODE", request.getParameter("CHECK_CODE"));
			}
		};

		String cSysTrustCode = "qPER2Hfjic"; // 系統信任碼
		String cShopTrustCode = "at6sc8l3av"; // 廠商信任碼

		parMap.put("cSysTrustCode", "qPER2Hfjic");
		parMap.put("cShopTrustCode", "at6sc8l3av");

		String cTmp = cSysTrustCode + "#" + parMap.get("SHOP_ID") + "#" + parMap.get("ORDER_ID") + "#"
				+ parMap.get("AMOUNT") + "#" + parMap.get("SESS_ID") + "#" + parMap.get("PROD_ID") + "#"
				+ parMap.get("USER_ID") + "#" + cShopTrustCode;

		if (StringUtils.isNotBlank(parMap.get("CHECK_CODE").toString())
				&& parMap.get("CHECK_CODE").toString().equals(Md5Util.md5(cTmp))) { // 支付失败
			logger.info("--CHECK_CODE验证成功--");
			if ("0".equals(parMap.get("TRADE_CODE").toString())) {
				logger.info("--支付状态为-->{}",parMap.get("TRADE_CODE").toString());
				if (this.consumeService.checkMoney(parMap.get("ORDER_ID").toString(),
						new BigDecimal(parMap.get("AMOUNT").toString()))) {
					logger.info("--支付金额验证成功---");
					// 修改用户充值结果
					this.consumeService.payNotify(parMap.get("ORDER_ID").toString(), null, 1);

				}
			}
		}else {
			logger.info("---验证失败---");
		}
		// 装载参数
		request.setAttribute("resMap", parMap);
		return "receive02";
	}

	/**
	 * 回调到receive01页面
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "web_pay_receive01")
	public String web_pay_receive01(HttpServletRequest req) {

		logger.info("web_pay_receive01 进入了跳转页面");

		req.setAttribute("SHOP_ID", req.getParameter("SHOP_ID"));
		req.setAttribute("ORDER_ID", req.getParameter("ORDER_ID"));
		req.setAttribute("AMOUNT", req.getParameter("AMOUNT"));
		req.setAttribute("SESS_ID", req.getParameter("SESS_ID"));
		req.setAttribute("PROD_ID", req.getParameter("PROD_ID"));
		req.setAttribute("cCheckCode", req.getParameter("CHECK_CODE"));
		req.setAttribute("cSysTrustCode", "qPER2Hfjic");
		req.setAttribute("cShopTrustCode", "at6sc8l3av");
		// 验签失败
		return "receive01";
	}

	/**
	 * 线上支付回调
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "webOnLine", method = RequestMethod.POST)
	public String webOnLine(HttpServletRequest req, HttpServletResponse response) {

		Map<String, Object> parMap = req.getParameterMap();

		logger.info("webOnLine-->{}", JSONObject.fromObject(parMap).toString());

		String cSysTrustCode = "qPER2Hfjic"; // 系統信任碼
		String cShopTrustCode = "at6sc8l3av"; // 廠商信任碼

		parMap.put("cSysTrustCode", "qPER2Hfjic");
		parMap.put("cShopTrustCode", "at6sc8l3av");

		String cTmp = cSysTrustCode + "#" + parMap.get("SHOP_ID") + "#" + parMap.get("ORDER_ID") + "#"
				+ parMap.get("AMOUNT") + "#" + parMap.get("SESS_ID") + "#" + parMap.get("PROD_ID") + "#"
				+ parMap.get("USER_ID") + "#" + cShopTrustCode;

		if (StringUtils.isNotBlank(parMap.get("CHECK_CODE").toString())
				&& parMap.get("CHECK_CODE").toString().equals(Md5Util.md5(cTmp))) { // 支付失败
			if ("0".equals(parMap.get("TRADE_CODE").toString())) {
				if (this.consumeService.checkMoney(parMap.get("ORDER_ID").toString(),
						new BigDecimal(parMap.get("AMOUNT").toString()))) {
					// 修改用户充值结果
					this.consumeService.payNotify(parMap.get("ORDER_ID").toString(), null, 1);

				}
			}
		}

		return null;
	}

	/**
	 * google 支付回调
	 * 
	 * @param req
	 */
	@RequestMapping(value = { "validationGooglePay" }, method = RequestMethod.POST)
	@ResponseBody
	public MessageUtil validationGooglePay(HttpServletRequest req) {
		try {

			String productId = req.getParameter("productId");
			String packageName = req.getParameter("packageName");
			String purchaseToken = req.getParameter("purchaseToken");
			int purchaseState = Integer.parseInt(req.getParameter("purchaseState"));
			if (purchaseState != 0) {
				logger.error("订单未支付!");
				return new MessageUtil(-1, "订单未支付");
			}
			logger.info("productId->{},packageName->{},purchaseToken->{},purchaseState->{}", productId, packageName,
					purchaseToken, purchaseState);
			try {

				HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
				PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
						new FileInputStream(new File(SystemConfig.getValue("google_key"))), "notasecret", "privatekey",
						"notasecret");
				GoogleCredential credential = new GoogleCredential.Builder().setTransport(transport)
						.setJsonFactory(JacksonFactory.getDefaultInstance())
						.setServiceAccountId("id-656@chatyou-236007.iam.gserviceaccount.com")
						.setServiceAccountScopes(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
						.setServiceAccountPrivateKey(privateKey).build();
				AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
						JacksonFactory.getDefaultInstance(), credential).build();
				AndroidPublisher.Purchases.Products products = publisher.purchases().products();
				AndroidPublisher.Purchases.Products.Get product = products.get(packageName, productId, purchaseToken);

				ProductPurchase purchase = product.execute();
				if (purchase.getPurchaseState() != 0) {
					logger.error("订单未支付!");
					return new MessageUtil(-1, "订单未支付");
				}
				// 修改订单
				this.consumeService.payNotify(req.getParameter("serviceOrderId"), req.getParameter("orderId"),1);
				
				return new MessageUtil(1, "接口调用成功!");
			} catch (Exception e) {
				logger.error("订单验证失败! ", e);
				return new MessageUtil(-2, "订单验证失败.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new MessageUtil(0, "程序异常!");
		}
	}
	
	
	/**
	 * IOS 支付回调
	 * 
	 * @param req
	 */
	@RequestMapping(value = { "validationIOSPay" }, method = RequestMethod.POST)
	@ResponseBody
	public MessageUtil validationIOSPay(HttpServletRequest req) {
		try {
				this.consumeService.payNotify(req.getParameter("serviceOrderId"), req.getParameter("orderId"),0);
				return new MessageUtil(1, "接口调用成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new MessageUtil(0, "程序异常!");
		}
	}


}
