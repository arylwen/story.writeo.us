package org.openintents.filemanager.lists;

import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import org.openintents.filemanager.files.*;
import org.openintents.filemanager.util.*;
import org.openintents.filemanager.view.*;
import us.writeo.novelwriter.*;

/**
 * Dedicated file list fragment, used for multiple selection on platforms older than Honeycomb.
 * OnDestroy sets RESULT_OK on the parent activity so that callers refresh their lists if appropriate.
 * @author George Venios
 */
public class MultiselectListFragment extends FileListFragment {
	private LegacyActionContainer mLegacyActionContainer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.filelist_legacy_multiselect, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		super.onViewCreated(view, savedInstanceState);
		
		mAdapter.setItemLayout(R.layout.item_filelist_multiselect);
		
		// Init members
		mLegacyActionContainer =  (LegacyActionContainer) view.findViewById(R.id.action_container);
		mLegacyActionContainer.setMenuResource(R.menu.multiselect);
		mLegacyActionContainer.setOnActionSelectedListener(new LegacyActionContainer.OnActionSelectedListener() {
			@Override
			public void actionSelected(MenuItem item) {
				if(getListView().getCheckItemIds().length == 0){
					Toast.makeText(getActivity(), R.string.no_selection, Toast.LENGTH_SHORT).show();
					return;
				}
				
				ArrayList<FileHolder> fItems = new ArrayList<FileHolder>();
				
				for(long i : getListView().getCheckItemIds()){
					fItems.add((FileHolder) mAdapter.getItem((int) i));
				}
				
				MenuUtils.handleMultipleSelectionAction(MultiselectListFragment.this, item, fItems, getActivity());
			}
		});
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.options_multiselect, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ListView list = getListView();
		
		switch(item.getItemId()){
		/*GC case R.id.check_all:
			for(int i = 0; i < mAdapter.getCount(); i++){
				list.setItemChecked(i, true);
			}
			return true;
		case R.id.uncheck_all:
			for(int i = 0; i < mAdapter.getCount(); i++){
				list.setItemChecked(i, false);
			}
			return true;*/
		default:
			return false;
		}
	}
}
