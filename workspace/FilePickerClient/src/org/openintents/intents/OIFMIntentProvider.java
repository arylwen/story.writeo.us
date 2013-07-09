package org.openintents.intents;

import java.io.*;

public class OIFMIntentProvider implements IntentProvider, Serializable
{

	public String getActionPickFile()
	{
		return FileManagerIntents.ACTION_PICK_FILE;
	}

	public String getActionPickDirectory()
	{
		return FileManagerIntents.ACTION_PICK_DIRECTORY;
	}

}
