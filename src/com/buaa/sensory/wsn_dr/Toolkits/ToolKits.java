package com.buaa.sensory.wsn_dr.Toolkits;

import android.content.Context;
import android.content.SharedPreferences;
public class ToolKits {
	
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("com.sensor.lashou", Context.MODE_PRIVATE);
	}
	//保存共享参数
	public static void putBooble(Context context,String key,boolean value){
		//共享参数对象
		SharedPreferences sp =getSharedPreferences(context);
		//获得共享参数的编辑器
		SharedPreferences.Editor editor = sp.edit();
		//通过编辑器提交共享参数
		editor.putBoolean(key, value);
		//提交编辑内容
		editor.commit();	
	}
	//key值存在返回保存值，如果不存在返回默认值
	public static Boolean fetchBooble(Context context,String key,boolean defaultValue){
		
		return getSharedPreferences(context).getBoolean(key,defaultValue);
		
		
	}
}
