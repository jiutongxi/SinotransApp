package com.sinotrans.sh.activity;



/**
 * 
 * @author zhanglong
 *
 */
public class LoginActivity /*extends Activity  implements OnClickListener*/{/*
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button loginButton;
	private Button cancelButton;
	private CheckBox rememberPasswordCheckBox;
	private CheckBox autologinCheckBox;
	private SharedPreferences sharedPreferences;
	private String userNameValue="";
	private String passWordValue="";

@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.sh_activity_login);
	setTitle("中外运照片上传系统");
	//获得实例
	sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
	usernameEditText=(EditText)findViewById(R.id.usernameEditText);
	passwordEditText=(EditText)findViewById(R.id.passwordEditText);
	loginButton=(Button)findViewById(R.id.loginButton);
	cancelButton=(Button)findViewById(R.id.cancelButton);
	rememberPasswordCheckBox = (CheckBox) findViewById(R.id.rememberPasswordCheckBox);  
	autologinCheckBox = (CheckBox) findViewById(R.id.autologinCheckBox); 
	
	loginButton.setOnClickListener(this);
	cancelButton.setOnClickListener(this);
	//详见StrictMode文档
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

	//判断记住密码多选框的状态
    if(sharedPreferences.getBoolean("ISCHECK", false))
      {
  	  //设置默认是记录密码状态
    	rememberPasswordCheckBox.setChecked(true);
    	usernameEditText.setText(sharedPreferences.getString("USERNAME", ""));
    	passwordEditText.setText(sharedPreferences.getString("PASSWORD", ""));
     	  //判断自动登陆多选框状态
     	  if(sharedPreferences.getBoolean("AUTO_ISCHECK", false))
     	  {
     	  //设置默认是自动登录状态
     	autologinCheckBox.setChecked(true);
    	if(validate()){
			if(login()){
				   //跳转界面
		     	Intent intent = new Intent(LoginActivity.this,OrdersActivity.class);
				LoginActivity.this.startActivity(intent);
			}else{
				Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
			}
	     	}
   	 
     	  }
      }
    
  //监听记住密码多选框按钮事件
    rememberPasswordCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    	public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
    		if (rememberPasswordCheckBox.isChecked()) {
    			userNameValue = usernameEditText.getText().toString();
    			passWordValue = passwordEditText.getText().toString();
    			if(login()){
    				sharedPreferences.edit().putBoolean("ISCHECK", true).commit();
    		}
    			
    		}else {
    			
    			sharedPreferences.edit().putBoolean("ISCHECK", false).commit();
    			
    		}

    	}
    });

    //监听自动登录多选框事件
    autologinCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
    		if (autologinCheckBox.isChecked()) {
    			userNameValue = usernameEditText.getText().toString();
    			passWordValue = passwordEditText.getText().toString();
    			if(login()){
    				sharedPreferences.edit().putBoolean("AUTO_ISCHECK", true).commit();
    		}
    	

    		} else {
    			sharedPreferences.edit().putBoolean("AUTO_ISCHECK", false).commit();
    		}
    	}
    });   
    

}
//为登陆和取消按钮响应点击事件处理
@Override
public void onClick(View view) {
	switch(view.getId()){
	case R.id.loginButton:
		if(validate()){
			if(login()){
				userNameValue = usernameEditText.getText().toString();
			    passWordValue = passwordEditText.getText().toString();
//			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//			startActivity(intent);
				Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
				//登录成功和记住密码框为选中状态才保存用户信息
				if(rememberPasswordCheckBox.isChecked())
				{
				 //记住用户名、密码、
				  Editor editor = sharedPreferences.edit();
				  editor.putString("USERNAME", userNameValue);
				  editor.putString("PASSWORD",passWordValue);
				  editor.commit();
				}
				//跳转界面
				Intent intent = new Intent(LoginActivity.this,OrdersActivity.class);
				LoginActivity.this.startActivity(intent);
			}else{
				Toast.makeText(LoginActivity.this,"用户名称或者密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
			}
		}
		break;
	case R.id.cancelButton:
		finish();
		break;
	default:
		break;
	
	}
}

// 验证方法
private boolean validate(){
	String username = usernameEditText.getText().toString();
	String pwd = passwordEditText.getText().toString();
	if(username.equals("")){
		showDialog("用户名称是必填项！");
		return false;
	}

	if(pwd.equals("")){
		showDialog("用户密码是必填项！");
		return false;
	}
	return true;
}
private void showDialog(String msg){
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(msg)
	       .setCancelable(false)
	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	       });
	AlertDialog alert = builder.create();
	alert.show();
}
// 登录方法
private boolean login(){

	String username = usernameEditText.getText().toString();
	String pwd = passwordEditText.getText().toString();
	// 获得登录结果
	String result=query(username,pwd);
	if((result!=null&&result.equals("0"))||result==null){
		return false;
	}else{
	//	saveUserMsg(result);
		return true;
	}

}
// 根据用户名称密码查询
private String query(String account,String password){
	// 查询参数
	String queryString = "account="+account+"&password="+password;
	// url
	String url = HttpUtil.BASE_URL+"servlet/LoginServlet?"+queryString;
	// 查询返回结果
	try {
		return HttpUtil.queryStringForPost(url);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}



*/}
