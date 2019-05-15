//package com.yiliao.util;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.Signature;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import java.util.Collections;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.SecurityUtils;
//import com.google.api.services.androidpublisher.AndroidPublisher;
//import com.google.api.services.androidpublisher.AndroidPublisherScopes;
//import com.google.api.services.androidpublisher.model.ProductPurchase;
//
//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;
//
//
//
//public class GooglePlaySampleUtil {
//	
//	public static Logger logger = LoggerFactory.getLogger(GooglePlaySampleUtil.class);
//	
//	
//	static String public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoZE0jSzdujsKtDpFy8k7QWKMCc+d00lFHPFN3qIG5Oyyui/EsdIi5X3idfKBQ8X3Y7QCzq60d+bxIQuCCtLAJ6w6bgTK4XPe6cOCpuynwWecsFAwdT3FVTDEcGRqEa3Vjwj1NNm2I8bVNVmom0RLHWUTgjvAhoT2kX4BJLhKg4lx05aI03eMZstJRFCjDdP1FHn2/2oB7EIwepkeiCQ1EbHOQvynNBAz2lFAn+DD/nS0uNa0s3SMfKCaSrRniPGKAAy4DXPBvFEdQlHBaE9+OrcqNS7PbduvdasIOmDm789y4t+M7chdQuK2pCvDeTQCD2OrOENu5az0LbsiBu234wIDAQAB";
//	
//	public static boolean validationGooglePay(JSONObject jsonObject) {
//		try {
//
//			if(null == jsonObject || jsonObject.isEmpty()) {
//				logger.info("--google参数异常--");
//				return false;
//			}
//			
//			String productId = jsonObject.getString("productId");
//			String packageName = jsonObject.getString("packageName");
//			String purchaseToken = jsonObject.getString("purchaseToken");
//			int purchaseState = jsonObject.getInt("purchaseState");
//			if (purchaseState != 0) {
//				logger.error("订单未支付!");
//			}
//			try {
//
//				HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
//				PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
//						new FileInputStream(new File("{P12 key file}")), "notasecret", "privatekey", "notasecret");
//				GoogleCredential credential = new GoogleCredential.Builder().setTransport(transport)
//						.setJsonFactory(JacksonFactory.getDefaultInstance()).setServiceAccountId("id-656@chatyou-236007.iam.gserviceaccount.com")
//						.setServiceAccountScopes(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
//						.setServiceAccountPrivateKey(privateKey).build();
//				AndroidPublisher publisher = new AndroidPublisher.Builder(transport, JacksonFactory.getDefaultInstance(),
//						credential).build();
//				AndroidPublisher.Purchases.Products products = publisher.purchases().products();
//				AndroidPublisher.Purchases.Products.Get product = products.get(packageName, productId, purchaseToken);
//
//				ProductPurchase purchase = product.execute();
//				if (purchase.getPurchaseState() != 0) {
//					logger.error("订单未支付!");
//				}
//			} catch (Exception e) {
//				logger.error("订单验证失败! ", e);
//			}
//			 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
////	public static void main(String[] args) {
////		try {
////			HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
////
////			PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
////					new FileInputStream(new File("{P12 key file}")), // 生成的P12文件
////					"notasecret", "privatekey", "notasecret");
////
////			GoogleCredential credential = new GoogleCredential.Builder().setTransport(transport)
////					.setJsonFactory(JacksonFactory.getDefaultInstance()).setServiceAccountId("{Email address}") // e.g.:
////																												// 626891557797-frclnjv31rn4ss81ch746g9t6pd3mmej@developer.gserviceaccount.com
////					.setServiceAccountScopes(AndroidPublisherScopes.all()).setServiceAccountPrivateKey(privateKey)
////					.build();
////
////			AndroidPublisher publisher = new AndroidPublisher.Builder(transport, JacksonFactory.getDefaultInstance(),
////					credential).build();
////
////			AndroidPublisher.Purchases.Products products = publisher.purchases().products();
////
////			// 参数详细说明:
////			// https://developers.google.com/android-publisher/api-ref/purchases/products/get
////			AndroidPublisher.Purchases.Products.Get product = products.get("{packageName}", "{productId}", "{token}");
////
////			// 获取订单信息
////			// 返回信息说明:
////			// https://developers.google.com/android-publisher/api-ref/purchases/products
////			// 通过consumptionState, purchaseState可以判断订单的状态
////			ProductPurchase purchase = product.execute();
////
////			if (purchase.getPurchaseState() != 0) {
////				return false;
////			}
////
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////	}
////	
////	
////
////	public Object preHandle(ProceedingJoinPoint proceedingJoinPoint) {
////		
////		Object[] args = proceedingJoinPoint.getArgs();
////		// 获取参数
////		String signature = null;
////		String purchaseData = null;
////		try {
////			purchaseData = String.valueOf(args[0]);
////			signature = String.valueOf(args[1]);
////		} catch (NullPointerException e) {
////			logger.error("参数为空");
////		}
////
////		
////	}
//
//	private boolean docheck(String content, String sign, String publicKey) throws Exception {
//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//		byte[] encodedKey = Base64.getDecoder().decode(publicKey);
//		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
//		Signature signature = Signature.getInstance("SHA1WithRSA");
//		signature.initVerify(pubKey);
//		signature.update(content.getBytes("utf-8"));
//		return signature.verify(Base64.getDecoder().decode(sign));
//	}
//
//}
