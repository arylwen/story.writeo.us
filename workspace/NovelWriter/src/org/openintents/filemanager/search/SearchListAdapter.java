package org.openintents.filemanager.search;

import android.content.*;
import android.database.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import org.openintents.filemanager.files.*;
import org.openintents.filemanager.view.*;
import us.writeo.novelwriter.*;

/**
 * Simple adapter for displaying search results.
 * @author George Venios
 *
 */
public class SearchListAdapter extends CursorAdapter {
	private HashMap<String, FileHolder> itemCache = new HashMap<String, FileHolder>();

	public SearchListAdapter(Context context, Cursor c) {
		super(context, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String path = cursor.getString(cursor.getColumnIndex(SearchResultsProvider.COLUMN_PATH));
		FileHolder fHolder;
		if((fHolder = itemCache.get(path)) == null){
			fHolder = new FileHolder(new File(path), context);
			itemCache.put(path, fHolder);
		}

		ViewHolder h = (ViewHolder) view.getTag();
		h.primaryInfo.setText(fHolder.getName());
		h.secondaryInfo.setText(path);
		h.icon.setImageDrawable(fHolder.getIcon());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Inflate the view
		ViewGroup v = (ViewGroup) ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.item_filelist, null);

		// Set the viewholder optimization.
		ViewHolder holder = new ViewHolder();
		holder.icon = (ImageView) v.findViewById(R.id.icon);
		holder.primaryInfo = (TextView) v.findViewById(R.id.primary_info);
		holder.secondaryInfo = (TextView) v.findViewById(R.id.secondary_info);
		v.findViewById(R.id.tertiary_info).setVisibility(View.GONE);
		
		v.setTag(holder);
		
		return v;
	}
}
