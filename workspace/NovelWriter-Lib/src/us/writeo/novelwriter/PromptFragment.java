package us.writeo.novelwriter;

import us.writeo.common.TextViewUndoRedo;
import us.writeo.novelwriter.lib.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import com.actionbarsherlock.app.SherlockFragment;

public class PromptFragment extends SherlockFragment {

	public static final String ARG_PROMPT = "prompt";

	private EditText prompt;
	private TextViewUndoRedo undoRedo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_prompt, container,
				false);

		ScrollView scroll = (ScrollView) rootView
				.findViewById(R.id.promptscroll);
		if (scroll != null) {
			scroll.setFillViewport(true);
			scroll.setHorizontalScrollBarEnabled(false);
		}

		Bundle args = getArguments();
		prompt = ((EditText) rootView.findViewById(R.id.prompt));
		prompt.setText(args.getString((ARG_PROMPT)));

		// attach undo/redo behavior to text _after_ cleaning up
		// so it doesn't register a undo operarion
		undoRedo = new TextViewUndoRedo(prompt);

		return rootView;
	}

	public String getPrompt() {
		String ret = null;
		if (prompt != null) {
			ret = prompt.getText().toString();
		}
		return ret;
	}

	// undo/redo related functions
	public boolean canUndo() {
		return undoRedo.getCanUndo();
	}

	public boolean canRedo() {
		return undoRedo.getCanRedo();
	}

	public void undo() {
		undoRedo.undo();
	}

	public void redo() {
		undoRedo.redo();
	}
}
