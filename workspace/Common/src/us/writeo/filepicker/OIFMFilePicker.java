package us.writeo.filepicker;

import android.app.*;
import android.content.*;
import android.net.*;
import java.io.*;
import org.openintents.intents.*;
import us.writeo.common.*;

public class OIFMFilePicker implements FilePicker
{
    private Activity activity;

	public OIFMFilePicker(Activity aActivity)
	{
		activity = aActivity;
	}

	public void pickFileForNew()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(FileManagerIntents.ACTION_PICK_FILE);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setData(startDir);

		intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.save_title));
		intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.save_button));

		activity.startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);
	}
	
	public void pickFileForSave()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(FileManagerIntents.ACTION_PICK_FILE);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setData(startDir);
		
		intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.save_title));
		intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.save_button));
		
		activity.startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);
	}

	public void pickFileForOpen()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(FileManagerIntents.ACTION_PICK_FILE);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setData(startDir);

		intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.open_title));
		intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.open_button));

		activity.startActivityForResult(intent2Browse, OPEN_FILE_REQUEST_CODE);
	}

    public void pickDirectory(){}
}
