package com.halcyon.novelwriter;

import android.content.*;
import android.database.*;
import android.view.*;
import android.widget.*;
import com.halcyon.novelwriter.model.*;


public class ExpandableNovelAdapter extends BaseExpandableListAdapter {
      private LayoutInflater inflater;
	  private Novel novel;
	  
	  public ExpandableNovelAdapter(Context context, Novel aNovel){	  
	      novel = aNovel;
	      inflater = LayoutInflater.from(context);
	  }
	  
	  @Override
	  //counts the number of group/parent items so the list knows how many times calls getGroupView() method
	  public int getGroupCount() {
		  return novel.getChapters().size();
      }
	  
	  @Override
	  //counts the number of children items so the list knows how many times calls getChildView() method 
	  public int getChildrenCount(int i) {
		  return novel.getChapters().get(i).getScenes().size();
	}
	
	@Override
	//gets the title of each parent/group    
	public Object getGroup(int i) {
		return novel.getChapters().get(i).getName();
		}
		
		
   @Override
   //gets the name of each item    
   public Object getChild(int i, int i1) {
	   return novel.getChapters().get(i).getScenes().get(i1);
   }
   
   @Override
   public long getGroupId(int i) {
	   return i;
   }
   
   @Override
   public long getChildId(int i, int i1) {
	   return i1;
   }
   
   @Override
   public boolean hasStableIds() {
	   return true;
   }
   
   @Override
   //in this method you must set the text to see the parent/group on the list    
   public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
	   ViewHolder holder = new ViewHolder();
	   holder.groupPosition = groupPosition;
	   
	   if (view == null) {
		   view = inflater.inflate(R.layout.list_item_parent, viewGroup,false);
	   }
	   
	   TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
	   
	   //"i" is the position of the parent/group in the list        
	   textView.setText(getGroup(groupPosition).toString());
	   view.setTag(holder);
	   
	   //return the entire view
	   return view;
   }
   
   @Override
   //in this method you must set the text to see the children on the list    
   public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
	   ViewHolder holder = new ViewHolder();
	   holder.childPosition = childPosition;
	   holder.groupPosition = groupPosition;
	   if (view == null) {
		   view = inflater.inflate(R.layout.list_item_child, viewGroup,false);
	   }
	   
	   TextView textView = (TextView) view.findViewById(R.id.list_item_text_child);
	   
	   //"i" is the position of the parent/group in the list and
	   //"i1" is the position of the child
	   textView.setText(novel.getChapters().get(groupPosition).getScenes().get(childPosition).getName());	   
	   view.setTag(holder);
	   //return the entire view        
	   return view;
    }
	
   @Override
   public boolean isChildSelectable(int i, int i1) {
	   return true;
   }
   
   @Override
   public void registerDataSetObserver(DataSetObserver observer) 
   {
	   /* used to make the notifyDataSetChanged() method work */
	   super.registerDataSetObserver(observer);
   }
   
   // Intentionally put on comment, if you need on click deactivate it
   /*  @Override    public void onClick(View view) { 
          ViewHolder holder = (ViewHolder)view.getTag(); 
          if (view.getId() == holder.button.getId()){ 
              // DO YOUR ACTION  
       }    }*/
   
   protected class ViewHolder {
	   protected int childPosition;
	   protected int groupPosition;
	   protected Button button;
	}
	
	}
