package com.halcyon.novelwriter;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.novelwriter.dummy.*;
import com.halcyon.novelwriter.model.*;

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

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.part_detail))
					.setText(mItem.getName());
		}

		return rootView;
	}
}
