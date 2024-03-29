package us.writeo;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import us.writeo.*;

public class MudServerService extends Service{
    
   private static final String TAG = "MSS";

   private NotificationManager mNM;
   // Unique Identification Number for the Notification.    
   // We use it on Notification start, and to cancel it.    
   
   private int NOTIFICATION = R.string.mud_server_service_started;
   
   /**     * Class for clients to access.  Because we know this service always     
   * runs in the same process as its clients, we don't need to deal with     
   * IPC.     */
   
   public class LocalBinder extends Binder {
	   MudServerService getService() {
		   return MudServerService.this;
		}
	}
	
	@Override
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		// Display a notification about us starting.  We put an icon in the status bar.    

		showNotification();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Received start id " + startId + ": " + intent);
		
		Log.e(TAG, "About to start server");
		new Thread(new Runnable(){
			public void run(){
				EchoServer.main(null);
			}
		}).start();

		// We want this service to continue running until it is explicitly        
		// stopped, so return sticky.        
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		EchoServer.stopServer();
		// Cancel the persistent notification.        
		mNM.cancel(NOTIFICATION);
		
		// Tell the user we stopped.        
		Toast.makeText(this, R.string.mud_server_service_stopped, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	// This is the object that receives interactions from clients.  See    
	// RemoteService for a more complete example.    
	private final IBinder mBinder = new LocalBinder();
	
	/**     
	* Show a notification while this service is running.     */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the expanded notification        
		CharSequence text = getText(R.string.mud_server_service_started);
		
		// Set the icon, scrolling text and timestamp        
		Notification notification = new Notification(R.drawable.ic_launcher, text,System.currentTimeMillis());
		
		// The PendingIntent to launch our activity if the user selects this notification        
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		    new Intent(this, MainActivity.class), 0);
			
		// Set the info for the views that show in the notification panel.        
		notification.setLatestEventInfo(this, getText(R.string.mud_server_service_label),text, contentIntent);
		
        // Send the notification.        
		mNM.notify(NOTIFICATION, notification);
	}
}
