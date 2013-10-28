package us.writeo.novelwriter;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import us.writeo.*;
import com.actionbarsherlock.app.*;
import us.writeo.common.novel.model.*;


public class EditSceneDataDialogFragment extends SherlockDialogFragment {

    private Scene scene = null;


    public EditSceneDataDialogFragment(){

	}

	public EditSceneDataDialogFragment(Scene aScene){
		scene = aScene;
	}

	public void selectChapter(Scene aScene){
		scene = aScene;
	}

	public interface EditSceneDialogListener
	{
		public void onSceneChanged(Scene scene);
	}

    // Use this instance of the interface to deliver action events    
	EditSceneDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener    
	@Override public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Verify that the host activity implements the callback interface        
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host            
			mListener = (EditSceneDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception            
			throw new ClassCastException(activity.toString()
										 + " must implement EditSceneDialogListener");
		}
	}

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {  	

	    if(scene == null)
			throw new IllegalStateException("You must set the scene before invoking the dialog");

		// Get the layout inflater 
		LayoutInflater inflater = getActivity().getLayoutInflater();

	    View chapterEdit = inflater.inflate(R.layout.edit_scene, null);
		final EditText name = (EditText)chapterEdit.findViewById(R.id.scene_name);
		name.setText(scene.getName());
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(("Edit Scene..."))
			.setView(chapterEdit)
			// Add action buttons
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					scene.setName(name.getText().toString());
					mListener.onSceneChanged(scene);
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

