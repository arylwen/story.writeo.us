package com.halcyon.storywriter;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.halcyon.storywriter.*;
import com.halcyon.storywriter.template.*;
import java.util.*;

import com.halcyon.storywriter.StructureFileTemplate;

public class TemplateArrayAdapter extends ArrayAdapter<StructureFileTemplate>
 { 
	    private HashMap<StructureFileTemplate, Integer> mIdMap = new HashMap<StructureFileTemplate, Integer>(); 
		// used to keep selected position in ListView 		
		private int selectedPos = -1;	// init value for not-selected 
		
		//private StoryTemplateHelper storyTemplateHelper;
		
		public TemplateArrayAdapter(Context context, int textViewResourceId, List<StructureFileTemplate> objects) {
			super(context, textViewResourceId, objects); 
			for (int i = 0; i < objects.size(); ++i) { 
			    mIdMap.put(objects.get(i), i); 
			} 
		} 
		
		@Override 
		public long getItemId(int position) { 
		    StructureFileTemplate item = getItem(position); 
			return mIdMap.get(item); 
		} 
		
		@Override public boolean hasStableIds() { 
		    return true; 
		} 
		
		public void setSelectedPosition(int pos){ 			
		    selectedPos = pos; 			
			// get item, read file, update helper
			// inform the view of this change 			
			notifyDataSetChanged(); 		
		} 		
		
		public int getSelectedPosition(){ 			
		    return selectedPos; 		
		} 		
		
		@Override 		
		public View getView(int position, View convertView, ViewGroup parent) { 		 
		    View v = convertView; 		 // only inflate the view if it's null 		 
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(
				    Context.LAYOUT_INFLATER_SERVICE); 		 
					v = vi.inflate(R.layout.selected_row, null); 		 
			} 		 
			
			// get text view 	 
			TextView label = (TextView)v.findViewById(R.id.txtExample); 	 
			
			// change the row color based on selected state 	 
			if(selectedPos == position){
				label.setBackgroundColor(Color.BLUE);
			}else{
				label.setBackgroundColor(Color.TRANSPARENT);
			} 	 
			//label.setText(this.getItem(position).toString()); 	 
			 // to use something other than .toString()
			StructureFileTemplate myobj = this.getItem(position);
			label.setText(myobj.getName());
			
			
			return(v);
		}
	} 
