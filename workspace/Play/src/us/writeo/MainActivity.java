package us.writeo;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity
{

	/** Messenger for communicating with service. */
	Messenger mService = null;
	
	/** Flag indicating whether we have called bind on the service. */
	
	boolean mIsBound;
	/** Some text view we are using to show state information. */
	TextView console;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal);
		
		console = (TextView) findViewById(R.id.console);
		
		// Watch for button clicks. 
		Button button;
		
		button = (Button)findViewById(R.id.start); 		
		button.setOnClickListener(mStartListener); 
		button = (Button)findViewById(R.id.stop); 
		button.setOnClickListener(mStopListener);
		
		button = (Button)findViewById(R.id.connect); 		
		button.setOnClickListener(mConnectListener); 
		button = (Button)findViewById(R.id.disconnect); 
		button.setOnClickListener(mDisconnectListener);
		
		startService(new Intent(MainActivity.this, MudServerService.class));
    }
	
	private OnClickListener mStartListener = new OnClickListener() { 
	    public void onClick(View v) { 
		// Make sure the service is started. It will continue running 
		// until someone calls stopService(). The Intent we use to find 
		// the service explicitly specifies our service component, because 
		// we want it running in our own process and don't want other 
		// applications to replace it. 
		
		startService(new Intent(MainActivity.this, MudServerService.class)); } }; 
		
	private OnClickListener mStopListener = new OnClickListener() { 
		public void onClick(View v) { 
	    // Cancel a previous call to startService(). Note that the 
		// service will not actually stop at this point if there are 
		// still bound clients. 
		
		stopService(new Intent(MainActivity.this, MudServerService.class)); 
	    } 
	}; 
	
	
	private OnClickListener mConnectListener = new OnClickListener() { 
	    public void onClick(View v) { 
			// Make sure the service is started. It will continue running 
			// until someone calls stopService(). The Intent we use to find 
			// the service explicitly specifies our service component, because 
			// we want it running in our own process and don't want other 
			// applications to replace it. 

			//startService(new Intent(MainActivity.this, MudClientService.class)); } }; 
			doBindService();
		}
    };
	
	private OnClickListener mDisconnectListener = new OnClickListener() { 
		public void onClick(View v) { 
			// Cancel a previous call to startService(). Note that the 
			// service will not actually stop at this point if there are 
			// still bound clients. 

			//stopService(new Intent(MainActivity.this, MudClientService.class)); 
			doUnbindService();
	    } 
	}; 
	
	/** * Handler of incoming messages from service. */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MudClientService.MSG_REPLY:
				    String reply = (String)msg.getData().get("data");
				    addText("Received from service: " + reply);
				break;
				
				default:
				super.handleMessage(msg);
			}
		}
	}
	
	/** * Target we publish for clients to send messages to IncomingHandler. */
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	/** * Class for interacting with the main interface of the service. */
	
	private ServiceConnection mConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName className,IBinder service) {
			// This is called when the connection with the service has been        
			// established, giving us the service object we can use to        
			// interact with the service.  We are communicating with our        
			// service through an IDL interface, so get a client-side        
			// representation of that from the raw service object.        
			
			mService = new Messenger(service);
			addText("Attached.");
			
			// We want to monitor the service for as long as we are
			// connected to it.        
			try {
				Message msg = Message.obtain(null, MudClientService.MSG_CONNECT);
				msg.replyTo = mMessenger;
				mService.send(msg);
				
				// Give it some value as an example.
				msg = Message.obtain(null, MudClientService.MSG_SEND, 0, 0);
				Bundle b = new Bundle();
				b.putString("command", "I command");
				msg.setData(b);
				mService.send(msg);
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even            
				// do anything with it; we can count on soon being            
				// disconnected (and then reconnected if it can be restarted)            
				// so there is no need to do anything here.        
			}
			// As part of the sample, tell the user what happened.        
			Toast.makeText(MainActivity.this, R.string.remote_service_connected, Toast.LENGTH_SHORT).show();
		}
		
		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been        
			// unexpectedly disconnected -- that is, its process crashed.        
			
			mService = null;
			addText("Disconnected.");
			
			// As part of the sample, tell the user what happened.        
			Toast.makeText(MainActivity.this, R.string.remote_service_disconnected, Toast.LENGTH_SHORT).show();
		}
	};
	
	void doBindService() {
		// Establish a connection with the service.  We use an explicit    
		// class name because there is no reason to be able to let other    
		// applications replace our component.    
		
		if(!mIsBound){
		bindService(new Intent(this, MudClientService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
		addText("Binding.");
		}
	}
	
	void doUnbindService() {
		if (mIsBound) {
			// If we have received the service, and hence registered with        
			// it, then now is the time to unregister.        
			
			if (mService != null) {
				try {
					Message msg = Message.obtain(null,MudClientService.MSG_DISCONNECT);
					msg.replyTo = mMessenger;
					mService.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service                
					// has crashed.            
				}
			}
			// Detach our existing connection.        
			unbindService(mConnection);
			mIsBound = false;
			addText("Unbinding.");
		}
	}
	
	public void addText(String text){
		String ctext = console.getText().toString();
		console.setText(ctext+"\n"+text);
	}
}

