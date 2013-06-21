package com.halcyon.novelwriter;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.novelwriter.model.*;



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
		PartListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private static String TAG = "NW";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_list);

		if (findViewById(R.id.part_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((PartListFragment) getSupportFragmentManager().findFragmentById(
					R.id.part_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
    {
		Log.e(TAG, "partlistactivity:onviewcreated");
		((PartListFragment) getSupportFragmentManager().findFragmentById(
			R.id.part_list)).onContentChanged();
	}

	@Override
	public void onContentChanged()
	{
		Log.e(TAG, "partlistactivity:oncontentchanged");
		((PartListFragment) getSupportFragmentManager().findFragmentById(
			R.id.part_list)).onContentChanged();
	}
	
	
	/**
	 * Callback method from {@link PartListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		Toast.makeText(this, "on item selected", Toast.LENGTH_SHORT);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(PartDetailFragment.ARG_ITEM_ID, id);
			PartDetailFragment fragment = new PartDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.part_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PartDetailActivity.class);
			detailIntent.putExtra(PartDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public void onChapterSelected(Chapter chapter)
	{
		
	}
	
	public void onSceneSelected(Scene scene)
	{
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putSerializable("scene", scene);
			//arguments.putString(PartDetailFragment.ARG_ITEM_ID, id);
			PartDetailFragment fragment = new PartDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.part_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PartDetailActivity.class);
			detailIntent.putExtra(PartDetailFragment.ARG_ITEM_ID, scene);
			startActivity(detailIntent);
		}
	}
}
