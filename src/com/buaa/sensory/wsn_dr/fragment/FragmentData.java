package com.buaa.sensory.wsn_dr.fragment;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.entity.DateAndTime;
import com.buaa.sensory.wsn_dr.entity.NodeClass;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.google.gson.Gson;

public class FragmentData extends Fragment implements OnClickListener{
	
	private RadioGroup group;
	private View flyView;
	private LinearLayout new_data;		// 最新数据的显示控件
	private LinearLayout his_data;		// 历史数据的显示控件
	private ImageView but_help;
	private Button but_set;				// 按键：设置条件
	private Button but_ok;				// 按键：开始查询
	private Button but_set_his;			// 按键：设置his条件
	private Button but_ok_his;			// 按键：开始his查询
	private TextView new_time;
	private TextView new_id;
	private GridView gv_new_data;
	private TextView his_time;
	private TextView his_id;
	private GridView gv_his_data;
	private ProgressBar pb;
	
	// 查询参数
	Long st, et;
	Long st_his,et_his;

	int[] nodeId_int, nodeId_int_his;
	// 动画控件
	private Animation move_to_right, move_to_left;
	RequestQueue mQueue;  

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_data, container, false);
		initView(view);
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
		new_time = (TextView) view.findViewById(R.id.new_time);
		new_id = (TextView) view.findViewById(R.id.new_id);
		gv_new_data = (GridView) view.findViewById(R.id.gv_new_data);
		his_time = (TextView) view.findViewById(R.id.his_time);
		his_id = (TextView) view.findViewById(R.id.his_id);
		gv_his_data = (GridView) view.findViewById(R.id.gv_his_data);
		pb = (ProgressBar) view.findViewById(R.id.progressBar1);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
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
		});

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
				try {
					pb.setVisibility(View.VISIBLE);
					requestAllNewData(String.valueOf(st), nodeId_int);
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
				try {
					pb.setVisibility(View.VISIBLE);
					requestHisData(String.valueOf(st_his),String.valueOf(et_his), nodeId_int_his);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	public void requestAllNewData(final String st, final int[] nodeId) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.NEWDATA_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						pb.setVisibility(View.GONE);
						//UI线程处理数据
						Gson gson = new Gson();
						ResponseMsg responseMsg = gson.fromJson(response, ResponseMsg.class);
						MyAdapter adapter = new MyAdapter(getActivity(), responseMsg.getData());
						gv_new_data.setAdapter(adapter);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pb.setVisibility(View.GONE);
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
				map.put("startTime", st);
				return map;
			}
		};
		stringRequest.setTag("requestAllNewData");
		MyApplication.getHttpQueues().add(stringRequest);
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
						pb.setVisibility(View.GONE);
						Gson gson = new Gson();
						ResponseMsg responseMsg = gson.fromJson(response, ResponseMsg.class);
						Log.v("TAG", responseMsg.getData().toString());
						MyAdapter adapter = new MyAdapter(getActivity(), responseMsg.getData());
						gv_his_data.setAdapter(adapter);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pb.setVisibility(View.GONE);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle bundle = new Bundle();
		// 最新查询设置返回数据
		if (requestCode == 1 && resultCode == 2) {

			bundle = data.getExtras();
			st = bundle.getLong("st");
			et = bundle.getLong("et");
			nodeId_int = bundle.getIntArray("nodeId_int");
			new_time.setVisibility(View.VISIBLE);
			new_time.setText(DateAndTime.getDateFormat(st) + "至" + DateAndTime.getDateFormat(et));

			new_id.setVisibility(View.VISIBLE);
			if (nodeId_int.length >= 50) {

				new_id.setText("所有节点");
			} else {
				StringBuffer nodeId = new StringBuffer() ;
				for (int i = 0; i < nodeId_int.length; i++) {
					nodeId.append(String.valueOf(nodeId_int[i]));
				}
				new_id.setText("选中节点：" + nodeId);
			}
		}
		// 历史查询设置返回数据
		if (requestCode == 2 && resultCode == 3) {
			bundle = data.getExtras();
			st_his = bundle.getLong("st");
			et_his = bundle.getLong("et");
			nodeId_int_his = bundle.getIntArray("nodeId_int");
			his_time.setVisibility(View.VISIBLE);
			his_time.setText(DateAndTime.getDateFormat(st_his) + "至" + DateAndTime.getDateFormat(et_his));

			his_id.setVisibility(View.VISIBLE);
			
			StringBuffer nodeId = new StringBuffer() ;
			for (int i = 0; i < nodeId_int_his.length; i++) {
				nodeId.append(String.valueOf(nodeId_int_his[i]));
			}
			his_id.setText("选中节点：" + nodeId);

		}
	}
	
}

