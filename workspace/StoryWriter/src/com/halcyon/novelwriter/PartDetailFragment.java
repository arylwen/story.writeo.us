package com.halcyon.novelwriter;

import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.novelwriter.model.*;
import com.halcyon.storywriter.*;
import com.halcyon.storywriter.template.*;
import java.io.*;
import java.util.*;

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
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The content this fragment is presenting.
	 */
	private Scene mItem;
	private StoryTemplateHelper helper;
	private EditText text;
	private NovelColoriser coloriser;
	private TemplateFileManager tfm;
	private StructureFileTemplate currentFileTemplate;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PartDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new StoryTemplateHelper();		
		tfm = new TemplateFileManager(getActivity());
		
		if (getArguments().containsKey("scene")) {
			mItem = (Scene)getArguments().getSerializable(("scene"));
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
			
		String textStr = null;
		if (getArguments().containsKey("text")) {
			textStr = (String)getArguments().getSerializable(("text"));
		}						
		text = 	((EditText) rootView.findViewById(R.id.note));			
		if (textStr != null) {		
					text.setText(textStr);
		}
		long wordsBeforeScene = 0;
		if (getArguments().containsKey("wordsBeforeScene")) {
			wordsBeforeScene = getArguments().getLong("wordsBeforeScene");
		}	
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

		List<StructureFileTemplate> templates = tfm.getStructureTemplates();
		onTemplateChosen(templates.get(0));	
		
		String prompt = null;
		if (getArguments().containsKey("prompt")) {
			prompt = (String)getArguments().getSerializable(("prompt"));
		}
		
		if (prompt != null) {
			((EditText) rootView.findViewById(R.id.prompt))
					.setText(prompt);
		}
		
		return rootView;
	}
	
	@Override
	public void onTemplateChosen(StructureFileTemplate file){
		String template = tfm.readAssetFile("templates"+File.separator+ file.getFile());
		helper.setTemplate(template);
		currentFileTemplate = file;
		//if(templateName != null)
		//{
			//this widget doesn't exist in portrait mode
		//    templateName.setText(file.getName());
		//	updateTemplateSummary();
		//}
		text.getText().clearSpans();
		text.setText(text.getText());
	}
	
}
