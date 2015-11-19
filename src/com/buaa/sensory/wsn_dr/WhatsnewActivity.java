package com.buaa.sensory.wsn_dr;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.buaa.sensory.wsn_dr.activity.LoginActivity;
import com.buaa.sensory.wsn_dr.adapter.GuideAdapter;

public class WhatsnewActivity extends Activity {
	private ViewPager pager;
	private Button bt;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_whatsnew);
		pager = (ViewPager) findViewById(R.id.view_pager);
		bt = (Button) findViewById(R.id.welbt);
		// 添加监听，点击按钮切换到主界面
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(WhatsnewActivity.this,
						LoginActivity.class));

			}
		});
		// 初始化ViewpPager
		intViewPager();
	}

	private void intViewPager() {
		List<View> list = new ArrayList<View>();
		ImageView img1 = new ImageView(this);
		img1.setImageResource(R.drawable.guide_1);
		list.add(img1);
		ImageView img2 = new ImageView(this);
		img2.setImageResource(R.drawable.guide_2);
		list.add(img2);
		ImageView img3 = new ImageView(this);
		img3.setImageResource(R.drawable.guide_3);
		list.add(img3);

		GuideAdapter mAdapter = new GuideAdapter(list);
		pager.setAdapter(mAdapter);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				if (arg0 == 2) {
					bt.setVisibility(View.VISIBLE);

				} else {
					bt.setVisibility(View.GONE);
				}

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}
}
