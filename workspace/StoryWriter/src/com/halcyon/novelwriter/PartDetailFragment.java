package com.halcyon.novelwriter;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.novelwriter.model.*;
import com.halcyon.storywriter.*;

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
	 * The dummy content this fragment is presenting.
	 */
	private Scene mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PartDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("scene")) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = (Scene)getArguments().getSerializable((
					"scene"));
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
			
		String text = null;
		if (getArguments().containsKey("text")) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			text = (String)getArguments().getSerializable((
										 "text"));
		}
			
		if (text != null) {
			((EditText) rootView.findViewById(R.id.note))
					.setText(text);
		}

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
}
