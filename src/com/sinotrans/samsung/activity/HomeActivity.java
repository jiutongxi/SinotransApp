package com.sinotrans.samsung.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.sinotrans.samsung.activity.R;
/*
 * 功能主页面
 */
public class HomeActivity extends Activity {
	private ImageButton button_arrive;
	private ImageButton button_transport;
	private ImageButton button_pickgoods;
	private ImageButton button_dispach;
	private ImageButton button_sign;
	private ImageButton button_exception;
	private Button button_logout;
	private ImageButton exit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		button_arrive=(ImageButton)findViewById(R.id.busarrive);
		
		button_arrive.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					
					Intent busarrive=new Intent();
					busarrive.setClass(HomeActivity.this, ArriveActivity.class);
					startActivity(busarrive);
					
				}
	        });	 
		button_transport=(ImageButton)findViewById(R.id.transport);
		
		button_transport.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					Intent bustransport=new Intent();
					bustransport.setClass(HomeActivity.this, TransportActivity.class);
					startActivity(bustransport);
						
				}
	        });	 
		button_pickgoods=(ImageButton)findViewById(R.id.pickgoods);
		
		button_pickgoods.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
				
					Intent buspickgoods=new Intent();
					buspickgoods.setClass(HomeActivity.this, PickGoodsActivity.class);
					startActivity(buspickgoods);

				}
	        });	 
		button_dispach=(ImageButton)findViewById(R.id.dispach);
		
		button_dispach.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
				
					Intent busdispatch=new Intent();
					busdispatch.setClass(HomeActivity.this, DispachActivity.class);
					startActivity(busdispatch);

				}
	        });	 
		button_sign=(ImageButton)findViewById(R.id.signgoods);
		
		button_sign.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					
					Intent bussign=new Intent();
					bussign.setClass(HomeActivity.this, SignActivity.class);
					startActivity(bussign);

				}
	        });	 
 
		button_exception=(ImageButton)findViewById(R.id.ecxeption);
		
		button_exception.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
				
					Intent busexception=new Intent();
					busexception.setClass(HomeActivity.this, ExceptiuionActivity.class);
					startActivity(busexception);

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

}
