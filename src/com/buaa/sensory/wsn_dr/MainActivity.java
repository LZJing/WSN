package com.buaa.sensory.wsn_dr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.buaa.sensory.wsn_dr.fragment.FragmentData;
import com.buaa.sensory.wsn_dr.fragment.FragmentMap;
import com.buaa.sensory.wsn_dr.fragment.FragmentMore;
import com.buaa.sensory.wsn_dr.fragment.FragmentTopo;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	private RadioGroup bottom_bar;
	private FrameLayout layout_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bottom_bar = (RadioGroup) findViewById(R.id.buttom_bar);
		layout_content = (FrameLayout) findViewById(R.id.layout_content);
		bottom_bar.setOnCheckedChangeListener(this);
		bottom_bar.check(R.id.radio0);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = 0;
		switch (checkedId) {
		case R.id.radio0:
			index = 0;
			break;
		case R.id.radio1:
			index = 1;
			break;
		case R.id.radio2:
			index = 2;
			break;
		case R.id.radio3:
			index = 3;
			break;

		default:
			break;
		}
		Fragment fragment = (Fragment) fragments.instantiateItem(layout_content, index);
		fragments.setPrimaryItem(layout_content, 0, fragment);
		fragments.finishUpdate(layout_content);
		
	}

	FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			switch (arg0) {
			case 0:
				fragment = new FragmentData();
				break;
			case 1:
				fragment = new FragmentTopo();
				break;
			case 2:
				fragment = new FragmentMap();
				break;
			case 3:
				fragment = new FragmentMore();
				break;

			default:
				break;
			}
			return fragment;
		}
	};



}
