package org.twodee.acknet;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver  {
		
	@Override
	public void onReceive(Context context, Intent intent) {
		
		System.out.println("onReceive!");
		
		// Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
        GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        System.out.println("Activity...");
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
        System.out.println("Fin...");
		
	}
	
	

}
