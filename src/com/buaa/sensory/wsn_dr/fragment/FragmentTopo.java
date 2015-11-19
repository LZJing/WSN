package com.buaa.sensory.wsn_dr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.activity.DataHelpActivity;
import com.buaa.sensory.wsn_dr.activity.TopoSetActivity;
import com.buaa.sensory.wsn_dr.entity.GetDataClass;
import com.buaa.sensory.wsn_dr.entity.NodeClass;
import com.buaa.sensory.wsn_dr.entity.TopoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class FragmentTopo extends Fragment {
	@ViewInject(R.id.but_help_topo)
	private ImageView but_help_topo;
	@ViewInject(R.id.but_set_topo)
	private ImageView but_set_topo;
	@ViewInject(R.id.topo_view)
	private TopoView topo_view;
	@ViewInject(R.id.info_display)
	private TextView info_display;

	// 更新参数
	private boolean isauto = false;
	private int update_time = 10;
	private String st = "2014-03-20 00:00:00";
	// 停止参数
	boolean run = true;
	// 保存数据
	NodeClass[] nodes_topo = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.frag_topo, null);
		ViewUtils.inject(this, view);
		info_display.setVisibility(View.GONE);
		initGetData();
		return view;
	}

	private Handler handler_timer = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			if (run) {
				handler_timer.postDelayed(this, update_time * 1000);// 启动之后每次的延时

				initGetData();
			}

		}
	};

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		run = false;
	}

	public void initGetData() {
		cleanCanvas();
		new Thread() {

			@Override
			public void run() {
				try {
					nodes_topo = GetDataClass.requestTopoData(st);
					if (nodes_topo != null) {
						handler.sendEmptyMessage(0);

					} else {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					Log.e("Tag", "進入socket異常");
					handler.sendEmptyMessage(1);
					e.printStackTrace();
				}
				// 消息传送：当本线程执行完时，发送消息，主线程handle再执行相应操作

			}

		}.start();
	}

	// 有消息传送出来时，执行主线程操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Log.v("Tag", "case 0");
				Log.v("Tag", "" + nodes_topo[0].getId());
				// TopoView.this.draw(canvas_topo);
				try {
					topo_view.setNodes_topo(nodes_topo);
					topo_view.invalidate();// 重绘
				} catch (Exception e) {
					Log.e("Tag", "重绘");
					e.printStackTrace();
				}

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

	@OnClick({ R.id.but_help_topo, R.id.but_set_topo })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_help_topo:
			startActivity(new Intent(getActivity(), DataHelpActivity.class));
			break;

		case R.id.but_set_topo:
			startActivityForResult(new Intent(getActivity(),
					TopoSetActivity.class), 1);
			break;

		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle bundle = new Bundle();
		run = true;
		// 最新查询设置返回数据
		if (requestCode == 1 && resultCode == 2) {

			bundle = data.getExtras();
			st = bundle.getString("st");
			isauto = bundle.getBoolean("isauto");
			update_time = bundle.getInt("update_time");
			if (isauto) {
				Log.e("Tag", "进入自动");
				handler_timer.post(task);
				info_display.setVisibility(View.VISIBLE);
				info_display.setText("每隔" + update_time + "s更新一次");
			} else {
				Log.e("Tag", "进入手动");
				initGetData();
				info_display.setVisibility(View.GONE);
			}
		}
	}

	public void cleanCanvas() {
		nodes_topo = null;
		topo_view.setNodes_topo(nodes_topo);
		topo_view.invalidate();
	}

	/*
	 * @Override public void setMenuVisibility(boolean menuVisible) {
	 * super.setMenuVisibility(menuVisible); Log.e("Tag", "" + menuVisible); if
	 * (menuVisible) { run = true; if (isauto) { handler_timer.post(task); } }
	 * else { run = false; }
	 * 
	 * if (this.getView() != null) this.getView() .setVisibility(menuVisible ?
	 * View.VISIBLE : View.GONE); }
	 */
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
