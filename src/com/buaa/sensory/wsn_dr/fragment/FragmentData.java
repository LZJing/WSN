package com.buaa.sensory.wsn_dr.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.buaa.sensory.wsn_dr.MyApplication;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.activity.DataHelpActivity;
import com.buaa.sensory.wsn_dr.activity.HisDataSetActivity;
import com.buaa.sensory.wsn_dr.activity.NewDataSetActivity;
import com.buaa.sensory.wsn_dr.adapter.MyAdapter;
import com.buaa.sensory.wsn_dr.entity.GetDataClass;
import com.buaa.sensory.wsn_dr.entity.NodeClass;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

public class FragmentData extends Fragment implements OnClickListener{
	
	
	
	private RadioGroup group;
	private View flyView;
	// 最新数据的显示控件
	private LinearLayout new_data;
	// 历史数据的显示控件
	private LinearLayout his_data;
	
	private ImageView but_help;
	private Button but_set;// 按键：设置条件
	private Button but_ok;// 按键：开始查询
	private Button but_set_his;// 按键：设置his条件
	private Button but_ok_his;// 按键：开始his查询
	@ViewInject(R.id.new_time)
	private TextView new_time;
	@ViewInject(R.id.new_id)
	private TextView new_id;
	@ViewInject(R.id.gv_new_data)
	private GridView gv_new_data;
	@ViewInject(R.id.his_time)
	private TextView his_time;
	@ViewInject(R.id.his_id)
	private TextView his_id;
	@ViewInject(R.id.gv_his_data)
	private GridView gv_his_data;

	// 查询参数
	String st, et;
	String nodeId = "";
	int[] nodeId_int, nodeId_int_his;
	// 查询结构存放类
	NodeClass[] nodes;
	NodeClass[] nodes_his;
	// 动画控件
	private Animation move_to_right, move_to_left;
	//
	private List<Map<String, Object>> mData;
	
	RequestQueue mQueue;  


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_data, container, false);
		initView(view);
		
		
		
		
		ViewUtils.inject(this, view);
		
		
		move_to_left = AnimationUtils.loadAnimation(getActivity(),R.anim.move_to_left);
		move_to_right = AnimationUtils.loadAnimation(getActivity(),R.anim.move_to_right);
		
		return view;
	}

	private void initView(View view) {
		but_set = (Button) view.findViewById(R.id.but_set);
		but_set.setOnClickListener(this);
		but_ok = (Button) view.findViewById(R.id.but_ok);
		but_ok.setOnClickListener(this);
		but_set_his = (Button) view.findViewById(R.id.but_set_his);
		but_set_his.setOnClickListener(this);
		but_ok_his =(Button) view.findViewById(R.id.but_ok_his);
		but_ok_his.setOnClickListener(this);
		but_help = (ImageView) view.findViewById(R.id.but_help);
		but_help.setOnClickListener(this);
		group = (RadioGroup) view.findViewById(R.id.rg_datamode);
		flyView = view.findViewById(R.id.fly_view);
		new_data = (LinearLayout) view.findViewById(R.id.new_data);
		his_data = (LinearLayout) view.findViewById(R.id.his_data);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_help:
			startActivity(new Intent(getActivity(), DataHelpActivity.class));
			break;
		case R.id.but_set:
			startActivityForResult(new Intent(getActivity(),NewDataSetActivity.class), 1);
			break;
		case R.id.but_ok:
			if (TextUtils.isEmpty(new_time.getText())&& TextUtils.isEmpty(new_id.getText())) {
				Toast.makeText(getActivity(), "请先设置查询条件", Toast.LENGTH_SHORT).show();
				break;
			} else {
				int[] a = {3,5};
				try {
					requestAllNewData(" ", a);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			break;
		case R.id.but_set_his:
			startActivityForResult(new Intent(getActivity(),HisDataSetActivity.class), 2);
			break;
		case R.id.but_ok_his:
			if (TextUtils.isEmpty(his_time.getText())&& TextUtils.isEmpty(his_id.getText())) {
				Toast.makeText(getActivity(), "请先设置查询条件", Toast.LENGTH_SHORT).show();
				break;
			} else {
				new Thread() {

					@Override
					public void run() {
						try {
							nodes_his = GetDataClass.requestHistoryData(st,et,nodeId_int_his);
							if (nodes_his != null) {
								handler_his.sendEmptyMessage(0);

							} else {
								handler_his.sendEmptyMessage(1);
							}

						} catch (Exception e) {
							Log.e("Tag", "進入socket異常");
							handler_his.sendEmptyMessage(1);
							e.printStackTrace();
						}
						// 消息传送：当本线程执行完时，发送消息，主线程handle再执行相应操作

					}

				}.start();
			}
			break;
		default:
			break;
		}

	}
	/**
	 * 获取指定节点的最新数据
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public NodeClass[] requestAllNewData(String st, int[] nodeId) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,"http://192.168.1.221:8080/JavaWeb/nodedata/newdata",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						
						//UI线程处理数据
						
						Log.d("TAG", response);
						
						
						Gson gson = new Gson();
						@SuppressWarnings("unchecked")
						ResponseMsg responseMsg = gson.fromJson(response, ResponseMsg.class);
						
						Log.d("TAG", "result = "+responseMsg.getMsg());
						Log.d("TAG", "result = "+responseMsg.getData().get(0).getDataId());
						
						
						
							
							
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				}){
			@Override
			protected Map<String, String> getParams()
					throws AuthFailureError {
				Map<String,String> map = new HashMap<String, String>();
				map.put("nodeIds","1#2#3#35");
				map.put("startTime", "100000");
				return map;
			}
		};
		stringRequest.setTag("requestAllNewData");
		MyApplication.getHttpQueues().add(stringRequest);
		
		
		
		return null;
	}

	// 有消息传送出来时，执行主线程操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				mData = getData(nodes);
				MyAdapter adapter = new MyAdapter(getActivity(), mData, nodes);
				gv_new_data.setAdapter(adapter);
				break;
			case 1:
				Log.v("Tag", "case 1");
				Toast.makeText(getActivity(), "连接失败，请检查网络或服务器",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		}

	};
	// 有消息传送出来时，执行主线程操作
	private Handler handler_his = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				mData = getData(nodes_his);
				MyAdapter adapter = new MyAdapter(getActivity(), mData,	nodes_his);
				gv_his_data.setAdapter(adapter);
				break;
			case 1:
				Log.v("Tag", "his_case 1");
				Toast.makeText(getActivity(), "连接失败，请检查网络或服务器",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		}

	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle bundle = new Bundle();
		// 最新查询设置返回数据
		if (requestCode == 1 && resultCode == 2) {

			bundle = data.getExtras();
			st = bundle.getString("st");
			et = bundle.getString("et");
			nodeId_int = bundle.getIntArray("nodeId_int");
			new_time.setVisibility(View.VISIBLE);
			new_time.setText(st + "至" + et);

			new_id.setVisibility(View.VISIBLE);
			if (nodeId_int.length == 50) {

				new_id.setText("所有节点");
			} else {
				for (int i = 0; i < nodeId_int.length; i++) {
					nodeId += String.valueOf(nodeId_int[i]);
				}
				new_id.setText("选中节点：" + nodeId);
				nodeId = "";
			}
		}
		// 历史查询设置返回数据
		if (requestCode == 2 && resultCode == 3) {
			bundle = data.getExtras();
			st = bundle.getString("st");
			et = bundle.getString("et");
			nodeId_int_his = bundle.getIntArray("nodeId_int");
			his_time.setVisibility(View.VISIBLE);
			his_time.setText(st + "至" + et);

			his_id.setVisibility(View.VISIBLE);

			for (int i = 0; i < nodeId_int_his.length; i++) {
				nodeId += String.valueOf(nodeId_int_his[i]) + " ";
			}
			his_id.setText("选中节点：" + nodeId);
			nodeId = "";

		}
	}

	@OnRadioGroupCheckedChange({ R.id.rg_datamode })
	public void onRadioGroupCheckedChange(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rd1:
			flyView.startAnimation(move_to_left);
			new_data.setVisibility(View.VISIBLE);
			his_data.setVisibility(View.GONE);
			break;
		case R.id.rd2:
			flyView.startAnimation(move_to_right);
			new_data.setVisibility(View.GONE);
			his_data.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
	
	
	private List<Map<String, Object>> getData(NodeClass[] nodes_in) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("title", "G1");
		// map.put("info", Integer.toString(1));

		map.put("node_id", "节点");
		map.put("node_time", "时间");
		map.put("node_GPS_lon", "温度");
		// map.put("node_GPS_lat", "GPS纬度");
		list.add(map);

		for (int i = 0; i < nodes_in.length; i++) {

			map = new HashMap<String, Object>();
			// map.put("title", "G2");
			// map.put("info", Integer.toString(2));
			map.put("node_id", Integer.toString(nodes_in[i].getId()));// id转为字符串
			map.put("node_time", nodes_in[i].getTime());
			map.put("node_GPS_lon", "" + nodes_in[i].getTempture());
			// map.put("node_GPS_lat", "" + nodes_in[i].getGPSLati());
			// map.put("img", R.drawable.i1);
			list.add(map);
		}

		return list;
	}

}
