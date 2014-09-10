package com.sinotrans.gz.activity;

import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinotrans.samsung.activity.GetLogNameActivity;
import com.sinotrans.samsung.activity.R;
import com.sinotrans.samsung.activity.WebService;

public class MainActivity extends Activity {
	private Button button_Login;
	AutoCompleteTextView cardNumAuto;
	CheckBox savePasswordCB;
	SharedPreferences sp;
	String cardNumStr;
	String passwordStr;
	String version="";
	private TextView showversion = null;
	String pz="";
	String userid="";
	String username="";
	String userproject="";	
	@SuppressWarnings("unused")
	private GetLogNameActivity myApp;
	private EditText getpassword;
    @SuppressWarnings("unused")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	if("4".equals(android.os.Build.VERSION.RELEASE.substring(0, 1))){
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    	}
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.gz_activity_main);
		
		
		myApp = (GetLogNameActivity) getApplication(); 
        pz=getCityName();
        showversion=(TextView)findViewById(R.id.versiongz);
        button_Login=(Button)findViewById(R.id.logingz);
        getpassword=(EditText)findViewById(R.id.passwordgz);
        cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAutogz);
        sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
        savePasswordCB = (CheckBox) findViewById(R.id.savepasswordgz);
        savePasswordCB.setChecked(true);// 默认为记住密码
        cardNumAuto.setThreshold(1);// 输入1个字母就开始自动提示
        getpassword.setInputType(InputType.TYPE_CLASS_TEXT
		| InputType.TYPE_TEXT_VARIATION_PASSWORD);
// 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0 x81
// 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0 x91
        PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			version=info.versionName;
			showversion.setText(version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
        cardNumAuto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s,int start,int before,
					int count) {
				// TODO Auto-generated method stub
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll（）.size（）返回的是有多少个键值对
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				Log.i("Sinotrans",allUserName.toString());
				// sp.getAll（）返回一张hash map
				// keySet（）得到的是a set of the keys.
				// hash map是由key-value组成的

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						MainActivity.this,
						R.layout.list_item,
						allUserName);
				cardNumAuto.setAdapter(adapter);// 设置数据适配器

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				getpassword.setText(sp.getString(cardNumAuto.getText()
						.toString(), ""));// 自动输入密码
				Log.i("Sinotrans",sp.getString(cardNumAuto.getText().toString(),""));

			}
		});
        
        
        cardNumAuto.setOnKeyListener(new OnKeyListener() {  

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				// TODO Auto-generated method stub
			 	if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) 
			  		{  
				  		cardNumAuto.clearFocus();
				  		getpassword.requestFocus();
				  		return true;
			  		}
			 	else{
			 			return false;
			 	}
			}
        });
        
//        选择平台
//        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        
//        选择平台

		button_Login=(Button)findViewById(R.id.logingz);
		button_Login.setOnClickListener(new OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
//					String platform = spinner.getSelectedItem().toString();
					cardNumStr = cardNumAuto.getText().toString();
					passwordStr = getpassword.getText().toString();
					
					String s="";
	        		Webservice wb = new Webservice();
	        		s=wb.getID(cardNumAuto.getText().toString(),getpassword.getText().toString());
	        		Log.i("Sinotrans",s);
	        		if("S".equals(s))
	        		{

						sp.edit().putString(cardNumStr, passwordStr).commit();
						myApp = (GetLogNameActivity) getApplication(); 
						myApp.setVersioncode(version);
						myApp.setUsername(cardNumStr);
						myApp.setPosition(pz);
						Intent login=new Intent();
						login.setClass(MainActivity.this, WelcomeActivity.class);
						startActivity(login);
					
					
					}else{
	        		Toast.makeText(MainActivity.this, "您输入的用户名密码有误",
									Toast.LENGTH_SHORT).show();	
					}
				}
	        });	 
    	
	}
    
    
    public  String getCityName() { 
	     
	    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria(); 
	    criteria.setAccuracy(Criteria.ACCURACY_FINE); 
	    criteria.setAltitudeRequired(false); 
	    criteria.setBearingRequired(false); 
	    criteria.setCostAllowed(false); 
	    criteria.setPowerRequirement(Criteria.POWER_LOW); 
	    String cityName = null; 
	    // 取得效果最好的criteria 
	    String provider = locationManager.getBestProvider(criteria, true); 
	    if (provider == null) 
	    { 
	        return null; 
	    } 
	    // 得到坐标相关的信息 
	    Location location = locationManager.getLastKnownLocation(provider); 
	    if (location == null) { 
	        return null; 
	    } 

	    if (location != null) { 
	        double latitude = location.getLatitude(); 
	        double longitude = location.getLongitude(); 
	        // 更具地理环境来确定编码 
	        Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault()); 
	        try { 
	        	
	            // 取得地址相关的一些信息\经度、纬度 
	            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1); 
	            StringBuilder sb = new StringBuilder(); 
	            if (addresses.size() > 0) { 
	                Address address = addresses.get(0); 
	                sb.append(address.getLocality()).append("\n"); 
	                cityName = sb.toString(); 
	            } 
	        } catch (IOException e) { 
	        } 
	    } 
	    return cityName; 
	}
}
