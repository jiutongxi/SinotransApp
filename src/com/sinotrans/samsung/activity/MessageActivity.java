package com.sinotrans.samsung.activity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.sinotrans.samsung.activity.R;
/*
 * 消息界面
 */
public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
