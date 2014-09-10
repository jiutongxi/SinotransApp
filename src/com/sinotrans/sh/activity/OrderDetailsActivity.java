package com.sinotrans.sh.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sinotrans.samsung.activity.R;
import com.sinotrans.sh.service.SyncHttp;

/**
 *@author zhanglong
 *@date 2012-3-19
 *@blog 
 */
public class OrderDetailsActivity extends Activity
{
	private final int FINISH = 0;

	private ViewFlipper mNewsBodyFlipper;
	private LayoutInflater mNewsBodyInflater;
	private float mStartX;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private int mPosition = 0;
	private int mCursor;
	private int mNid;
	private TextView mNewsDetails;
	private Button mNewsdetailsTitlebarComm;// 新闻回复数
    private String orderCode;	
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.arg1)
			{
			case FINISH:
				// 把获取到的订单显示到界面上
				mNewsDetails.setText(Html.fromHtml(msg.obj.toString()));
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_orderdetails);

		NewsDetailsTitleBarOnClickListener newsDetailsTitleBarOnClickListener = new NewsDetailsTitleBarOnClickListener();
		// 上一订单
	//	Button newsDetailsTitlebarPref = (Button) findViewById(R.id.newsdetails_titlebar_previous);
   //		newsDetailsTitlebarPref.setOnClickListener(newsDetailsTitleBarOnClickListener);
		// 下一订单
	//	Button newsDetailsTitlebarNext = (Button) findViewById(R.id.newsdetails_titlebar_next);
	//	newsDetailsTitlebarNext.setOnClickListener(newsDetailsTitleBarOnClickListener);
		//订单回复Button
		mNewsdetailsTitlebarComm = (Button) findViewById(R.id.newsdetails_titlebar_comments);
		mNewsdetailsTitlebarComm.setOnClickListener(newsDetailsTitleBarOnClickListener);
		
		// 获取传递的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 设置标题栏名称
		String categoryName = bundle.getString("categoryName");
		TextView titleBarTitle = (TextView) findViewById(R.id.newsdetails_titlebar_title);
		titleBarTitle.setText(categoryName);
		// 获取订单集合
		Serializable s = bundle.getSerializable("newsDate");
		mNewsData = (ArrayList<HashMap<String, Object>>) s;
		// 获取点击位置
		mCursor = mPosition = bundle.getInt("position");

		// 动态创建新闻视图，并赋值
		mNewsBodyInflater = getLayoutInflater();
		inflateView(0);
	}

	/**
	 * 处理NewsDetailsTitleBar点击事件
	 */
	class NewsDetailsTitleBarOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			// 上一订单
			case R.id.newsdetails_titlebar_previous:
				showPrevious();
				break;
			// 下一订单
			case R.id.newsdetails_titlebar_next:
				showNext();
				break;
				// 拍照
			case R.id.newsdetails_titlebar_comments:
				Intent intent = new Intent(OrderDetailsActivity.this, MainActivity.class);
		    	intent.putExtra("orderCode",orderCode);
				startActivity(intent);
				break;
			}

		}
	}

	/**
	 * 处理订单NewsBody触摸事件
	 */
	class NewsBodyOnTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			switch (event.getAction())
			{
			// 手指按下
			case MotionEvent.ACTION_DOWN:
				// 记录起始坐标
				mStartX = event.getX();
				break;
			// 手指抬起
			case MotionEvent.ACTION_UP:
				// 往左滑动
				if (event.getX() < mStartX)
				{
					showPrevious();
				}
				// 往右滑动
				else if (event.getX() > mStartX)
				{
					showNext();
				}
				break;
			}
			return true;
		}
	}

	/**
	 * 显示下一订单
	 */
	private void showNext()
	{
		//判断是否是最后一篇win问
		if (mPosition<mNewsData.size()-1)
		{
			// 设置下一屏动画
			mNewsBodyFlipper.setInAnimation(this, R.anim.push_left_in);
			mNewsBodyFlipper.setOutAnimation(this, R.anim.push_left_out);
			mPosition++;
			//判断下一屏是否已经创建
			if (mPosition >= mNewsBodyFlipper.getChildCount())
			{
				inflateView(mNewsBodyFlipper.getChildCount());
			}
			// 显示下一屏
			mNewsBodyFlipper.showNext();
		}
		else
		{
			Toast.makeText(this, R.string.no_next_news, Toast.LENGTH_SHORT).show();
		}
		System.out.println(mCursor +";"+mPosition);
	}

	private void showPrevious()
	{
		if (mPosition>0)
		{
			mPosition--;
			//记录当前订单编号
			HashMap<String, Object> hashMap = mNewsData.get(mPosition);
			mNid = (Integer) hashMap.get("orderlist_item_orderId");
			if (mCursor>mPosition)
			{
				mCursor = mPosition;
				inflateView(0);
				System.out.println(mNewsBodyFlipper.getChildCount());
				mNewsBodyFlipper.showNext();// 显示下一页
			}
			mNewsBodyFlipper.setInAnimation(this, R.anim.push_right_in);// 定义下一页进来时的动画
			mNewsBodyFlipper.setOutAnimation(this, R.anim.push_right_out);// 定义当前页出去的动画
			mNewsBodyFlipper.showPrevious();// 显示上一页
		}
		else
		{
			Toast.makeText(this, R.string.no_pre_news, Toast.LENGTH_SHORT).show();
		}
		System.out.println(mCursor +";"+mPosition);
	}

	private void inflateView(int index)
	{
		// 动态创建订单视图，并赋值
		View newsBodyLayout = mNewsBodyInflater.inflate(R.layout.sh_orders_body, null);
		// 获取点击新闻基本信息
		HashMap<String, Object> hashMap = mNewsData.get(mPosition);
		// 订单编号
		TextView newsTitle = (TextView) newsBodyLayout.findViewById(R.id.news_body_title);
		orderCode=hashMap.get("orderlist_item_orderCode").toString();
		newsTitle.setText("订单编号："+orderCode);
		// 发布时间和出处
		TextView newsPtimeAndSource = (TextView) newsBodyLayout.findViewById(R.id.news_body_ptime_source);
		newsPtimeAndSource.setText("配送日期"+hashMap.get("orderlist_item_distributionDate").toString() + "    ");
		//收货人
		TextView order_body_receiverName = (TextView) newsBodyLayout.findViewById(R.id.order_body_receiverName);
		order_body_receiverName.setText("收货人："+ hashMap.get("orderlist_item_receiverName").toString());

		// 订单编号
		//mNid = (Integer) hashMap.get("nid");
		// 订单总数量
		//mNewsdetailsTitlebarComm.setText("订单总数"+hashMap.get("newslist_item_comments"));
		//项目名称
		TextView order_body_projectName = (TextView) newsBodyLayout.findViewById(R.id.order_body_projectName);
		order_body_projectName.setText("项目名称："+ hashMap.get("orderlist_item_projectName").toString());
		//司机
		TextView order_body_driverName = (TextView) newsBodyLayout.findViewById(R.id.order_body_driverName);
		order_body_driverName.setText("司机名称："+ hashMap.get("orderlist_item_driverName").toString());	
		//车牌号
		TextView order_body_carCode = (TextView) newsBodyLayout.findViewById(R.id.order_body_carCode);
		order_body_carCode.setText("车牌号码："+ hashMap.get("orderlist_item_carCode").toString());		
		//客户订单号
			
		//交货地址
		TextView order_body_receiveAdress = (TextView) newsBodyLayout.findViewById(R.id.order_body_receiveAdress);
		order_body_receiveAdress.setText("交货地址："+ hashMap.get("orderlist_item_receiveAdress").toString());	

		//数量
		//TextView order_body_receiveNum = (TextView) newsBodyLayout.findViewById(R.id.order_body_receiveNum);
		//order_body_receiveNum.setText("数量："+ hashMap.get("orderlist_item_receiveNum").toString());	

		
		//体积
		TextView order_body_receiveVolume = (TextView) newsBodyLayout.findViewById(R.id.order_body_receiveVolume);
		order_body_receiveVolume.setText("体积："+ hashMap.get("orderlist_item_receiveVolume").toString());	

		
		//重量
		TextView order_body_receiveWeight = (TextView) newsBodyLayout.findViewById(R.id.order_body_receiveWeight);
		order_body_receiveWeight.setText("重量："+ hashMap.get("orderlist_item_receiveWeight").toString());	

		
		// 把新闻视图添加到Flipper中
		mNewsBodyFlipper = (ViewFlipper) findViewById(R.id.news_body_flipper);
		mNewsBodyFlipper.addView(newsBodyLayout,index);
	
		// 给订单Body添加触摸事件
		mNewsDetails = (TextView) newsBodyLayout.findViewById(R.id.news_body_details);
		mNewsDetails.setOnTouchListener(new NewsBodyOnTouchListener());
	
		// 启动线程
		//new UpdateNewsThread().start();
	}

	private class UpdateNewsThread extends Thread
	{
		@Override
		public void run()
		{
			// 从网络上获取新闻
			String newsBody = getNewsBody();
			Message msg = mHandler.obtainMessage();
			msg.arg1 = FINISH;
			msg.obj = newsBody;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 获取订单详细信息
	 * 
	 * @return
	 */
	private String getNewsBody()
	{
		String retStr = "网络连接失败，请稍后再试";
		SyncHttp syncHttp = new SyncHttp();
		//String url = "http://192.168.3.167:8080/web/getNews";
		String url = "http://172.168.123.1:8080/web/getNews";
		String params = "nid=" + mNid;
		try
		{
			String retString = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retString);
			// 获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			if (0 == retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				JSONObject newsObject = dataObject.getJSONObject("news");
				retStr = newsObject.getString("body");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return retStr;
	}
}
