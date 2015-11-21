package com.buaa.sensory.wsn_dr.adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.buaa.sensory.wsn_dr.MyApplication;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.Toolkits.Calculate;
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.entity.DateAndTime;
import com.buaa.sensory.wsn_dr.model.GPSData;
import com.buaa.sensory.wsn_dr.model.NodeData;
import com.buaa.sensory.wsn_dr.model.ResponseGPSMsg;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.google.gson.Gson;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mcontext;
	private List<NodeData> list;

	public MyAdapter(Context context, List<NodeData> nodes) {
		this.mInflater = LayoutInflater.from(context);
		this.mcontext = context;
		this.list = nodes;
	}

	@Override
	public int getCount() {
		return list.size()+1;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
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
			holder.node_time = (TextView) convertView.findViewById(R.id.node_time);
			holder.node_GPS_lon = (TextView) convertView.findViewById(R.id.node_GPS_lon);
			holder.node_GPS_lat = (TextView) convertView.findViewById(R.id.node_GPS_lat);
			holder.humidity = (TextView) convertView.findViewById(R.id.humidity);
			holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		if (position == 0) {
			holder.node_id.setText("�ڵ�ID");
			holder.node_time.setText("ʱ��");
			holder.node_GPS_lon.setText("��ǿ");
			holder.node_GPS_lat.setText("�¶�");
			holder.humidity.setText("ʪ��");
			holder.viewBtn.setBackgroundResource(R.color.white);
			
		} else {
			holder.node_id.setText(String.valueOf(list.get(position-1).getNodeId()));
			holder.node_time.setText(Calculate.transferLongToDate(list.get(position-1).getLastTime()));
			holder.node_GPS_lon.setText(String.valueOf(Calculate.getLight(list.get(position-1).getLight())));
			holder.node_GPS_lat.setText(String.valueOf(Calculate.getTemperature(list.get(position-1).getTemperature())));
			holder.humidity.setText(String.valueOf(Calculate.getHumidity(list.get(position-1).getHumidity())));
			holder.viewBtn.setBackgroundResource(R.color.them_blue);

			holder.viewBtn.setId(position);// ��λ������Ϊ������ID
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						requestGPSData(list.get(v.getId() - 1).getGpsId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
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
	
	/**
	 * ��ȡĳ�����ݶ�Ӧ��GPS����
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void requestGPSData(final int gpsId) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.GPSDATA_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI�̴߳�������
						Gson gson = new Gson();
						ResponseGPSMsg responseGPSMsg = gson.fromJson(response, ResponseGPSMsg.class);
						GPSData data = responseGPSMsg.getData();
						
						showInfo("GPSʱ�䣺"+ DateAndTime.getDateFormat(data.getDateTime()) + "\n"
								+ "GPS���ȣ�"+ data.getGpsLo() + "\n"
								+ "GPSγ�ȣ�"+ data.getGpsLa() + "\n"
								+ "GPS�ٶȣ�"+data.getSpeed() + "\n"
								+ "GPS���Σ�"+data.getAltitude());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> map = new HashMap<String, String>();
				map.put("gpsId",String.valueOf(gpsId));
				return map;
			}
		};
		stringRequest.setTag("requestGPSData");
		MyApplication.getHttpQueues().add(stringRequest);
	}
}

class ViewHolder {
	public ImageView img;
	// public TextView title;
	// public TextView info;
	public TextView node_id;
	public TextView node_time;
	public TextView node_temp;
	public TextView node_GPS_lon;
	public TextView node_GPS_lat;
	public TextView humidity;
	public Button viewBtn;
}


