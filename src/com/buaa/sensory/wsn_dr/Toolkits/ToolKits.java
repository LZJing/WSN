package com.buaa.sensory.wsn_dr.Toolkits;

import android.content.Context;
import android.content.SharedPreferences;
public class ToolKits {
	
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("com.sensor.lashou", Context.MODE_PRIVATE);
	}
	//���湲�����
	public static void putBooble(Context context,String key,boolean value){
		//�����������
		SharedPreferences sp =getSharedPreferences(context);
		//��ù�������ı༭��
		SharedPreferences.Editor editor = sp.edit();
		//ͨ���༭���ύ�������
		editor.putBoolean(key, value);
		//�ύ�༭����
		editor.commit();	
	}
	//keyֵ���ڷ��ر���ֵ����������ڷ���Ĭ��ֵ
	public static Boolean fetchBooble(Context context,String key,boolean defaultValue){
		
		return getSharedPreferences(context).getBoolean(key,defaultValue);
		
		
	}
}
