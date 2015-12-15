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

	private MapView mapView;									//基本地图
	private AMap aMap;											//基本地图

	private LatLng latlng = new LatLng(36.061, 103.834);
	private ImageView but_help;
	
	private Button clearMap;
	private Button resetMap;
	private Spinner spinner;
	
	List<NodeData> list;
	private OnLocationChangedListener mListener;				//定位
	private LocationManagerProxy mAMapLocationManager;			//定位




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_map, null);
		mapView = (MapView) view.findViewById(R.id.map);		//基本地图
		mapView.onCreate(savedInstanceState); 					//基本地图， 此方法必须重写
		but_help = (ImageView) view.findViewById(R.id.but_help);
		clearMap = (Button) view.findViewById(R.id.clearMap);
		resetMap = (Button) view.findViewById(R.id.resetMap);
		spinner = (Spinner) view.findViewById(R.id.layers_spinner);
		clearMap.setOnClickListener(this);
		resetMap.setOnClickListener(this);
		but_help.setOnClickListener(this);
		
		initMap();												//基本地图
		init();
		return view;
	}

	/**
	 * 初始化AMap对象,基本地图
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
	 * ↑↑↑初始化AMap对象完毕
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
	 * 定位小蓝点，设置一些amap的属性---------------------
	 */
	private void setUpLocate() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	   // aMap.setMyLocationType()
	}


	/**
	 * 定位Marker，设置一些amap的属性---------------------
	 */
	private void setUpMarker() {
		// aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
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
		// addMarkersToMap();// 往地图上添加marker
	}

	

	/**
	 * 在地图上添加marker
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
				.title("" + nodeData.getNodeId() + "号节点")
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
	 * 选择矢量地图和卫星地图事件的响应
	 */
	private void setLayer(String layerName) {
		if (layerName.equals(getString(R.string.normal))) {
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
		} else if (layerName.equals(getString(R.string.satellite))) {
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
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

	/*-----------定位结束-------------------*/
	/**
	 * 自定义infowinfow窗口
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
		Toast.makeText(getActivity(), "你点击了infoWindow窗口" + arg0.getTitle(),
				Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(Constants.gym_NE).include(Constants.gym_SW).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}
	
	/**
	 * 获取某条数据对应的GPS数据
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
						//UI线程处理数据
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
	 * 获取指定节点的最新数据
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
						//UI线程处理数据
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
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
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
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
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
