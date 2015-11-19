package com.buaa.sensory.wsn_dr.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class HisLineView extends View {

	NodeClass[] nodes_his;
	int[] nodeId_int_his = { 1 };
	int time_xlength;
	public int XPoint = 40; // 原点的X坐标
	public int YPoint = 500; // 原点的Y坐标
	public int XScale = 60; // X的刻度长度
	public int YScale = 80; // Y的刻度长度
	public int XLength = 410; // X轴的长度
	public int YLength = 420; // Y轴的长度
	public String[] XLabel = { "15:00", "15:30", "16:00", "16:30", "17:00",
			"17:30", "18:00" }; // X的刻度
	public String[] YLabel = { "-10", "0", "10", "20", "30", "40" }; // Y的刻度
	public int[] Data = { 15, 23, 10, 36, 45, 40, 12, 15, 15, 15 }; // 数据
	public String Title = "历史数据 "; // 显示的标题
	public String Range = "查询范围：2014-04-21 15:00 至 2014-04-21 18:00";
	public String id = "1";
	public String time_start = "2014-04-21 15:00:00";
	public String time_end = "2014-04-21 18:00:00";

	public HisLineView(Context context, AttributeSet set) {
		super(context, set);
	}

	public void SetInfo(String[] XLabels, String[] YLabels, int[] AllData,
			String strTitle) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		nodes_his = GetThePaperPig.requestFakeHisData("0", "0", nodeId_int_his);
		time_xlength = (int) ((Stringtolong(time_end) - Stringtolong(time_start)) / 1000);

		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(Color.BLUE);// 颜色
		Paint paint1 = new Paint();
		paint1.setStyle(Paint.Style.STROKE);
		paint1.setAntiAlias(true);// 去锯齿
		paint1.setColor(Color.DKGRAY);
		paint.setTextSize(12); // 设置轴文字大小
		// 设置Y轴
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // 轴线
		// 刻度
		for (int i = 0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint); // 刻度
			try {
				canvas.drawText(YLabel[i], XPoint - 22,
						YPoint - i * YScale + 5, paint); // 文字
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint); // 箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);
		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 轴线
		// 刻度
		for (int i = 0; i * XScale < XLength; i++) {
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale,
					YPoint - 5, paint); // 刻度
			try {
				canvas.drawText(XLabel[i], XPoint + i * XScale - 10,
						YPoint + 20, paint); // 文字
				/*
				 * // 数据值 if (i > 0 && Data[i - 1] != -999 && Data[i] != -999)
				 * // 保证有效数据 canvas.drawLine(XPoint + (i - 1) * XScale, Data[i -
				 * 1], XPoint + i * XScale, Data[i], paint);
				 * canvas.drawCircle(XPoint + i * XScale, Data[i], 2, paint);
				 */
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);
		// 数据
		int cy;
		int cx;
		int cy2;
		int cx2;
		for (int i = 0; i < nodes_his.length - 1; i++) {
			Log.v("MAPDraw", "时间" + time_xlength);
			Log.v("MAPDraw", "时间" + nodes_his[i].time);
			cx = (int) ((Stringtolong(nodes_his[i].time) - Stringtolong(time_start)) / 1000);
			Log.v("MAPDraw", "" + cx);
			cx = (400 * cx) / time_xlength;
			cy = (int) nodes_his[i].tempture;
			canvas.drawCircle(cx + 40, YPoint - 80 - (int) (cy / 1.25), 1,
					paint);
			cx2 = (int) ((Stringtolong(nodes_his[i + 1].time) - Stringtolong(time_start)) / 1000);
			Log.v("MAPDraw", "" + cx);
			cx2 = (400 * cx2) / time_xlength;
			cy2 = (int) nodes_his[i + 1].tempture;
			canvas.drawLine(cx + 40, YPoint - 80 - (int) (cy / 1.25), cx2 + 40,
					YPoint - 80 - (int) (cy2 / 1.25), paint);

		}

		paint.setTextSize(16);

		canvas.drawText(Range, 50, 65, paint);
		paint.setTextSize(26); // 设置轴文字大小
		canvas.drawText(Title, 200, 40, paint);
	}

	public long Stringtolong(String time) {
		long longtime;
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try {
			date = formatter.parse(time);

		} catch (Exception e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		longtime = calendar.getTimeInMillis();
		return longtime;
	}
}
