package org.openintents.filemanager.compatibility;

import android.os.Build.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import org.openintents.filemanager.bookmarks.*;

public class BookmarkListActionHandler {
	/**
	 * Offers a centralized bookmark action execution component.
	 * 
	 * @param item
	 *            The MenuItem selected.
	 * @param list
	 *            The list to act upon.
	 *            
	 * @param pos The selected item's position.
	 */
	public static void handleItemSelection(MenuItem item, ListView list) {
		
		// Single selection
		if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB
				|| ListViewMethodHelper.listView_getCheckedItemCount(list) == 1) {

			// Get id of selected bookmark.
			long id = -1;
			if(item.getMenuInfo() instanceof AdapterContextMenuInfo)
				id = list.getAdapter().getItemId(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			if (VERSION.SDK_INT > VERSION_CODES.HONEYCOMB)
				id = ListViewMethodHelper.listView_getCheckedItemIds(list)[0];
			
			// Handle selection
			switch (item.getItemId()) {
			/* GC case R.id.menu_delete:
				list.getContext().getContentResolver().delete(BookmarksProvider.CONTENT_URI, BookmarksProvider._ID + "=?", new String[] {""+id});
				break;*/
			}
			// Multiple selection
		} else {
			switch (item.getItemId()) {
			/*case R.id.menu_delete:
				long[] ids = ListViewMethodHelper.listView_getCheckedItemIds(list);
				for(int i=0; i<ids.length; i++){
					list.getContext().getContentResolver().delete(BookmarksProvider.CONTENT_URI, BookmarksProvider._ID + "=?", new String[] {""+ids[i]});
				}
				break;*/
			}
		}
		
		((BookmarkListAdapter)list.getAdapter()).notifyDataSetChanged();
	}
}
