package com.buaa.sensory.wsn_dr.activity;

import com.buaa.sensory.wsn_dr.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DataHelpActivity extends Activity{
	@ViewInject(R.id.help_back)
	TextView help_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_data);
		ViewUtils.inject(this);//start inject
	}
	@OnClick(R.id.help_back)
	public void onClick(View v){
		finish();
	}
}
