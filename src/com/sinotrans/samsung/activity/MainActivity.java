package com.sinotrans.samsung.activity;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinotrans.samsung.activity.R;
import com.sinotrans.sh.activity.SHHomeActivity;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 登陆页面
 */
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
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	if("4".equals(android.os.Build.VERSION.RELEASE.substring(0, 1))){
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    	}
//     System.out.println("Product Model: " + android.os.Build.MODEL + "," 
//             + android.os.Build.VERSION.SDK + "," 
//             + android.os.Build.VERSION.RELEASE); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceid = tm.getDeviceId(); 
//        String tel = tm.getLine1Number(); 
//        String  imei = tm.getSimSerialNumber(); 
//        String imsi = tm.getSubscriberId(); 
//        int simState = tm.getSimState();
        
     



        myApp = (GetLogNameActivity) getApplication(); 

         pz=getCityName();
        showversion=(TextView)findViewById(R.id.version);
        button_Login=(Button)findViewById(R.id.login);
        getpassword=(EditText)findViewById(R.id.password);
        cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
        sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
        savePasswordCB = (CheckBox) findViewById(R.id.savepassword);
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

			}
		});

		// 登陆
        button_Login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cardNumStr = cardNumAuto.getText().toString();
				passwordStr = getpassword.getText().toString();
				String userinformation="";
        		WebService wb = new WebService();
        		userinformation=wb.getID(cardNumAuto.getText().toString(),getpassword.getText().toString());
				Log.i("M","userinformation" + userinformation);
        		if(!"".equals(userinformation))
        		{
         		 try {
      				JSONObject jsonObject = new JSONObject(userinformation) ;
      				JSONArray jsonArray = jsonObject.getJSONArray("userdata"); 
      				 for(int i=0;i<jsonArray.length();i++){ 
      					 JSONObject jsonObject2 = (JSONObject)jsonArray.opt(0);
      					username=jsonObject2.getString("username");
      					Log.i("M",username);
      					userproject=jsonObject2.getString("companyid");
      					Log.i("M",userproject);
      					userid=jsonObject2.getString("userid");
      					Log.i("M",userid);
      					
      					
      				 }
      				 }
      				 catch (JSONException e1) {
           				// TODO Auto-generated catch block
           				e1.printStackTrace();
           			}
      					if(userproject.equals("5020000"))//三星项目
      									// 测试环境5020000，生产环境5010000
      					{
      						sp.edit().putString(cardNumStr, passwordStr).commit();
      					
      						Intent showNextPage_Intent=new Intent();
      						showNextPage_Intent.setClass(MainActivity.this, HomeActivity.class);
      						startActivity(showNextPage_Intent);
      						myApp.setNum(userid);
      						myApp.setVersioncode(version);
      						myApp.setPosition(pz);
      						myApp.setUsername(username);
//      						Log.i("M",myApp.getNum());
//      						Log.i("M",myApp.getPosition());
//      				       Context ctx = MainActivity.this;       
//      				        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
//      				  //存入数据
//      				       Editor editor = sp.edit();
//      				       editor.putString("userid", userid.toString());
//      				       editor.putInt("INT_KEY", 0);
//      				       editor.commit();
//      				      
//      				//返回STRING_KEY的值
//      				        Log.d("SP", sp.getString("userid", "none"));
//      				       
//
//      				//如果NOT_EXIST不存在，则返回值为"none"
//      				        Log.d("SP", sp.getString("NOT_EXIST", "none"));
      						
      						finish();
      					}
      					else 
      						if(userproject.equals("5012000"))//市配项目
      						//测试环境5024001,生产环境5012000
      						{
      							sp.edit().putString(cardNumStr, passwordStr).commit();
          						Intent showNextPage_Intent=new Intent();
          						showNextPage_Intent.setClass(MainActivity.this, SHHomeActivity.class);
          						startActivity(showNextPage_Intent);
          						myApp.setNum(userid);
          						finish();
      						} 
      							else 
      						{
        			Toast.makeText(MainActivity.this, "您输入的用户名密码有误",
								Toast.LENGTH_SHORT).show();	
      						}
        		}
        		
			}

        });
        
 
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
