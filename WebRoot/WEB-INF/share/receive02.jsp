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
	String nTradeCode=request.getParameter("TRADE_CODE");					//���\:0 �F ����:������0
	String cShopPara=request.getParameter("SHOP_PARA");						//�p�t��Receive01���a�h���ѼƬ���
	String cCDate=request.getParameter("CDATE");
	String cCTime=request.getParameter("CTIME");
	String cBillDate=request.getParameter("BILL_DATE");
	String cBillTime=request.getParameter("BILL_TIME");
	String cDate=request.getParameter("DATE");
	String cTime=request.getParameter("TIME");
	String cCheckCode=request.getParameter("CHECK_CODE");
	
	Map<String,Object> resMap = (Map<String,Object>)request.getAttribute("resMap");
	
// 	String cSysTrustCode="SwJtmvlMje";		//�t�ΫH���X
// 	String cShopTrustCode="DlXcjBeq5m";		//�t�ӫH���X
	
	String cTmp=resMap.get("cSysTrustCode") + "#" + resMap.get("SHOP_ID") + "#" + resMap.get("ORDER_ID") + "#" + 
			resMap.get("AMOUNT") + "#" + resMap.get("SESS_ID") + "#" + resMap.get("PROD_ID") + "#" +resMap.get("USER_ID") +
			"#" + resMap.get("cShopTrustCode");
	String cTrustCode=md5(cTmp);
	
	if(null ==resMap.get("CHECK_CODE") || !cCheckCode.equals(cTrustCode)){
		//�ˬd�X�����~�A�����D�����
		nRes = 20002;
	}else{
		if("0".equals(resMap.get("TRADE_CODE").toString())){
			nRes = 0;
		}else {
			nRes = -1;
		}
		
	}

	//��X�Q��惡����ƳB�z���G
	cOutput="RES_CODE=" + nRes + "&";

	out.print(cOutput);
	//pepay�t�Φ���0�Ϊ�20290�h����ǰe�A��L�h�����t�ӥ����������B�z�ӫ���ǰe
%>