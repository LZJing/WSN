package com.buaa.sensory.wsn_dr.adapter;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.entity.NodeClass;
import com.buaa.sensory.wsn_dr.entity.ViewHolder;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;
	private Context mcontext;
	private NodeClass[] nodes_adapter;

	public MyAdapter(Context context, List<Map<String, Object>> datalist,
			NodeClass[] nodes) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = datalist;
		this.mcontext = context;
		this.nodes_adapter = nodes;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.adapter_list, null);
			// holder.img = (ImageView) convertView.findViewById(R.id.img);
			// holder.title = (TextView) convertView.findViewById(R.id.title);
			// holder.info = (TextView) convertView.findViewById(R.id.info);
			holder.node_id = (TextView) convertView.findViewById(R.id.node_id);
			holder.node_time = (TextView) convertView
					.findViewById(R.id.node_time);
			holder.node_GPS_lon = (TextView) convertView
					.findViewById(R.id.node_GPS_lon);
			holder.node_GPS_lat = (TextView) convertView
					.findViewById(R.id.node_GPS_lat);
			holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.node_id.setText((String) mData.get(position).get("node_id"));
		holder.node_time.setText((String) mData.get(position).get("node_time"));
		holder.node_GPS_lon.setText((String) mData.get(position).get(
				"node_GPS_lon"));
		holder.node_GPS_lat.setText((String) mData.get(position).get(
				"node_GPS_lat"));
		if (position == 0) {
			holder.viewBtn.setBackgroundResource(R.color.white);
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});
		} else {
			holder.viewBtn.setBackgroundResource(R.color.them_blue);

			holder.viewBtn.setId(position);// ��λ������Ϊ������ID
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// showInfo("" + );
					showInfo("�ڵ�ţ�" + nodes_adapter[v.getId() - 1].getId()
							+ "\n" + "ʱ�䣺"
							+ nodes_adapter[v.getId() - 1].getTime() + "\n"
							+ "GPS���ȣ�"
							+ nodes_adapter[v.getId() - 1].getGPSLong() + "\n"
							+ "GPSγ�ȣ�"
							+ nodes_adapter[v.getId() - 1].getGPSLati() + "\n"
							+ "GPS�ٶȣ�"
							+ nodes_adapter[v.getId() - 1].getSpeed() + "\n"
							+ "ת���ڵ�ID��0\n" + "�¶ȣ�26\n");
					/*
					 * + "�㷨���ȣ�" + nodes_adapter[v.getId() - 1].getLongi() +
					 * "\n" + "�㷨γ�ȣ�" + nodes_adapter[v.getId() - 1].getLati() +
					 * "\n" + "������" + nodes_adapter[v.getId() -
					 * 1].getLongiError() + "\n" + "γ����" +
					 * nodes_adapter[v.getId() - 1].getLatiError() + "\n" +
					 * "position��" + v.getId() + "\n");
					 */
					// Log.v("Tag", "" + v.getId());
					//
				}
			});
		}
		return convertView;
	}

	/**
	 * listview�е�����������Ի���
	 */
	public void showInfo(String info) {
		new AlertDialog.Builder(mcontext).setTitle("��ϸ��Ϣ").setMessage(info)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}
}
