package com.buaa.sensory.wsn_dr.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.buaa.sensory.wsn_dr.R;

public class TopoView extends View {
	Context context_topo;
	NodeClass[] nodes_topo = null;

	public void setNodes_topo(NodeClass[] nodes_topo) {
		this.nodes_topo = nodes_topo;
	}

	Canvas canvas_topo;
	boolean isgetteddata;

	public TopoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		context_topo = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		try {
			if (nodes_topo != null) {

				Log.v("Tag", "onDraw");
				int height = this.getHeight();
				int width = this.getWidth();
				// 画布背景为白色
				canvas.drawColor(getResources().getColor(R.color.white));

				// 绘制一跳节点
				int one_hop = 0;

				for (int i = 0; i < nodes_topo.length; i++) {
					if (nodes_topo[i].getTurnnode() == 0) {
						one_hop++;
					}

				}
				Log.v("Tag", "" + one_hop);
				int num = 0;
				for (int i = 0; i < nodes_topo.length; i++) {
					if (nodes_topo[i].getTurnnode() == 0) {
						draw_onehop(canvas, one_hop, num, nodes_topo[i].getId());
						num++;
					}
				}
				// 绘制路由
				Paint paint1 = new Paint();
				paint1.setAntiAlias(true);
				paint1.setColor(getResources().getColor(R.color.them_blue));
				canvas.drawCircle(width / 2, height / 2, 40, paint1);
				paint1.setColor(getResources().getColor(R.color.white));
				paint1.setTextSize(22);
				canvas.drawText("网关", width / 2 - 20, height / 2 + 5, paint1);

			} else {
				clean_canvas(canvas);
			}
		} catch (Exception e) {
			Log.e("Tag", "画图时异常");
			e.printStackTrace();
		}

	}

	public void clean_canvas(Canvas canvas) {
		canvas.drawColor(getResources().getColor(R.color.white));
	}

	public void draw_onehop(Canvas can, int onehop, int num, int id)
			throws Exception {
		/*
		 * 参数 can-画布，onehop-一跳节点数，num_序号，id-节点的id
		 */
		Log.v("Tag", "" + num);
		int height = this.getHeight();
		int width = this.getWidth();
		int d = 150;
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);

		Point point = new Point(width / 2, height / 2, onehop, num, 150);
		// 画连线
		paint2.setColor(getResources().getColor(R.color.black));
		can.drawLine(width / 2, height / 2, point.getX(), point.getY(), paint2);

		int two_hop = 0;
		for (int i = 0; i < nodes_topo.length; i++) {
			if (nodes_topo[i].getTurnnode() == id) {
				two_hop++;
			}

		}
		int num_two_hop = 0;
		for (int i = 0; i < nodes_topo.length; i++) {
			if (nodes_topo[i].getTurnnode() == id) {
				Point point_two = new Point(point.getX(), point.getY(),
						two_hop, num_two_hop, 80);
				Paint paint3 = new Paint();
				paint3.setAntiAlias(true);
				// 画连线
				paint3.setColor(getResources().getColor(R.color.black));
				can.drawLine(point.getX(), point.getY(), point_two.getX(),
						point_two.getY(), paint3);
				// 画圆
				paint3.setColor(getResources().getColor(R.color.gray_shallow));
				can.drawCircle(point_two.getX(), point_two.getY(), 16, paint3);
				paint3.setColor(getResources().getColor(R.color.white));
				paint3.setTextSize(16);
				can.drawText(Integer.toString(nodes_topo[i].getId()),
						point_two.getX() - 5, point_two.getY() + 6, paint3);
				num_two_hop++;
			}
		}
		// 画圆
		paint2.setColor(getResources().getColor(R.color.red));
		can.drawCircle(point.getX(), point.getY(), 20, paint2);
		paint2.setColor(getResources().getColor(R.color.white));
		paint2.setTextSize(18);
		if (id < 10) {
			can.drawText(Integer.toString(id), point.getX() - 5,
					point.getY() + 6, paint2);
		} else {
			can.drawText(Integer.toString(id), point.getX() - 10,
					point.getY() + 6, paint2);
		}

	}

}
