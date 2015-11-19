package com.buaa.sensory.wsn_dr.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Point {

	private int parent_id;
	private int parent_x;
	private int parent_y;

	private int id;
	private int x;
	private int y;

	private int count;
	private int num;
	private int d;

	public Point(int parent_id, int parent_x, int parent_y, int id, int onehop,
			int num, int d) {
		this.parent_id = parent_id;
		this.parent_x = parent_x;
		this.parent_y = parent_y;
		this.id = id;
		this.count = onehop;
		this.num = num;
		this.d = d;
	}

	public Point(int parent_x, int parent_y, int onehop, int num, int d) {
		this.parent_x = parent_x;
		this.parent_y = parent_y;
		this.count = onehop;
		this.num = num;
		this.d = d;
	}

	public int getParent_id() {
		return parent_id;
	}

	public int getParent_x() {
		return parent_x;
	}

	public int getParent_y() {
		return parent_y;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		double delt_x_double = Math.sin(2 * Math.PI / count * num) * d;
		int delt_x_int = Integer.parseInt(new java.text.DecimalFormat("0")
				.format(delt_x_double));
		x = parent_x + delt_x_int;
		return x;
	}

	public int getY() {
		double delt_y_double = Math.cos(2 * Math.PI / count * num) * d;
		int delt_y_int = Integer.parseInt(new java.text.DecimalFormat("0")
				.format(delt_y_double));
		y = parent_y + delt_y_int;
		return y;
	}

	public void drawline(Canvas can, Paint paint) {
		can.drawLine(getParent_x(), getParent_y(), getX(), getY(), paint);
	}

	public void drawCircle(Canvas can, float radius, Paint paint) {
		can.drawCircle(getX(), getY(), radius, paint);
	}

}
