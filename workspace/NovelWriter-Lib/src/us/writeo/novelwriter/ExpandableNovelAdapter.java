package us.writeo.novelwriter;

import android.content.*;
import android.database.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import us.writeo.novel.model.*;
import us.writeo.novel.persistence.*;
import us.writeo.novelwriter.lib.*;


public class ExpandableNovelAdapter extends BaseExpandableListAdapter implements OnClickListener {

      private static final String TAG = "NW ExpandableNovelAdapter";

      private LayoutInflater inflater;
	  private Novel novel;
	  private NovelPersistenceManager npm = null;
	  
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
		return novel.getChapters().get(i);
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
	   textView.setText(((Chapter)getGroup(groupPosition)).getName());
	   view.setTag(holder);
	   
	   ImageButton up = (ImageButton) view.findViewById(R.id.up);
	   ImageButton down = (ImageButton) view.findViewById(R.id.down);
	   
	   up.setOnClickListener(this);
	   down.setOnClickListener(this);
	   up.setTag(holder);
	   down.setTag(holder);

	   holder.up = up;
	   holder.down = down;

	   if(groupPosition == 0)
	   {
		   //up.setActivated(false);
		   up.setVisibility(View.INVISIBLE);
	   } else {
		   //up.setActivated(true);
		   up.setVisibility(View.VISIBLE);
	   }

	   if(groupPosition == 
		  novel.getChapters().size()-1){
		   //down.setActivated(false);
		   down.setVisibility(View.INVISIBLE);
	   } else {
		   //down.setActivated(true);
		   down.setVisibility(View.VISIBLE);
	   }
	   
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
	   
	   ImageButton up = (ImageButton) view.findViewById(R.id.up);
	   ImageButton down = (ImageButton) view.findViewById(R.id.down);
	   
	   up.setOnClickListener(this);
	   down.setOnClickListener(this);
	   up.setTag(holder);
	   down.setTag(holder);
	   
	   holder.up = up;
	   holder.down = down;
	   
	   if(childPosition == 0)
	   {
		   //up.setActivated(false);
		   up.setVisibility(View.INVISIBLE);
	   } else {
		   //up.setActivated(true);
		   up.setVisibility(View.VISIBLE);
	   }
	   
	   
	   if(childPosition == 
	           novel.getChapters().get(groupPosition).getScenes().size()-1){
			//down.setActivated(false);
			down.setVisibility(View.INVISIBLE);
		} else {
			//down.setActivated(true);
			down.setVisibility(View.VISIBLE);
		}
		
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
   
   public void resetModel(Novel aNovel)
   {
	   novel = aNovel;
	   notifyDataSetChanged();
   }
   
	public Novel resetModel(NovelPersistenceManager aNpm)
	{
		novel = aNpm.getNovel();
		npm = aNpm;
		notifyDataSetChanged();
		return novel;
	}
	
   
   public void addChapter()
   {
	   novel.addChapter(new Chapter());
	   npm.updateNovel();
	   notifyDataSetChanged();
   }
   
	public void addScene(int toChapter)
	{
		novel.getChapters().get(toChapter).addScene(new Scene());
		npm.updateNovel();
		notifyDataSetChanged();
	}
   
   	public void insertScene(int toChapter, int beforeScene)
	{
		novel.getChapters().get(toChapter).getScenes().add(beforeScene, new Scene());
		npm.updateNovel();
		notifyDataSetChanged();
	}
   
	public void insertChapter(int position)
	{
		novel.insertChapter(new Chapter(), position);
		npm.updateNovel();
		notifyDataSetChanged();
	}
   
   public void removeChapter(int position)
   {
	   Chapter chapter = novel.getChapters().get(position);
	   List<String> scenesToRemove = new ArrayList<String>();
	   for(Scene scene:chapter.getScenes()){
		   scenesToRemove.add(scene.getPath());
	   }
	   novel.removeChapter(position);
	   npm.updateNovel();
	   npm.deleteEntries(scenesToRemove);
	   notifyDataSetChanged();
   }
   
   public void removeScene(int chapterPos, int scenePos)
   {
	   Chapter chapter = novel.getChapters().get(chapterPos);
	   Scene scene = chapter.getScenes().get(scenePos);
	   chapter.removeScene(scenePos);
	   npm.updateNovel();
	   npm.deleteEntry(scene.getPath());
	   notifyDataSetChanged();
   }
   
   public void updateNovel(){
	   npm.updateNovel();
	   notifyDataSetChanged();
   }
      
   @Override public void onClick(View view) {
	   Log.e(TAG, "onClick called");
	   
       ViewHolder holder = (ViewHolder)view.getTag();
	   if(holder != null ){
	       if(holder.up != null){
               if (view.getId() == holder.up.getId()){
                    // up pressed
			        if(holder.childPosition == -1){
				        //chapter button presses
			            swap(novel.getChapters(), holder.groupPosition, holder.groupPosition-1);
						npm.updateNovel();
			            notifyDataSetChanged();
			        } else {
			           swap(novel.getChapters().get(holder.groupPosition).getScenes(), 
			                holder.childPosition, holder.childPosition-1);
						npm.updateNovel();
				        notifyDataSetChanged();
			        }				
                }
	        }
			
		   if(holder.down != null){
               if (view.getId() == holder.down.getId()){
				   // down pressed
				   if(holder.childPosition == -1){
					   //chapter button presses
					   swap(novel.getChapters(), holder.groupPosition, holder.groupPosition+1);
					   notifyDataSetChanged();
				   } else {
			           swap(novel.getChapters().get(holder.groupPosition).getScenes(), 
								  holder.childPosition, holder.childPosition+1);
					   notifyDataSetChanged();
				   }				
			   }
		   }
	   }
   }
   
   protected class ViewHolder {
	   protected int childPosition = -1;
	   protected int groupPosition = -1 ;
	   protected ImageButton up;
	   protected ImageButton down;
	}
	
	/*ArrayList<T> syntax doesn't work */
	
	
	public <E> void swap(List<E> list, int firstInd, int secondInd){
		E temp =  list.set( firstInd, list.get( secondInd ) ) ; 
	    list.set( secondInd, temp ) ; 
	}
	
	/*public void swapChapters( List<Chapter> list, int firstInd, int secondInd ) { 
	    Chapter temp =  list.set( firstInd, list.get( secondInd ) ) ; 
	    list.set( secondInd, temp ) ; 
	}
	
	public void swapScenes( List<Scene> list, int firstInd, int secondInd ) { 
	    Scene temp =  list.set( firstInd, list.get( secondInd ) ) ; 
	    list.set( secondInd, temp ) ; 
	}*/
}
