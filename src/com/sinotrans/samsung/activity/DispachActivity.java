package com.sinotrans.samsung.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinotrans.samsung.activity.R;
/*
 * 派送发车
 */
@SuppressLint("SimpleDateFormat")
public class DispachActivity extends Activity {
	private ImageButton dispatch_return;
	private ImageButton dispatch_scan;
	private ImageButton dispatch_submit;
	private ImageButton dispatch_takephoto;
	private EditText dispatchdonum;
	public static final int REQUEST_SCAN_CODE =0;
	public static final int REQUEST_TAKE_PHOTO=1;
	public ListView orderdetail;
	private String mCurrentPhotoPath="";
	private ImageView mImageView;
	public MyAdapter adapter; 
	String process="DISPATCH";
	private  Intent openscanIntent;
	String des="派送";
	private Spinner exception_reasion=null;
	StringBuffer sb = new StringBuffer();
	private ArrayAdapter<String> exceptionreasion_choice=null;
	 Animation exceptionreasion_list ;
	@SuppressWarnings("rawtypes")
	ArrayList exceptionreasion = new ArrayList();
	private EditText dispatchdescribe;
	String exceptionreasion1="";
	String version;
	private GetLogNameActivity myApp;
	private TextView  welcome;
	 @SuppressWarnings("rawtypes")
	final HashMap hs_orderCode = new HashMap();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_dispach);
		  PackageManager manager = getPackageManager();
			try {
				PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
				version=info.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 myApp = (GetLogNameActivity) getApplication(); 
		welcome=(TextView)findViewById(R.id.dispatchwelcode);
		
		welcome.setText("欢迎"+ myApp.getUsername()+"使用手机应用系统"+version);
		 
		dispatchdonum=(EditText) this.findViewById(R.id.dispatchdonu);
		dispatchdonum.setTextColor(Color.BLUE);
		orderdetail = (ListView) findViewById(R.id.dispatchdodetail); 
		 mImageView=(ImageView) findViewById(R.id.dispatphoto); 
		 dispatchdescribe=(EditText) findViewById(R.id.dispatchexcdes); 
		 adapter = new MyAdapter(this);  
		 orderdetail.setAdapter(adapter);  
		 final HashMap hs_ExceptionCode = new HashMap();
			final WebService wb = new WebService();
			exceptionreasion1=wb.getcode(des);
			exceptionreasion.add("无异常");
			hs_ExceptionCode.put("无异常","");
			 try {
				JSONObject jsonObject = new JSONObject(exceptionreasion1) ;
				JSONArray jsonArray = jsonObject.getJSONArray("descodedata"); 
				 for(int i=0;i<jsonArray.length();i++){ 
					 JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
					 hs_ExceptionCode.put(jsonObject2.getString("ebcdNameCn"),jsonObject2.getString("ebcdCode"));
					 exceptionreasion.add(jsonObject2.getString("ebcdNameCn"));
	             }
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
				exception_reasion = (Spinner) findViewById(R.id.dispatchexception);
				exceptionreasion_choice= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, exceptionreasion);
				exceptionreasion_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
				exception_reasion.setAdapter(exceptionreasion_choice);
				exceptionreasion_list= AnimationUtils.loadAnimation(this,R.anim.my_anim );	
			
				dispatchdonum.setOnKeyListener(new OnKeyListener() {  

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event)
					{
						// TODO Auto-generated method stub
				  if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) 
				  		{  

						  			if(!hs_orderCode.containsKey(dispatchdonum.getText().toString()))
									{
										
						  				hs_orderCode.put(dispatchdonum.getText().toString(), "");
										adapter.arr.add(dispatchdonum.getText().toString());
										adapter.notifyDataSetChanged();
										dispatchdonum.getText().clear();
							  			return true;  
									
									}
									else 
										
									{
											
										Toast.makeText(DispachActivity.this, "您输入的订单号已录入",
													Toast.LENGTH_SHORT).show();
							  		} 
						  			
						  			
				  		} 
				  			
						return false;
					} 
				});
		dispatch_scan = (ImageButton) this.findViewById(R.id.dispatchscan);
		dispatch_scan.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					 openscanIntent = new Intent(DispachActivity.this,ScanActivity.class);
					startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
				}
			});
		dispatch_return = (ImageButton) this.findViewById(R.id.dispatchreturn);
		dispatch_return.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 				finish();
// 		Intent openIntent = new Intent(DispachActivity.this,HomeActivity.class);
// 		startActivityForResult(openIntent, 0);
 			}
 		});
		dispatch_takephoto = (ImageButton) this.findViewById(R.id.dispatchtakephoto);
		dispatch_takephoto.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					
					//打开拍照界面
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
					try {
						// 指定存放拍摄照片的位置
						File f = createImageFile();
						takePictureIntent
								.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

						startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
						
					} catch (IOException e) {
						e.printStackTrace();
					}

					
				}
			});
		
		dispatch_submit = (ImageButton) this.findViewById(R.id.dispatchsave);
		dispatch_submit.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v) {
 				//打开扫描界面扫描条形码或二维码
 				String s="";
 					if(adapter.getCount()!=0 )
 					{
 						Toast.makeText(DispachActivity.this, "数据保存中......",
									Toast.LENGTH_LONG).show();
 						for(int i=0;i<adapter.getCount();i++)
							{
		 							sb.append(adapter.arr.get(i).toString()+",");	
							}
 							if(!"".equals(mCurrentPhotoPath))
 								{	
 								s=wb.sign(myApp.getNum(),sb.substring(0, sb.length()-1),process,hs_ExceptionCode.get(exception_reasion.getSelectedItem()).toString(),exception_reasion.getSelectedItem().toString(),dispatchdescribe.getText().toString(),myApp.getPosition(),PictureUtil.bitmapToString(mCurrentPhotoPath),"","");  						
 								}
 							else 
 								{
 								s=wb.sign(myApp.getNum(),sb.substring(0, sb.length()-1),process,hs_ExceptionCode.get(exception_reasion.getSelectedItem()).toString(),exception_reasion.getSelectedItem().toString(),dispatchdescribe.getText().toString(),myApp.getPosition(),"","","");  						
 								}
 							if("SUCCESS".equals(s))
 			 				{
 			 					Toast.makeText(DispachActivity.this, "数据保存成功",
 										Toast.LENGTH_SHORT).show();
 				 				finish();
 				 				startActivity(new Intent(DispachActivity.this,DispachActivity.class));
 			 				}
 			 				else
 			 				{
 			 					Toast.makeText(DispachActivity.this, "信息保存失败"+s,
 										Toast.LENGTH_SHORT).show();
 			 				}	
 					}
 					else{
 						Toast.makeText(DispachActivity.this, "订单信息为空,请录入相关订单信息",
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
					if(!hs_orderCode.containsKey(scanResult))
					{
							hs_orderCode.put(scanResult, "");
							adapter.arr.add(scanResult);
							adapter.notifyDataSetChanged();
						 
							Toast.makeText(DispachActivity.this, "订单扫描成功，订单号为:"+scanResult,
									Toast.LENGTH_SHORT).show();

					}
					else 
					{
						Toast.makeText(DispachActivity.this, "您输入的订单号已经扫描",
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
	
