package org.openintents.filemanager.dialogs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import org.openintents.filemanager.util.*;
import us.writeo.novelwriter.*;

import android.support.v4.app.DialogFragment;

public class OverwriteFileDialog extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setInverseBackgroundForced(UIUtils.shouldDialogInverseBackground(getActivity()))
				.setTitle(R.string.file_exists)
				.setMessage(R.string.overwrite_question)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Overwritable) getTargetFragment()).overwrite();
							}
						}).setNegativeButton(android.R.string.cancel, null)
				.create();
	}
	
	public interface Overwritable {
		public void overwrite();
	}
}
