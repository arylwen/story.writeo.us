/**
 * 
 */
package us.writeo.places;

import android.graphics.*;
import android.util.*;
import android.view.*;


/**
 * @author impaler
 *
 * The Main thread which contains the game loop. The thread must have access to 
 * the surface view and holder to trigger events every game tick.
 */
public class MainThread extends Thread {
	
	private static final String TAG = MainThread.class.getSimpleName();

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private MainGamePanel gamePanel;

	// flag to hold game state 
	private boolean running;
	public void setRunning(boolean running) {
		this.running = running;
	}

	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	@Override
	public void run() {
		long tickCount = 0L;
		Log.d(TAG, "Starting game loop");
		while (running) {
			tickCount++;
			// update game state 
			// render state to the screen
			//gamePanel.postInvalidate();
			Canvas canvas = surfaceHolder.lockCanvas();
			Paint myPaint = new Paint(); 
			myPaint.setColor(Color.rgb(255, 255, 255)); 
			myPaint.setStrokeWidth(1);
			myPaint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(100,100,110,110,myPaint);
			surfaceHolder.unlockCanvasAndPost(canvas);
			//surfaceHolder.
		}
		Log.d(TAG, "Game loop executed " + tickCount + " times");
	}
	
}
