package com.ebizu.manis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author kazao
 */
public class Logger {

    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void l(String message) {
    	if(Session.getInstance().isDebug()){
    		System.out.println(Logger.class.getPackage().getName() + " -- " + formatter.format(Calendar.getInstance().getTime()) + " -- " + message);
    	}
    }
}
