package us.writeo.intents;

import java.io.*;
import org.openintents.intents.*;

public class WriteoUSIntentProvider implements IntentProvider, Serializable
	{

		public String getActionPickFile()
		{
			return WriteoUSIntents.ACTION_PICK_FILE;
		}

		public String getActionPickDirectory()
		{
			return WriteoUSIntents.ACTION_PICK_DIRECTORY;
		}

	}
