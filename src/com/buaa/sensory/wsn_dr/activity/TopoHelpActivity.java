package com.buaa.sensory.wsn_dr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.buaa.sensory.wsn_dr.R;
public class TopoHelpActivity extends Activity implements OnClickListener{
	TextView help_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_topo);
		help_back = (TextView) findViewById(R.id.help_back);
		help_back.setOnClickListener(this);
	}
	public void onClick(View v){
		finish();
	}
}
