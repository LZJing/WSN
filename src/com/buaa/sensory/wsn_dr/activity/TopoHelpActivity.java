package com.buaa.sensory.wsn_dr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.buaa.sensory.wsn_dr.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class TopoHelpActivity extends Activity{
	@ViewInject(R.id.help_back)
	TextView help_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_topo);
		ViewUtils.inject(this);//start inject
	}
	@OnClick(R.id.help_back)
	public void onClick(View v){
		finish();
	}
}
