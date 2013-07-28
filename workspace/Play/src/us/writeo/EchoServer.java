package us.writeo;

import android.util.*;
import java.io.*;
import java.net.*; 

public class EchoServer extends Thread { 

    private static final String TAG = "EchoServer";

    protected static boolean serverContinue = true; 
	protected Socket clientSocket; 
	
	
	
	public static void main(String[] args) { 
	    
		ServerSocket serverSocket = null; 
		
		try { 
		    serverSocket = new ServerSocket(10008); 
			Log.e (TAG, "Connection Socket Created"); 
			
			try { 
			    while (serverContinue) { 
			        //serverSocket.setSoTimeout(100000); 
				    Log.e (TAG, "Waiting for Connection"); 
				
				    try { 
				        new EchoServer (serverSocket.accept()); 
				    } catch (SocketTimeoutException ste) { 
				        Log.e (TAG, "Timeout Occurred"); 
				    } 
			    } 
		    } catch (IOException e) { 
		        Log.e(TAG, "Accept failed."); 
			    //System.exit(1); 
		    } 		
	    } catch (IOException e) { 
		    Log.e(TAG, "Could not listen on port: 10008."); 
			//System.exit(1); 
	    } finally { 
		    try { Log.e (TAG, "Closing Server Connection Socket"); 
			if(serverSocket != null) serverSocket.close(); 
		    } catch (IOException e) { 
		    Log.e(TAG, "Could not close port: 10008."); 
			//System.exit(1); 
		    } 
		    Log.e(TAG, "Server started.");
	   } 
    } 
	
private EchoServer (Socket clientSoc) { 
	clientSocket = clientSoc;
	start(); 
} 
	
public void run() { 
	Log.e (TAG, "New Communication Thread Started"); 
	boolean connected = true;
	try { 
	        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 
		    BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream())); 
		
		    String inputLine; 
			while(connected){
				out.println("connected\n");out.flush();
		        while ((inputLine = in.readLine()) != null) { 
		            Log.e (TAG, "Server: " + inputLine); 
			        if (inputLine.equals("?")) 
				        inputLine = new String ("\"Bye.\" ends Client, " + "\"End Server.\" ends Server"); 
			        out.println(inputLine);out.flush();
			        if (inputLine.equals("Bye.")) break; 
			        if (inputLine.equals("End Server.")) 
				        connected = false; 
			    } 
				if(!connected) break;
				
			}
			
			out.close(); 
			in.close(); 
			clientSocket.close(); 
		} catch (IOException e) { 
		    Log.e(TAG,"Problem with Communication Server"); 
			//System.exit(1); 
		} 
	} 
	
	public static void stopServer(){
		serverContinue = false;
	}
} 
