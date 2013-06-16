package com.halcyon.storywriter;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.actionbarsherlock.app.*;
import java.io.*;
import java.util.*;

public class ChooseTemplateDialogFragment extends SherlockDialogFragment {

    private TemplateFileManager tfm;

    public ChooseTemplateDialogFragment(){
		
	}

	public interface TemplateDialogListener {
		public void onTemplateChosen(StructureFileTemplate file);
	}

    // Use this instance of the interface to deliver action events    
	TemplateDialogListener mListener;
	
	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener    
	@Override public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Verify that the host activity implements the callback interface        
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host            
			mListener = (TemplateDialogListener) activity;
		
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception            
			throw new ClassCastException(activity.toString()
			+ " must implement TemplateDialogListener");
			}
		}
	
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {  	

		tfm = new TemplateFileManager(getActivity());
	    final List<StructureFileTemplate> templates = tfm.getStructureTemplates();
		final TemplateArrayAdapter adapter = new TemplateArrayAdapter(getActivity(),
																	  0, templates); 
	
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(("Choose template..."))
			.setAdapter(adapter, new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			           // The 'which' argument contains the index position               
			           // of the selected item         
					   
					   mListener.onTemplateChosen(templates.get(which));
			      }});
			
		return builder.create();
	}

	
	/*private List<StructureTemplate> getStructureTemplates()
	{
		ArrayList<StructureTemplate> ret = new ArrayList<StructureTemplate>();

		AssetManager assetManager = getActivity().getAssets();

		try
		{
		    String[] files = assetManager.list("templates");
			StructureTemplate st;
			for(int i=0; i<files.length; i++) {
				//Toast.makeText(this, files[i], Toast.LENGTH_SHORT);
				st = new StructureTemplate();
				st.setFile(files[i]);
				st.setName(files[i].substring(0, files[i].lastIndexOf('.')).replace('_', ' '));
				ret.add(st);
			}
		}catch (IOException e1) {
		    e1.printStackTrace();
		}

		return ret;
	}*/
}
