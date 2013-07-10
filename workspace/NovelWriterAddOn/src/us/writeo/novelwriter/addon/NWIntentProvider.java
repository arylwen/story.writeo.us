package us.writeo.novelwriter.addon;

import java.io.*;
import org.openintents.intents.*;

public class NWIntentProvider implements IntentProvider, Serializable
{

	public String getActionPickFile()
	{
		return NWFilePickerIntents.ACTION_PICK_FILE;
	}

	public String getActionPickDirectory()
	{
		return NWFilePickerIntents.ACTION_PICK_DIRECTORY;
	}

}
