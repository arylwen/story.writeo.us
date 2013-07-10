package us.writeo.novelwriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import us.writeo.filepicker.FilePicker;
import us.writeo.novel.model.Chapter;
import us.writeo.novel.model.Novel;
import us.writeo.novel.model.NovelHelper;
import us.writeo.novel.model.Scene;
import us.writeo.novel.persistence.NovelPersistenceManager;
import us.writeo.novel.persistence.NovelZipManager;
import us.writeo.novel.persistence.SceneIterator;
import us.writeo.novel.persistence.ZipSceneIterator;
import us.writeo.structuretemplate.model.StructureFileTemplate;
import us.writeo.structuretemplate.persistence.TemplateFileManager;
import us.writeo.structuretemplate.view.ChooseTemplateDialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
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
		PartListFragment.Callbacks, EditChapterDialogListener, 
        EditSceneDataDialogFragment.EditSceneDialogListener, NovelColoriser.CounterListener, 
		ChooseTemplateDialogFragment.TemplateDialogListener
{

	private final static int MENU_NEW_FILE = Menu.FIRST;
	private final static int MENU_OPEN_FILE = Menu.FIRST + 2;
	private final static int MENU_SAVE_FILE_AS = Menu.FIRST + 3;
	private final static int MENU_CHOOSE_STRUCT = Menu.FIRST + 4;
	private final static int MENU_GENERATE_NOVEL = Menu.FIRST + 1;
	private final static int MENU_UNDO = Menu.FIRST + 5;
	private final static int MENU_REDO = Menu.FIRST + 6;
				
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private static String TAG = "NW PartListActivity";
	
	private TextView title;
	private String fileName;
	private boolean isUntitled = true;
	
	private PartListFragment partList;
	private Scene currentScene;
	
	private FilePicker fpk;
	private NovelHelper nh;
	private NovelPersistenceManager npm;
	
	private long totalWordCount = 0;
	private long currentSceneWordCount = 0;
	private long partialWordCount = 0;
	private String currentPart = "";
	private boolean first = false;
	
	private Novel novel;
	
	private TemplateFileManager templateFileManager;
	StructureFileTemplate currentTemplate;
	PartDetailFragment fragment;

	@Override
	public void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		
		//fm = new FileManager();
		
		try
		{
			ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), 
																		PackageManager.GET_META_DATA);
			if(ai != null){
				Bundle metadata = ai.metaData;
			    String value = metadata.getString("filePicker");	
		
		        if(value != null){
			        Class clazz = Class.forName(value);
		
			        Constructor[] constructors = clazz.getDeclaredConstructors();
			        Constructor c = constructors[0];
			        Object[] params = new Object[1];
			        params[0] = this;
			
		            fpk = (FilePicker)c.newInstance(params);
			        Log.e(TAG, "obtained a filepicker "+fpk.getClass().getName());
				}
			}
		
		}
		catch (PackageManager.NameNotFoundException e)
		{
            Log.e(TAG, "couldn't get a file picker "+e.toString());
		} 
		catch (ClassNotFoundException e)
		{
			Log.e(TAG, "couldn't get a file picker "+e.toString());
		}
		catch (InstantiationException e)
		{
			Log.e(TAG, "couldn't get a file picker "+e.toString());
		}
		catch (InvocationTargetException e)
		{
			Log.e(TAG, "couldn't get a file picker "+e.toString());
		}
		catch (IllegalAccessException e)
		{
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		}
		catch (IllegalArgumentException e)
		{
			Log.e(TAG, "couldn't get a file picker "+e.toString());
		}
		finally{
			//if (fpk == null ) fpk = new SWFilePicker(this);
		}

		nh = new NovelHelper();
				
		setContentView(R.layout.activity_part_list);
		
		partList = 	((PartListFragment) getSupportFragmentManager().findFragmentById(
			R.id.part_list));
		
		View customNav = LayoutInflater.from(this).inflate(R.layout.custom_title_bar, null);		
		title = (TextView) customNav.findViewById(R.id.notetitle);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		getSupportActionBar().setDisplayShowHomeEnabled(true); 
		
		fileName = getResources().getString( R.string.newFileName);

		if (findViewById(R.id.part_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large-lanc and
			// res/values-sw600dp-land). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
		    //partList.setActivateOnItemClick(true);
		}

		templateFileManager = new TemplateFileManager(this);
		List<StructureFileTemplate> templates = templateFileManager.getStructureTemplates();
		onTemplateChosen(templates.get(0));	
	}
	
	/*@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
    {
		Log.e(TAG, "partlistactivity:onviewcreated");;
	}*/

	@Override
	public void onContentChanged()
	{
		Log.e(TAG, "partlistactivity:oncontentchanged");
		((PartListFragment) getSupportFragmentManager().findFragmentById(
			R.id.part_list)).onContentChanged();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		if(fragment != null) {
			fragment.save();
			Log.e(TAG, "fragment saved");
		}
		saveState();
	}
	
	private void saveState(){
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

		if (editor != null) {
			if(partList != null){
				long selectedItemPosition = partList.getActivatedPosition();
				editor.putLong("selectedPosition", selectedItemPosition);
				Log.e(TAG, "save selectedPosition "+ selectedItemPosition);
			}
			
			if(!isUntitled){
			   editor.putString("fileName", fileName);			   
			}
			
			editor.commit();
		}
	}
		
	@Override
	protected void onResume()
	{
		super.onResume();	
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);		
		String restoredFileName = prefs.getString("fileName", null);
		
		if((restoredFileName != null)) {		
			fileName = restoredFileName;
			
			File f = new File(fileName);
			if(!f.exists()){
				fileName = getResources().getString( R.string.newFileName);
				isUntitled = true;
			} else {
				isUntitled = false;
			}
		}
				
		if( ( fileName != null ) && !isUntitled ){
			totalWordCount = countWordsForNovel(fileName);
			partialWordCount = totalWordCount; //no scene selected yet
			updateTitle();
			npm = new NovelZipManager(fileName, null, getCacheDir());
			novel = partList.resetModel(npm);
			
			//restore position
			long packed;
			packed = prefs.getLong("selectedPosition ", ExpandableListView.PACKED_POSITION_VALUE_NULL);
			if(packed == ExpandableListView.PACKED_POSITION_VALUE_NULL)
			   packed = partList.getActivatedPosition();
			Log.e(TAG, "restore selectedPosition " + packed);
			if(partList.getExpandableListView().getPackedPositionType(packed) 
			   != ExpandableListView.PACKED_POSITION_TYPE_NULL){
                Log.e(TAG, "not null");
				if(partList.getExpandableListView().getPackedPositionType(packed) 
				   == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

					int chapterIndex = partList.getExpandableListView().getPackedPositionGroup(packed);
					int sceneIndex = partList.getExpandableListView().getPackedPositionChild(packed);
					partList.getExpandableListView().setSelectedChild(chapterIndex, sceneIndex, true);
					//the third parameter in the call above does not work.
					partList.getExpandableListView().expandGroup(chapterIndex);
					Scene scene = (Scene)partList.getExpandableListAdapter().getChild(chapterIndex, sceneIndex);
					onSceneSelected(scene);
				}
			}
		}
	}
		
	@Override
	public void onChapterSelected(Chapter chapter)
	{
		
	}
	
	public void onSceneSelected(Scene scene)
	{		
		currentScene = scene;

		long wordsBeforeScene = countWordsBeforeScene(fileName, scene);
		Log.e(TAG, "wordsBeforeScene "+wordsBeforeScene);
		
		if (mTwoPane) {			
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a fragment transaction.
			//save the data of the  fragment we are about to replace
			if(fragment != null) fragment.save();
			
			Bundle arguments = new Bundle();
			arguments.putSerializable(PartDetailFragment.ARG_SCENE, scene);
			arguments.putSerializable(PartDetailFragment.ARG_FILE_NAME, fileName);
			arguments.putLong("wordsBeforeScene", wordsBeforeScene);
			arguments.putSerializable("currentTemplate", currentTemplate);
						
			fragment = new PartDetailFragment();
			Log.e(TAG, "created new fragment");
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.part_detail_container, fragment).commit();
			first = true;
			//partialWordCount = totalWordCount - fragment.getWordCount();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PartDetailActivity.class);
			detailIntent.putExtra(PartDetailFragment.ARG_SCENE, scene);
			detailIntent.putExtra(PartDetailFragment.ARG_FILE_NAME, fileName);
			
			totalWordCount = countWordsForNovel(fileName);
			detailIntent.putExtra("wordsBeforeScene", wordsBeforeScene);
			detailIntent.putExtra("totalWordCount", totalWordCount);
			detailIntent.putExtra("currentTemplate", currentTemplate);
			
			startActivity(detailIntent);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_NEW_FILE, 0, "New Novel").setShortcut('0', 'n').setIcon(R.drawable.ic_new);		
		menu.add(0, MENU_OPEN_FILE, 0, "Open Novel").setShortcut('0', 'o').setIcon(R.drawable.ic_open);
		menu.add(0, MENU_CHOOSE_STRUCT, 0, "Choose Structure...").
		            setShortcut('0', 'a').setIcon(R.drawable.ic_open);
		menu.add(0, MENU_GENERATE_NOVEL, 0, "Generate Novel").
		            setShortcut('0', 'g').setIcon(R.drawable.ic_save);
		
		if(mTwoPane){
			//undo/redo make sense only if we have a text field displayed
		    menu.add(0, MENU_UNDO, 0, "Undo") .setIcon(R.drawable.undo) .
		           setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		    menu.add(0, MENU_REDO, 0, "Redo") .setIcon(R.drawable.redo) .
		           setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);	
	    }
		return true;
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case MENU_NEW_FILE:
			    if( mTwoPane && ( fragment != null ) ) fragment.save();		
			    fpk.pickFileForNew();			
			    break;
				
			case MENU_OPEN_FILE:
			    fpk.pickFileForOpen();
				break;
				
			/*case MENU_GENERATE_NOVEL:	
		
			    NovelGenerator ng = new ZipNovelGenerator(this);
				fileName = ng.generate();
				
				totalWordCount = countWordsForNovel(fileName);
				partialWordCount = totalWordCount; //no scene selected yet
				updateTitle();
				npm = new NovelZipManager(fileName, null, getCacheDir());
				novel = partList.resetModel(npm);
				isUntitled = false;
				
				break;*/
				
			case MENU_SAVE_FILE_AS:
			    fpk.pickFileForSave();
			break;
			
			case MENU_CHOOSE_STRUCT:
				showChooseTemplateDialog();
			break;
			
			case MENU_UNDO:
			    if(fragment != null){
					fragment.undo();
				}
			break;
			
			case MENU_REDO:
			    if(fragment != null){
					fragment.redo();
				}
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
						//Log.e(TAG, xml);						
						novel = nh.readNovel(xml);						
												
						fileName = fName;
						totalWordCount = countWordsForNovel(fileName);
						partialWordCount = totalWordCount; //no scene selected yet
						updateTitle();
						npm = new NovelZipManager(fileName, novel, getCacheDir());
						npm.createNovel();
						//partList.resetModel(npm);
						saveState();
						isUntitled = false;
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
					fileName = fileUri.getPath();
					totalWordCount = countWordsForNovel(fileName);
					partialWordCount = totalWordCount; //no scene selected yet
					updateTitle();
					npm = new NovelZipManager(fileName, null, getCacheDir());
					//novel = partList.resetModel(npm);
					saveState();
					isUntitled = false;
				}
			break;
		}
	}
	
	@Override
	public void onTemplateChosen(StructureFileTemplate file){
		//String template = templateFileManager.readAssetFile("templates"+File.separator+ file.getFile());
		//helper.setTemplate(template);
		currentTemplate = file;
		if(fragment != null)
		{
			//fragment showing, needs update
			fragment.onTemplateChosen(currentTemplate);
		}
	}
	
	private void updateTitle(){
		totalWordCount = currentSceneWordCount+partialWordCount;
		title.setText(fileName+" "+currentSceneWordCount+":"+(totalWordCount)+":"+currentPart);		
	}
	
	private long countWordsForNovel(String fileName){
		SceneIterator si = new ZipSceneIterator(fileName);
		CountWordsProcessor sp = new CountWordsProcessor();
		si.iterate(sp, null);
		
		return sp.getWordCount();
	}
	
	private long countWordsBeforeScene(String fileName, Scene scene){
		SceneIterator si = new ZipSceneIterator(fileName);
		CountWordsUpToProcessor sp = new CountWordsUpToProcessor(novel, scene);
		si.iterate(sp, null);

		return sp.getWordCount();
	}
	
	@Override
	public void onChapterChanged(Chapter chapter){
        partList.onChapterChanged(chapter);
	}
	
	@Override
	public void onSceneChanged(Scene scene){
        partList.onSceneChanged(scene);
	}
	
	@Override
	public void onWordCountUpdate(long wordCount)
	{
		if(first){
		    partialWordCount = totalWordCount - wordCount;
			first = false;
		}
		currentSceneWordCount = wordCount;
		updateTitle();
	}
	
	@Override
	public void onPartUpdate(String aPart)
	{
	    currentPart = aPart;
		updateTitle();
	}
	
	private void showChooseTemplateDialog()
	{
		DialogFragment newFragment = new ChooseTemplateDialogFragment();
		newFragment.show(getSupportFragmentManager(), "templates");
	}
}
