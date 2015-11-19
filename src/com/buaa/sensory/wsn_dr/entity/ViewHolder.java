package com.buaa.sensory.wsn_dr.entity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 当Listie有大量的数据需要加载的时候，会占据大量内存，影响性能，这时候 就需要按需填充并重新使用view来减少对象的创建
 * 最快的方式是定义一个Pewholder，将convex的tag设置为Pewholder,不为空时重新使用即可
 */
public final class ViewHolder {
	public ImageView img;
	// public TextView title;
	// public TextView info;
	public TextView node_id;
	public TextView node_time;
	public TextView node_temp;
	public TextView node_GPS_lon;
	public TextView node_GPS_lat;
	public Button viewBtn;
}
