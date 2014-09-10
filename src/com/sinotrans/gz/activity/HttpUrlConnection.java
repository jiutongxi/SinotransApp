package com.sinotrans.gz.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;

public class HttpUrlConnection extends Activity {
	//STMS内网地址
//	private static String URL = "http://172.16.99.53:7777/stms/service/mk.edi.service?indata="; 
	//STMS外网地址
	private static String URL = "http://219.141.225.71:7086/stms/service/mk.edi.service?indata="; 
	private String result = "";
	private String res = "";
	private String str = "";
	public String transit (String operation, String code, String username, int orderqty){
		
		SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		String   strdate   =   sDateFormat.format(curDate);    
		Log.i("M",strdate);
		StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<XML>");
        xml.append("<MESSAGEHEAD>");
        xml.append("<SENDER>TESTGZ</SENDER>");
        xml.append("<RECIEVER>OMS</RECIEVER>");
        xml.append("<FILETYPE>XML</FILETYPE>");
        xml.append("<CONTENTTYPE>OPERATION</CONTENTTYPE>");
        xml.append("<FILEFUNCTION>BACK</FILEFUNCTION>");
        xml.append("<SENDTIME>");
        xml.append(strdate);
        xml.append("</SENDTIME>");
//        xml.append("<FILENAME>");
//        xml.append(date);
//        xml.append(".xml");
        xml.append("<FILENAME>XCD_2012-12-27_1152683.xml</FILENAME>");
        xml.append("</MESSAGEHEAD>");
        xml.append("<MESSAGEDETAIL><![CDATA[");
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<XML>");
        xml.append("<CONTENTLIST>");
//        多张订单同时传送
        String[] obj=code.split(",");
        for(int i=0; i < orderqty; i++){
	        xml.append("<CONTENT>");
	        xml.append("<HEADER>");
	        xml.append("<OPERATION>");
	        xml.append(operation);
	        xml.append("</OPERATION>");
//	        需要邮件反馈则加
	        xml.append("<CONTRACTOR_ID>GZ1</CONTRACTOR_ID>");
	        xml.append("<ID>");
	        xml.append(obj[i]);
	        xml.append("</ID>");
	        xml.append(" <CONSIGNOR_ID></CONSIGNOR_ID>");
	//        客户代码
	        xml.append("<CODE></CODE>");
	        xml.append("<OPERATION_TIME>");
	        xml.append(strdate);
	        xml.append("</OPERATION_TIME>");
	        xml.append("<OPERATOR>");
	        xml.append(username);
	        xml.append("</OPERATOR>");
	        xml.append("<FLAG></FLAG>");
	        xml.append("</HEADER>");
	        xml.append("<DETAIL/>");
	        xml.append("</CONTENT>");
        }
        xml.append("</CONTENTLIST>");
        xml.append("</XML>");
        xml.append("]]></MESSAGEDETAIL>");
        xml.append("</XML>");
        
        try {
        	byte[] xmlbyte = xml.toString().getBytes("UTF-8");
        	//编码
        	str=URLEncoder.encode(xml.toString(), "utf-8");
            System.out.println(str);
            URL url = new URL(URL+str);
            System.out.println(url);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setConnectTimeout(5000);
//            conn.setDoOutput(true);// 允许输出
//            conn.setDoInput(true);
//            conn.setUseCaches(false);// 不使用缓存
            conn.setRequestMethod("GET");
//            conn.getOutputStream().write(xmlbyte);
//            conn.getOutputStream().flush();
//            conn.getOutputStream().close();
            
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {  
            	System.out.println("yes++");  
            	 //请求返回的数据  
            	InputStream in=conn.getInputStream();  
            	String res =null;  
            	try {  
            		byte[] data1 = new byte[in.available()];  
            		in.read(data1);  
            		//转成字符串  
            		res = new String(data1);  
            		System.out.println(res);  
            		//把接收过来的xml文件解析
				    XmlPullParser parser = Xml.newPullParser();  
				    parser.setInput(new StringReader(res));  
				    int event = parser.getEventType();  
				    while (event != XmlPullParser.END_DOCUMENT) {  
				    switch (event) {  
				        case XmlPullParser.START_DOCUMENT:  
				            break;  
				        case XmlPullParser.START_TAG:  // 开始元素事件
				        	if("MSGCONTENT".equals(parser.getName())){
				        		result = parser.nextText();
				        		break;
				        	}				          
				        case XmlPullParser.END_TAG:  //结束元素事件
				            break;  
				        }  
				        event = parser.next();  
				    } 
            	} catch (Exception e1) {  
	            	// TODO Auto-generated catch block  
	            	e1.printStackTrace();  
	            }  
            } else {  
            	System.out.println("no++");  
            	}  
          } catch (IOException e) {  
            	// TODO Auto-generated catch block  
            	 e.printStackTrace();  
          }  

        return result;
        
       
 
            
	}
}
