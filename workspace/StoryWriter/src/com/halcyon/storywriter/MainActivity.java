package com.halcyon.storywriter;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.halcyon.storywriter.template.*;
import java.io.*;
import java.util.*;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity 
    implements ChooseTemplateDialogFragment.TemplateDialogListener, Coloriser.CounterListener
{

	private final static int MENU_NEW_FILE = Menu.FIRST;
	private final static int MENU_SAVE_FILE = Menu.FIRST + 1;
	private final static int MENU_OPEN_FILE = Menu.FIRST + 2;
	private final static int MENU_SAVE_FILE_AS = Menu.FIRST + 3;
	private final static int MENU_CHOOSE_STRUCT = Menu.FIRST + 4;
	
	private final static int SAVE_FILE_REQUEST_CODE = 10;
	private final static int OPEN_FILE_REQUEST_CODE = 20;

    private EditText text;
	private EditText prompt;
	private TextView title = null;
	private TextView templateName;
	private TextView templateSummary;
	private String fileName = "";
	private boolean isUntitled = true;
	private boolean isChanged = false;
	
	private final StructureTemplateHelper helper = new StoryTemplateHelper();
	private TemplateFileManager tfm;
	StructureFileTemplate currentFileTemplate;
	Coloriser coloriser;

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
						
        setContentView(R.layout.edit);
		
		text = (EditText) findViewById(R.id.note);
		text.setHorizontallyScrolling(false);

		// setup the scroll view to fill the parent
		ScrollView scroll = (ScrollView) findViewById(R.id.scroll);	
		if (scroll != null)
		{
			scroll.setFillViewport(true);
			scroll.setHorizontalScrollBarEnabled(false);
		}
		
		prompt = (EditText) findViewById(R.id.prompt);
		
		View customNav = LayoutInflater.from(this).inflate(R.layout.custom_title_bar, null);		
		title = (TextView) customNav.findViewById(R.id.notetitle);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		getSupportActionBar().setDisplayShowHomeEnabled(true); 
		
		fileName = getResources().getString( R.string.newFileName);
		templateName = (TextView) findViewById(R.id.template_name);
		templateSummary = (TextView) findViewById(R.id.template_summary);
		
		coloriser = new Coloriser(text, helper);
		tfm = new TemplateFileManager(this);
		List<StructureFileTemplate> templates = tfm.getStructureTemplates();
		onTemplateChosen(templates.get(0));	
		coloriser.setCounterListener(this);
		
		text.addTextChangedListener( new TextWatcher() {
				public void onTextChanged(CharSequence one, int a, int b, int c) {
					coloriser.onTextChanged(one, a,b,c);
					isChanged = true;
				}
					
				// complete the interface
				public void afterTextChanged(Editable s) { }
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});	
    }
	
	private void updateTemplateSummary()
	{
	
		SpannableString spannablecontent = coloriser.getColorisedTemplateInfo();		
		templateSummary.setText(spannablecontent);
	}
	
	private void showChooseTemplateDialog()
	{
		DialogFragment newFragment = new ChooseTemplateDialogFragment();
		newFragment.show(getSupportFragmentManager(), "templates");
	}
	
	@Override
	public void onTemplateChosen(StructureFileTemplate file){
		String template = tfm.readAssetFile("templates"+File.separator+ file.getFile());
		helper.setTemplate(template);
		currentFileTemplate = file;
		if(templateName != null)
		{
			//this widget doesn't exist in portrait mode
		    templateName.setText(file.getName());
			updateTemplateSummary();
		}
		text.getText().clearSpans();
		text.setText(text.getText());
	}
		
	@Override
	public void onWordCountUpdate(long wordc)
	{
		//update file name and counter									
		String temp = (isChanged?"*":"") + fileName + " " + wordc + " words";		
		title.setText(temp);
		
	}
	
	protected void onPause()
	{
		super.onPause();

		Toast.makeText(this, "on pause called", Toast.LENGTH_SHORT).show();
		
		saveState();
	}

	private void saveState()
	{
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

		if (editor != null && text != null)
		{
			String t;

			t = text.getText().toString();
			editor.putString("text", t);

			if(prompt != null)
			{
				//null in portrait mode
			    t = prompt.getText().toString();
			    editor.putString("prompt", t);
			}

			editor.putString("fileName", fileName);

			editor.putBoolean("isUntitled", isUntitled);
			editor.putBoolean("isChanged", isChanged);
			
		    editor.putString("templateName", currentFileTemplate.getName());
			editor.putString("templateFile", currentFileTemplate.getFile());

			editor.commit();
		}
	} // end saveState()
	
	protected void onResume()
	{
		super.onResume();	
		
		Toast.makeText(this, "on resume called", Toast.LENGTH_SHORT).show();

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String restoredText = prefs.getString("text", null);
		String restoredPrompt = prefs.getString("prompt", null);
		String restoredFileName = prefs.getString("fileName", null);
		String restoredTemplateName = prefs.getString("templateName", null);
		String restoredTemplateFileName = prefs.getString("templateFile", null);
		
		fileName = restoredFileName;
		if (restoredText != null && text != null )
		{
			text.setText(restoredText);
		}

		if (restoredPrompt != null && prompt != null)
		{
			prompt.setText(restoredPrompt);
		}

		isChanged = prefs.getBoolean("isChanged", false);
		isUntitled = prefs.getBoolean("isUntitled", true);
		
		if(restoredTemplateName != null)
		{
			StructureFileTemplate ft = new StructureFileTemplate();
			ft.setFile(restoredTemplateFileName);
			ft.setName(restoredTemplateName);
			//load template and update the colors
			onTemplateChosen(ft);
		}
		
		if (text != null)
			text.requestFocus();
	} // end onResume
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_NEW_FILE, 0, "New").setShortcut('0', 'n').setIcon(R.drawable.ic_new);		
		menu.add(0, MENU_OPEN_FILE, 0, "Open").setShortcut('0', 'o').setIcon(R.drawable.ic_open);
		menu.add(0, MENU_SAVE_FILE, 0, "Save").setShortcut('0', 's').setIcon(R.drawable.ic_save);
		menu.add(0, MENU_SAVE_FILE_AS, 0, "Save As...").setShortcut('0', 'a').setIcon(R.drawable.ic_save);
		menu.add(0, MENU_CHOOSE_STRUCT, 0, "Choose Structure...").setShortcut('0', 'a').setIcon(R.drawable.ic_open);
		
		return true;
	} // end onCreateOptionsMenu()
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case MENU_NEW_FILE:
			    //TO DO check if dirty
				//initialize
				prompt.setText("add notes here");
				title.setText("");
				isUntitled = true;
				isChanged = false;
				fileName = getResources().getString( R.string.newFileName);
				
				//this would also trigger the update of the word counter and file name
				text.setText("");
			
			    break;
			case MENU_OPEN_FILE:
			    pickFileForOpen();
				break;
			case MENU_SAVE_FILE:	
		
				if (isUntitled) {
					//browse for a file
					pickFileForSave();
				} else
				    //see if in text or meta
					saveText(fileName);
					updateText(text.getText().toString(), fileName);
				break;
				
			case MENU_SAVE_FILE_AS:
			    pickFileForSave();
			break;
			
			case MENU_CHOOSE_STRUCT:
				showChooseTemplateDialog();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void pickFileForSave()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(Intent.ACTION_PICK);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");

		intent2Browse.putExtra("browser_line", "enabled");
		intent2Browse.putExtra("browser_line_textfield", "story.txt");

		intent2Browse.putExtra("explorer_title", "Save As...");
		intent2Browse.putExtra("browser_list_background_color", "66000000");

		startActivityForResult(intent2Browse, SAVE_FILE_REQUEST_CODE);
	}
	
	private void pickFileForOpen()
	{
		Intent intent2Browse = new Intent();
		intent2Browse.setAction(Intent.ACTION_PICK);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		intent2Browse.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");

		//intent2Browse.putExtra("browser_line", "enabled");
		//intent2Browse.putExtra("browser_line_textfield", "story.txt");

		intent2Browse.putExtra("explorer_title", "Select file...");
		intent2Browse.putExtra("browser_list_background_color", "66000000");

		startActivityForResult(intent2Browse, OPEN_FILE_REQUEST_CODE);
	}
	
	private void saveText(String fName)
	{
		Toast.makeText(this, "Saving..."+fName, Toast.LENGTH_SHORT).show();

		// actually save the file here
		try {
			File f = new File(fName.toString());

			if ( (f.exists() && !f.canWrite()) || (!f.exists() && !f.getParentFile().canWrite()))
			{
				
				Toast.makeText(this, "Cannot save, not enough permissions!", Toast.LENGTH_SHORT).show();
				
				text.requestFocus();

				f = null;
			
			} else {
			    f = null; // hopefully this gets garbage collected

			    // Create file 
			    FileWriter fstream = new FileWriter(fName.toString());
			    BufferedWriter out = new BufferedWriter(fstream);

		        out.write(text.getText().toString());
			
			    out.close();

			    // inform the user of success			     				 				 
				Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();			
			}
		} catch (Exception e) { //Catch exception if any
			Toast.makeText(this, "There was an error saving the file. "+
			                       e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}
	
	public StringBuffer openFile(CharSequence fname)
	{
		
		Toast.makeText(this, "Opening..."+fname, Toast.LENGTH_SHORT).show();
		
		StringBuffer result = new StringBuffer();
		
		try {
			// open file
			FileReader f = new FileReader(fname.toString());
			File file = new File(fname.toString());
			
			if (f == null)
			{
				throw(new FileNotFoundException());
			}

			if (file.isDirectory())
			{
				throw(new IOException());
			}
			
			// if the file has nothing in it there will be an exception here
			// that actually isn't a problem
			if (file.length() != 0 && !file.isDirectory())
			{			
				char[] buffer;
				buffer = new char[4800];	// made it bigger just in case
	
				int read = 0;

				do {
					read = f.read(buffer, 0, 4800);
					
					if (read >= 0)
					{
						result.append(buffer, 0, read);
					}
				} while (read >= 0);
			}
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "Couldn't find file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this, "There was an error opening the file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();
	
		} catch (Exception e) {
			Toast.makeText(this, "There was an error opening the file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		return result;
		
	} // end openFile(CharSequence fname)

	private void updateText(String aText, String aFileName){
		// the filename is the new title
		fileName = aFileName;		
		isUntitled = false;
		isChanged = false;
		//saveState();
		//refreshes the spans and updates the title
		text.setText(aText);
		saveState();
		text.requestFocus();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		Toast.makeText(this, "on activity result called", Toast.LENGTH_SHORT).show();	
		
	    switch (requestCode) {

			case SAVE_FILE_REQUEST_CODE:

				if(RESULT_OK == resultCode) {

				    Uri fileUri = intent.getData();
					saveText(fileUri.getPath());
					
					updateText(text.getText().toString(), fileUri.getPath());
					/*
					// the filename is the new title
					fileName = fileUri.getPath();		
					isUntitled = false;
					isChanged = false;
					saveState();
					//refreshes the spans and updates the title
					text.setText(text.getText());
					text.requestFocus();*/
				}
			break;
			
			case OPEN_FILE_REQUEST_CODE:

				if(RESULT_OK == resultCode) {

				    Uri fileUri = intent.getData();
					StringBuffer txt = openFile(fileUri.getPath());
					
					updateText(txt.toString(), fileUri.getPath());
					
					/*fileName = fileUri.getPath();
					isChanged = false;
					isUntitled = false;
					saveState();
					text.setText(txt.toString());
					text.requestFocus();*/
				}
			break;
		}
	}
}
