package com.sinotrans.samsung.activity;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinotrans.samsung.activity.R;
public class SignActivity extends Activity {
	private String mCurrentPhotoPath="";
	public static final int REQUEST_TAKE_PHOTO = 0;
	public static final int REQUEST_SCAN_CODE =1;
	private ImageButton sign_return;
	private ImageButton sign_scan;
	private ImageButton sign_submit;
	private ImageButton sign_takephoto;
	private Long orderidLong=(long) 0;
	String Donum="";
	private EditText signdonum;
	private ImageView mImageView;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private ImageView mImageView4;
	private ImageView mImageView5;
	private TextView  welcome;
	private GetLogNameActivity myApp;
	StringBuffer sb = new StringBuffer();
	private Spinner exception_reasion=null;
	private ArrayAdapter<String> exceptionreasion_choice=null;
	 Animation exceptionreasion_list ;
	 private EditText describe;
	 String process="SIGN";
	 private List<String> listPath = new ArrayList<String>();
	 String des="签收";
	 int i = 0;
	 String	content = "";
	 String buffer="";
	 String path="";
	 String version;
	private  Intent openscanIntent;
	String exceptionreasion1="";
	@SuppressWarnings("rawtypes")
	ArrayList exceptionreasion = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_sign);
		  PackageManager manager = getPackageManager();
			try {
				PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
				version=info.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 myApp = (GetLogNameActivity) getApplication(); 
		signdonum=(EditText) this.findViewById(R.id.signdonum);
		Donum=signdonum.getText().toString();
		final HashMap hs_ExceptionCode = new HashMap();
		final GenJson bean=new GenJson();
		final Gson gson = new Gson();
		final WebService wb = new WebService();
		final StmsWebService  swb= new StmsWebService(); 
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
		
		
		signdonum.setTextColor(Color.BLUE);
		describe=(EditText) this.findViewById(R.id.signdescribe);
		mImageView=(ImageView) findViewById(R.id.signpicture);
		mImageView1=(ImageView) findViewById(R.id.signpicture1);
		mImageView2=(ImageView) findViewById(R.id.signpicture2);
		mImageView3=(ImageView) findViewById(R.id.signpicture3);
		mImageView4=(ImageView) findViewById(R.id.signpicture4);
		mImageView5=(ImageView) findViewById(R.id.signpicture5);
		
		welcome=(TextView)findViewById(R.id.welcome);
		
		welcome.setText("欢迎"+ myApp.getUsername()+"使用手机应用系统"+version);
		exception_reasion = (Spinner) findViewById(R.id.exceptiontype);
		
		exceptionreasion_choice= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, exceptionreasion);
		exceptionreasion_choice.setDropDownViewResource(R.layout. myspinner_dropdown );
		exception_reasion.setAdapter(exceptionreasion_choice);
		exceptionreasion_list= AnimationUtils.loadAnimation(this,R.anim.my_anim );	
		sign_scan = (ImageButton) this.findViewById(R.id.signscan);
		sign_scan.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					openscanIntent = new Intent(SignActivity.this,ScanActivity.class);
					startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
				}
			});
		sign_takephoto = (ImageButton) this.findViewById(R.id.signtakephoto);
		sign_takephoto.setOnClickListener(new OnClickListener() {	
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
	
		
		sign_return = (ImageButton) this.findViewById(R.id.signreturn);
		sign_return.setOnClickListener(new OnClickListener() {	
 			@Override
 			public void onClick(View v){
 				finish();
// 		Intent openIntent = new Intent(SignActivity.this,HomeActivity.class);
// 		startActivityForResult(openIntent, 0);
 			}
 		});
		
		sign_submit = (ImageButton) this.findViewById(R.id.signsave);
		sign_submit.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				//打开扫描界面扫描条形码或二维码
			
				String s="";
				String stms="";
				if(signdonum.getText().toString() != null )
				{
					if(listPath.size()==0)
					{
						Toast.makeText(SignActivity.this, "请拍摄照片",
								Toast.LENGTH_LONG).show();
					}
						else
						{	
							Toast.makeText(SignActivity.this, "数据保存中",
									Toast.LENGTH_SHORT).show();
							String json="";
								for(int i=0;i<listPath.size();i++)
									{
										if(!"".equals(PictureUtil.bitmapToString(listPath.get(i))))
										{
//									sb.append(PictureUtil.bitmapToString(listPath.get(i))+",");
									bean.setFileContent(PictureUtil.bitmapToString(listPath.get(i)));
									File f = new File(listPath.get(i));
									String fileName = f.getName();
									String fileType = ".jpg";
									bean.setFileName(fileName);
									bean.setFileType(fileType);
									bean.setConsignorCode("SAMSUN");
									bean.setOrderId(orderidLong);
									bean.setOrderCode(signdonum.getText().toString());
									json=gson.toJson(bean);
//									System.out.println("----------"+json+"----------");
									stms=swb.sign("test", "1234", json);
//									System.out.println("---------"+s+"---------");
										}
									 }
									
								
								s=wb.sign(myApp.getNum(),signdonum.getText().toString(),process, hs_ExceptionCode.get(exception_reasion.getSelectedItem()).toString(),exception_reasion.getSelectedItem().toString(),describe.getText().toString(),myApp.getPosition(),"","","");  	
								Log.i("Sinotrans",s);
								if("SUCCESS".equals(s))
								{
									Toast.makeText(SignActivity.this, "保存信息成功",
									Toast.LENGTH_SHORT).show();
									finish();
									startActivity(new Intent(SignActivity.this,SignActivity.class));

								}
							else
								{
									Toast.makeText(SignActivity.this, "保存信息失败"+s,
										Toast.LENGTH_SHORT).show();

								}
						}
					
					
			}
				else
				{
					Toast.makeText(SignActivity.this, "订单号不能为空",
							Toast.LENGTH_SHORT).show();
				}
				
				
				
		}
			
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）
		if (requestCode == REQUEST_SCAN_CODE) {
		if (resultCode == RESULT_OK) {	
			String scanResult="";
				Bundle bundle = data.getExtras();
				scanResult = bundle.getString("result");	
				 if(scanResult!=null)
				 	{
					 signdonum.setText(scanResult);
				 	}
				 startActivityForResult(openscanIntent, REQUEST_SCAN_CODE);
		}
		};
		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
//				linearLayout.removeAllViews();

				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				PictureUtil.galleryAddPic(this, mCurrentPhotoPath);

				for(int i=0;i<listPath.size();i++)
				{
					
					if(i==0)
					{
						mImageView.setImageBitmap(PictureUtil
								.getSmallBitmap(listPath.get(0)));
						mImageView.setId(i);		
						mImageView.setOnClickListener(listviewOnClickListener);
						
					}
					if(i==1)
					{
						mImageView1.setImageBitmap(PictureUtil
								.getSmallBitmap(listPath.get(1)));
						
						mImageView1.setId(i);		
						mImageView1.setOnClickListener(listviewOnClickListener);
					}
					if(i==2)
					{
						mImageView2.setImageBitmap(PictureUtil
								.getSmallBitmap(listPath.get(2)));
						mImageView2.setId(i);		
						mImageView2.setOnClickListener(listviewOnClickListener);
					}
					if(i==3)
					{
						mImageView3.setImageBitmap(PictureUtil
								.getSmallBitmap(listPath.get(3)));
						mImageView3.setId(i);		
						mImageView3.setOnClickListener(listviewOnClickListener);
					}
					if(i==4)
					{
						mImageView4.setImageBitmap(PictureUtil
								.getSmallBitmap(listPath.get(4)));
						mImageView4.setId(i);		
						mImageView4.setOnClickListener(listviewOnClickListener);
					}
					 
				}
				 	
//				 linearLayout.addView(mImageView);

			} else {
				// 取消照相后，删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
			}
			
			};
	};
	
	
	OnClickListener listviewOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			mImageView5.setImageBitmap(PictureUtil
					.getSmallBitmap(listPath.get(v.getId())));
		}
	};
	
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
	
		String imageFileName = Donum + timeStamp + ".jpg";
		
		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		listPath.add(mCurrentPhotoPath);
		
		return image;
		
	}
	
	



	}


    
	 
		 
	

	

	
