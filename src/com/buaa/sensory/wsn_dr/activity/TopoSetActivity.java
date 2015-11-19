package com.buaa.sensory.wsn_dr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.buaa.sensory.wsn_dr.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class TopoSetActivity extends Activity {
	@ViewInject(R.id.topo_set_bt_cancel)
	private Button topo_set_bt_cancel;
	@ViewInject(R.id.topo_set_bt_ok)
	private Button topo_set_bt_ok;
	@ViewInject(R.id.topo_st)
	private EditText topo_st;

	@ViewInject(R.id.topo_rg)
	private RadioGroup topo_rg;
	@ViewInject(R.id.spinner1)
	private Spinner spinner;

	private boolean isauto;
	private int update_time;
	private String st;
	String[] times = { "5", "8", "10", "20", "30", "60", "600" };
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toposet);
		ViewUtils.inject(this);
		// 将可选内容与ArrayAdapter连接起来
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, times);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);

		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

		topo_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.topo_radio0:
					isauto = false;
					Toast.makeText(TopoSetActivity.this, "手动",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.topo_radio1:
					isauto = true;
					Toast.makeText(TopoSetActivity.this, "自动",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		});
		topo_rg.check(R.id.topo_radio0);
	}

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			update_time = Integer.parseInt(times[arg2]);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@OnClick({ R.id.topo_set_bt_cancel, R.id.topo_set_bt_ok })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topo_set_bt_ok:
			// String a = topo_ut.getText().toString();
			// update_time = Integer.parseInt(a);

			st = topo_st.getText().toString();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putBoolean("isauto", isauto);
			bundle.putString("st", st);
			bundle.putInt("update_time", update_time);
			intent.putExtras(bundle);
			setResult(2, intent);
			finish();
			break;
		case R.id.topo_set_bt_cancel:
			finish();
			break;
		default:
			break;
		}
	}
}
