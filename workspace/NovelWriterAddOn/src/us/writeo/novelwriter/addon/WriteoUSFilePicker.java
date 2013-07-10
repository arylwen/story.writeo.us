package us.writeo.novelwriter.addon;

import android.app.*;
import android.content.*;
import android.net.*;
import java.io.*;
import org.openintents.intents.*;
import us.writeo.filepicker.*;
import us.writeo.intents.*;

public class WriteoUSFilePicker implements FilePicker
{
		private Activity activity;
		private WriteoUSIntentProvider intentProvider;

		public WriteoUSFilePicker(Activity aActivity)
		{
			activity = aActivity;
			intentProvider = new WriteoUSIntentProvider();
		}

		public void pickFileForNew()
		{
			Intent intent2Browse = new Intent();
			intent2Browse.setAction(intentProvider.getActionPickFile());
			Uri startDir = Uri.fromFile(new File("/sdcard"));
			intent2Browse.setData(startDir);

			intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.new_title));
			intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.new_button));
			intent2Browse.putExtra("intentProvider", intentProvider);

			activity.startActivityForResult(intent2Browse, NEW_FILE_REQUEST_CODE);
		}

		public void pickFileForSave()
		{
			Intent intent2Browse = new Intent();
			intent2Browse.setAction(intentProvider.getActionPickFile());
			Uri startDir = Uri.fromFile(new File("/sdcard"));
			intent2Browse.setData(startDir);

			intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.save_title));
			intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.save_button));
			intent2Browse.putExtra("intentProvider", intentProvider);

			activity.startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);
		}

		public void pickFileForOpen()
		{
			Intent intent2Browse = new Intent();
			intent2Browse.setAction(intentProvider.getActionPickFile());
			Uri startDir = Uri.fromFile(new File("/sdcard"));
			intent2Browse.setData(startDir);

			intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.open_title));
			intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.open_button));
			intent2Browse.putExtra("intentProvider", intentProvider);

			activity.startActivityForResult(intent2Browse, OPEN_FILE_REQUEST_CODE);
		}

		public void pickDirectory()
		{
			Intent intent2Browse = new Intent();
			intent2Browse.setAction(intentProvider.getActionPickDirectory());
			Uri startDir = Uri.fromFile(new File("/sdcard"));
			intent2Browse.setData(startDir);

			intent2Browse.putExtra(FileManagerIntents.EXTRA_TITLE, activity.getString(R.string.new_title));
			intent2Browse.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, activity.getString(R.string.new_button));
			intent2Browse.putExtra("intentProvider", intentProvider);

			activity.startActivityForResult(intent2Browse, NEW_FILE_REQUEST_CODE);
		}

	}
