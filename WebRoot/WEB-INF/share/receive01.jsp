<%@page contentType="text/html;charset=big5" language="java"%>
<%@page import="java.security.*"%>
<%!public String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}%>
<%
	/*
	0:成功
	20001:廠商參數傳送失敗
	20002:檢查碼錯誤
	20003:廠商資料庫連接失敗
	20004:廠商資料庫寫入失敗
	20099:其他失敗
	*/

	String cShopID = (String) request.getAttribute("SHOP_ID");
	String cOrderID = (String) request.getAttribute("ORDER_ID");
	String nAmount = (String) request.getAttribute("AMOUNT");
	String cSessID = (String) request.getAttribute("SESS_ID");
	String cProdID = (String) request.getAttribute("PROD_ID");
	String cCheckCode = (String) request.getAttribute("cCheckCode");

	int nRes = 0;
	String cOutput = "";
	String cUserID = "";
	String cShopPara = "";
	String cShopParaUrlEncode = "";
	String cRetUrl = "";
	String cSysTrustCode = (String) request.getAttribute("cSysTrustCode"); //系統信任碼
	String cShopTrustCode = (String) request.getAttribute("cShopTrustCode"); //廠商信任碼

	String cTmp = cSysTrustCode + "#" + cShopID + "#" + cOrderID + "#" + nAmount + "#" + cSessID + "#" + cProdID
			+ "#" + cShopTrustCode;
	String cTrustCode = md5(cTmp);

	if (null == cCheckCode || !cCheckCode.equals(cTrustCode)) {
		nRes = 20002;
	} else {
		//查詢廠商該筆ORDER_ID的消費者儲值碼USER_ID（假設為0910123456）
		cUserID = "0910123456";
		cShopPara = "aaa#bbb#ccc";
		//對shoppara作url encode，最後廠商收到後再自行用urldecode解碼
		cShopParaUrlEncode = java.net.URLEncoder.encode(cShopPara, "big5");
		cRetUrl = "";
	}

	//輸出此筆交易的USER_ID及結果RES_CODE
	//（如有自訂參數則再加上&SHOP_PARA=xxx（作UrlEncode）、
	//交易結果回傳網址則再加上RET_URL=http://www.xxx.com/xxx.jsp）
	cOutput = "USER_ID=" + cUserID + "&RES_CODE=" + nRes + "&SHOP_PARA=" + cShopParaUrlEncode + "&RET_URL="	+ cRetUrl + "&";

	out.print(cOutput);
%>