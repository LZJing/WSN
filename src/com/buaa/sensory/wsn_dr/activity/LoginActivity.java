package com.buaa.sensory.wsn_dr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.buaa.sensory.wsn_dr.MainActivity;
import com.buaa.sensory.wsn_dr.R;

public class LoginActivity extends Activity implements OnClickListener{

	private Button bt_login;
	private Button bt_register;
	private EditText et_name;
	private EditText et_key;
	private Button jump;
	byte permission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_register = (Button) findViewById(R.id.bt_register);
		et_name = (EditText) findViewById(R.id.et_name);
		et_key = (EditText) findViewById(R.id.et_key);
		jump = (Button) findViewById(R.id.jump);
		jump.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jump:
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
