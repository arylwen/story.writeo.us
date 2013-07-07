package com.halcyon.storywriter;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.halcyon.storywriter.template.StoryTemplateHelper;

public class StoryWriterActivity extends SherlockFragmentActivity implements
		ChooseTemplateDialogFragment.TemplateDialogListener,
		Coloriser.CounterListener {

	private static final String TAG = "SW StoryWriterActivity";

	private final static int MENU_NEW_FILE = Menu.FIRST;
	private final static int MENU_SAVE_FILE = Menu.FIRST + 1;
	private final static int MENU_OPEN_FILE = Menu.FIRST + 2;
	private final static int MENU_SAVE_FILE_AS = Menu.FIRST + 3;
	private final static int MENU_CHOOSE_STRUCT = Menu.FIRST + 4;

	private EditText text;
	private EditText prompt;
	private String promptStr; // when not visible we still need to keep it
	private TextView title = null;
	private TextView templateName;
	private TextView templateSummary;
	private String fileName = "";
	private boolean isUntitled = true;
	private boolean isChanged = false;

	private StructureTemplateHelper helper;
	private TemplateFileManager tfm;
	private StructureFileTemplate currentFileTemplate;
	private Coloriser coloriser;
	private FileManager fm;
	private FilePicker fpk;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = new FileManager();
		helper = new StoryTemplateHelper();
		fpk = new SWFilePicker(this);

		setContentView(R.layout.edit);

		text = (EditText) findViewById(R.id.note);
		text.setHorizontallyScrolling(false);

		// setup the scroll view to fill the parent
		ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
		if (scroll != null) {
			scroll.setFillViewport(true);
			scroll.setHorizontalScrollBarEnabled(false);
		}

		prompt = (EditText) findViewById(R.id.prompt);
		if (prompt != null) {
			prompt.setText("add notes here");

			// setup the scroll view to fill the parent
			ScrollView promptscroll = (ScrollView) findViewById(R.id.promptscroll);
			if (promptscroll != null) {
				promptscroll.setFillViewport(true);
				promptscroll.setHorizontalScrollBarEnabled(false);
			}

			// setup the template scroll view to fill the parent
			ScrollView templatescroll = (ScrollView) findViewById(R.id.templatescroll);
			if (templatescroll != null) {
				templatescroll.setFillViewport(true);
				templatescroll.setHorizontalScrollBarEnabled(false);
			}
		}

		View customNav = LayoutInflater.from(this).inflate(
				R.layout.custom_title_bar, null);
		title = (TextView) customNav.findViewById(R.id.notetitle);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		fileName = getResources().getString(R.string.newFileName);
		templateName = (TextView) findViewById(R.id.template_name);
		templateSummary = (TextView) findViewById(R.id.template_summary);

		coloriser = new Coloriser(text, helper);
		tfm = new TemplateFileManager(this);
		List<StructureFileTemplate> templates = tfm.getStructureTemplates();
		onTemplateChosen(templates.get(0));
		coloriser.setCounterListener(this);

		text.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence one, int a, int b, int c) {
				coloriser.onTextChanged(one, a, b, c);
				isChanged = true;
			}

			// complete the interface
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
	}

	@Override
	public void onTemplateChosen(StructureFileTemplate file) {
		String template = tfm.readAssetFile("templates" + File.separator
				+ file.getFile());
		helper.setTemplate(template);
		currentFileTemplate = file;
		if (templateName != null) {
			// this widget doesn't exist in portrait mode
			templateName.setText(file.getName());
			updateTemplateSummary();
		}
		text.getText().clearSpans();
		text.setText(text.getText());
	}

	@Override
	public void onWordCountUpdate(long wordc) {
		// update file name and counter
		String temp = (isChanged ? "*" : "") + fileName + " " + wordc
				+ " words";
		title.setText(temp);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause called");

		Toast.makeText(this, "on pause called", Toast.LENGTH_SHORT).show();

		saveState();
	}

	private void saveState() {
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

		if (editor != null && text != null) {
			String t;

			t = text.getText().toString();
			editor.putString("text", t);

			if (prompt != null) {
				// null in portrait mode
				t = prompt.getText().toString();
				editor.putString("prompt", t);
			}

			editor.putString("fileName", fileName);

			editor.putBoolean("isUntitled", isUntitled);
			editor.putBoolean("isChanged", isChanged);

			editor.putString("templateName", currentFileTemplate.getName());
			editor.putString("templateFile", currentFileTemplate.getFile());

			editor.commit();
		}
	} // end saveState()

	@Override
	protected void onResume() {
		super.onResume();

		Log.e(TAG, "onResume");
		Toast.makeText(this, "on resume called", Toast.LENGTH_SHORT).show();

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String restoredText = prefs.getString("text", null);
		String restoredPrompt = prefs.getString("prompt", null);
		String restoredFileName = prefs.getString("fileName", null);
		String restoredTemplateName = prefs.getString("templateName", null);
		String restoredTemplateFileName = prefs.getString("templateFile", null);

		if (restoredFileName != null) {
			fileName = restoredFileName;
		} else {
			fileName = getResources().getString(R.string.newFileName);
		}
		if (restoredText != null && text != null) {
			text.setText(restoredText);
		}

		if (restoredPrompt != null && prompt != null) {
			prompt.setText(restoredPrompt);
		}

		if (restoredPrompt != null) {
			promptStr = restoredPrompt;
		}

		isChanged = prefs.getBoolean("isChanged", false);
		isUntitled = prefs.getBoolean("isUntitled", true);

		if (restoredTemplateName != null) {
			StructureFileTemplate ft = new StructureFileTemplate();
			ft.setFile(restoredTemplateFileName);
			ft.setName(restoredTemplateName);
			// load template and update the colors
			onTemplateChosen(ft);
		}

		if (text != null)
			text.requestFocus();
	} // end onResume

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_NEW_FILE, 0, "New").setShortcut('0', 'n')
				.setIcon(R.drawable.ic_new);
		menu.add(0, MENU_OPEN_FILE, 0, "Open").setShortcut('0', 'o')
				.setIcon(R.drawable.ic_open);
		menu.add(0, MENU_SAVE_FILE, 0, "Save").setShortcut('0', 's')
				.setIcon(R.drawable.ic_save);
		menu.add(0, MENU_SAVE_FILE_AS, 0, "Save As...").setShortcut('0', 'a')
				.setIcon(R.drawable.ic_save);
		menu.add(0, MENU_CHOOSE_STRUCT, 0, "Choose Structure...")
				.setShortcut('0', 'a').setIcon(R.drawable.ic_open);

		return true;
	} // end onCreateOptionsMenu()

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_NEW_FILE:
			// TO DO check if dirty
			// initialize
			prompt.setText("add notes here");
			title.setText("");
			isUntitled = true;
			isChanged = false;
			fileName = getResources().getString(R.string.newFileName);

			// this would also trigger the update of the word counter and file
			// name
			text.setText("");

			break;
		case MENU_OPEN_FILE:
			fpk.pickFileForOpen();
			break;
		case MENU_SAVE_FILE:

			if (isUntitled) {
				// browse for a file
				fpk.pickFileForSave();
			} else
				// see if in text or meta
				fm.saveText(fileName, text.getText().toString(), this);
			updateText(text.getText().toString(), fileName);
			// save prompt
			String pmt;
			if ((prompt != null) || (promptStr != null)) {
				if (prompt != null)
					pmt = prompt.getText().toString();
				else
					pmt = promptStr;
				fm.saveText(getPromptFileName(fileName), pmt, this);
			}
			break;

		case MENU_SAVE_FILE_AS:
			fpk.pickFileForSave();
			break;

		case MENU_CHOOSE_STRUCT:
			showChooseTemplateDialog();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		Toast.makeText(this, "on activity result called", Toast.LENGTH_SHORT)
				.show();

		switch (requestCode) {

		case FilePicker.SAVE_FILE_REQUEST_CODE:

			if (RESULT_OK == resultCode) {

				Uri fileUri = intent.getData();
				String fName = fileUri.getPath();

				// save story, update file name and save state
				fm.saveText(fName, text.getText().toString(), this);
				updateText(text.getText().toString(), fName);

				// save prompt
				String pmt;
				if ((prompt != null) || (promptStr != null)) {
					if (prompt != null)
						pmt = prompt.getText().toString();
					else
						pmt = promptStr;
					fm.saveText(getPromptFileName(fName), pmt, this);
				}
			}
			break;

		case FilePicker.OPEN_FILE_REQUEST_CODE:

			if (RESULT_OK == resultCode) {

				Uri fileUri = intent.getData();
				String fName = fileUri.getPath();

				// restore prompt
				StringBuffer pmt = fm.openFile(getPromptFileName(fName), this);
				prompt.setText(pmt.toString());

				// restore story
				StringBuffer txt = fm.openFile(fName, this);
				// update file name, save story and prompt as state
				updateText(txt.toString(), fName);
			}
			break;
		}
	}

	private String getPromptFileName(String fName) {
		String ret = fName;

		if (ret.contains(".")) {
			ret = ret.substring(0, fName.lastIndexOf('.'));
		}

		return ret + ".pmt";
	}

	private void updateText(String aText, String aFileName) {
		// the filename is the new title
		fileName = aFileName;
		isUntitled = false;
		isChanged = false;

		// refreshes the spans and updates the title
		text.setText(aText);
		// set text would set the is changed to true...
		isChanged = false;
		saveState();
		text.requestFocus();
	}

	private void updateTemplateSummary() {

		SpannableString spannablecontent = coloriser.getColorisedTemplateInfo();
		templateSummary.setText(spannablecontent);
	}

	private void showChooseTemplateDialog() {
		DialogFragment newFragment = new ChooseTemplateDialogFragment();
		newFragment.show(getSupportFragmentManager(), "templates");
	}
}
