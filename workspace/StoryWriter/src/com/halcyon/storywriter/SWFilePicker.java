package com.halcyon.storywriter;

import android.app.*;
import android.content.*;
import android.net.*;
import com.halcyon.filepicker.*;
import java.io.*;
import org.openintents.intents.*;

public class SWFilePicker implements FilePicker
{
    private Activity activity;

	public SWFilePicker(Activity aActivity)
	{
		activity = aActivity;
	}

	public void pickFileForSave()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(FilePickerIntents.ACTION_PICK_FILE);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setData(startDir);

		intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.save_title));
		intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.save_button));

		activity.startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);
	}

	public void pickFileForOpen()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(FilePickerIntents.ACTION_PICK_FILE);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setData(startDir);

		intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.open_title));
		intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.open_button));

		activity.startActivityForResult(intent2Browse, OPEN_FILE_REQUEST_CODE);
	}


}
