package com.sinotrans.sh.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sinotrans.samsung.activity.R;

public class SignActivity extends Activity {
	protected static final  int CHANGE_UI=1;
	private static final String NAMESPACE = "http://webservice.webservice.shsp.commission.oms.sinotrans.com";  
	//private static String URL="http://172.20.42.233:8080/OMS_TEST/services/Orderservice";  
	private static String URL="http://sinotrans.wicp.net/OMS_TEST/services/Orderservice"; 
	//private static String URL="http://192.168.3.154:8080/OMS_TEST/services/Orderservice";  

	private static final String METHOD_UPLOAD = "getOrder";  
	private static final int REQUEST_SCAN_CODE =1;
	private ImageButton sign_return;
	private ImageButton sign_scan;
	private ImageButton sign_submit;
	private ImageButton sign_takephoto;
	//显示改订单详情
	private ImageButton orderDetailSign;
	private EditText orderId;
	//配送日期
	private EditText distributionDate;
	//项目名称
	private EditText projectName;
	//司机名称
	private EditText driverName;
	//车牌号
	private EditText carCode;
	//客户订单号
	private EditText customerOrder;
	//交货地址
	private EditText receiveAdress;
	//收货单位（公司）
	private EditText receiverCompany;
	//重量
	private EditText receiveWeight;
	//体积
	private EditText receiveVolume;
	//数量
	private EditText receiveQuantity;
	private StringBuffer sb = new StringBuffer("");
    private String orderCode="";
	//1.主线程创建消息处理器
	private Handler handler=new Handler(){
		public void handleMessage(Message message){
			if(message.what==CHANGE_UI){
			String resultString=(String)message.obj;
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(resultString);
				// 获取返回码，0表示成功
				int retCode = jsonObject.getInt("ret");
				if (0 == retCode)
				{
					JSONObject dataObject = jsonObject.getJSONObject("data");
					//获取返回订单集合
					JSONArray orderslist = dataObject.getJSONArray("orderlist");
					String orderCodeValue="";
					String customerOrderCodeValue="";
			        String receiveQuantityValue="";
				    String receiveAdressValue="";
				    String projectNameValue="";
				    String distributionDateValue="";
				    String receiverCompanyValue="";
					for(int i=0;i<orderslist.length();i++)
					{
						JSONObject newsObject = (JSONObject)orderslist.opt(i); 
						orderCodeValue+=newsObject.getString("orderCode");	
					     //	hashMap.put("orderlist_item_orderId",newsObject.getString("orderId"));	
						customerOrderCodeValue+=newsObject.getString("customerOrderCode");	
					    //	hashMap.put("orderlist_item_projectId", newsObject.getString("projectId"));	
						//	hashMap.put("orderlist_item_projectCode", newsObject.getString("projectCode"));	
						projectNameValue=newsObject.getString("projectName");
							//hashMap.put("orderlist_item_receiverCompany", newsObject.getString("receiverCompany"));	
							//hashMap.put("orderlist_item_receiverName", newsObject.getString("receiverName"));	
						//hashMap.put("orderlist_item_receiverTel", newsObject.getString("receiverTel"));	
						receiveAdressValue=newsObject.getString("receiveAdress");	
						//hashMap.put("orderlist_item_driverId", newsObject.getString("driverId"));	
							//	hashMap.put("orderlist_item_driverTel", newsObject.getString("driverTel"));	
						
						receiveQuantityValue=newsObject.getString("receiveQuantity");	
							//	hashMap.put("orderlist_item_legsId",newsObject.getString("legsId"));	
							//	hashMap.put("orderlist_item_legsCode",newsObject.getString("legsCode"));	
							//hashMap.put("orderlist_item_carCode", newsObject.getString("carCode"));	
						//	ordersList.add(hashMap);
					}
//					if(orderCode!=null&&orderCode.length()>0){
//						 orderId.setText(orderCode);
//					}
					
					if(receiveAdressValue!=null&&receiveAdressValue.length()>0){
						receiveAdress.setText(receiveAdressValue);
					}
					if(projectNameValue!=null&&projectNameValue.length()>0){
						 projectName.setText(projectNameValue);
					}
				
					if(distributionDateValue!=null&&distributionDateValue.length()>0){
						 distributionDate.setText(distributionDateValue);
							
					}
	               if(customerOrderCodeValue!=null&&customerOrderCodeValue.length()>0){
	               	 customerOrder.setText(customerOrderCodeValue);				
					}
	               if(receiverCompanyValue!=null&&receiverCompanyValue.length()>0){
		             receiverCompany.setText(receiverCompanyValue);
	                }
                   if(receiveQuantityValue!=null&&receiveQuantityValue.length()>0){
		             receiveQuantity.setText(receiveQuantityValue);
	                }

				}else{
					Toast.makeText(SignActivity.this, "找不到该订单请确认订单存在", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			};
		
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_activity_sign);
		orderId=(EditText) this.findViewById(R.id.orderIdeditText);
		distributionDate=(EditText)this.findViewById(R.id.distributionDateeditText) ;
		projectName= (EditText)this.findViewById(R.id.projectNameeditText) ; 
		//客户订单号
		customerOrder= (EditText)this.findViewById(R.id.customerOrderCodeeditText);
		//交货地址
		receiveAdress= (EditText)this.findViewById(R.id.receiveAdresseditText);
		//收货单位（公司）
		receiverCompany=(EditText )this.findViewById(R.id.receiverCompanyeditText);
		//重量
		receiveWeight=(EditText)this.findViewById(R.id.receiveWeighteditText);
		//体积
		receiveVolume=(EditText)this.findViewById(R.id.receiveVolumeeditText);
		//数量
		receiveQuantity=(EditText)this.findViewById(R.id.receiveQuantityeditText);
		orderDetailSign=(ImageButton) this.findViewById(R.id.orderDetailSign);
		orderDetailSign.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
			//扫描条码或者输入订单ID点击显示订单明细
				if(orderId!=null&&orderId.length()>0){
					new Thread(){
						public void run(){
							String orderIdString=orderId.getText().toString();
							String resultString=getOrderDetail(orderIdString);	
							Message message=new Message();
							message.what=CHANGE_UI;
							message.obj=resultString;
							handler.sendMessage(message);
						}
					}.start();
				}
		
			}
		});
		sign_scan = (ImageButton) this.findViewById(R.id.signscan);
		sign_scan.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					Intent openCameraIntent = new Intent(SignActivity.this,ScanActivity.class);
					startActivityForResult(openCameraIntent, REQUEST_SCAN_CODE);
				}
			});
		sign_return = (ImageButton) this.findViewById(R.id.signreturn);
		sign_return.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 		
 			}
 		});
		
		sign_submit = (ImageButton) this.findViewById(R.id.signsave);
		sign_submit.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {}
		});
		//拍照
		sign_takephoto= (ImageButton) this.findViewById(R.id.signtakephoto);
		sign_takephoto.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				orderCode=orderId.getText().toString(); 
				if(orderCode!=null&&orderCode.length()>0){
					Intent intent = new Intent(SignActivity.this, MainActivity.class);
			    	intent.putExtra("orderCode",orderCode);
					startActivity(intent);
				}else{
					Toast.makeText(SignActivity.this, "订单不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//
		if (requestCode == REQUEST_SCAN_CODE) {
		if (resultCode == RESULT_OK) {	
			String scanResult="";
				Bundle bundle = data.getExtras();
				scanResult = bundle.getString("result");	
				 if(scanResult!=null)
				 	{
				
						 sb.append(scanResult); 					 
				
				 	}
				 orderId.setText(sb.toString());
		}
		};
	};
	//获取订单详情
	  public String getOrderDetail(String orderId)
	  {
		  String res = "";
		  try {
			  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_UPLOAD);
				rpc.addProperty("orderId", orderId);
				HttpTransportSE ht = new HttpTransportSE(URL); 
				ht.debug = true; 
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
				envelope.bodyOut = rpc;  
				envelope.dotNet = false;  
				envelope.encodingStyle = "UTF-8";
				envelope.setOutputSoapObject(rpc);  
				new MarshalBase64().register(envelope);
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
	
