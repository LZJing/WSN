package com.buaa.sensory.wsn_dr;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.Toolkits.ToolKits;
import com.buaa.sensory.wsn_dr.adapter.MyAdapter;
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.buaa.sensory.wsn_dr.model.ResponseUserMsg;
import com.google.gson.Gson;

public class LoginActivity extends Activity implements OnClickListener{

	String userName;
	String password;
	private Button bt_login;
	private Button bt_register;
	private EditText et_name;
	private EditText et_key;
	private CheckBox cb;
	byte permission;
	private boolean isRemembered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_register = (Button) findViewById(R.id.bt_register);
		et_name = (EditText) findViewById(R.id.et_name);
		et_key = (EditText) findViewById(R.id.et_key);
		cb = (CheckBox) findViewById(R.id.remember_pw);
		isRemembered = ToolKits.fetchBooble(this, "isRemembered", false);
		bt_login.setOnClickListener(this);
		
		if(isRemembered){
			et_name.setText(ToolKits.fetchString(this, "userName", ""));
			et_key.setText(ToolKits.fetchString(this, "password", ""));
			cb.setChecked(true);
		}
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login:
			if(TextUtils.isEmpty(et_name.getText())||TextUtils.isEmpty(et_key.getText())){
				Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
				break;
			}
			if(cb.isChecked()){
				userName = et_name.getText().toString();
				password = et_key.getText().toString();
				saveRememberInfo(true,userName,password);
			}else{
				saveRememberInfo(false, "", "");
			}
			
			try {
				login(userName, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		default:
			break;
		}
	}
	
	/**
	 * 登录
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void login(final String userName, final String password) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.Login_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI线程处理数据
						Gson gson = new Gson();
						ResponseUserMsg responseUserMsg = gson.fromJson(response, ResponseUserMsg.class);
						if(responseUserMsg.isResult()){
							saveUserInfo(responseUserMsg.getData().getUserId(),responseUserMsg.getData().getUserName());
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
							LoginActivity.this.finish();
						}
						
					}

					
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(LoginActivity.this, "请检查网络连接或服务器状态", Toast.LENGTH_SHORT).show();
						Log.e("TAG", error.getMessage(), error);
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> map = new HashMap<String, String>();
				map.put("userName",userName);
				map.put("password", password);
				return map;
			}
		};
		stringRequest.setTag("login");
		MyApplication.getHttpQueues().add(stringRequest);
	}


	private void saveRememberInfo(boolean c,String userName,String password) {
		
		ToolKits.putBooble(this, "isRemembered", c);
		ToolKits.putString(this, "userName", userName);
		ToolKits.putString(this,"password",password);
	}
	
	private void saveUserInfo(int userId, String userName) {
		ToolKits.putString(this, "name", userName);
		ToolKits.putInt(this,"id",userId);
	}
}
