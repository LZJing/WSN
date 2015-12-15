package com.buaa.sensory.wsn_dr.Toolkits;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calculate {
	
	 public static String transferLongToDate(Long millSec){
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	    
	     Date date= new Date(millSec);
	     return sdf.format(date);
	}
	
	public static double getLight(double light){
		
		DecimalFormat df = new DecimalFormat("#.00");
		String str = df.format((4095/light-1)*10000);
		return Double.parseDouble(str);
	}
	
	public static double getTemperature(double tem){
		
		DecimalFormat df = new DecimalFormat("#.00");
		String str = df.format(tem*0.01-40.1);
		return Double.parseDouble(str);
	}
	
	public static double getHumidity(double hum){
		
		DecimalFormat df = new DecimalFormat("#.00");
		String str = df.format(-2.0468+0.0367*hum-hum*hum*1.5955e-6);
		return Double.parseDouble(str);
	}

}
