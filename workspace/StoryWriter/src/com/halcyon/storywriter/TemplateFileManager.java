package com.halcyon.storywriter;

/**
    Manages the template files. for now only
	deals with assets
**/
import android.content.res.*;
import android.util.*;
import java.io.*;
import java.util.*;
import android.content.*;

public class TemplateFileManager
{
	private Context context;
	
	public TemplateFileManager(Context aContext)
	{
		context = aContext;
	}
		
	public String readAssetFile(String fileName)
	{
		AssetManager assetManager = context.getAssets(); 
		InputStream inputStream = null; 

		String localFileName = fileName.replace(':', '/');

		try { 
		    inputStream = assetManager.open(localFileName); 
		} catch (IOException e) { 
			Log.e("cannot open "+localFileName, e.getMessage()); 
		}

		return readTextFile(inputStream);
	}

	private String readTextFile(InputStream inputStream) { 
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		byte buf[] = new byte[1024]; int len; 
		try { 
		    while ((len = inputStream.read(buf)) != -1) { 
			    outputStream.write(buf, 0, len); 
			} 
			outputStream.close(); 
			inputStream.close(); 
		} catch (IOException e) { } 

		return outputStream.toString(); 
	}


	public List<StructureTemplate> getStructureTemplates()
	{
		ArrayList<StructureTemplate> ret = new ArrayList<StructureTemplate>();

		AssetManager assetManager = context.getAssets();

		try
		{
		    String[] files = assetManager.list("templates");
			StructureTemplate st;
			for(int i=0; i<files.length; i++) {
				//Toast.makeText(this, files[i], Toast.LENGTH_SHORT);
				st = new StructureTemplate();
				st.setFile(files[i]);
				st.setName(files[i].substring(0, files[i].lastIndexOf('.')).replace('_', ' '));
				ret.add(st);
			}
		}catch (IOException e1) {
		    e1.printStackTrace();
		}

		return ret;
	}
}
