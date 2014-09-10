package com.sinotrans.samsung.activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import android.app.Activity;
	public class StmsWebService extends Activity {  
//		private static final String NAMESPACE = "http://webservice.webservice.samsung.commission.oms.sinotrans.com";  
//WebService地址  
		//本机地址
//	private static String URL="http://172.16.7.65:8080/OMS_WS_TEST/services/Orderadvicequeryservice";  
	//private static     String URL="http://219.141.225.157:7003/SINOTRANSWS/services/Orderadvicequeryservice";  
	
		//private static String URL = "http://172.16.99.209:7004/OMS_WS_UAT/services/Orderadvicequeryservice";  
//测试环境外网地址
//				private static String URL="http://219.141.225.147:7004/OMS_WS_UAT/services/Orderadvicequeryservice"; 
//		生产环境地址
//		private static     String URL="http://219.141.225.157:7003/SINOTRANSWS/services/Orderadvicequeryservice";  
		
//private static final String NAMESPACE = "http://172.16.0.118:7777/samsun-stms/services/receiptUpload.jws?wsdl";
//private static String URL = "http://172.16.0.118:7777/samsun-stms/services/receiptUpload.jws?wsdl";
private static final String NAMESPACE = "http://stms.sinotrans.com:7786/samsun-stms/services/receiptUpload.jws?wsdl";
private static String URL = "http://stms.sinotrans.com:7786/samsun-stms/services/receiptUpload.jws?wsdl";
//private static final String NAMESPACE = "http://172.16.99.53:7777/stms/services/receiptUpload.jws";
//private static String URL = "http://172.16.99.53:7777/stms/services/receiptUpload.jws";

private static final String METHOD_SIGN = "receiptUpload"; 

public String sign( String username,String password,String data)
{
	  String res = "";
	  try {
		  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_SIGN);  
		  	rpc.addProperty("USERNAME", username);   
			rpc.addProperty("PASSWORD", password);  
			rpc.addProperty("CONTENT", data); 
			MyAndroidHttpTransport ht = new MyAndroidHttpTransport(URL,10000); 
			ht.debug = true; 
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
			envelope.bodyOut = rpc;  
			envelope.dotNet = false;  
			envelope.setOutputSoapObject(rpc);  

			ht.call(null, envelope);
			Object result = null;
			try
				{
					result =  envelope.getResponse();  
					res= result.toString();  
				}
			catch(Exception e)
				{
					e.printStackTrace();
					res=e.getMessage();
				}
	  		}
	  catch(Exception e)
	  {
		  e.printStackTrace();
			res=e.getMessage();
	  }
	  
	  return res;
	}

}   