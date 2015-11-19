package com.buaa.sensory.wsn_dr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.entity.DataAndTime;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class HisDataSetActivity extends Activity {

	String nodeId = "";
	@ViewInject(R.id.new_data_st)
	private EditText new_data_st;
	@ViewInject(R.id.new_data_et)
	private EditText new_data_et;
	@ViewInject(R.id.new_data_choseid)
	private EditText new_data_choseid;
	@ViewInject(R.id.new_data_set_bt_ok)
	private Button ok;
	@ViewInject(R.id.new_data_set_bt_cancel)
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.his_data_set);
		ViewUtils.inject(this);
		new_data_st.setText(DataAndTime.getAddedDateTime(60 * 24));// 向前查询一天
		new_data_et.setText(DataAndTime.getSysCurDateTime());

	}

	@OnClick({ R.id.new_data_set_bt_ok, R.id.new_data_set_bt_cancel })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_data_set_bt_ok:
			boolean input_error = false;
			input_error = checkChoseId();
			if (input_error == true) {
				break;
			}
			for (int i = 0; i < nodeId.length(); i++) {
				char c = nodeId.charAt(i);
				if (!((c >= '0' && c <= '9') || (c == ' '))) {
					Toast.makeText(this, "输入有误,仅可输入数字及空格", Toast.LENGTH_SHORT)
							.show();
					input_error = true;
					break;
				}
			}
			if (nodeId.charAt(0) == ' ' && nodeId.charAt(1) == ' ') {
				Toast.makeText(this, "不能全为空格", Toast.LENGTH_SHORT).show();
				break;
			}
			// 留有一个bug，输入全是空格出错。
			if (input_error == true) {
				break;
			}
			String[] nodeIds = nodeId.split(" ");
			int[] nodeId_int = new int[nodeIds.length];
			if (nodeIds.length > 3) {

				Toast.makeText(this, "最多可输入3个节点", Toast.LENGTH_SHORT).show();
				break;
			}
			for (int i = 0; i < nodeIds.length; i++) {
				if (0 <= Integer.parseInt(nodeIds[i])
						&& Integer.parseInt(nodeIds[i]) <= 50)
					nodeId_int[i] = Integer.parseInt(nodeIds[i]);
				else {
					Toast.makeText(this, "输入有误，节点ID范围1-50", Toast.LENGTH_SHORT)
							.show();
					input_error = true;
					break;
				}
			}
			if (input_error == true) {
				break;
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putIntArray("nodeId_int", nodeId_int);
			bundle.putString("st", new_data_st.getText().toString());
			bundle.putString("et", new_data_et.getText().toString());
			bundle.putString("nodeId", nodeId);
			intent.putExtras(bundle);
			setResult(3, intent);
			finish();
			break;
		case R.id.new_data_set_bt_cancel:
			finish();
			break;

		default:
			break;
		}
	}

	public boolean checkChoseId() {
		if (TextUtils.isEmpty(new_data_choseid.getText())) {
			Toast.makeText(this, "请输入查询节点", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			nodeId = new_data_choseid.getText().toString();
			return false;
		}
	}
}
