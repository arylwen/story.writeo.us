package us.writeo;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.net.*;

public class MudClientService extends Service{

	private static final String TAG = "MCS";

	private NotificationManager mNM;
	// Unique Identification Number for the Notification.    
	// We use it on Notification start, and to cancel it.    

	private int NOTIFICATION = R.string.mud_server_service_started;

	/**     * Class for clients to access.  Because we know this service always     
	 * runs in the same process as its clients, we don't need to deal with     
	 * IPC.     */

	/*public class MudClientBinder extends Binder {
		MudClientService getService() {
			return MudClientService.this;
		}
	}*/

	Messenger mClient = null;
	/** Holds last value set by a client. */
	String mValue = null;
	
	/**     
	* Command to the service to register a client, receiving callbacks     
	* from the service.  The Message's replyTo field must be a Messenger of     
	* the client where callbacks should be sent.
	*/
	
	static final int MSG_CONNECT = 1;
	
	/**     
	* Command to the service to unregister a client, ot stop receiving callbacks     
	* from the service.  The Message's replyTo field must be a Messenger of     
	* the client as previously given with MSG_REGISTER_CLIENT.     
	*/
	
	static final int MSG_DISCONNECT = 2;
	
	/**     
	* Command to service to set a new value.  This can be sent to the     
	* service to supply a new value, and will be sent by the service to     
	* any registered clients with the new value.     
	*/
	
	static final int MSG_SEND = 3;
	
	static final int MSG_REPLY = 4;
	
	Socket echoSocket = null; 
	PrintWriter out = null; 
	BufferedReader in = null;
	boolean running = true;
	String host = null;
	String port = null;
	
	/**     * Handler of incoming messages from clients.     */
	
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_CONNECT:
				    mClient = msg.replyTo;
					host = (String)msg.getData().get("host");
					port = (String)msg.getData().get("port");
					Thread t = new Thread(new ClientLoop());
					t.start();
					try{ Thread.sleep(500); }catch (InterruptedException e){};
					if(out == null){
					    int retries = 3;
					    while(retries > 0){						
							t = new Thread(new ClientLoop());
							t.start();
							try{ Thread.sleep(500); }catch (InterruptedException e){};
							if(out != null ) break;
							Log.e(TAG, "still trying");
						    retries--;
						}
					}
					break;
				case MSG_DISCONNECT:
				     running = false;
					 break;
			    case MSG_SEND:				
				     mValue = (String)msg.getData().get("command")+"\n";
					 Log.e(TAG, "received "+mValue);
					 if(out == null){
					    int retries = 3;
					    while(retries > 0){

							 t = new Thread(new ClientLoop());
							 t.start();
							 try{ Thread.sleep(500); }catch (InterruptedException e){};
							 if(out != null ) break;
							 Log.e(TAG, "still trying");
						     retries--;
						}
					 }
					Log.e(TAG, "sending "+mValue+ " out "+ out);
				    if(out != null){ out.write(mValue); out.flush();}
					//TODO what to do if still cannot connect
				break;
				
				default:
				    super.handleMessage(msg);
			}
		}
	}
	
	/**     * Target we publish for clients to send messages to IncomingHandler.     */
	
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	
	@Override
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting.  We put an icon in the status bar.    

		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Received start id " + startId + ": " + intent);

		Log.e(TAG, "About to start client");
		

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
		return mMessenger.getBinder();
	}

	// This is the object that receives interactions from clients.  See    
	// RemoteService for a more complete example.    
	//private final IBinder mBinder = new MudClientBinder();

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
	
	
	class ClientLoop implements Runnable{
		public void run(){
		    try { 
			    echoSocket = new Socket("localhost", 10008); 
				out = new PrintWriter(echoSocket.getOutputStream(), true); 
				in = new BufferedReader(new InputStreamReader( echoSocket.getInputStream())); 
			
			
				String mudInput; 	
				while(running){
				    StringBuffer messageBuffer = new StringBuffer();
		
				    while (in.ready() && ((mudInput = in.readLine()) != null)) { 	 
					
					    messageBuffer.append(mudInput); 
				    } 
					
					if(messageBuffer.length() > 0){
					try {
						Bundle b = new Bundle();
						b.putString("data", messageBuffer.toString());
						Message m = Message.obtain(null, MSG_REPLY, 0, 0);
						m.setData(b);
						mClient.send(m);
				    } catch (RemoteException e) {
						// The client is dead.  Remove it from the list;                            
						// we are going through the list from back to front                            
						// so this is safe to do inside the loop.                            
						mClient = null;
						running = false;
						//TODO maybe send a disconnected message back????
					}
					}
			    }
			
			} catch (UnknownHostException e) { 
			    System.err.println("Don't know about host: " + "localhost"); 
				System.exit(1); 
			} catch (IOException e) { 
			    System.err.println("Couldn't get I/O for " + "the connection to: " + "localhost"); 
				System.exit(1); 
			} finally {
				if(out != null) out.close(); 	
				try{
				   if(in != null) in.close();
				} catch (IOException e){
					
				}
				try{
				    if(echoSocket != null) echoSocket.close();
				} catch (IOException e){
					
				}
			}
		
		}
	}
	

}
