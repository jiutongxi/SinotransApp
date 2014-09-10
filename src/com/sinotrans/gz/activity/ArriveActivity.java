package com.sinotrans.gz.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.sinotrans.samsung.activity.GetLogNameActivity;
import com.sinotrans.samsung.activity.MyAdapter;
import com.sinotrans.samsung.activity.PictureUtil;
import com.sinotrans.samsung.activity.R;
import com.sinotrans.samsung.activity.ScanActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ArriveActivity extends Activity {
	private ImageButton arrive_return;
	private ImageButton arrive_scan;
	private ImageButton arrive_submit;
	private ImageButton arrive_takephoto;
	private EditText arrivedonum;
	public static final int REQUEST_SCAN_CODE =0;
	public static final int REQUEST_TAKE_PHOTO=1;
	public ListView orderdetail;
	private String mCurrentPhotoPath="";
	private ImageView mImageView;
	public MyAdapter adapter; 
	String process="ARRIVE_DESTINATION";
	private  Intent openscanIntent;
	String des="提货";
//	private Spinner exception_reasion=null;
	StringBuffer sb = new StringBuffer();
//	private ArrayAdapter<String> exceptionreasion_choice=null;
	 Animation exceptionreasion_list ;
	@SuppressWarnings("rawtypes")
	ArrayList exceptionreasion = new ArrayList();
//	private EditText arrivedescribe;
	String exceptionreasion1="";
	String version;
	private GetLogNameActivity myApp;
	private TextView  welcome;
	 @SuppressWarnings("rawtypes")
	final HashMap hs_orderCode = new HashMap();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		setContentView(R.layout.gz_activity_arrive);
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			version=info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myApp = (GetLogNameActivity) getApplication(); 
		welcome=(TextView)findViewById(R.id.arrivewelcode);
		welcome.setText("欢迎"+ myApp.getUsername()+"使用手机应用系统"+version + "  " + myApp.getPosition());
		arrivedonum=(EditText) this.findViewById(R.id.arrivedonu);
		arrivedonum.setTextColor(Color.BLUE);
		orderdetail = (ListView) findViewById(R.id.arrivedodetail); 
		mImageView=(ImageView) findViewById(R.id.arrive_photo); 
//		arrivedescribe=(EditText) findViewById(R.id.arriveexcdes); 
		adapter = new MyAdapter(this);  
		orderdetail.setAdapter(adapter);  
//		final HashMap hs_ExceptionCode = new HashMap();
		final HttpUrlConnection huc = new HttpUrlConnection();
//			异常选择
//			final WebService wb = new WebService();
//			exceptionreasion1=wb.getcode(des);
//			exceptionreasion.add("无异常");
//			hs_ExceptionCode.put("无异常","");
//			 try {
//				JSONObject jsonObject = new JSONObject(exceptionreasion1) ;
//				JSONArray jsonArray = jsonObject.getJSONArray("descodedata"); 
//				 for(int i=0;i<jsonArray.length();i++){ 
//					 JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
//					 hs_ExceptionCode.put(jsonObject2.getString("ebcdNameCn"),jsonObject2.getString("ebcdCode"));
//					 exceptionreasion.add(jsonObject2.getString("ebcdNameCn"));
//	             }
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			 
//			exception_reasion = (Spinner) findViewById(R.id.arriveexception);
//			exceptionreasion_choice= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, exceptionreasion);
//			exceptionreasion_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
//			exception_reasion.setAdapter(exceptionreasion_choice);
//			exceptionreasion_list= AnimationUtils.loadAnimation(this,R.anim.my_anim );	
			
				arrivedonum.setOnKeyListener(new OnKeyListener() {  

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event)
					{
						// TODO Auto-generated method stub
				  if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) 
				  		{  

						  			if(!adapter.arr.contains(arrivedonum.getText().toString()))
									{
										
						  				hs_orderCode.put(arrivedonum.getText().toString(), "");
										adapter.arr.add(arrivedonum.getText().toString());
										adapter.notifyDataSetChanged();
										arrivedonum.getText().clear();
//										隐藏软键盘
										Timer timer = new Timer();   
										        timer.schedule(new TimerTask(){   
										  
										              
										            public void run() {   
										                InputMethodManager m = (InputMethodManager)   
										                arrivedonum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
										                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);   
										            }   
										               
										        }, 500);  

							  			return true;  
									
									}
									else 
										
									{
											
										Toast.makeText(ArriveActivity.this, "您输入的订单号已录入",
													Toast.LENGTH_SHORT).show();
							  		} 
						  			
						  			
				  		} 
				  			
						return false;
					} 
				});
		arrive_scan = (ImageButton) this.findViewById(R.id.arrivescan);
		arrive_scan.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					 openscanIntent = new Intent(ArriveActivity.this,ScanActivity.class);
					startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
				}
			});
		arrive_return = (ImageButton) this.findViewById(R.id.arrivereturn);
		arrive_return.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 				finish();
// 		Intent openIntent = new Intent(ArriveActivity.this,HomeActivity.class);
// 		startActivityForResult(openIntent, 0);
 			}
 		});
//		暂时禁用拍照功能
//		arrive_takephoto = (ImageButton) this.findViewById(R.id.arrivephoto);
//		arrive_takephoto.setOnClickListener(new OnClickListener() {	
//				@Override
//				public void onClick(View v) {
//					
//					//打开拍照界面
//					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
//					try {
//						// 指定存放拍摄照片的位置
//						File f = createImageFile();
//						takePictureIntent
//								.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//						startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//						
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//					
//				}
//			});
//		暂时禁用拍照功能	
		arrive_submit = (ImageButton) this.findViewById(R.id.arrivesave);
		arrive_submit.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v) {
 				//打开扫描界面扫描条形码或二维码
 				String s="";
 					if(adapter.getCount()!=0 )
 					{
 						Toast.makeText(ArriveActivity.this, "数据保存中......",
									Toast.LENGTH_SHORT).show();
 						int orderqty = adapter.getCount();
 						for(int i=0;i<orderqty;i++)
							{
		 							sb.append(adapter.arr.get(i).toString()+",");	
							}
 							if(!"".equals(mCurrentPhotoPath))
 								{	
 								s=huc.transit(process, sb.substring(0, sb.length()-1),myApp.getUsername(), orderqty);
 								Log.i("M1",process + sb.substring(0, sb.length()-1));
 								}
 							else 
 								{
 								s=huc.transit(process, sb.substring(0, sb.length()-1), myApp.getUsername(), orderqty); 
 								Log.i("M2",process + sb.substring(0, sb.length()-1) + myApp.getUsername() + orderqty); 						
 								}
 							if("success".equals(s))
 			 				{
 			 					Toast.makeText(ArriveActivity.this, "数据保存成功",
 										Toast.LENGTH_SHORT).show();
// 			 					1.8秒后跳出下个Activity
 			 					Timer timer = new Timer();   
						        timer.schedule(new TimerTask(){   
						  
						              
						            public void run() {   
						            	finish();
		 				 				startActivity(new Intent(ArriveActivity.this,ArriveActivity.class));
						            }   
						               
						        }, 1800);  
 				 				
 			 				}
 			 				else
 			 				{
 			 					Toast.makeText(ArriveActivity.this, "信息保存失败"+s,
 										Toast.LENGTH_SHORT).show();
 			 					Log.i("M",s);
 			 				}	
 					}
 					else{
 						Toast.makeText(ArriveActivity.this, "订单信息为空,请录入相关订单信息",
									Toast.LENGTH_SHORT).show();
 					}
 				
 			}
 		});
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）
		if (requestCode == REQUEST_SCAN_CODE) {
		if (resultCode == Activity.RESULT_OK) {	
			String scanResult="";
				Bundle bundle = data.getExtras();
				scanResult = bundle.getString("result");	
					if(!adapter.arr.contains(scanResult))
					{
							hs_orderCode.put(scanResult, "");
							adapter.arr.add(scanResult);
							adapter.notifyDataSetChanged();
						 
							Toast.makeText(ArriveActivity.this, "订单扫描成功，订单号为:"+scanResult,
									Toast.LENGTH_SHORT).show();

					}
					else 
					{
						Toast.makeText(ArriveActivity.this, "您输入的订单号已经扫描",
										Toast.LENGTH_SHORT).show();
					}

				 startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
		}
		};
		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				PictureUtil.galleryAddPic(this, mCurrentPhotoPath);
				mImageView.setImageBitmap(PictureUtil
						.getSmallBitmap(mCurrentPhotoPath));

			} else {
				// 取消照相后，删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
			}

			};
	};
	
	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "sinotrans_" + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
	
		return image;
	}

}
