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
    implements ChooseTemplateDialogFragment.TemplateDialogListener
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
	
	private final StoryTemplateHelper helper = new StoryTemplateHelper();
	private TemplateFileManager tfm;

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
		
		tfm = new TemplateFileManager(this);
		List<StructureTemplate> templates = tfm.getStructureTemplates();
		onTemplateChosen(templates.get(0));
		
		text.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence one, int a, int b, int c) {

					Map<Integer, SNode> structure = helper.getStructure();
					List<Integer> limits = helper.getLimits();
					Map<Integer, ForegroundColorSpan> spans = helper.getSpans();
					
					//Log.e("limits "+limits.size(), "SW");
					
					isChanged = true;
					long wordc = updateTitle();
					
					//color according to structure
					String stext = text.getText().toString();
					Editable s = text.getText();
					int lower = 0;
					int clower = 0;
					
					for(int i=0; i < limits.size(); i++)
					{
						int upper = limits.get(i);
						SNode current = structure.get(upper);
						if(upper < wordc)
						{
							//still inside the text
							int cupper = charCount(stext, clower, upper-lower);
							//s.setSpan(new ForegroundColorSpan(current.getColor()), clower, cupper, 
							//     Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 

							s.setSpan(spans.get(current.getId()), clower, cupper, 
									  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
							
								 
							lower = upper;
							clower = cupper-1;
						} else {
							//color up to the end of the text; break
							//s.setSpan(new ForegroundColorSpan(current.getColor()), clower, stext.length(), 
							//		  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
									  
							s.setSpan(spans.get(current.getId()), clower, stext.length(), 
									  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
									  
									  
						    break;
						}
					}
				}

				// complete the interface
				public void afterTextChanged(Editable s) { }
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			});
					
    }
	
	
	private void updateTemplateSummary()
	{
		StringBuilder sb = new StringBuilder();
		List<Integer> limits = helper.getLimits();
		List<Integer> slimits = new ArrayList<Integer>();
		for(Integer limit:limits)
		{
			SNode node = helper.getStructure().get(limit);
			sb.append(node.getName());
			sb.append("--");
			sb.append(limit.intValue());
			sb.append("\n");
			slimits.add(sb.length());
		}
		
		SpannableString spannablecontent=new SpannableString(sb.toString()); 
		Map<Integer, ForegroundColorSpan> spans = helper.getSpans();
		
		int prev = 0;
		int idx =0;
		for(Integer limit:limits)
		{
			SNode node = helper.getStructure().get(limit);
			ForegroundColorSpan span = spans.get(node.getId());
		    spannablecontent.setSpan(span, prev,slimits.get(idx), 
			                          SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE); 
			prev = slimits.get(idx);
			idx++;
		}
		
		templateSummary.setText(spannablecontent);
	}
	
	private void showChooseTemplateDialog()
	{
		DialogFragment newFragment = new ChooseTemplateDialogFragment();
		newFragment.show(getSupportFragmentManager(), "templates");
	}
	
	@Override
	public void onTemplateChosen(StructureTemplate file){
		String template = tfm.readAssetFile("templates"+File.separator+ file.getFile());
		helper.setTemplate(template);
		if(templateName != null)
		{
			//this widget doesn't exist in portrait mode
		    templateName.setText(file.getName());
			updateTemplateSummary();
		}
		text.getText().clearSpans();
		text.setText(text.getText());
	}
		
	private long updateTitle()
	{
		//update file name and counter					
		long wordc = wordCount(text.getText().toString());					
		String temp = (isChanged?"*":"") + fileName + " " + wordc + " words";		
		//Toast.makeText(this, "temp "+temp+" fileName "+fileName+" wordc "+wordc, Toast.LENGTH_SHORT).show();
		title.setText(temp);
		
		return wordc;
	}
	
	private static long wordCount(String line){
		long numWords = 0;
		int index = 0;
		boolean prevWhiteSpace = true;
		
		while(index < line.length())
		{ 
		    char c = line.charAt(index++);
			boolean currWhiteSpace = Character.isWhitespace(c);
			if(prevWhiteSpace && !currWhiteSpace)
				{numWords++;}
			prevWhiteSpace = currWhiteSpace;
		}
		
	    return numWords;
	}
	
	/**
	    determines the index of the last char for the next numWords words
	**/
	private static int charCount(String line, int start, int numWords){
		long localNumWords = 0;
		int index = start;
		boolean prevWhiteSpace = true;

		while((index < line.length()) && (localNumWords <= numWords))
		{ 
		    char c = line.charAt(index++);
			boolean currWhiteSpace = Character.isWhitespace(c);
			if(prevWhiteSpace && !currWhiteSpace)
			{
				localNumWords++;
			}
			prevWhiteSpace = currWhiteSpace;
		}

	    return index;
	}
	
	
	/*public boolean alreadyMarkedDirty()	// checks if the text is already marked dirty
	{
		CharSequence temp = title.getText();

		try {	

			if (temp.charAt(0) == '*')
			{
				return true;
			}
		} catch (Exception e) {
			return false;
		} 

		return false;
	} 
	
	public Map<Integer, SNode> getStructure()
	{
		Map<Integer, SNode> result = new HashMap<Integer, SNode>();
		
		SNode node;
		
		node = new SNode();
		node.setName("Act 1");
		node.setColor(Color.BLUE);		
		result.put(new Integer(75), node);
		
		node = new SNode();
		node.setName("Act 2");
		node.setColor(Color.GREEN);	
		result.put(new Integer(225), node);
		
		node = new SNode();
		node.setName("Act 3");
		node.setColor(Color.YELLOW);			
		result.put(new Integer(300), node);
		
		return result;
	} 
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) 
	{ 
	  List<T> list = new ArrayList<T>(c); 
	  java.util.Collections.sort(list); 
	  
	  return list; 
	}*/
	

	
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
		
		if (restoredText != null && text != null )
		{
			text.setText(restoredText);
		}

		if (restoredPrompt != null && prompt != null)
		{
			prompt.setText(restoredPrompt);
		}

		fileName = restoredFileName;
		isChanged = prefs.getBoolean("isChanged", false);
		isUntitled = prefs.getBoolean("isUntitled", true);
		
		updateTitle();
		
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
				text.setText("");
				prompt.setText("add notes here");
				title.setText("");
				isChanged = false;
				isUntitled = true;
				fileName = getResources().getString( R.string.newFileName);
				
				updateTitle();
			    break;
			case MENU_SAVE_FILE:	// Save
				//savingFile = true;
				if (isUntitled) {
					//browse for a file
					pickFile();
				} else
				    //see if in text or meta
					saveText(fileName);
				break;
				
			case MENU_SAVE_FILE_AS:
			    pickFile();
			break;
			
			case MENU_CHOOSE_STRUCT:
				showChooseTemplateDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void pickFile()
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
			    f = null; // hopefully this gets garbage colle

			    // Create file 
			    FileWriter fstream = new FileWriter(fName.toString());
			    BufferedWriter out = new BufferedWriter(fstream);

		        out.write(text.getText().toString());
			
			    out.close();

			    // inform the user of success			     				 				 
				Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();

			    // the filename is the new title
			    //title.setText(fName);
			    fileName = fName;
			    isUntitled = false;
				isChanged = false;
				
				saveState();
				
				updateTitle();
			}
		} catch (Exception e) { //Catch exception if any
			Toast.makeText(this, "There was an error saving the file. "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		text.requestFocus();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		Toast.makeText(this, "on activity result called", Toast.LENGTH_SHORT).show();	
		
	    switch (requestCode) {

			case SAVE_FILE_REQUEST_CODE:

				if(RESULT_OK == resultCode) {

				    Uri fileUri = intent.getData();
					saveText(fileUri.getPath());
				}
			}
		}
		
		

}
