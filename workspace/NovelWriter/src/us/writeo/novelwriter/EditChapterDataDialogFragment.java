package us.writeo.novelwriter;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import us.writeo.common.novel.model.*;
import us.writeo.*;


public class EditChapterDataDialogFragment extends SherlockDialogFragment {

    private Chapter chapter = null;


    public EditChapterDataDialogFragment(){

	}

	public EditChapterDataDialogFragment(Chapter aChapter){
         chapter = aChapter;
	}
	
	public void selectChapter(Chapter aChapter){
		chapter = aChapter;
	}
	


    // Use this instance of the interface to deliver action events    
	EditChapterDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener    
	@Override public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Verify that the host activity implements the callback interface        
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host            
			mListener = (EditChapterDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception            
			throw new ClassCastException(activity.toString()
										 + " must implement EditChapterDialogListener");
		}
	}

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {  	

	    if(chapter == null)
			throw new IllegalStateException("You must set the chapter before invoking the dialog");

		// Get the layout inflater 
		LayoutInflater inflater = getActivity().getLayoutInflater();
	
	    View chapterEdit = inflater.inflate(R.layout.edit_chapter, null);
		final EditText name = (EditText)chapterEdit.findViewById(R.id.chapter_name);
		name.setText(chapter.getName());
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(("Edit Chapter..."))
			.setView(chapterEdit)
			// Add action buttons
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					chapter.setName(name.getText().toString());
					mListener.onChapterChanged(chapter);
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					getDialog().cancel();
				}
			});

		return builder.create();
	}
}

