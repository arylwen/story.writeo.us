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
import org.openintents.filemanager.files.*;
import org.openintents.filemanager.lists.*;
import org.openintents.filemanager.util.*;
import org.openintents.intents.*;
import us.writeo.novelwriter.*;

import android.support.v4.app.DialogFragment;

public class SingleCompressDialog extends DialogFragment implements Overwritable {
	private FileHolder mFileHolder;
	private CompressManager mCompressManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFileHolder = getArguments().getParcelable(FileManagerIntents.EXTRA_DIALOG_FILE_HOLDER);
		
		mCompressManager = new CompressManager(getActivity());
		mCompressManager.setOnCompressFinishedListener(new CompressManager.OnCompressFinishedListener() {
			
			@Override
			public void compressFinished() {
				((FileListFragment) SingleCompressDialog.this.getTargetFragment()).refresh();

				MediaScannerUtils.informFileAdded(getTargetFragment().getActivity().getApplicationContext(), tbcreated);
			}
		});
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dialog_text_input, null);
		final EditText v = (EditText) view.findViewById(R.id.foldername);
		v.setHint(R.string.compressed_file_name);
		
		v.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			public boolean onEditorAction(TextView text, int actionId, KeyEvent event) {
				   if (actionId == EditorInfo.IME_ACTION_GO)
					   compress(v.getText().toString());
				   dismiss();
				   return true;
				}
		});
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.menu_compress)
				.setView(view)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						compress(v.getText().toString());
					}
				}).setNegativeButton(android.R.string.cancel, null).create();
	}
	
	private void compress(final String zipname){
		tbcreated = new File(mFileHolder.getFile().getParent() + File.separator + zipname + ".zip");
		if (tbcreated.exists()) {
			this.zipname = zipname;
			OverwriteFileDialog dialog = new OverwriteFileDialog();
			dialog.setTargetFragment(this, 0);
			dialog.show(getFragmentManager(), "OverwriteFileDialog");
		} else {
			mCompressManager.compress(mFileHolder, tbcreated.getName());
		}
	}

	private File tbcreated;
	private String zipname;
	
	@Override
	public void overwrite() {
		tbcreated.delete();
		compress(zipname);
	}
}
