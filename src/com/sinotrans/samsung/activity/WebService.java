package com.sinotrans.samsung.activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.util.Log;
	public class WebService extends Activity {  
		private static final String NAMESPACE = "http://webservice.webservice.samsung.commission.oms.sinotrans.com";  
//WebService地址  
		//本机地址
//	private static String URL="http://172.16.7.65:8080/OMS_WS_TEST/services/Orderadvicequeryservice";  
//	private static     String URL="http://219.141.225.157:7003/SINOTRANSWS/services/Orderadvicequeryservice";  
	
//		private static String URL = "http://172.16.99.209:7004/OMS_WS_UAT/services/Orderadvicequeryservice";  
//测试环境外网地址
				private static String URL="http://219.141.225.147:7004/OMS_WS_UAT/services/Orderadvicequeryservice"; 
//		生产环境地址
//		private static     String URL="http://219.141.225.157:7003/SINOTRANSWS/services/Orderadvicequeryservice";  
		private static final String METHOD_LOGIN = "applogin";  
		private static final String METHOD_GETEXCEPTIONCODE = "getcode";  
		private static final String METHOD_GETEXCEPTION = "getall";  
		private static final String METHOD_SIGN = "sign"; 
//		private static final String METHOD_NAME = "login"; 
		private static final String METHOD_TRANSPORT = "transport"; 
 
public String getID(String username,String password)
	{  
		String userid="";
		try 
			{  
				SoapObject rpc = new SoapObject(NAMESPACE, METHOD_LOGIN);  
				rpc.addProperty("username", username);  
				rpc.addProperty("password", password);  
				
//				HttpTransportSE ht = new HttpTransportSE(URL); 
				MyAndroidHttpTransport ht = new MyAndroidHttpTransport(URL,10000); 
//				if(ht.debug==false){
//	        		Log.i("M","===");
//				}
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
						Log.i("M","=====" + result);
						userid= result.toString();  
					}
				catch(Exception e)
					{
						e.printStackTrace();

						Log.i("M","exception4");
						userid="";
					}
			
			} 
		catch (Exception e) 
			{  
				e.printStackTrace();  
				userid="";
			} 

		Log.i("M","userid:" + userid);
		return userid;
	}  

public String transport( String userid,String DO,String transportprocess,String exceptioncode,String exceptiondescribe,String processmode,String proceslocation,String picture,String mastercode,String casenumber)
{
//	  Log.i("M","=====");
	  String res = "";
	  try {
		  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_TRANSPORT);  
		  	rpc.addProperty("USERID", userid);   
			rpc.addProperty("EORTTOID", DO);  
			rpc.addProperty("transportprocess", transportprocess); 
			rpc.addProperty("exceptioncode", exceptioncode); 
			rpc.addProperty("exceptiondescribe", exceptiondescribe); 
			rpc.addProperty("processmode", processmode); 
			rpc.addProperty("proceslocation", proceslocation); 
			rpc.addProperty("picture", picture); 
			rpc.addProperty("mastercode", mastercode);  
			rpc.addProperty("casenumber", casenumber);  
//			
//			HttpTransportSE ht = new HttpTransportSE(URL); 
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


  public String sign( String userid,String DO,String transportprocess,String exceptioncode,String exceptiondescribe,String processmode,String proceslocation,String picture,String mastercode,String signer)
  {
	  String res = "";
	  try {
		  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_SIGN);  
		  	rpc.addProperty("USERID", userid);   
			rpc.addProperty("EORTTOID", DO);  
			rpc.addProperty("transportprocess", transportprocess); 
			rpc.addProperty("exceptioncode", exceptioncode); 
			rpc.addProperty("exceptiondescribe", exceptiondescribe); 
			rpc.addProperty("processmode", processmode); 
			rpc.addProperty("proceslocation", proceslocation); 
			rpc.addProperty("picture", picture); 
			rpc.addProperty("mastercode", mastercode);  
			rpc.addProperty("signer", signer);  
			
//			HttpTransportSE ht = new HttpTransportSE(URL); 
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
					Log.i("M","sign" + result);
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
  public String getcode( String des)
  {
	  String res = "";
	  try {
		  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_GETEXCEPTIONCODE);  
		  	rpc.addProperty("des", des); 
//			HttpTransportSE ht = new HttpTransportSE(URL); 
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
					Log.i("M","getcode" + result);
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
	  }
	  
	  return res;
  	}
  public String getall( )
  {
	  String res = "";
	  try {
		  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_GETEXCEPTION);  
//			HttpTransportSE ht = new HttpTransportSE(URL); 
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
	  }
	  
	  return res;
  	}
}   