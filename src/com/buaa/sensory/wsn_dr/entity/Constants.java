package com.buaa.sensory.wsn_dr.entity;

import com.amap.api.maps2d.model.LatLng;

public class Constants {
	
	public static final String NEWDATA_REQUEST="http://192.168.1.221:8080/JavaWeb/nodedata/newdata";
	public static final String HISDATA_REQUEST="http://192.168.1.221:8080/JavaWeb/nodedata/hisdata";
	public static final String GPSDATA_REQUEST="http://192.168.1.221:8080/JavaWeb/nodedata/gpsdata";

	public static final int ERROR = 1001;// �����쳣
	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_BUS_RESULT = 2002;// ·���滮�й���ģʽ
	public static final int ROUTE_DRIVING_RESULT = 2003;// ·���滮�мݳ�ģʽ
	public static final int ROUTE_WALK_RESULT = 2004;// ·���滮�в���ģʽ
	public static final int ROUTE_NO_RESULT = 2005;// ·���滮û�����������

	public static final int GEOCODER_RESULT = 3000;// ������������������ɹ�
	public static final int GEOCODER_NO_RESULT = 3001;// ������������������û������

	public static final int POISEARCH = 4000;// poi���������
	public static final int POISEARCH_NO_RESULT = 4001;// poiû�����������
	public static final int POISEARCH_NEXT = 5000;// poi������һҳ

	public static final int BUSLINE_LINE_RESULT = 6001;// ������·��ѯ
	public static final int BUSLINE_id_RESULT = 6002;// ����id��ѯ
	public static final int BUSLINE_NO_RESULT = 6003;// �쳣���

	public static final double NW_longi = 116.2035156;
	public static final double NW_lati = 39.5877849;
	public static final double NE_longi = 116.2041309;
	public static final double NE_lati = 39.5878138;
	public static final double SW_longi = 116.2035742;
	public static final double SW_lati = 39.5868840;
	public static final double SE_longi = 116.2042383;
	public static final double SE_lati = 39.5869500;
	public static final double testlo = 116.203644;
	public static final double testla = 39.5872526;

	public static final double la_err = 0.00132; // GPSƫ��У��
	public static final double lo_err = 0.00618;

	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// �����о�γ��
	public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// �������йش徭γ��
	public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// �Ϻ��о�γ��
	public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// ����������ľ�γ��
	public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// �ɶ��о�γ��
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// �����о�γ��
	public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// ֣���о�γ��
	public static final LatLng gym_NW = new LatLng(getDegreela(NW_lati),
			getDegreelo(NW_longi));// �����ٳ���γ��
	public static final LatLng gym_NE = new LatLng(getDegreela(NE_lati),
			getDegreelo(NE_longi));// �����ٳ���γ��
	public static final LatLng gym_SE = new LatLng(getDegreela(SE_lati),
			getDegreelo(SE_longi));// �����ٳ���γ��
	public static final LatLng gym_SW = new LatLng(getDegreela(SW_lati),
			getDegreelo(SW_longi));// �����ٳ���γ��
	public static final LatLng gym_test = new LatLng(getDegreela(testla),
			getDegreelo(testlo));// �����ٳ���γ��
	

	public static double getDegreela(double dm) {
		double dd;
		dd = Math.floor(dm) + dm % 1 * 100 / 60;
		dd = dd + la_err;
		return dd;
	}

	public static double getDegreelo(double dm) {
		double dd;
		dd = Math.floor(dm) + dm % 1 * 100 / 60;
		dd = dd + lo_err;
		return dd;
	}

}
