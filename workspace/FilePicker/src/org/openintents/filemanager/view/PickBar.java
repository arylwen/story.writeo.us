package org.openintents.filemanager.view;

import android.content.*;
import android.os.*;
import android.os.Build.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import org.openintents.filemanager.*;

public class PickBar extends LinearLayout {
	private EditText mEditText;
	private Button mButton;
	private OnPickRequestedListener mListener;
	
	public PickBar(Context context) {
		super(context);
		init();
	}
	public PickBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		// Apply borderless style when applicable.
		if(VERSION.SDK_INT >= 11)
			mButton = new Button(getContext(), null, android.R.attr.buttonBarButtonStyle);
		else
			mButton = new Button(getContext());
		{
			mButton.setText(R.string.pick_button_default);
			mButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener!=null)
						mListener.pickRequested(mEditText.getText().toString());
				}
			});
		}

		// EditText
		mEditText = new EditText(getContext());
		{
			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			// Take up as much space as possible.
			layoutParams.weight = 1;

			mEditText.setLayoutParams(layoutParams);
			mEditText.setHint(R.string.filename_hint);
			mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
			mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
			mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_GO || (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
								if(mListener!=null)
									mListener.pickRequested(mEditText.getText().toString());
								return true;
							}

							return false;
						}
					});
		}
		
		addView(mEditText);
		addView(mButton);
	}

	public void setText(CharSequence name) {
		mEditText.setText(name);
	}
	
	public void setOnPickRequestedListener(OnPickRequestedListener listener) {
		mListener = listener;
	}
	
	public interface OnPickRequestedListener {
		public void pickRequested(String filename);
	}

	public void setButtonText(CharSequence text) {
		mButton.setText( (text == null || text.toString().trim().length() == 0) ? getResources().getString(R.string.pick_button_default) : text);
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle state = new Bundle();
		state.putParcelable("superState", super.onSaveInstanceState());
		state.putParcelable("editText", mEditText.onSaveInstanceState());
		state.putParcelable("button", mButton.onSaveInstanceState());
		
		return state;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle savedstate = (Bundle) state;
		
		super.onRestoreInstanceState(savedstate.getParcelable("superState"));
		mEditText.onRestoreInstanceState(savedstate.getParcelable("editText"));
		mButton.onRestoreInstanceState(savedstate.getParcelable("button"));
	}
}
