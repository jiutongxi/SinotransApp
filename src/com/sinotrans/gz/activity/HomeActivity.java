package com.sinotrans.gz.activity;

import com.sinotrans.samsung.activity.R;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private ImageButton button_pickup;
	private ImageButton button_departure;
	private ImageButton button_destinationcity;
	private ImageButton button_arrive;
//	ImageButton M
	private Button button_logout;
	private ImageButton exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gz_activity_home);
		button_pickup=(ImageButton)findViewById(R.id.pickup);
		
		button_pickup.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					
					Intent pickup=new Intent();
					pickup.setClass(HomeActivity.this, PickupActivity.class);
					startActivity(pickup);
					
				}
	        });	 
		button_departure=(ImageButton)findViewById(R.id.departure);
		
		button_departure.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					Intent departure=new Intent();
					departure.setClass(HomeActivity.this, DepartureActivity.class);
					startActivity(departure);
						
				}
	        });	 
		button_destinationcity=(ImageButton)findViewById(R.id.destinationcity);
		
		button_destinationcity.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
				
					Intent destinationcity =new Intent();
					destinationcity.setClass(HomeActivity.this,DestinationCityActivity.class);
					startActivity(destinationcity);

				}
	        });	 
		button_arrive=(ImageButton)findViewById(R.id.arrive);
		
		button_arrive.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
				
					Intent arrive=new Intent();
					arrive.setClass(HomeActivity.this, ArriveActivity.class);
					startActivity(arrive);

				}
	        });	 
		
		exit=(ImageButton)findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
					finish();
				}
	        });	 
//        button_logout=(Button)findViewById(R.id.pickreturn);
//		
//        button_logout.setOnClickListener(new OnClickListener(){ 
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub		
//					finish();
//				}
//	        });	 

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}

	//按两次返回键退出
    boolean isExit; 
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            exit();  
            return false;  
        } else {  
            return super.onKeyDown(keyCode, event);  
        }  
    }  
    public void exit(){  
        if (!isExit) {  
            isExit = true;  
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
            mHandler.sendEmptyMessageDelayed(0, 2000);  
        } else {  
            Intent intent = new Intent(Intent.ACTION_MAIN);  
            intent.addCategory(Intent.CATEGORY_HOME);  
            startActivity(intent);  
            System.exit(0);  
        }  
    }  
    Handler mHandler = new Handler() {  
        
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);  
            isExit = false;  
        }  
  
    };  
    //按两次返回键退出

}
