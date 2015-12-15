package com.buaa.sensory.wsn_dr.activity;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.buaa.sensory.wsn_dr.MyApplication;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.buaa.sensory.wsn_dr.view.HisLineView;
import com.google.gson.Gson;

public class HisLineActivity extends Activity implements OnClickListener{
	HisLineView hisLineView;
	TextView back;
	private Spinner spinnerV,spinnerT,spinnerU;
	private int time = 300*1000;
	long currentTime=1449066665000L;
	int updateTime;
	int[] nodeId = new int[1];
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hisline);
		back = (TextView) findViewById(R.id.back_hisline);
		back.setOnClickListener(this);
		Intent intent = getIntent();
		nodeId[0]=intent.getIntExtra("nodeId",0);
		currentTime = System.currentTimeMillis();
		hisLineView = (HisLineView) findViewById(R.id.hislineview);
		hisLineView.setCurrentTime(currentTime);
		spinnerV= (Spinner) findViewById(R.id.spinner_variate_hisline);
		spinnerT = (Spinner) findViewById(R.id.spinner_time_hisline);
		spinnerU = (Spinner) findViewById(R.id.spinner_update_hisline);

		spinnerV.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					hisLineView.setType(0);
					hisLineView.setMaxValue(50);
					hisLineView.setMinValue(0);
					break;
				case 1:
					hisLineView.setType(1);
					hisLineView.setMaxValue(100);
					hisLineView.setMinValue(0);
					break;
				case 2:
					hisLineView.setType(2);
					hisLineView.setMaxValue(50000);
					hisLineView.setMinValue(0);
					break;
				default:
					break;
				}
				hisLineView.invalidate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		spinnerT.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					time = 60*1000;
					break;
				case 1:
					time = 300*1000;
					break;
				case 2:
					time = 10*60*1000;
					break;
				case 3:
					time = 30*60*1000;
					break;

				default:
					break;
				}
				try {
					requestHisData(String.valueOf(currentTime-time), String.valueOf(currentTime), nodeId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinnerT.setSelection(1);
		
		spinnerU.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					updateTime=1000;
					break;
				case 1:
					updateTime=2000;
					break;
				case 2:
					updateTime=5000;
					break;
				case 3:
					updateTime=10000;
					break;
				case 4:
					updateTime=30000;
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		handler_timer.post(task);
	
	}
	
	private Handler handler_timer = new Handler();
	private Runnable task = new Runnable() {
		public void run() {

			
			try {
				requestHisData(String.valueOf(currentTime-time), String.valueOf(currentTime), nodeId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler_timer.postDelayed(this, updateTime);// 启动之后每次的延时
			Log.v("Tag","刷一次"+" st="+updateTime);

		}
	};
	
	@Override
	protected void onDestroy() {
		handler_timer.removeCallbacks(task);
		super.onDestroy();
	}

	/**
	 * 获取指定节点的历史数据
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void requestHisData(final String st,final String et, final int[] nodeId) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.HISDATA_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI线程处理数据
						Gson gson = new Gson();
						ResponseMsg responseMsg = gson.fromJson(response, ResponseMsg.class);
						Log.v("TAG", responseMsg.getData().toString());
						currentTime = System.currentTimeMillis();
						hisLineView = (HisLineView) findViewById(R.id.hislineview);
						hisLineView.setCurrentTime(currentTime);
						hisLineView.setList(responseMsg.getData());
						hisLineView.setAllTime(time);
						hisLineView.invalidate();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> map = new HashMap<String, String>();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < nodeId.length; i++) {
					if(i!=0){
						sb.append("#"+nodeId[i]);
					}else{
						sb.append(nodeId[i]);
					}
				}
				map.put("nodeId",new String(sb));
				map.put("startTime", st);
				map.put("endTime",et);
				return map;
			}
		};
		stringRequest.setTag("requestHisData");
		MyApplication.getHttpQueues().add(stringRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_hisline:
			finish();
			break;

		default:
			break;
		}
		
	}

}
