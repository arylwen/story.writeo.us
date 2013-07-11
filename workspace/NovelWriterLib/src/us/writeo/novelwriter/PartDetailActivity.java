package us.writeo.novelwriter;

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
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import us.writeo.*;
import us.writeo.common.novel.model.*;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PartDetailActivity extends SherlockFragmentActivity 
                                implements NovelColoriser.CounterListener
{
    private static final String TAG = "NW PartDetailActivity";
	public static final String FRAGMENT_TAG = "PartDetailFragmentTag";
	private final static int MENU_UNDO = Menu.FIRST + 5;
	private final static int MENU_REDO = Menu.FIRST + 6;
 
    private TextView title;
	private String fileName;
	private Scene scene;
		   
	private long totalWordCount = 0;
	private long currentSceneWordCount = 0;
	private long partialWordCount = 0;
	private String currentPart = "";	   
	private boolean first;
	
	private PartDetailFragment detailFragment;
		   
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
		
        fileName = getIntent().getStringExtra(PartDetailFragment.ARG_FILE_NAME);
		scene = (Scene)getIntent().getSerializableExtra(PartDetailFragment.ARG_SCENE);
        totalWordCount = getIntent().getLongExtra("totalWordCount", 0);		
		
		// savedInstanceState is non-null when there is fragment state  saved from previous configurations 
		// of this activity (e.g. when rotating the screen from portrait to landscape).In this case, the 
		// fragment will automatically be re-added to its container and we need to manually retrieve it.
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putSerializable(PartDetailFragment.ARG_SCENE, getIntent()
					.getSerializableExtra(PartDetailFragment.ARG_SCENE));
			arguments.putSerializable(PartDetailFragment.ARG_FILE_NAME, getIntent()
									  .getSerializableExtra(PartDetailFragment.ARG_FILE_NAME));
					
			arguments.putLong("wordsBeforeScene", getIntent().getLongExtra("wordsBeforeScene", 0));	
			arguments.putSerializable("currentTemplate", getIntent().getSerializableExtra("currentTemplate"));	
			
			detailFragment = new PartDetailFragment();
			detailFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.part_detail_container, detailFragment, FRAGMENT_TAG).commit();
			first = true;		
		} else {
			//retrieve the fragment from a previous configuration
			detailFragment = (PartDetailFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG); 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, MENU_UNDO, 0, "Undo") .setIcon(R.drawable.undo) .
				setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(0, MENU_REDO, 0, "Redo") .setIcon(R.drawable.redo) .
				setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);	
	    
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case android.R.id.home:
			    detailFragment.save();
			    NavUtils.navigateUpTo(this,
					new Intent(this, PartListActivity.class));
			    return true;
			
			
		    case MENU_UNDO:
			    if(detailFragment != null){
					detailFragment.undo();
				}
				break;

		    case MENU_REDO:
			    if(detailFragment != null){
					detailFragment.redo();
				}
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
		
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
