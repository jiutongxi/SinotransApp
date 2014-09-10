package com.sinotrans.sh.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sinotrans.samsung.activity.R;
import com.sinotrans.sh.adapter.CustomSimpleAdapter;
import com.sinotrans.sh.model.Category;
import com.sinotrans.sh.service.SyncHttp;
import com.sinotrans.sh.util.DensityUtil;
import com.sinotrans.sh.util.StringUtil;

public class OrdersActivity extends Activity
{
	private static final String NAMESPACE = "http://webservice.shanghai.commission.oms.sinotrans.com";  
	//private static String URL="http://172.20.42.233:8080/OMS_TEST/services/Orderservice";  
	private static String URL="http://sinotrans.wicp.net/OMS_TEST/services/Orderservice";  
	//private static String URL="http://192.168.3.154:8080/OMS_TEST/services/Orderservice";  

	private static final String METHOD_UPLOAD = "getOrders";
	private static final String tag = "TEST";  
	private final int COLUMNWIDTHPX = 755;
	private final int FLINGVELOCITYPX = 800; // 滚动距离
	private final int NEWSCOUNT = 5; //返回订单数目
	private final int SUCCESS = 0;//加载成功
	private final int NONEWS = 1;//该栏目下没有订单
	private final int NOMORENEWS = 2;//该栏目下没有更多订单
	private final int LOADERROR = 3;//加载失败
	private final int PAGECOUNT=5;
	
	private int mColumnWidthDip;
	private int mFlingVelocityDip;
	private int mCid;
	private String status="ADD";
	private String mCatName;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private ListView mOrdersList;
	private SimpleAdapter mOrdersListAdapter;
	private LayoutInflater mInflater;
	private Button mTitlebarRefresh;
	private ProgressBar mLoadnewsProgress;
	private Button mLoadMoreBtn;
	//获取手机屏幕分辨率的类
   private DisplayMetrics dm;
	
	private LoadOrdersAsyncTask loadOrdersAsyncTask;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_main);
	
		mInflater = getLayoutInflater();
		mNewsData = new ArrayList<HashMap<String,Object>>();
		mOrdersList = (ListView)findViewById(R.id.newslist);
		mTitlebarRefresh = (Button)findViewById(R.id.titlebar_refresh);
		mLoadnewsProgress = (ProgressBar)findViewById(R.id.loadnews_progress);
		mTitlebarRefresh.setOnClickListener(loadMoreListener);
	       dm = new DisplayMetrics();
	       getWindowManager().getDefaultDisplay().getMetrics(dm);
	       	                  //获得手机的宽度和高度像素单位为px
	       	//          String strPM = "手机屏幕分辨率为:" + dm.widthPixels+"* "+dm.heightPixels;
	       		//把px转换成dip
	       		mColumnWidthDip = DensityUtil.px2dip(this,  dm.widthPixels);
	       		mFlingVelocityDip = DensityUtil.px2dip(this, dm.heightPixels);
	       	       
		//把px转换成dip
		//mColumnWidthDip = DensityUtil.px2dip(this, COLUMNWIDTHPX);
		//mFlingVelocityDip = DensityUtil.px2dip(this, FLINGVELOCITYPX);
		
		//获取订单分类
		String[] categoryArray = getResources().getStringArray(R.array.categories);
		//把订单分类保存到List中
		final List<HashMap<String, Category>> categories = new ArrayList<HashMap<String, Category>>();
		//分割订单类型字符串
		for(int i=0;i<categoryArray.length;i++)
		{
			String[] temp = categoryArray[i].split("[|]");
			if (temp.length==2)
			{
				//int cid = StringUtil.String2Int(temp[0]);
				String status=temp[0];
				String title = temp[1];
				//Category type = new Category(cid, title);
				Category type = new Category(status, title);
				HashMap<String, Category> hashMap = new HashMap<String, Category>();
				hashMap.put("category_title", type);
				categories.add(hashMap);
			}
		}
		//默认选中的订单分类
		//mCid = 1;
		status="ADD";
		mCatName ="未处理";
		//创建Adapter，指明映射字段
		CustomSimpleAdapter categoryAdapter = new CustomSimpleAdapter(this, categories, R.layout.sh_category_title, new String[]{"category_title"}, new int[]{R.id.category_title});
		
		GridView category = new GridView(this);
		category.setColumnWidth(mColumnWidthDip);//每个单元格宽度
		category.setNumColumns(2);//单元格数目//GridView.AUTO_FIT
		category.setGravity(Gravity.CENTER);//设置对其方式
		//设置单元格选择是背景色为透明，这样选择时就不现实黄色背景
		category.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//根据单元格宽度和数目计算总宽度
		//int width = mColumnWidthDip * categories.size();
		int width = (int) (mColumnWidthDip * 1.5);
		LayoutParams params = new LayoutParams(width, LayoutParams.FILL_PARENT);
		//更新category宽度和高度，这样category在一行显示
		category.setLayoutParams(params);
		//设置适配器
		category.setAdapter(categoryAdapter);
		//把category加入到容器中
		LinearLayout categoryList = (LinearLayout) findViewById(R.id.category_layout);
		categoryList.addView(category);
		//添加单元格点击事件
		category.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				TextView categoryTitle;
				//恢复每个单元格背景色
				for (int i = 0; i < parent.getChildCount(); i++)
				{
					categoryTitle = (TextView) (parent.getChildAt(i));
					categoryTitle.setBackgroundDrawable(null);
					categoryTitle.setTextColor(0XFFADB2AD);
				}
				//设置选择单元格的背景色
				categoryTitle = (TextView) (parent.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
				categoryTitle.setTextColor(0XFFFFFFFF);
				//获取选中的订单分类id
				status = categories.get(position).get("category_title").getStatus();
				mCatName = categories.get(position).get("category_title").getTitle();
				//获取该栏目下订单
				//getSpeCateNews(mCid,mNewsData,0,true);
				//通知ListView进行更新
				//mNewsListAdapter.notifyDataSetChanged();
				loadOrdersAsyncTask = new LoadOrdersAsyncTask();
				loadOrdersAsyncTask.execute(status,0,true);
			}
		});
		
		// 箭头
		final HorizontalScrollView categoryScrollview = (HorizontalScrollView) findViewById(R.id.category_scrollview);
		Button categoryArrowRight = (Button) findViewById(R.id.category_arrow_right);
		categoryArrowRight.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				categoryScrollview.fling(DensityUtil.px2dip(OrdersActivity.this, mFlingVelocityDip));
			}
		});
		//获取指定栏目的订单列表
		//getSpeCateNews(mCid,mNewsData,0,true);
		mOrdersListAdapter = new SimpleAdapter(this, mNewsData, R.layout.sh_orderslist_item,new String[]{"orderlist_item_orderCode","orderlist_item_orderId","orderlist_item_customerOrderCode","orderlist_item_projectId","orderlist_item_projectCode","orderlist_item_projectName","orderlist_item_receiverCompany","orderlist_item_receiverName","orderlist_item_receiverTel","orderlist_item_receiveAdress","orderlist_item_distributionDate","orderlist_item_driverId","orderlist_item_driverName","orderlist_item_driverCode","orderlist_item_driverTel","orderlist_item_receiveWeight","orderlist_item_receiveVolume","orderlist_item_receiveQuantity","orderlist_item_legsId","orderlist_item_legsCode","orderlist_item_carCode","orderlist_item_status"},					
				                           new int[]{R.id.orderlist_item_orderCode,R.id.orderlist_item_orderId,R.id.orderlist_item_customerOrderCode,R.id.orderlist_item_projectId,R.id.orderlist_item_projectCode,R.id.orderlist_item_projectName,R.id.orderlist_item_receiverCompany,R.id.orderlist_item_receiverName,R.id.orderlist_item_receiverTel,R.id.orderlist_item_receiveAdress,R.id.orderlist_item_distributionDate,R.id.orderlist_item_driverId,R.id.orderlist_item_driverName,R.id.orderlist_item_driverCode,R.id.orderlist_item_driverTel,R.id.orderlist_item_receiveWeight,R.id.orderlist_item_receiveVolume,R.id.orderlist_item_receiveQuantity,R.id.orderlist_item_legsId,R.id.orderlist_item_legsCode,R.id.orderlist_item_carCode,R.id.orderlist_item_status});
		View loadMoreLayout = mInflater.inflate(R.layout.sh_loadmore, null);
		mOrdersList.addFooterView(loadMoreLayout);
		mOrdersList.setAdapter(mOrdersListAdapter);
		mOrdersList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
				//把需要的信息放到Intent中
				intent.putExtra("newsDate", mNewsData);
				intent.putExtra("position", position);
				intent.putExtra("categoryName", mCatName);
				startActivity(intent);
			}
		});
		
		mLoadMoreBtn = (Button)findViewById(R.id.loadmore_btn);
		mLoadMoreBtn.setOnClickListener(loadMoreListener);
		
		loadOrdersAsyncTask = new LoadOrdersAsyncTask();
		loadOrdersAsyncTask.execute(status,0,true);
	}
	
	/**
	 * 获取指定状态的订单列表
	 * @param cid 类型ID
	 * @param newsList 保存订单信息的集合
	 * @param startnid 分页
	 * @param firstTimes	是否第一次加载
	 */
	private int getSpeCateNews(String status,List<HashMap<String, Object>> newsList,int startnid,Boolean firstTimes)
	{
		if (firstTimes)
		{
			//如果是第一次，则清空集合里数据
			newsList.clear();
		}
		//请求URL和字符串
		String url="http://192.168.3.154:8080/FileUploadServerDemo/servlet/OrdersServlet";
		String params = "startnid="+startnid+"&count="+NEWSCOUNT+"&status="+status;
	
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			//以Get方式请求，并获得返回结果
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			//获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			if (0==retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				//获取返回数目
				int totalnum = dataObject.getInt("totalnum");
				if (totalnum>0)
				{
					//获取返回订单集合
					JSONArray newslist = dataObject.getJSONArray("newslist");
					for(int i=0;i<newslist.length();i++)
					{
						JSONObject newsObject = (JSONObject)newslist.opt(i); 
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("nid", newsObject.getInt("nid"));
						    hashMap.put("orderlist_item_orderId", newsObject.getString("orderId"));
							hashMap.put("orderlist_item_orderCode", newsObject.getString("orderCode"));	
							hashMap.put("orderlist_item_projectCode", newsObject.getString("projectCode"));	
							hashMap.put("orderlist_item_projectName", newsObject.getString("projectName"));	
							hashMap.put("orderlist_item_receiverName", newsObject.getString("receiverName"));	
							hashMap.put("orderlist_item_distributionDate", newsObject.getString("distributionDate"));
							//hashMap.put("newslist_item_comments", newsObject.getString("commentcount"));
							hashMap.put("orderlist_item_uuids", newsObject.getString("uuid"));	
							hashMap.put("orderlist_item_receiveAdress", newsObject.getString("receiveAdress"));	
							hashMap.put("orderlist_item_driverId", newsObject.getString("driverId"));	
							hashMap.put("orderlist_item_driverName", newsObject.getString("driverName"));	
							hashMap.put("orderlist_item_driverCode", newsObject.getString("driverCode"));	
							hashMap.put("orderlist_item_carId", newsObject.getString("carId"));	
							hashMap.put("orderlist_item_carCode", newsObject.getString("carCode"));	
							hashMap.put("orderlist_item_receiveNum",newsObject.getString("receiveNum"));	
							hashMap.put("orderlist_item_receiveVolume",newsObject.getString("receiveVolume"));	
							hashMap.put("orderlist_item_receiveWeight",newsObject.getString("receiveWeight"));	
						newsList.add(hashMap);
					}
					return SUCCESS;
				}
				else
				{
					if (firstTimes)
					{
						return NONEWS;
					}
					else
					{
						return NOMORENEWS;
					}
				}
			}
			else
			{
				return LOADERROR;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return LOADERROR;
		}
	}
	
	private OnClickListener loadMoreListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			loadOrdersAsyncTask = new LoadOrdersAsyncTask();
			switch (v.getId())
			{
			case R.id.loadmore_btn:
				//获取该栏目下订单
				//getSpeCateNews(mCid,mNewsData,mNewsData.size(),false);
				//通知ListView进行更新
				//mNewsListAdapter.notifyDataSetChanged();
				loadOrdersAsyncTask.execute(status,mNewsData.size(),false);
				break;
			case R.id.titlebar_refresh:
				loadOrdersAsyncTask.execute(status,0,true);
				break;
			}
			
		}
	};
	
	private class LoadOrdersAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			//隐藏刷新按钮
			mTitlebarRefresh.setVisibility(View.GONE);
			//显示进度条
			mLoadnewsProgress.setVisibility(View.VISIBLE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText(R.string.loadmore_txt);
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			String username="18601969293";
			Integer aInteger=0;
			//return getSpeCateNews((String)params[0],mNewsData,(Integer)params[1],(Boolean)params[2]);
		    try {
		    	aInteger= getOrders((String)params[0],mNewsData,(Integer)params[1],(Boolean)params[2],username);
			} catch (SoapFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return aInteger;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//根据返回值显示相关的Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(OrdersActivity.this, R.string.no_news, Toast.LENGTH_LONG).show();
			break;
			case NOMORENEWS:
				Toast.makeText(OrdersActivity.this, R.string.no_more_news, Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(OrdersActivity.this, R.string.load_news_failure, Toast.LENGTH_LONG).show();
				break;
			}
			mOrdersListAdapter.notifyDataSetChanged();
			//显示刷新按钮
			mTitlebarRefresh.setVisibility(View.VISIBLE);
			//隐藏进度条
			mLoadnewsProgress.setVisibility(View.GONE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText(R.string.loadmore_btn);
		}
	}
	  public  int getOrders(String status,List<HashMap<String, Object>> ordersList,int startnid,Boolean firstTimes,String username) throws SoapFault
	  {
		  
			if (firstTimes)
			{
				//如果是第一次，则清空集合里数据
				ordersList.clear();
			}
		      String startNum=String.valueOf(startnid);
		      String pagecount= String.valueOf(PAGECOUNT);
			  SoapObject rpc = new SoapObject(NAMESPACE, METHOD_UPLOAD);
				rpc.addProperty("status", status);  
				rpc.addProperty("start",startNum);  
				rpc.addProperty("count",pagecount);
				rpc.addProperty("username",username);
				HttpTransportSE ht = new HttpTransportSE(URL); 
				ht.debug = true; 
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
				envelope.bodyOut = rpc;  
				envelope.dotNet = false;  
				envelope.encodingStyle = "UTF-8";
				envelope.setOutputSoapObject(rpc);  
				new MarshalBase64().register(envelope);
				try {
					ht.call(null, envelope);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
		
				Object result;
					result =  (Object) envelope.getResponse();
					String testString=result.toString();
					testString=StringUtil.JSONTokener(testString);
						//获取返回订单集合
						try {
							JSONObject jsonObject = new JSONObject(testString);
							
							JSONObject dataObject = jsonObject.getJSONObject("data");
							//获取返回数目
							int totalnum = dataObject.getInt("totalnum");
							//获取返回码，0表示成功
							int retCode = jsonObject.getInt("ret");
							if (0!=retCode){
								return	LOADERROR;
							}
							if (totalnum>0)
							{
								//获取返回订单集合
								JSONArray newslist = dataObject.getJSONArray("orderlist");
								for(int i=0;i<newslist.length();i++)
								{
									JSONObject newsObject = (JSONObject)newslist.opt(i); 
									HashMap<String, Object> hashMap = new HashMap<String, Object>();
								     	hashMap.put("orderlist_item_orderCode",newsObject.getString("orderCode"));	
								     	hashMap.put("orderlist_item_orderId",newsObject.getString("orderId"));	
								     	hashMap.put("orderlist_item_customerOrderCode", newsObject.getString("customerOrderCode"));	
								    	hashMap.put("orderlist_item_projectId", newsObject.getString("projectId"));	
										hashMap.put("orderlist_item_projectCode", newsObject.getString("projectCode"));	
										hashMap.put("orderlist_item_projectName", newsObject.getString("projectName"));	
										hashMap.put("orderlist_item_receiverCompany", newsObject.getString("receiverCompany"));	
										hashMap.put("orderlist_item_receiverName", newsObject.getString("receiverName"));	
										hashMap.put("orderlist_item_receiverTel", newsObject.getString("receiverTel"));	
										hashMap.put("orderlist_item_receiveAdress", newsObject.getString("receiveAdress"));	
										hashMap.put("orderlist_item_distributionDate", newsObject.getString("distributionDate"));
										hashMap.put("orderlist_item_driverId", newsObject.getString("driverId"));	
										hashMap.put("orderlist_item_driverName", newsObject.getString("driverName"));	
										hashMap.put("orderlist_item_driverCode", newsObject.getString("driverCode"));	
										hashMap.put("orderlist_item_driverTel", newsObject.getString("driverTel"));	
										hashMap.put("orderlist_item_receiveWeight",newsObject.getString("receiveWeight"));	
										hashMap.put("orderlist_item_receiveVolume",newsObject.getString("receiveVolume"));	
										hashMap.put("orderlist_item_receiveQuantity",newsObject.getString("receiveQuantity"));	
										hashMap.put("orderlist_item_legsId",newsObject.getString("legsId"));	
										hashMap.put("orderlist_item_legsCode",newsObject.getString("legsCode"));	
										hashMap.put("orderlist_item_carCode", newsObject.getString("carCode"));	
										hashMap.put("status", newsObject.getString("status"));
										ordersList.add(hashMap);
								}
								return SUCCESS;
							}else
							{
								if (firstTimes)
								{
									return NONEWS;
								}
								else
								{
									return NOMORENEWS;
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							return	LOADERROR;
						}
	  }	 
	
}