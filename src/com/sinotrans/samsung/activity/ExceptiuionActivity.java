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
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinotrans.samsung.activity.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ExceptiuionActivity extends Activity {
	private ImageButton exception_return;
	private ImageButton exception_scan;
	private ImageButton exception_submit;
	private ImageButton exception_takephoto;
	private ImageButton exception_selecthoto;
	private EditText exceptiondonum;
	@SuppressWarnings("rawtypes")
	final HashMap hs_orderCode = new HashMap();
	public static final int REQUEST_SCAN_CODE =0;
	public static final int REQUEST_TAKE_PHOTO=1;
	public static final int REQUEST_SELECT_PHOTO=2;
	public ListView orderdetail;
	private String mCurrentPhotoPath="";
	StringBuffer sb = new StringBuffer();
	private ImageView image=null;
	private GetLogNameActivity myApp;
	 private Spinner exception_reasion=null;
	 private Spinner exception_node=null;
	 private String[] exceptionnode=new String[]{"签收","派送","提货","交运","到货"};
	 private ArrayAdapter<String> exceptionnode_choice=null;
	 private ArrayAdapter<String> exceptionreasion_choice=null;
	 @SuppressWarnings("rawtypes")
	ArrayList exceptionreasion = new ArrayList();
	 public MyAdapter adapter; 
	 String exception_list="";
	 Animation exceptionnode_list ;
	 Animation exceptionreasion_list ;
	 private  Intent openscanIntent;
	 String select="";
	 private Uri photoUri;
	 private Cursor cursor;
	 private EditText exceptiondescribe;
	 private TextView  welcome;
	String version;
	@SuppressLint("NewApi")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_exceptiuion);
		  PackageManager manager = getPackageManager();
			try {
				PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
				version=info.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 myApp = (GetLogNameActivity) getApplication(); 
		image=(ImageView) findViewById(R.id.exceptionpicture); 
		
		welcome=(TextView)findViewById(R.id.exceptionwelcome);
		
		welcome.setText("欢迎"+ myApp.getUsername()+"使用手机应用系统"+version);
		orderdetail = (ListView) findViewById(R.id.exceptionorderlist); 
		exceptiondescribe=(EditText) findViewById(R.id.exceptiondes); 
		exceptiondonum=(EditText) findViewById(R.id.excepptiondounm); 
		exceptiondonum.setTextColor(Color.BLUE);
		 adapter = new MyAdapter(this);  
		 orderdetail.setAdapter(adapter); 
		
		 final WebService wb = new WebService();

		 final HashMap hs_ExceptionCode = new HashMap();
		 final HashMap hs_exception =new HashMap();
		 hs_exception.put("签收", "SIGN");
		 hs_exception.put("派送", "DISPATCH");
		 hs_exception.put("提货", "PICK_UP_GOODS");
		 hs_exception.put("交运", "EXTEND_FIELD7");
		 hs_exception.put("到货", "EXTEND_FIELD6");
		exception_node = (Spinner) findViewById(R.id.exception_node);
		exceptionnode_choice= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, exceptionnode);
		exceptionnode_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
		exception_node.setAdapter(exceptionnode_choice);
		exceptionnode_list= AnimationUtils.loadAnimation(this,R.anim.my_anim );
		select=exception_node.getSelectedItem().toString();
		 exception_list=wb.getcode(select );
		 try {
				JSONObject jsonObject = new JSONObject(exception_list) ;
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
			exception_reasion = (Spinner) findViewById(R.id.exception_reasion);
			exceptionreasion_choice= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, exceptionreasion);
			exceptionreasion_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
			exception_reasion.setAdapter(exceptionreasion_choice);
			exceptionreasion_list= AnimationUtils.loadAnimation(this,R.anim.my_anim );	
			
		exception_node.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> exceptionnode_choice1, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				hs_ExceptionCode.clear();
//				exception_reasion.clearAnimation();
				exceptionreasion.clear();
				 select=exception_node.getSelectedItem().toString();
				 exception_list=wb.getcode(select );		 	
				 try {
						JSONObject jsonObject = new JSONObject(exception_list) ;
						JSONArray jsonArray = jsonObject.getJSONArray("descodedata"); 
						 for(int i=0;i<jsonArray.length();i++){ 
							 JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
							 hs_ExceptionCode.put(jsonObject2.getString("ebcdNameCn"),jsonObject2.getString("ebcdCode"));
							 exceptionreasion.add(jsonObject2.getString("ebcdNameCn"));
								exception_reasion = (Spinner) findViewById(R.id.exception_reasion);
								exceptionreasion_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
								exception_reasion.setAdapter(exceptionreasion_choice);
								
			             }
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
//				Log.i(TAG, view.getClass().getName());
				
			}
			
		} );
		
				exceptiondonum.setOnKeyListener(new OnKeyListener() {  

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event)
					{
						// TODO Auto-generated method stub
				  if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) 
				  		{  
					  		
					  if(!hs_orderCode.containsKey(exceptiondonum.getText().toString()))
						{
							hs_orderCode.put(exceptiondonum.getText().toString(), "");
							adapter.arr.add(exceptiondonum.getText().toString());
							adapter.notifyDataSetChanged();
						
							exceptiondonum.getText().clear();
				  			return true;  
						}
						else 
							
						{
								
							Toast.makeText(ExceptiuionActivity.this, "您输入的订单号已录入",
										Toast.LENGTH_SHORT).show();
				  		} 
						
				  		} 
				  			
						return false;
					} 
				});
		
		
		exception_scan = (ImageButton) this.findViewById(R.id.exceptionscan);
		exception_scan.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					 openscanIntent = new Intent(ExceptiuionActivity.this,ScanActivity.class);
					startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
				}
			});
		exception_takephoto = (ImageButton) this.findViewById(R.id.exceptiontakephoto);
		exception_takephoto.setOnClickListener(new OnClickListener() {	
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
		exception_return = (ImageButton) this.findViewById(R.id.exceptionreturn);
		exception_return.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 				finish();
// 		Intent openIntent = new Intent(ExceptiuionActivity.this,HomeActivity.class);
// 		startActivityForResult(openIntent, 0);
 			}
 		});
		exception_selecthoto = (ImageButton) this.findViewById(R.id.exceptionselectpicture);
		exception_selecthoto.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 				Intent intent = new Intent();
 			intent.setType("image/*");
 			intent.setAction(Intent.ACTION_GET_CONTENT);
 			startActivityForResult(intent, REQUEST_SELECT_PHOTO);
 			}
 		});
		
		exception_submit = (ImageButton) this.findViewById(R.id.exceptionsave);
		exception_submit.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v) {
 				//打开扫描界面扫描条形码或二维码
 				String s="";
 					if(adapter.getCount()!=0)
 					{
 						
 						for(int i=0;i<adapter.getCount();i++)
						{
	 							sb.append(adapter.arr.get(i).toString()+",");	
						}
 						if(!"".equals(mCurrentPhotoPath))
 						{
 							Toast.makeText(ExceptiuionActivity.this, "数据保存中......",
									Toast.LENGTH_LONG).show();
 							s=wb.sign(myApp.getNum(),sb.substring(0, sb.length()-1), hs_exception.get(exception_node.getSelectedItem()).toString(),hs_ExceptionCode.get(exception_reasion.getSelectedItem()).toString(),exception_reasion.getSelectedItem().toString(),exceptiondescribe.getText().toString(),myApp.getPosition(),PictureUtil.bitmapToString(mCurrentPhotoPath),"",""); 
 						}
 						
 						else
 						{
 							Toast.makeText(ExceptiuionActivity.this, "请拍摄相关照片",
	 								Toast.LENGTH_SHORT).show();
 						}
 						
 					}
 					else
 					{
 						Toast.makeText(ExceptiuionActivity.this, "订单信息为空，请输入正确的订单信息",
 								Toast.LENGTH_SHORT).show();
 					}
 					if("SUCCESS".equals(s))
		 				{
		 					Toast.makeText(ExceptiuionActivity.this, "保存信息成功",
									Toast.LENGTH_LONG).show();
		 					finish();
			 				startActivity(new Intent(ExceptiuionActivity.this,ExceptiuionActivity.class));
		 				}
		 				else
		 				{
		 					Toast.makeText(ExceptiuionActivity.this, "信息保存失败"+s,
									Toast.LENGTH_SHORT).show();	
		 				}
 					}
 		});
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
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
					 
						Toast.makeText(ExceptiuionActivity.this, "订单扫描成功，订单号为:"+scanResult,
								Toast.LENGTH_SHORT).show();
			
				}
				 startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
		}
		}
		else
		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {


				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
			PictureUtil.galleryAddPic(this, mCurrentPhotoPath);
				image.setImageBitmap(PictureUtil
						.getSmallBitmap(mCurrentPhotoPath));


			} else {
				// 取消照相后，删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
			}

			}
			else 
				if(requestCode==REQUEST_SELECT_PHOTO)
			{
					
					if(data == null){
						Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
						return;
					}
					photoUri = data.getData();
					if(photoUri == null ){
						Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
						return;
					}
				
					String[] pojo = {MediaStore.Images.Media.DATA};
			
		 cursor = managedQuery(photoUri, pojo, null, null,null);   
			if(cursor != null ){
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				mCurrentPhotoPath = cursor.getString(columnIndex);
//				cursor.close();
			}
			
			Log.i("ExceptiuionActivity", "imagePath = "+mCurrentPhotoPath);
			/*if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))*/
			if(mCurrentPhotoPath !=null){
				image.setImageBitmap(PictureUtil
						.getSmallBitmap(mCurrentPhotoPath));
				
	};
			}
		}
	
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "sinotrans_" + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
	
		return image;
	}

}



	