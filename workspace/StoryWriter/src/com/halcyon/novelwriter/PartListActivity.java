package com.halcyon.novelwriter;

import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.halcyon.novelwriter.model.*;
import com.halcyon.storywriter.*;
import java.io.*;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * An activity representing a list of Parts. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link PartDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PartListFragment} and the item details (if present) is a
 * {@link PartDetailFragment}.
 * <p>
 * This activity also implements the required {@link PartListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class PartListActivity extends SherlockFragmentActivity implements
		PartListFragment.Callbacks {

	private final static int MENU_NEW_FILE = Menu.FIRST;
	private final static int MENU_SAVE_FILE = Menu.FIRST + 1;
	private final static int MENU_OPEN_FILE = Menu.FIRST + 2;
	private final static int MENU_SAVE_FILE_AS = Menu.FIRST + 3;
	private final static int MENU_CHOOSE_STRUCT = Menu.FIRST + 4;
				
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private static String TAG = "NW";
	
	private TextView title;
	private String fileName;
	private boolean isUntitled = true;
	private boolean isChanged = false;
	private PartListFragment partList;
	
	private FileManager fm;
	private FilePicker fpk;
	private NovelHelper nh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fm = new FileManager();
		fpk = new SWFilePicker(this);
		nh = new NovelHelper();
				
		setContentView(R.layout.activity_part_list);
		
		partList = 	((PartListFragment) getSupportFragmentManager().findFragmentById(
			R.id.part_list));
		
		View customNav = LayoutInflater.from(this).inflate(R.layout.custom_title_bar, null);		
		title = (TextView) customNav.findViewById(R.id.notetitle);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		getSupportActionBar().setDisplayShowHomeEnabled(true); 

		fileName = getResources().getString( R.string.newFileName);

		if (findViewById(R.id.part_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
		    partList.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
    {
		Log.e(TAG, "partlistactivity:onviewcreated");
		//((PartListFragment) getSupportFragmentManager().findFragmentById(
		//	R.id.part_list)).onContentChanged();
	}

	@Override
	public void onContentChanged()
	{
		Log.e(TAG, "partlistactivity:oncontentchanged");
		((PartListFragment) getSupportFragmentManager().findFragmentById(
			R.id.part_list)).onContentChanged();
	}
	
	
	/**
	 * Callback method from {@link PartListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		Toast.makeText(this, "on item selected", Toast.LENGTH_SHORT);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(PartDetailFragment.ARG_ITEM_ID, id);
			PartDetailFragment fragment = new PartDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.part_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PartDetailActivity.class);
			detailIntent.putExtra(PartDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public void onChapterSelected(Chapter chapter)
	{
		
	}
	
	public void onSceneSelected(Scene scene)
	{
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putSerializable("scene", scene);
			//arguments.putString(PartDetailFragment.ARG_ITEM_ID, id);
			PartDetailFragment fragment = new PartDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.part_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PartDetailActivity.class);
			detailIntent.putExtra(PartDetailFragment.ARG_ITEM_ID, scene);
			startActivity(detailIntent);
		}
	}
	
	
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
				//prompt.setText("add notes here");
				//title.setText("");
				//isUntitled = true;
				//isChanged = false;
				//fileName = getResources().getString( R.string.newFileName);
				
				//this would also trigger the update of the word counter and file name
				//text.setText("");
			
			    fpk.pickFileForNew();
			
			    break;
			case MENU_OPEN_FILE:
			    fpk.pickFileForOpen();
				break;
			case MENU_SAVE_FILE:	
		
				if (isUntitled) {
					//browse for a file
					fpk.pickFileForSave();
				} else
				    //see if in text or meta
					//fm.saveText(fileName, text.getText().toString(), this);
					//updateText(text.getText().toString(), fileName);
				    //save prompt 
				    //fm.saveText(getPromptFileName(fileName), prompt.getText().toString(), this);
				break;
				
			case MENU_SAVE_FILE_AS:
			    fpk.pickFileForSave();
			break;
			
			case MENU_CHOOSE_STRUCT:
				//showChooseTemplateDialog();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		Toast.makeText(this, "on activity result called", Toast.LENGTH_SHORT).show();	
		
	    switch (requestCode) {

			case FilePicker.NEW_FILE_REQUEST_CODE:
			    
				if(RESULT_OK == resultCode) {

				    Uri fileUri = intent.getData();
					String fName = fileUri.getPath();
					
					InputStream inputStream = null;
					try{
						inputStream = getResources().openRawResource(R.raw.new_novel);
						byte[] reader = new byte[inputStream.available()];
						while (inputStream.read(reader) != -1) {};
						
						String xml = new String(reader);
						
						Log.e(TAG, xml);
						
						Novel novel = nh.readNovel(xml);
						
						partList.resetModel(novel);
						
					}catch(IOException e) {
						Log.e(TAG, e.getMessage());
					} finally {
						if(inputStream != null) {
							try { 
							    inputStream.close();
							} catch (IOException e) { 
							    Log.e(TAG, e.getMessage());
							}
						}
					}
					
					
				}
			break;
			
			case FilePicker.SAVE_FILE_REQUEST_CODE:

				if(RESULT_OK == resultCode) {

				    Uri fileUri = intent.getData();
					String fName = fileUri.getPath();
					
					//save story, update file name and save state
					//fm.saveText(fName, text.getText().toString(), this);					
					//updateText(text.getText().toString(), fName);
					
					//save prompt 
					//fm.saveText(getPromptFileName(fName), prompt.getText().toString(), this);
				}
			break;
			
			case FilePicker.OPEN_FILE_REQUEST_CODE:

				if(RESULT_OK == resultCode) {

				    Uri fileUri = intent.getData();
					String fName = fileUri.getPath();
					
					//restore prompt 
					//StringBuffer pmt = fm.openFile(getPromptFileName(fName), this);
					//prompt.setText(pmt.toString());
					
					//restore story
					StringBuffer txt = fm.openFile(fName, this);	
					//update file name, save story and prompt as state
					//updateText(txt.toString(), fName);
				}
			break;
		}
	}
}
