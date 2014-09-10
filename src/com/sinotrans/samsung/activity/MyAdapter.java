package com.sinotrans.samsung.activity;

import java.util.ArrayList;

import com.sinotrans.samsung.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


	
	 public class MyAdapter extends BaseAdapter {  
		   
	         @SuppressWarnings("unused")
			private Context context;  
	          private LayoutInflater inflater;  
	         public ArrayList<String> arr;  
	         public MyAdapter(Context context) {  
	             super();  
	             this.context = context;  
	              inflater = LayoutInflater.from(context);  
	             arr = new ArrayList<String>();  
	             
	             }
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return arr.size();  
			}
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
public View getView(final int position, View view, ViewGroup arg2) 
			{
				// TODO Auto-generated method stub
				if(view == null)
					{
						view = inflater.inflate(R.layout.list_view, null);  
					 }  
				final TextView donum = (TextView) view.findViewById(R.id.ariveexceptin);  
				donum.setText(arr.get(position));  
				
			//在重构adapter的时候不至于数据错乱  position
				 Button del   = (Button) view.findViewById(R.id.del);  
				 donum.setOnFocusChangeListener(new OnFocusChangeListener() 
					 {
						 public void onFocusChange(View v, boolean hasFocus) 
						 	{  
					  // TODO Auto-generated method stub  
							 	if(arr.size()>0)
							 		{  
							 		arr.set(position, donum.getText().toString());  
					                 }  
					                }  
					          });  

				 del.setOnClickListener(new OnClickListener() 
				 {  
			               @Override  
			              public void onClick(View view)
			             {  
			                  // TODO Auto-generated method stub  
			                  //从集合中删除所删除项的EditText的内容  
			                   arr.remove(position);  
			                   MyAdapter.this.notifyDataSetChanged();  
			               }  
		           });  
				 return view;

			}  
		
	 
	 } 



