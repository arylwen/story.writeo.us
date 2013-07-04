package com.halcyon.novelwriter;



/**
 * An activity representing a single Part detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link PartListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link PartDetailFragment}.
 */
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.halcyon.novelwriter.*;
import com.halcyon.novelwriter.model.*;
import com.halcyon.storywriter.*;

import com.actionbarsherlock.view.MenuItem;

public class PartDetailActivity extends SherlockFragmentActivity 
implements NovelColoriser.CounterListener
 //, ViewPager.OnPageChangeListener
{
    private TextView title;
	private String fileName;
	private Scene scene;
		   
	private long totalWordCount = 0;
	private long currentSceneWordCount = 0;
	private long partialWordCount = 0;
	private String currentPart = "";	   
	private boolean first;
	
	private NovelPersistenceManager npm;
	private PartDetailFragment detailFragment;
		   
    //private int currentPage = 0;
		   
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
		scene = (Scene)getIntent().getSerializableExtra(PartDetailFragment.ARG_SCENE);
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
			arguments.putSerializable(PartDetailFragment.ARG_SCENE, getIntent()
					.getSerializableExtra(PartDetailFragment.ARG_SCENE));
			//arguments.putSerializable("text", getIntent().getSerializableExtra("text"));
			//arguments.putSerializable("prompt", getIntent().getSerializableExtra("prompt"));			
			arguments.putLong("wordsBeforeScene", getIntent().getLongExtra("wordsBeforeScene", 0));	
			arguments.putSerializable("currentTemplate", getIntent().getSerializableExtra("currentTemplate"));	
			
			detailFragment = new PartDetailFragment();
			detailFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.part_detail_container, detailFragment).commit();
			first = true;		
			//partialWordCount = totalWordCount - detailFragment.getWordCount();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			detailFragment.save();

			NavUtils.navigateUpTo(this,
					new Intent(this, PartListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*public void saveScene(){
		//update current scene
		String newScene = ((EditText) findViewById(R.id.note)).getText().toString();
		String newPrompt = ((EditText) findViewById(R.id.prompt)).getText().toString();
		npm.updateScene(scene.getPath(), newScene, newPrompt);
		
	}*/
	
	public void onWordCountUpdate(long wordCount)
	{
		if(first){
		    partialWordCount = totalWordCount - wordCount;
			first = false;
		}
		
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
	
	/*public void onPageScrolled(int p1, float p2, int p3)
	{
		// TODO: Implement this method
	}

	public void onPageSelected(int p1)
	{
		
		if(currentPage == 0) {
			//moving from prompt to something else
			saveScene();
		}
		
		currentPage = p1;
	}

	public void onPageScrollStateChanged(int p1)
	{
		// TODO: Implement this method
	}*/
	
}
