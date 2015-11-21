package com.buaa.sensory.wsn_dr.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {
	
	public static String getDateFormat(Long l){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
		return formatter.format(new Date(l));
	}
	/*
	 * 查询当前系统时间，以字符串格式返回
	 */
	public static String getSysCurDateTime() 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate);  
		return str;
	}
	/*
	 * 系统当前时间向前减去miniute个分钟的得到的时间
	 */
	public static String getAddedDateTime(int minute){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		long Milseconds = (long)(minute*1000*60);
		long totalMilliseconds = System.currentTimeMillis()-Milseconds;
		
		Date addDate = new Date(totalMilliseconds);//获取当前时间       
		String str = formatter.format(addDate);  
		return str;
		
	}
}
