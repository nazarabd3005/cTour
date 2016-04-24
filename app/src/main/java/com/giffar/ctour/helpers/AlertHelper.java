/**
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Toast;

import com.giffar.ctour.activities.BaseActivity;

public class AlertHelper {

	private static AlertHelper alertHelper;
	
	public static AlertHelper getInstance(){
		if (alertHelper == null) {
			alertHelper = new AlertHelper();
		}
		
		return alertHelper;
	}
	
	public void showAlert(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public void showAlertDialog(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("Ok", new OnClickListener() {
			@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
	}

	public AlertDialog.Builder showAlertWithoutListener(Context context,String title, String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setTitle(title);
		adb.setMessage(message);
		return adb;
	}
	
	
	public void showAlertChangeActivity(final Context context, View layout, final Class<?> destination, String title, String message, String postiveButton) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
	
		if(layout!=null){
			adb.setView(layout);
		}else{
			adb.setTitle(title);
			adb.setMessage(message);
		}
		
		if(postiveButton==null){postiveButton="OK";}
		
		adb.setPositiveButton(postiveButton, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface di, int argu) {
				if(destination!=null){
					((BaseActivity)context).changeActivity(destination);
				}else{
					di.dismiss();
				}
				
			}
		});
		
		adb.create().show();
		
	}
	
}
