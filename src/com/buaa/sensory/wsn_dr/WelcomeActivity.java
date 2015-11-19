package com.buaa.sensory.wsn_dr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.buaa.sensory.wsn_dr.Toolkits.ToolKits;
import com.buaa.sensory.wsn_dr.activity.LoginActivity;

public class WelcomeActivity extends Activity {

	// 是否第一登录
	public static final String IS_FIRST = "is_first";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				if (!ToolKits
						.fetchBooble(WelcomeActivity.this, IS_FIRST, false)) {
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this,
							WhatsnewActivity.class);
					startActivity(intent);

				} else {
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, LoginActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
				// 第一次登录时，存入共享参数IS_FIRST-->true
				ToolKits.putBooble(WelcomeActivity.this, IS_FIRST, true);

				return false;

			}
		}).sendEmptyMessageDelayed(0, 3000);

	}

}
