package com.sinotrans.gz.activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.sinotrans.samsung.activity.MyAndroidHttpTransport;

import android.app.Activity;
import android.util.Log;

public class Webservice extends Activity {
	private static final String NAMESPACE = "http://172.16.99.53:7777/stms/services/receiptUpload.jws";  
//stms内网测试地址
//			private static String URL="http://172.16.99.53:7777/stms/services/receiptUpload.jws"; 
//stms外网测试地址
			private static String URL="http://219.141.225.71:7086/stms/services/receiptUpload.jws"; 
			
		
			private static final String METHOD_LOGIN = "authenticateUser";  

	 
	public String getID(String loginName,String passWord)
		{  
			String res="";
			try 
				{  
					SoapObject rpc = new SoapObject(NAMESPACE, METHOD_LOGIN);  
	        		Log.i("Sinotrans",loginName + passWord);
					
					rpc.addProperty("loginName",loginName);  
					rpc.addProperty("passWord",passWord);  
					
//					HttpTransportSE ht = new HttpTransportSE(URL); 
					MyAndroidHttpTransport ht = new MyAndroidHttpTransport(URL,10000); 
					if(ht.debug==false){
		        		Log.i("Sinotrans","===");
					}
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
							Log.i("Sinotrans","=====" + result);
							res= result.toString();  
						}
					catch(Exception e)
						{
							e.printStackTrace();

							Log.i("Sinotrans","exception4");
							res="";
						}
				
				} 
			catch (Exception e) 
				{  
					e.printStackTrace();  
					res="";
				} 

			
			return res;
		}  
}
