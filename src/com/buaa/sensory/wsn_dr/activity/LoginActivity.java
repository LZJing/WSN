package com.buaa.sensory.wsn_dr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.sensory.wsn_dr.MainActivity;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.entity.GetDataClass;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LoginActivity extends Activity {

	@ViewInject(R.id.welcome)
	private TextView welcome;
	@ViewInject(R.id.bt_login)
	private Button bt_login;
	@ViewInject(R.id.bt_register)
	private Button bt_register;
	@ViewInject(R.id.et_name)
	private EditText et_name;
	@ViewInject(R.id.et_key)
	private EditText et_key;
	@ViewInject(R.id.jump)
	private Button jump;
	byte permission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);

	}


	@OnClick({ R.id.jump })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jump:
			Intent intent = new Intent();
			//intent.setClass(LoginActivity.this, HisLineActivity.class);
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
