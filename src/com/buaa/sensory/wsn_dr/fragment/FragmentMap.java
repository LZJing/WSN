package com.buaa.sensory.wsn_dr.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.buaa.sensory.wsn_dr.MyApplication;
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.activity.MapHelpActivity;
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.model.GPSData;
import com.buaa.sensory.wsn_dr.model.NodeData;
import com.buaa.sensory.wsn_dr.model.ResponseGPSMsg;
import com.buaa.sensory.wsn_dr.model.ResponseMsg;
import com.google.gson.Gson;

public class FragmentMap extends Fragment implements OnMarkerClickListener,
		OnInfoWindowClickListener, OnMapLoadedListener, OnClickListener,
		InfoWindowAdapter,OnItemSelectedListener,LocationSource,AMapLocationListener {
	private MarkerOptions markerOption;
	// private TextView markerText;

	private MapView mapView;									//������ͼ
	private AMap aMap;											//������ͼ

	private LatLng latlng = new LatLng(36.061, 103.834);
	private ImageView but_help;
	
	private Button clearMap;
	private Button resetMap;
	private Spinner spinner;
	
	List<NodeData> list;
	private OnLocationChangedListener mListener;				//��λ
	private LocationManagerProxy mAMapLocationManager;			//��λ




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_map, null);
		mapView = (MapView) view.findViewById(R.id.map);		//������ͼ
		mapView.onCreate(savedInstanceState); 					//������ͼ�� �˷���������д
		but_help = (ImageView) view.findViewById(R.id.but_help);
		clearMap = (Button) view.findViewById(R.id.clearMap);
		resetMap = (Button) view.findViewById(R.id.resetMap);
		spinner = (Spinner) view.findViewById(R.id.layers_spinner);
		clearMap.setOnClickListener(this);
		resetMap.setOnClickListener(this);
		but_help.setOnClickListener(this);
		
		initMap();												//������ͼ
		init();
		return view;
	}

	/**
	 * ��ʼ��AMap����,������ͼ
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();

		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();

	}
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_help:
			startActivity(new Intent(getActivity(), MapHelpActivity.class));
			break;
		case R.id.clearMap:
			if (aMap != null) {
				aMap.clear();
			}
			break;
		case R.id.resetMap:
			if (aMap != null) {
				aMap.clear();
				setUpLocate();
				setUpMarker();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * ��������ʼ��AMap�������
	 */

	private void init() {
		clearMap.setOnClickListener(this);
		resetMap.setOnClickListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.layers_array,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		setUpLocate();
		setUpMarker();
	}

	/**
	 * ��λС���㣬����һЩamap������---------------------
	 */
	private void setUpLocate() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
	   // aMap.setMyLocationType()
	}


	/**
	 * ��λMarker������һЩamap������---------------------
	 */
	private void setUpMarker() {
		// aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������
		aMap.setOnMapLoadedListener(this);// ����amap���سɹ��¼�������
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		int[] ids = new int[50];
		ids[0] = 0;
		for (int i = 1; i < ids.length; i++) {
			ids[i] =ids[i-1]+1;
		}
		try {
			requestAllNewData(String.valueOf(System.currentTimeMillis()-1000*3600*24*2), ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// addMarkersToMap();// ����ͼ�����marker
	}

	

	/**
	 * �ڵ�ͼ�����marker
	 */
	private void addMarkersToMap(GPSData data,NodeData nodeData) {
		double GPSx;
		double GPSy;
		GPSx = Constants.getDegreelo(data.getGpsLo());
		GPSy = Constants.getDegreela(data.getGpsLa());
		LatLng latlng = new LatLng(GPSy, GPSx);
		aMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 1f)
				.position(latlng)
				.title("" + nodeData.getNodeId() + "�Žڵ�")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.snippet("ID:" + nodeData.getNodeId()).draggable(true));

	}

	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (aMap != null) {
			setLayer((String) arg0.getItemAtPosition(arg2));
		}
	}

	

	

	/**
	 * ѡ��ʸ����ͼ�����ǵ�ͼ�¼�����Ӧ
	 */
	private void setLayer(String layerName) {
		if (layerName.equals(getString(R.string.normal))) {
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);// ʸ����ͼģʽ
		} else if (layerName.equals(getString(R.string.satellite))) {
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// ���ǵ�ͼģʽ
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker arg0) {
		View infoContent = getActivity().getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(arg0, infoContent);
		return infoContent;
	}

	/*-----------��λ����-------------------*/
	/**
	 * �Զ���infowinfow����
	 */
	public void render(Marker marker, View view) {
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(12);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Toast.makeText(getActivity(), "������infoWindow����" + arg0.getTitle(),
				Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLoaded() {
		// ��������maker��ʾ�ڵ�ǰ���������ͼ��
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(Constants.gym_NE).include(Constants.gym_SW).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}
	
	/**
	 * ��ȡĳ�����ݶ�Ӧ��GPS����
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void requestGPSData(final NodeData nodeData) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.GPSDATA_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI�̴߳�������
						Gson gson = new Gson();
						ResponseGPSMsg responseGPSMsg = gson.fromJson(response, ResponseGPSMsg.class);
						GPSData data = responseGPSMsg.getData();
						addMarkersToMap(data,nodeData);
						
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
				map.put("gpsId",String.valueOf(nodeData.getGpsId()));
				return map;
			}
		};
		stringRequest.setTag("requestGPSData");
		MyApplication.getHttpQueues().add(stringRequest);
	}
	
	/**
	 * ��ȡָ���ڵ����������
	 * @param st
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public void requestAllNewData(final String st, final int[] nodeId) throws Exception {
		StringRequest stringRequest = new StringRequest(Method.POST,Constants.NEWDATA_REQUEST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//UI�̴߳�������
						Gson gson = new Gson();
						ResponseMsg responseMsg = gson.fromJson(response, ResponseMsg.class);
						list=responseMsg.getData();
						for (int i = 0; i < list.size(); i++) {
							try {
								requestGPSData(list.get(i));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
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
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < nodeId.length; i++) {
					if(i!=0){
						sb.append("#"+nodeId[i]);
					}else{
						sb.append(nodeId[i]);
					}
				}
				map.put("nodeIds",new String(sb));
				map.put("startTime", st);
				return map;
			}
		};
		stringRequest.setTag("requestAllNewData");
		MyApplication.getHttpQueues().add(stringRequest);
	}


	


	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2�汾��������������true��ʾ��϶�λ�а���gps��λ��false��ʾ�����綨λ��Ĭ����true Location
			 * API��λ����GPS�������϶�λ��ʽ
			 * ����һ�������Ƕ�λprovider���ڶ�������ʱ�������2000���룬������������������λ���ף����ĸ������Ƕ�λ������
			 */
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// ��ʾϵͳС����
		}
	}
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(this.getView()!=null){
			this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
		}
	}
}
