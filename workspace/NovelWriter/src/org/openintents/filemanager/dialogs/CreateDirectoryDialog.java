package org.openintents.filemanager.dialogs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import java.io.*;
import org.openintents.filemanager.dialogs.OverwriteFileDialog.*;
import org.openintents.filemanager.lists.*;
import org.openintents.filemanager.util.*;
import org.openintents.intents.*;
import us.writeo.novelwriter.*;

import android.support.v4.app.DialogFragment;

public class CreateDirectoryDialog extends DialogFragment implements Overwritable {
	private File mIn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mIn = new File(getArguments().getString(FileManagerIntents.EXTRA_DIR_PATH));
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.dialog_text_input, null);
		final EditText v = (EditText) view.findViewById(R.id.foldername);
		v.setHint(R.string.folder_name);

		v.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView text, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO)
					createFolder(text.getText(), getActivity());
				return true;
			}
		});

		return new AlertDialog.Builder(getActivity())
				.setInverseBackgroundForced(UIUtils.shouldDialogInverseBackground(getActivity()))
				.setTitle(R.string.create_new_folder)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setView(view)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								createFolder(v.getText(), getActivity());
							}
						}).setNegativeButton(android.R.string.cancel, null)
				.create();
	}

	private void createFolder(final CharSequence text, Context c) {
		if (text.length() != 0) {
			tbcreated = new File(mIn + File.separator + text.toString());
			if (tbcreated.exists()) {
				this.text = text;
				this.c = c;
				OverwriteFileDialog dialog = new OverwriteFileDialog();
				dialog.setTargetFragment(this, 0);
				dialog.show(getFragmentManager(), "OverwriteFileDialog");
			} else {
				if (tbcreated.mkdirs())
					Toast.makeText(c, R.string.create_dir_success, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(c, R.string.create_dir_failure, Toast.LENGTH_SHORT).show();

				((FileListFragment) getTargetFragment()).refresh();
				dismiss();
			}
		}
	}
	
	private File tbcreated;
	private CharSequence text;
	private Context c;
	
	@Override
	public void overwrite() {
		tbcreated.delete();
		createFolder(text, c);
	}
}
