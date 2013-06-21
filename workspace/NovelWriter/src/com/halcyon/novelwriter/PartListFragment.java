package com.halcyon.novelwriter;

import android.app.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.novelwriter.model.*;

/**
 * A list fragment representing a list of Parts. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link PartDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PartListFragment extends SherlockExpandableListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private static final String TAG = "PartListFragment";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	
	private View mCurrentView = null;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onChapterSelected(Chapter chapter);
		public void onSceneSelected( Scene scene);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onChapterSelected(Chapter chapter) {
		}
		
		@Override
		public void onSceneSelected(Scene scene){
			
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PartListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: replace with a real list adapter.
		//int layout = (Build.VERSION.SDK_INT >= 11) ? android.R.layout.simple_list_item_activated_1
	    //			: android.R.layout.simple_list_item_1;
		setListAdapter(new ExpandableNovelAdapter(getActivity(), getNovel()));
	}
	
	private Novel getNovel()
	{
		Novel novel = new Novel();
		
		Chapter c ;
		Scene s;
		
		c = new Chapter();
		s = new Scene();
		
		c.setName("Chapter 1");
		s.setName("Scene 1");
		
		c.addScene(s);
		novel.addChapter(c);
		
		c = new Chapter();
		s = new Scene();

		c.setName("Chapter 2");
		s.setName("Scene 2");

		c.addScene(s);
		novel.addChapter(c);
		
		return novel;
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

	   Toast.makeText(getActivity(), "partlistfragment:onlistitemclick", Toast.LENGTH_LONG);
		Log.e(TAG, "onListItemClick");
		
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		//mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
	}

	@Override
	public boolean onChildClick( ExpandableListView parent, View v, 
	                              int groupPosition, int childPosition, long id) {
		
		  //Toast.makeText(getActivity(), "partlistfragment:onchildclick", Toast.LENGTH_LONG);
		  Log.e(TAG, "onChildClick");
		
		  //int index = parent.getFlatListPosition(
		  //       ExpandableListView.getPackedPositionForChild(groupPosition, childPosition)); 
		  setActivatedView(v);
		
		  Scene child = (Scene)getExpandableListAdapter().getChild(groupPosition, childPosition);						
		  mCallbacks.onSceneSelected(child);
						
	      return true;
	}
	
	@Override
    public void onGroupExpand(int arg0) {
        // TODO Auto-generated method stub
		Log.e(TAG, "onGroupExpand");
    }

    @Override
    public void onGroupCollapse(int arg0) {
        // TODO Auto-generated method stub
		Log.e(TAG, "onGroupCollapse");
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
	    					: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	private void setActivatedView(View currentView) {
		
		if(mCurrentView == currentView) return ;
		
		Drawable c = currentView.getBackground();
		
		if (mCurrentView != null) {
			mCurrentView.setBackgroundDrawable(c);
		}

		currentView.setBackgroundColor(Color.LTGRAY);
		mCurrentView = currentView;
	}
}
