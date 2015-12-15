package com.buaa.sensory.wsn_dr.fragment;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.buaa.sensory.wsn_dr.MyApplication;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.activity.DataHelpActivity;
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.model.ResponseMoveMsg;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.buaa.sensory.wsn_dr.view.TopoView;
import com.google.gson.Gson;

public class FragmentTopo extends Fragment implements OnClickListener{
	private ImageView but_help_topo;
	private TopoView topo_view;
	private Spinner spinner,spinnerM;
	private ToggleButton toggleButton;
	
	// 更新时间
	private long st = 0;
	// 停止参数
	boolean run = true;
	
	int move = 1;
	int type = 0;
	
	int[] ids ={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_topo, null);
		but_help_topo = (ImageView) view.findViewById(R.id.but_help_topo);
		but_help_topo.setOnClickListener(this);
		toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton1);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				toggleButton.setChecked(isChecked);
				type = isChecked?1:0;
				try {
					changeType(move, type);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		spinner = (Spinner) view.findViewById(R.id.spinner_topo);
		topo_view = (TopoView) view.findViewById(R.id.topo_view);
		spinnerM = (Spinner) view.findViewById(R.id.spinner_move);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
					st =0;
					run =false;
					handler_timer.removeCallbacks(task);
					//清除
					topo_view.setNodes_topo(null);
					topo_view.invalidate();
				}else if(position == 6){
					run = true;
					st = 3600*24*30*12*30;
					handler_timer.removeCallbacks(task);
					try {
						requestTopoData(st, ids);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					switch (position) {
					case 1:
						st=5;
						break;
					case 2:
						st =10;
						break;
					case 3:
						st = 20;
						break;
					case 4:
						st = 30;
						break;
					case 5:
						st = 60;
						break;
					default:
						break;
					}
					run=true;
					handler_timer.removeCallbacks(task);
					handler_timer.post(task);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		spinnerM.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				move = position+1;
				try {
					requestType(move);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});

		return view;
	}

	private Handler handler_timer = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			if (run) {
				try {
					requestTopoData(st,ids);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler_timer.postDelayed(this, st * 1000);// 启动之后每次的延时
				Log.v("Tag","刷一次"+" st="+st);
			}

		}
	};


	@Override
	public void onResume() {
		super.onResume();
		if(st!=0){
			run = true;
			handler_timer.post(task);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		run = false;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_help_topo:
			startActivity(new Intent(getActivity(), DataHelpActivity.class));
			break;

		default:
			break;
		}

	}

	/**
	 * 改变某节点的移动状态
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void changeType(final int move,final int type) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.CHANGETYPE_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI线程处理数据
						Log.v("Tag", "changeType:"+response);
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
				map.put("nodeId",String.valueOf(move));
				map.put("type",String.valueOf(type));
				return map;
			}
		};
		stringRequest.setTag("changeType");
		MyApplication.getHttpQueues().add(stringRequest);
	}
	/**
	 * 获取某节点的移动状态
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void requestType(final int move) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.TYPE_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v("Tag", ""+response);
						Gson gson = new Gson();
						ResponseMoveMsg responseMoveMsg = gson.fromJson(response, ResponseMoveMsg.class);
						toggleButton.setChecked(responseMoveMsg.getData().getType()==0?false:true);
						Log.v("Tag", "改变了Checked");
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
				map.put("nodeId",String.valueOf(move));
				return map;
			}
		};
		stringRequest.setTag("requestType");
		MyApplication.getHttpQueues().add(stringRequest);
	}
	/**
	 * 获取网络拓扑数据
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void requestTopoData(final long st, final int[] nodeId) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.NEWDATA_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI线程处理数据
						Gson gson = new Gson();
						//Json解析数据
						ResponseMsg responseMsg = gson.fromJson(response, ResponseMsg.class);
						try {
							//清除
							topo_view.setNodes_topo(null);
							topo_view.invalidate();
							// 重绘
							topo_view.setNodes_topo(responseMsg.getData());
							topo_view.invalidate();
						} catch (Exception e) {
							e.printStackTrace();
						}
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
				map.put("nodeIds",new String(sb));
				map.put("startTime", String.valueOf(System.currentTimeMillis()-st*1000));
				return map;
			}
		};
		stringRequest.setTag("requestAllNewData");
		MyApplication.getHttpQueues().add(stringRequest);
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
