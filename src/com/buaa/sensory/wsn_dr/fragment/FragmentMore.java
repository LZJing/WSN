package com.buaa.sensory.wsn_dr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.activity.AboutActivity;
import com.buaa.sensory.wsn_dr.activity.ContactUsActivity;
import com.buaa.sensory.wsn_dr.activity.ExplainActivity;
import com.buaa.sensory.wsn_dr.activity.LoginActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class FragmentMore extends Fragment {
	@ViewInject(R.id.changeuser)
	private Button change_user;
	@ViewInject(R.id.explain)
	private Button explain;
	@ViewInject(R.id.contactus)
	private Button contact_us;
	@ViewInject(R.id.about)
	private Button about;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.frag_more, null);
		ViewUtils.inject(this, view);
		Log.i("Tag", "FragmentMore");
		return view;
	}

	@OnClick({ R.id.changeuser, R.id.explain, R.id.contactus, R.id.about })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changeuser:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.explain:
			startActivity(new Intent(getActivity(), ExplainActivity.class));
			break;

		case R.id.contactus:
			startActivity(new Intent(getActivity(), ContactUsActivity.class));
			break;

		case R.id.about:
			startActivity(new Intent(getActivity(), AboutActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
