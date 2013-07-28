package us.writeo.storywriter.standalone;

import java.io.Serializable;

import org.openintents.intents.IntentProvider;

public class SWIntentProvider implements IntentProvider, Serializable
{

	public String getActionPickFile()
	{
		return SWFilePickerIntents.ACTION_PICK_FILE;
	}

	public String getActionPickDirectory()
	{
		return SWFilePickerIntents.ACTION_PICK_DIRECTORY;
	}

}
