package com.sinotrans.gz.activity;

import com.sinotrans.samsung.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);  
        setContentView(R.layout.gz_activity_welcome);  
        //Display the current version number  
         
        
        new Handler().postDelayed(new Runnable() {  
            public void run() {  

             Intent mainIntent = new Intent(WelcomeActivity.this, HomeActivity.class);  
             startActivity(mainIntent);  
             WelcomeActivity.this.finish();  
            }  
        }, 2000); //2000 for release  
    }  
	

}
