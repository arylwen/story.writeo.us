package com.halcyon.novelwriter;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.halcyon.novelwriter.model.*;
import com.halcyon.storywriter.*;

import com.actionbarsherlock.view.MenuItem;

/**
 * An activity representing a single Part detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link PartListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link PartDetailFragment}.
 */
public class PartDetailActivity extends SherlockFragmentActivity 
       implements NovelColoriser.CounterListener{

    private TextView title;
	private String fileName;
	private Scene scene;
		   
	private long totalWordCount = 0;
	private long currentSceneWordCount = 0;
	private long partialWordCount = 0;
	private String currentPart = "";	   
	
	private NovelPersistenceManager npm;
		   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_detail);

		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		View customNav = LayoutInflater.from(this).inflate(R.layout.custom_title_bar, null);		
		title = (TextView) customNav.findViewById(R.id.notetitle);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
        fileName = getIntent().getStringExtra("fileName");
		scene = (Scene)getIntent().getSerializableExtra(PartDetailFragment.ARG_ITEM_ID);
		npm = new NovelZipManager(fileName, null, getCacheDir());
		
		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putSerializable(PartDetailFragment.ARG_ITEM_ID, getIntent()
					.getSerializableExtra(PartDetailFragment.ARG_ITEM_ID));
			arguments.putSerializable("text", getIntent().getSerializableExtra("text"));
			arguments.putSerializable("prompt", getIntent().getSerializableExtra("prompt"));			
			arguments.putLong("wordsBeforeScene", getIntent().getLongExtra("wordsBeforeScene", 0));			
			
			PartDetailFragment fragment = new PartDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.part_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			
			//update current scene
			String newScene = ((EditText) findViewById(R.id.note)).getText().toString();
			String newPrompt = ((EditText) findViewById(R.id.prompt)).getText().toString();
			npm.updateScene(scene.getPath(), newScene, newPrompt);
			
			NavUtils.navigateUpTo(this,
					new Intent(this, PartListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onWordCountUpdate(long wordCount)
	{
		currentSceneWordCount = wordCount;
		updateTitle();
	}

	public void onPartUpdate(String aPart)
	{
	    currentPart = aPart;
		updateTitle();
	}
	
	private void updateTitle(){
		totalWordCount = currentSceneWordCount+partialWordCount;
		title.setText(fileName+" "+currentSceneWordCount+":"+(totalWordCount)+":"+currentPart);		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Toast.makeText(this, "on pause called", Toast.LENGTH_SHORT).show();
		saveState();
	}

	private void saveState(){
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

		if (editor != null) {
			if(fileName != null){
				editor.putString("fileName", fileName);
			}
		}
	}
}
