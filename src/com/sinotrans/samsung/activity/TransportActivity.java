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

import com.sinotrans.samsung.activity.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 交运界面
 */
import android.widget.ImageButton;
@SuppressLint("SimpleDateFormat")
public class TransportActivity extends Activity {
	private ImageButton transport_return;
	private ImageButton transport_scan;
	private ImageButton transport_submit;
	private ImageButton takepoto;
	private EditText transportbusnum;
	private EditText transportcase;
	public static final int REQUEST_SCAN_CODE =0;
	public static final int REQUEST_TAKE_PHOTO=1;
	private String mCurrentPhotoPath="";
	private ImageView mImageView;
	String process="EXTEND_FIELD7";
	String des="交运";
	String version;
	private Spinner exception_reasion=null;
	private ArrayAdapter<String> exceptionreasion_choice=null;
	 Animation exceptionreasion_list ;
	@SuppressWarnings("rawtypes")
	ArrayList exceptionreasion = new ArrayList();
	final HashMap hs_orderCode = new HashMap();
	private EditText transportdescribe;
	String exceptionreasion1="";
	private  Intent openscanIntent;
	private GetLogNameActivity myApp;
	private TextView  welcome;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transport);
		 mImageView=(ImageView) findViewById(R.id.transportphoto); 
		 transportdescribe=(EditText) findViewById(R.id.transportexcdescrib); 
		 PackageManager manager = getPackageManager();
			try {
				PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
				version=info.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 myApp = (GetLogNameActivity) getApplication(); 
		 welcome=(TextView)findViewById(R.id.transportwelcome);
		 welcome.setText("欢迎"+ myApp.getUsername()+"使用手机应用系统"+version); 	 
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
			 	exception_reasion = (Spinner) findViewById(R.id.transportexception);
				exceptionreasion_choice= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, exceptionreasion);
				exceptionreasion_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
				exception_reasion.setAdapter(exceptionreasion_choice);
				exceptionreasion_list= AnimationUtils.loadAnimation(this,R.anim.my_anim );			
		transportbusnum=(EditText) this.findViewById(R.id.transportbus);
		transportcase=(EditText) this.findViewById(R.id.transportcase);
		transport_scan = (ImageButton) this.findViewById(R.id.transportscan);
		transport_scan.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					 openscanIntent = new Intent(TransportActivity.this,ScanActivity.class);
					startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
				}
			});
		transport_return = (ImageButton) this.findViewById(R.id.transportreturn);
		transport_return.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 				finish();
// 		Intent openIntent = new Intent(TransportActivity.this,HomeActivity.class);
// 		startActivityForResult(openIntent, 0);
 			}
 		});
        takepoto = (ImageButton) this.findViewById(R.id.transporttakephoto);
        takepoto.setOnClickListener(new OnClickListener() {	
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

		
		transport_submit = (ImageButton) this.findViewById(R.id.transportsave);
		transport_submit.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v) {
 				//打开扫描界面扫描条形码或二维码
 				String s="";
 			
 			if(!"".equals(transportbusnum.getText().toString())  && !"".equals(transportcase.getText().toString()))
 			{
 				Toast.makeText(TransportActivity.this, "数据保存中......",
							Toast.LENGTH_LONG).show(); 
 						
 						if(!"".equals(mCurrentPhotoPath))
 						{					
 							s=wb.transport(myApp.getNum(),transportcase.getText().toString(),process, hs_ExceptionCode.get(exception_reasion.getSelectedItem()).toString(),exception_reasion.getSelectedItem().toString(),transportdescribe.getText().toString(),myApp.getPosition(),PictureUtil.bitmapToString(mCurrentPhotoPath),transportbusnum.getText().toString(),"");  
		
 							}
 						else
 						{

 							s=wb.transport(myApp.getNum(),transportcase.getText().toString(),process, hs_ExceptionCode.get(exception_reasion.getSelectedItem()).toString(),exception_reasion.getSelectedItem().toString(),transportdescribe.getText().toString(),myApp.getPosition(),"",transportbusnum.getText().toString(),"");  
							
 						}
 						if("SUCCESS".equals(s))
 						{
 							Toast.makeText(TransportActivity.this, "保存信息成功",
 									Toast.LENGTH_LONG).show();
 							finish();
 						startActivity(new Intent(TransportActivity.this,TransportActivity.class));

 						}
 						else
 						{
 							Toast.makeText(TransportActivity.this, "您输入的班车号不存在,系统已保存该单号！",
 									Toast.LENGTH_LONG).show();
 							finish();
 							startActivity(new Intent(TransportActivity.this,TransportActivity.class));
 						}	
 					
	
 			}
 			else {
 					Toast.makeText(TransportActivity.this, "请输入班车号信息或者箱数",
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
		        if(scanResult!="")
		        {
		        	transportbusnum.setText(scanResult);
		        }
		}
		}
		else
		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
//				linearLayout.removeAllViews();

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
	
