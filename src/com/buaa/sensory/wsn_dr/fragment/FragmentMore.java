package com.buaa.sensory.wsn_dr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buaa.sensory.wsn_dr.LoginActivity;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.Toolkits.ToolKits;
import com.buaa.sensory.wsn_dr.activity.AboutActivity;
import com.buaa.sensory.wsn_dr.activity.ContactUsActivity;
import com.buaa.sensory.wsn_dr.activity.ExplainActivity;

public class FragmentMore extends Fragment implements OnClickListener{
	private Button change_user;
	private Button explain;
	private Button contact_us;
	private Button about;
	private TextView name;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_more, null);
		change_user = (Button) view.findViewById(R.id.changeuser);
		change_user.setOnClickListener(this);
		explain = (Button) view.findViewById(R.id.explain);
		explain.setOnClickListener(this);
		contact_us = (Button) view.findViewById(R.id.contactus);
		contact_us.setOnClickListener(this);
		about = (Button) view.findViewById(R.id.about);
		about.setOnClickListener(this);
		name = (TextView) view.findViewById(R.id.username);
		name.setText(ToolKits.fetchString(getActivity(), "name", "Î´µÇÂ¼"));
		return view;
	}

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
