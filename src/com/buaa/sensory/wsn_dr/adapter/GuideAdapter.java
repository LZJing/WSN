package com.buaa.sensory.wsn_dr.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class GuideAdapter extends PagerAdapter{
	
	private List<View> mList;
	
	public GuideAdapter(List<View> list){
		mList = list;
		
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0==arg1;
	}
	
	
	//��д���ٺ���
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(mList.get(position));
	}
	//��д��ʼ������
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager)container).addView(mList.get(position));
		return mList.get(position);
	}
}
