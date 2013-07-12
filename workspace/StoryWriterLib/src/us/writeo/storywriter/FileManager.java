package us.writeo.storywriter;

import android.content.*;
import android.widget.*;
import java.io.*;

public class FileManager
{

	public boolean saveText(String fName, String aString, Context aContext)
	{
		boolean success = false;
		
		Toast.makeText(aContext, "Saving..."+fName, Toast.LENGTH_SHORT).show();

		// actually save the file here
		try {
			File f = new File(fName.toString());

			if ( (f.exists() && !f.canWrite()) || (!f.exists() && !f.getParentFile().canWrite()))
			{

				Toast.makeText(aContext, "Cannot save, not enough permissions!", Toast.LENGTH_SHORT).show();
              
				f = null;

			} else {
			    f = null; // hopefully this gets garbage collected

			    // Create file 
			    FileWriter fstream = new FileWriter(fName.toString());
			    BufferedWriter out = new BufferedWriter(fstream);

		        out.write(aString);

			    out.close();

			    // inform the user of success			     				 				 
				Toast.makeText(aContext, "File Saved", Toast.LENGTH_SHORT).show();		
				success = true;
			}
		} catch (Exception e) { //Catch exception if any
			Toast.makeText(aContext, "There was an error saving the file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		return success;
	}
	
	public StringBuffer openFile(CharSequence fname, Context aContext)
	{

		Toast.makeText(aContext, "Opening..."+fname, Toast.LENGTH_SHORT).show();

		StringBuffer result = new StringBuffer();

		try {
			// open file
			FileReader f = new FileReader(fname.toString());
			File file = new File(fname.toString());

			if (f == null)
			{
				throw(new FileNotFoundException());
			}

			if (file.isDirectory())
			{
				throw(new IOException());
			}

			// if the file has nothing in it there will be an exception here
			// that actually isn't a problem
			if (file.length() != 0 && !file.isDirectory())
			{			
				char[] buffer;
				buffer = new char[4800];	// made it bigger just in case

				int read = 0;

				do {
					read = f.read(buffer, 0, 4800);

					if (read >= 0)
					{
						result.append(buffer, 0, read);
					}
				} while (read >= 0);
			}
		} catch (FileNotFoundException e) {
			Toast.makeText(aContext, "Couldn't find file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(aContext, "There was an error opening the file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			Toast.makeText(aContext, "There was an error opening the file. "+
						   e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		return result;

	} // end openFile(CharSequence fname)
}
