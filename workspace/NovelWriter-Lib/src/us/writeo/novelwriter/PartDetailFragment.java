package us.writeo.novelwriter;

import android.os.*;
import android.support.v4.view.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import java.io.*;
import java.util.*;
import us.writeo.common.*;
import us.writeo.novel.model.*;
import us.writeo.novel.persistence.*;
import us.writeo.structuretemplate.model.*;
import us.writeo.structuretemplate.persistence.*;

/**
 * A fragment representing a single Part detail screen. This fragment is either
 * contained in a {@link PartListActivity} in two-pane mode (on tablets) or a
 * {@link PartDetailActivity} on handsets.
 */
public class PartDetailFragment extends SherlockFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_SCENE = "scene";
	public static final String ARG_FILE_NAME = "fileName";
	public static final String TAG = "NW PartDetailFragment";

	/**
	 * The content this fragment is presenting.
	 */
	private Scene scene;
	private StructureTemplateHelper helper;
	private EditText text;
	private NovelColoriser coloriser;
	private TextViewUndoRedo undoRedo;
	private TemplateFileManager tfm;
	private StructureFileTemplate currentFileTemplate;
	
	private InfoPagerAdapter mInfoPagerAdapter;
	private ViewPager mViewPager;

	private NovelPersistenceManager npm;
	private String fileName;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PartDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e(TAG, "onCreate");
		
		helper = new StructureTemplateHelperImpl();		
		tfm = new TemplateFileManager(getActivity());
		
		if (getArguments().containsKey(ARG_FILE_NAME)) {
			fileName = (String)getArguments().getSerializable((ARG_FILE_NAME));
		}
		npm = new NovelZipManager(fileName, null, getActivity().getCacheDir());
		
		if (getArguments().containsKey(ARG_SCENE)) {
			scene = (Scene)getArguments().getSerializable((ARG_SCENE));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_part_detail,
				container, false);
				
		// setup the scroll view to fill the parent
		ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scroll);	
		if (scroll != null)
		{
			scroll.setFillViewport(true);
			scroll.setHorizontalScrollBarEnabled(false);
		}		
			
		String textStr = npm.getScene(scene.getPath());
		String prompt = npm.getScene(scene.getPath()+".1");
			
		/*String textStr = null;
		if (getArguments().containsKey("text")) {
			textStr = (String)getArguments().getSerializable(("text"));
		}	*/	
		
		text = 	((EditText) rootView.findViewById(R.id.note));			
		if (textStr != null) {		
					text.setText(textStr);
		}
		long wordsBeforeScene = 0;
		if (getArguments().containsKey("wordsBeforeScene")) {
			wordsBeforeScene = getArguments().getLong("wordsBeforeScene");
		}	
				
		if (getArguments().containsKey("currentTemplate")) {
			currentFileTemplate = (StructureFileTemplate)getArguments().getSerializable("currentTemplate");
		}		
				
		if(currentFileTemplate == null){
		    List<StructureFileTemplate> templates = tfm.getStructureTemplates();
			currentFileTemplate = templates.get(0);
		}
		
		setUpTemplate(currentFileTemplate);
		
		coloriser = new NovelColoriser(text, helper, wordsBeforeScene);	
		coloriser.setCounterListener((NovelColoriser.CounterListener)getActivity());
		
		text.addTextChangedListener( new TextWatcher() {
				public void onTextChanged(CharSequence one, int a, int b, int c) {
                      coloriser.onTextChanged(one, a,b,c);
                      //isChanged = true;
				}
					
				// complete the interface
				public void afterTextChanged(Editable s) { }
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});	
		
		//attach undo/redo behavior to text
		//undoRedo = new TextViewUndoRedo(text);
		
		/*String prompt = null;
		if (getArguments().containsKey("prompt")) {
			prompt = (String)getArguments().getSerializable(("prompt"));
		}*/
				
		//SpannableString spannablecontent = coloriser.getColorisedTemplateInfo();
		mInfoPagerAdapter = new InfoPagerAdapter(getActivity().getSupportFragmentManager(), 
		                     prompt);
		Log.e(TAG, "Created pager adapter");
							 //, currentFileTemplate.getFile(), spannablecontent);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mInfoPagerAdapter);
		
		//onTemplateChosen(currentFileTemplate);	
		
		SpannableString spannablecontent = coloriser.getColorisedTemplateInfo();
		mInfoPagerAdapter.setTemplate(currentFileTemplate.getName(), spannablecontent);
		mInfoPagerAdapter.notifyDataSetChanged();
		text.getText().clearSpans();
		text.setText(text.getText());
		
		//attach undo/redo behavior to text _after_ cleaning up 
		//so it doesn't register a undo operarion
		undoRedo = new TextViewUndoRedo(text);
		
		return rootView;
	}
	
	//undo/redo related functions
	/*
	 * must ask both the text and the pager
	 */
	public boolean canUndo(){
		return (undoRedo.getCanUndo() || mInfoPagerAdapter.canUndo());
	}
	
	public boolean canRedo(){
		return (undoRedo.getCanRedo() || mInfoPagerAdapter.canRedo());
	}
	
	public void undo(){
		if(text.hasFocus()){
		    undoRedo.undo();
		} else {
			mInfoPagerAdapter.undo();
		}
	}
	
	public void redo(){
		if(text.hasFocus()){
		    undoRedo.redo();
		} else {
			mInfoPagerAdapter.redo();
		}
	}
	
	public void onTemplateChosen(StructureFileTemplate file){
		
		setUpTemplate(file);
		
		SpannableString spannablecontent = coloriser.getColorisedTemplateInfo();
		mInfoPagerAdapter.setTemplate(file.getName(), spannablecontent);
		text.getText().clearSpans();
		text.setText(text.getText());
	}

	private void setUpTemplate(StructureFileTemplate file)
	{
		String template = tfm.readAssetFile("templates" + File.separator + file.getFile());
		helper.setTemplate(template);
		currentFileTemplate = file;
	}
		
   public String getPrompt(){
	   return mInfoPagerAdapter.getPrompt();
   }
	
   public void save(){
	   
	   if(canUndo()){
		   //only of the fragment is modified - what do we do about prompt?				
		   //find the edit text
		   String newScene = text.getText().toString();
		   String newPrompt = getPrompt();
		   
		   npm.updateScene(scene.getPath(), newScene, newPrompt);
		   Log.e(TAG, "modified");
	   }
   }
   
   public long getWordCount(){
	   return NovelColoriser.wordCount(text.getText().toString());
   }
}
