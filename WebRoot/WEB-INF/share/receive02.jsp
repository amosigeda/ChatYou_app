<%@page import="java.util.Map"%>
<%@page contentType="text/html;charset=big5" language="java" %>
<%@page import="java.security.*"%>
<%!
	public String md5(String s){
		char hexDigits[] = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f'};
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
		}
		catch (Exception e){
			return null;
		}
	}
%>
<%
	int nRes=0;
	String cExFlag="";
	String cOutput="";
	
	String cSessID=request.getParameter("SESS_ID");
	String cOrderID=request.getParameter("ORDER_ID");
	String cBillID=request.getParameter("BILL_ID");
	String cDataID=request.getParameter("DATA_ID");
	String cShopID=request.getParameter("SHOP_ID");
	String cPayType=request.getParameter("PAY_TYPE");
	String cProdID=request.getParameter("PROD_ID");
	String cUserID=request.getParameter("USER_ID");
	String nSource_Amount=request.getParameter("SOURCE_AMOUNT");
	String nAmount=request.getParameter("AMOUNT");
	String cCurrency=request.getParameter("CURRENCY");						
	String nDataCode=request.getParameter("DATA_CODE");
	String nTradeCode=request.getParameter("TRADE_CODE");					//成功:0 ； 失敗:不等於0
	String cShopPara=request.getParameter("SHOP_PARA");						//如廠商Receive01未帶則此參數為空
	String cCDate=request.getParameter("CDATE");
	String cCTime=request.getParameter("CTIME");
	String cBillDate=request.getParameter("BILL_DATE");
	String cBillTime=request.getParameter("BILL_TIME");
	String cDate=request.getParameter("DATE");
	String cTime=request.getParameter("TIME");
	String cCheckCode=request.getParameter("CHECK_CODE");
	
	Map<String,Object> resMap = (Map<String,Object>)request.getAttribute("resMap");
	
// 	String cSysTrustCode="SwJtmvlMje";		//系統信任碼
// 	String cShopTrustCode="DlXcjBeq5m";		//廠商信任碼
	
	String cTmp=resMap.get("cSysTrustCode") + "#" + resMap.get("SHOP_ID") + "#" + resMap.get("ORDER_ID") + "#" + 
			resMap.get("AMOUNT") + "#" + resMap.get("SESS_ID") + "#" + resMap.get("PROD_ID") + "#" +resMap.get("USER_ID") +
			"#" + resMap.get("cShopTrustCode");
	String cTrustCode=md5(cTmp);
	
	if(null ==resMap.get("CHECK_CODE") || !cCheckCode.equals(cTrustCode)){
		//檢查碼比對錯誤，有問題的資料
		nRes = 20002;
	}else{
		if("0".equals(resMap.get("TRADE_CODE").toString())){
			nRes = 0;
		}else {
			nRes = -1;
		}
		
	}

	//輸出貴方對此筆資料處理結果
	cOutput="RES_CODE=" + nRes + "&";

	out.print(cOutput);
	//pepay系統收到0或者20290則停止傳送，其他則視為廠商未完成接收處理而持續傳送
%>