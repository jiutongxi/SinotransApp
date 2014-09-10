package com.sinotrans.gz.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sinotrans.samsung.activity.PictureUtil;
import com.sinotrans.samsung.activity.R;
public class PhotoActivity extends Activity implements OnClickListener{
	private String mCurrentPhotoPath;// 图片路径
	private static final int REQUEST_TAKE_PHOTO = 0;
	//使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	//使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	//从Intent获取图片路径的KEY
	public static final String KEY_PHOTO_PATH = "photo_path";	
	private static final String TAG = "SelectPicActivity";	
	private LinearLayout dialogLayout;
	private Button takePhotoBtn,pickPhotoBtn,cancelBtn;
	private ImageView mImageView;
	/**获取到的图片路径*/
	private String picPath;
	private Intent lastIntent ;	
	private Uri photoUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gz_activity_photo);
		mImageView=(ImageView) findViewById(R.id.photoviewgz);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layoutgz);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photogz);
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photogz);
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancelgz);
		cancelBtn.setOnClickListener(this);	
		lastIntent = getIntent();
	}

	 
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layoutgz:
			finish();
			break;
		case R.id.btn_take_photogz:
			takePhoto();
			finish();
			break;
		case R.id.btn_pick_photogz:
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		
//		String SDState = Environment.getExternalStorageState();
		
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
		Intent intent = getIntent();
		String filenm = intent.getStringExtra(MediaStore.EXTRA_OUTPUT);
		takePictureIntent
		.putExtra(MediaStore.EXTRA_OUTPUT, filenm);
		startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
		/***
		 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
		 * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
		 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
		 */
//		ContentValues values = new ContentValues();  
//		photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
//		System.out.println(values);
//		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
//		startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
//		try {
//			// 指定存放拍摄照片的位置
//			File f = createImageFile();
//			takePictureIntent
//					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//			startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
	@SuppressLint("SimpleDateFormat")
//	private File createImageFile() throws IOException {
//
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
//		String timeStamp = format.format(new Date());
//		String imageFileName = "sinotrans_" + timeStamp + ".jpg";
//
//		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
//		mCurrentPhotoPath = image.getAbsolutePath();
//		return image;
//	}
//				
	
	
	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == Activity.RESULT_OK)
//		{
//			doPhoto(requestCode,data);
//
//
//
//		}
//		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
					setResult(RESULT_OK);
//				 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				PictureUtil.galleryAddPic(this, mCurrentPhotoPath);

				mImageView.setImageBitmap(PictureUtil
						.getSmallBitmap(mCurrentPhotoPath));

			} else {
				// 取消照相后，删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
			}

			}
	}
	
	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 */

//	private void save() {
//
//		if (mCurrentPhotoPath != null) {
//
//			try {
//				File f = new File(mCurrentPhotoPath);
//
//				Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);
//
//				FileOutputStream fos = new FileOutputStream(new File(
//						PictureUtil.getAlbumDir(), "sinotrans" + f.getName()));
//
//				bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
//
//				Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
//
//			} catch (Exception e) {
//				Log.e(TAG, "error", e);
//			}
//
//		} 
//	}
}
