package com.buaa.sensory.wsn_dr.activity;

import com.buaa.sensory.wsn_dr.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MapHelpActivity extends Activity implements OnClickListener{
	TextView help_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_data);
		help_back = (TextView) findViewById(R.id.help_back);
		help_back.setOnClickListener(this);
	}
	public void onClick(View v){
		finish();
	}
}
