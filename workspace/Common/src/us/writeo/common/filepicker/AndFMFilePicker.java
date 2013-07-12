package us.writeo.common.filepicker;

import android.app.*;
import android.content.*;
import android.net.*;
import java.io.*;

public class AndFMFilePicker implements FilePicker
{
    private Activity activity;
	
	public AndFMFilePicker(Activity aActivity)
	{
		activity = aActivity;
	}

	
	public void pickFileForNew()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(Intent.ACTION_PICK);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");

		intent2Browse.putExtra("browser_line", "enabled");
		intent2Browse.putExtra("browser_line_textfield", "story.txt");

		intent2Browse.putExtra("explorer_title", "New file namw...");
		intent2Browse.putExtra("browser_list_background_color", "66000000");

		activity.startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);


	}
	
	public void pickFileForSave()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(Intent.ACTION_PICK);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");

		intent2Browse.putExtra("browser_line", "enabled");
		intent2Browse.putExtra("browser_line_textfield", "story.txt");

		intent2Browse.putExtra("explorer_title", "Save As...");
		intent2Browse.putExtra("browser_list_background_color", "66000000");

		activity.startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);
		
		
	}

	public void pickFileForOpen()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(Intent.ACTION_PICK);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");

		intent2Browse.putExtra("explorer_title", "Select file...");
		intent2Browse.putExtra("browser_list_background_color", "66000000");

		activity.startActivityForResult(intent2Browse, OPEN_FILE_REQUEST_CODE);
	}
	
    public void pickDirectory(){}
	
}
