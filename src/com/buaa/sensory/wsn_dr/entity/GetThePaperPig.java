package com.buaa.sensory.wsn_dr.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

/*
 * 本类的目的是产生论文所需要的伪造数据，用于历史查询和历史曲线
 */
public class GetThePaperPig {

	public static NodeClass[] requestFakeHisData(String st, String et,
			int[] nodeId) {
		int[] faketem = { 223, 221, 218, 215, 214, 210, 205, 203, 201, 199,
				195, 193, 190, 185, 178, 182, 176, 168 };
		Log.v("Tag", "进入requestFake方法");
		String time = "2014-04-21 15:00:05";
		NodeClass[][] nodes_his = null;
		NodeClass[] nodes_his_sum = null;
		final int COUNT = 18;// 返回数据数，设定30个。
		int num = 0;
		nodes_his = new NodeClass[nodeId.length][];
		for (int i = 0; i < nodeId.length; i++) {
			nodes_his[i] = new NodeClass[COUNT];
			for (int j = 0; j < COUNT; j++) {
				nodes_his[i][j] = new NodeClass();
				nodes_his[i][j].setId(nodeId[i]);
				nodes_his[i][j].setTime(time);
				time = getFakeTime(time);
				nodes_his[i][j].setGPSLong(0);
				nodes_his[i][j].setGPSeast(true);
				nodes_his[i][j].setGPSLati(0);
				nodes_his[i][j].setGPSnorth(true);
				nodes_his[i][j].setHigh(0);
				nodes_his[i][j].setSpeed(0);
				nodes_his[i][j].setLongi(0);
				nodes_his[i][j].setEast(true);
				nodes_his[i][j].setLati(0);
				nodes_his[i][j].setNorth(true);
				nodes_his[i][j].setTempture((float) Math.random() * 1
						+ faketem[j]);
				Log.v("Tag", "7");
			}
			num = nodes_his[i].length + num;
		}
		// 将二维数组整合到一维数组里
		nodes_his_sum = new NodeClass[num];
		int num2 = 0;
		for (int i = 0; i < nodes_his.length; i++) {
			for (int j = 0; j < nodes_his[i].length; j++) {
				nodes_his_sum[num2] = nodes_his[i][j];
				num2++;
			}
		}

		Log.v("Tag", "num2=" + num2);
		return nodes_his_sum;

	}

	public static String getFakeTime(String time) {
		long tim;
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try {
			date = formatter.parse(time);

		} catch (Exception e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		tim = calendar.getTimeInMillis();
		tim = tim + 600000 + (int) (5000 * Math.random());
		calendar.setTimeInMillis(tim);
		date = calendar.getTime();
		time = formatter.format(date);

		return time;
	}

	public static float getFakeTemp(int in) {

		return (float) Math.random() * 1 + in;
	}
}
