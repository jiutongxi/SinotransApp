package com.sinotrans.sh.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinotrans.samsung.activity.R;
import com.sinotrans.sh.model.FileBean;
import com.sinotrans.sh.util.PictureUtil;

public class MainActivity extends Activity {
	private static final String NAMESPACE = "http://webservice.webservice.shsp.commission.oms.sinotrans.com";  
	//private static String URL="http://192.168.3.154:8080/OMS_TEST/services/Orderservice";  
	//private static String URL="http://172.20.42.233:8080/OMS_TEST/services/Orderservice";  
	private static String URL="http://sinotrans.wicp.net/OMS_TEST/services/Orderservice"; 

	private static final String METHOD_UPLOAD = "uploadBybuffer";  

	private String mCurrentPhotoPath;// 图片路径
	private String mCurrentVideoPath;// 视频路径
	private static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_TAKE_VIDEO = 1;
	private static final String TAG = "MainActivity";
	public ProgressDialog progressDialog;
	private ImageView mImageView;
	//订单编号
	private String orderCode="";
	//private String userId="5034000";zhuwei
	private String userId="5037001";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_activity_main);
		mImageView = (ImageView) findViewById(R.id.imageView1);
		Intent intent=getIntent();
		orderCode=intent.getStringExtra("orderCode");
		//orderId=intent.getStringExtra("orderId");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

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
		} else if (requestCode == REQUEST_TAKE_VIDEO) {
			if (resultCode == Activity.RESULT_OK) {

				Uri uri = intent.getData();
				mCurrentVideoPath = getRealPathFromURI(uri);

				Log.d(TAG, mCurrentVideoPath);
			}
		}

	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sh_activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_photo:
			takePhoto();
			return true;
//		case R.id.menu_video:
//			//takeVideo();
//			save();
//			return true;
		case R.id.menu_upload_picture:
		 upload();
			//save();
			return true;
//		case R.id.menu_upload_video:
//			uploadVideo();
//			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void save() {

		if (mCurrentPhotoPath != null) {

			try {
				File f = new File(mCurrentPhotoPath);

				Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);

				FileOutputStream fos = new FileOutputStream(new File(
						PictureUtil.getAlbumDir(), "small" + f.getName()));

				bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);

				Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				Log.e(TAG, "error", e);
			}

		} else {
			Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
		}
	}

	private void uploadVideo() {
		if (mCurrentVideoPath != null) {
			FileUploadTask task = new FileUploadTask();
			task.execute(mCurrentVideoPath, "1");
		} else {
			Toast.makeText(this, "请先点击录像按钮拍摄视频", Toast.LENGTH_SHORT).show();
		}
	}

	private void takeVideo() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
	}

	/**
	 * 拍照
	 */
	private void takePhoto() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

	/**
	 * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sinotrans 文件夹中
	 * 照片的命名规则为：sinotrans_20130125_173729.jpg
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = orderCode + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * 上传到服务器
	 */
	private void upload() {
///storage/sdcard1/Pictures/sinotrans/orderCode132420131220_112642.jpg
		if (mCurrentPhotoPath != null) {
			File filedirFile=new File(mCurrentPhotoPath);
			File parentFile=filedirFile.getParentFile();
			
		//	 String filedir=filePath.replaceAll(fileName, "");
		 //   	File photodir=new File(filedir);
			File [] files = parentFile.listFiles();
	         for (int i = 0; i < files.length; i++)
	         {
	   
	             File file1 = files[i];
	             String fileAllName=file1.getName();
	          
	             if(fileAllName!=null&&fileAllName.startsWith(orderCode)){
	            	 FileUploadTask task = new FileUploadTask();
	  	 			 task.execute(parentFile.getPath()+"/"+file1.getName(), "0");
	             }
	          
	         }

			
			
		} else {
			Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
		}
	}

	private class FileUploadTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String filePath = params[0];
			FileBean bean = new FileBean();

			String content;
	        content = PictureUtil.bitmapToString(filePath);
			bean.setFileContent(content);

			File f = new File(filePath);
			String fileName = f.getName();
			bean.setFileName(fileName);

			Gson gson = new Gson();
			String json = gson.toJson(bean);
            return this.upload(json);
			
			
			//MessageHelper helper = new MessageHelper(MainActivity.this);
			// return helper.sendMsg(json);//使用webservice
			 //	return helper.sendPost(json);// 使用http post
		}



		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("正在提交,请稍候...");
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			 progressDialog.dismiss();
			 Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
		}
		
		
		

//图片上传原格式文件
		  public String upload(String json)
		  {
			  String res = "";
			  try {
				  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_UPLOAD);
				  if(userId!=null&&userId.length()>0){
					 rpc.addProperty("userId",userId);
				  }
				  if(orderCode!=null&&orderCode.length()>0){
						rpc.addProperty("orderCode", orderCode);  
				  }
				
					rpc.addProperty("arg0", json);
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

}
