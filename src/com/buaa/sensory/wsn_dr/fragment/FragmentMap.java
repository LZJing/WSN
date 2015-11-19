package com.buaa.sensory.wsn_dr.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.activity.MapHelpActivity;
import com.buaa.sensory.wsn_dr.entity.Constants;
import com.buaa.sensory.wsn_dr.entity.GetDataClass;
import com.buaa.sensory.wsn_dr.entity.NodeClass;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class FragmentMap extends Fragment implements OnMarkerClickListener,
		OnInfoWindowClickListener, OnMapLoadedListener, OnClickListener,
		InfoWindowAdapter, LocationSource, AMapLocationListener,
		OnItemSelectedListener {
	private MarkerOptions markerOption;
	// private TextView markerText;

	private AMap aMap;

	private LatLng latlng = new LatLng(36.061, 103.834);
	private OnLocationChangedListener mListener;// 定位小蓝点监听
	private LocationManagerProxy mAMapLocationManager;// 定位小蓝点管理
	@ViewInject(R.id.but_help)
	private ImageView but_help;
	// @ViewInject(R.id.map)
	private MapView mapView;
	@ViewInject(R.id.clearMap)
	private Button clearMap;
	@ViewInject(R.id.resetMap)
	private Button resetMap;
	@ViewInject(R.id.layers_spinner)
	private Spinner spinner;

	NodeClass[] nodes;
	int[] nodeId_int = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
			34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.frag_map, null);
		ViewUtils.inject(this, view);
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		initMap();
		init();
		Log.i("Tag", "FragmentMap");
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
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
		deactivate();// 关定位
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@OnClick({ R.id.but_help, R.id.clearMap, R.id.resetMap })
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
				// addMarkersToMap();
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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.layers_array,
				android.R.layout.simple_spinner_item);
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
		askData();
		// addMarkersToMap();// 往地图上添加marker
	}

	private void askData() {
		new Thread() {

			@Override
			public void run() {
				try {
					// GetDataClass dgc = new GetDataClass();
					nodes = GetDataClass.requestAllNewData(
							"2014-03-20 00:00:00", nodeId_int);
					if (nodes != null) {
						handler.sendEmptyMessage(0);

					} else {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					Log.e("Tag", "進入socket異常");
					handler.sendEmptyMessage(1);
					e.printStackTrace();
				}
				// 消息传送：当本线程执行完时，发送消息，主线程handle再执行相应操作

			}

		}.start();
	}

	// 有消息传送出来时，执行主线程操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Log.v("Tag", "case 0");
				Log.v("Tag", nodes[0].getTime());
				addMarkersToMap();

				break;
			case 1:
				Log.v("Tag", "case 1");
				Toast.makeText(getActivity(), "连接失败，请检查网络或服务器",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		}

	};

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		double GPSx;
		double GPSy;
		for (int i = 0; i < nodes.length; i++) {
			GPSx = Constants.getDegreelo(nodes[i].getGPSLong());
			GPSy = Constants.getDegreela(nodes[i].getGPSLati());
			LatLng latlng = new LatLng(GPSy, GPSx);
			aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1f)
					.position(latlng)
					.title("" + nodes[i].getId() + "号节点")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.snippet("ID:" + nodes[i].getId()).draggable(true));
		}

	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location arg0) {
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (aMap != null) {
			setLayer((String) arg0.getItemAtPosition(arg2));
		}
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation arg0) {
		if (mListener != null && arg0 != null) {
			mListener.onLocationChanged(arg0);// 显示系统小蓝点
		}

	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
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
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
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

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
