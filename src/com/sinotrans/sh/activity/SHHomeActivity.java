package com.sinotrans.sh.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.sinotrans.samsung.activity.R;
/*
 * 功能主页面
 */
public class SHHomeActivity extends Activity {
	private ImageButton button_arrive;
	private ImageButton button_transport;

	private Button button_logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		button_arrive=(ImageButton)findViewById(R.id.busarrive);
		
		button_arrive.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
					Intent showNextPage_Intent=new Intent();
					showNextPage_Intent.setClass(SHHomeActivity.this, OrdersActivity.class);
					startActivity(showNextPage_Intent);

				}
	        });	 
		button_transport=(ImageButton)findViewById(R.id.transport);
		
		button_transport.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
					Intent showNextPage_Intent=new Intent();
					showNextPage_Intent.setClass(SHHomeActivity.this, SignActivity.class);
					startActivity(showNextPage_Intent);

				}
	        });	 
	
		
		
        button_logout=(Button)findViewById(R.id.pickreturn);
		
        button_logout.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
					finish();

				}
	        });	 

		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
