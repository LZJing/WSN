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
	public static void putString(Context context,String key,String value){
		//共享参数对象
		SharedPreferences sp =getSharedPreferences(context);
		//获得共享参数的编辑器
		SharedPreferences.Editor editor = sp.edit();
		//通过编辑器提交共享参数
		editor.putString(key, value);
		//提交编辑内容
		editor.commit();	
	}
	public static String fetchString(Context context,String key,String defaultValue){
		
		return getSharedPreferences(context).getString(key,defaultValue);
		
		
	}
	public static void putInt(Context context,String key,int value) {
		//共享参数对象
		SharedPreferences sp =getSharedPreferences(context);
		//获得共享参数的编辑器
		SharedPreferences.Editor editor = sp.edit();
		//通过编辑器提交共享参数
		editor.putInt(key, value);
		//提交编辑内容
		editor.commit();
	}
	
	public static int fetchInt(Context context,String key,int defaultValue){
		
		return getSharedPreferences(context).getInt(key,defaultValue);
		
		
	}
}
