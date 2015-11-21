package com.buaa.sensory.wsn_dr.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {
	
	public static String getDateFormat(Long l){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
		return formatter.format(new Date(l));
	}
	/*
	 * ��ѯ��ǰϵͳʱ�䣬���ַ�����ʽ����
	 */
	public static String getSysCurDateTime() 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
		Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
		String str = formatter.format(curDate);  
		return str;
	}
	/*
	 * ϵͳ��ǰʱ����ǰ��ȥminiute�����ӵĵõ���ʱ��
	 */
	public static String getAddedDateTime(int minute){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		long Milseconds = (long)(minute*1000*60);
		long totalMilliseconds = System.currentTimeMillis()-Milseconds;
		
		Date addDate = new Date(totalMilliseconds);//��ȡ��ǰʱ��       
		String str = formatter.format(addDate);  
		return str;
		
	}
}
