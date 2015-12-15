package com.buaa.sensory.wsn_dr.view;


import java.util.List;

import com.buaa.sensory.wsn_dr.Toolkits.Calculate;
import com.buaa.sensory.wsn_dr.model.NodeData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class HisLineView extends View {

	
	public static int d = 50;
	public static int scale = 700-d;
	public static int lengthOfDegree = 10;
	public static int l = (scale -d)/10;
	
	
	//需要传入的参数
	List<NodeData> list = null;
	int allTime=60000;
	long currentTime = 1449066665000L;
	int type = 0; //0代表温度，1代表湿度，2代表光强
	
	double maxValue=100;
	double minValue=0;
	
	
	
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public void setAllTime(int allTime) {
		this.allTime = allTime;
	}

	public void setType(int type) {
		this.type = type;
	}


	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public void setList(List<NodeData> list){
		this.list = list;
	}
	

	public HisLineView(Context context, AttributeSet set) {
		super(context, set);
		
	}



	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);				//画北京
		drawFrame(canvas);							//画边框
		Log.v("Tag", "绘制");
		if(list!=null){
			drawDataLine(canvas,list,allTime);
			Log.v("Tag", "绘制,list有效");
		}
		
		
				
	}



	private void drawDataLine(Canvas canvas, List<NodeData> list, int allTime2) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);				//去锯齿
		paint.setColor(Color.RED);			//颜色
		paint.setStrokeWidth(6);				//设置画笔宽度
		if(list.size()==1){
			PointForDraw p1 = getXandYLocation(list.get(0));
			canvas.drawCircle(p1.x, p1.y, 3, paint);
		}
		for (int i = 0; i < list.size()-1; i++) {
			
			PointForDraw p1 = getXandYLocation(list.get(i));
			PointForDraw p2 = getXandYLocation(list.get(i+1));
			drawShortLine(canvas,p1,p2);
			canvas.drawCircle(p1.x, p1.y, 3, paint);
			canvas.drawCircle(p2.x, p2.y, 3, paint);
		}
		
		
	}


	//由NodeData中的数据，获得xy坐标
	private PointForDraw getXandYLocation(NodeData nodeData) {
//		long currentTime = System.currentTimeMillis();
		
		long lastTime = nodeData.getLastTime();
		double tem=0;
		switch (type) {
		case 0:
			tem = Calculate.getTemperature(nodeData.getTemperature());
			break;
		case 1:
			tem = Calculate.getHumidity(nodeData.getHumidity());
			break;
		case 2:
			tem = Calculate.getLight(nodeData.getLight());
			break;
		default:
			break;
		}
		int x =(int) (scale-(currentTime-lastTime)*(scale-d)/allTime);
		int y = (int) (scale-(scale-d)*tem/(maxValue-minValue));
		
		return new PointForDraw(x,y);
	}


	private void drawShortLine(Canvas canvas,PointForDraw point1,PointForDraw point2) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);				//去锯齿
		paint.setColor(Color.BLUE);			//颜色
		paint.setStrokeWidth(4);				//设置画笔宽度
		Log.v("Tag", "x="+point1.x+" y="+point1.y);
		canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint); // 轴线
	}


	private void drawFrame(Canvas canvas) {
//		double delt = getDegreeOfY(list,t);
		double scaleY = maxValue-minValue;
		int scaleX = allTime/1000;
		
		DisplayMetrics metric = new DisplayMetrics();  
		int width = metric.widthPixels;     // 屏幕宽度（像素）  
		int height = metric.heightPixels;   // 屏幕高度（像素）  
		Log.v("draw", width+""+height);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);				//去锯齿
		paint.setColor(Color.BLACK);			//颜色
		paint.setStrokeWidth(4);				//设置画笔宽度
		
		Paint paint2 = new Paint();
		paint2.setStyle(Paint.Style.STROKE);
		paint2.setAntiAlias(true);				//去锯齿
		paint2.setColor(Color.BLACK);			//颜色
		paint2.setTextSize(20);
		// 画Y轴
		canvas.drawLine(d, d, scale, d, paint); // 轴线
		canvas.drawLine(d, d, d, scale, paint); // 轴线
		canvas.drawLine(d, scale, scale, scale, paint);
		canvas.drawLine(scale, scale, scale, d, paint);
		
		//画Y轴上的刻度
		for (int i = 0; i <= 10; i++) {
			canvas.drawLine(d, scale-i*l, d+lengthOfDegree, scale-i*l, paint);
			canvas.drawText(String.valueOf((scaleY+minValue)/10*i), 0, scale-i*l+10, paint2);
		}
		//画X轴上的刻度
		for (int i = 0; i <= 10; i++) {
			canvas.drawLine(d+i*l, scale-lengthOfDegree, d+i*l, scale, paint);
			canvas.drawText(String.valueOf(scaleX/10*i), scale-i*l-15,scale+25, paint2);
		
		}
		
	}

}
class PointForDraw{
	public int x;
	public int y;
	public PointForDraw(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
}
