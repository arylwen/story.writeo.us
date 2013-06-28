package com.halcyon.novelwriter.model;

import android.util.*;
import com.halcyon.storywriter.template.*;
import java.io.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

public class NovelHelper
{
    private static final String TAG = "NW NovelHelper";

    public static Novel readNovel(String novelXml)
	{
		Novel novel = null;
		
		Serializer serializer = new Persister(); 
		Reader reader = new StringReader(novelXml); 
		try
		{
		    novel = serializer.read(Novel.class, reader, false);
			Log.e(TAG, "we got a novel"+novel);
		} catch (Exception e) 
		{ 
		    //todo
			Log.e(TAG, e.getMessage());
		} catch (Throwable t) {
			Log.e(TAG, t.getMessage());
		}
		
		return novel;
	}
	
	public static String novelToString(Novel novel){
		
		Serializer serializer = new Persister(); 
		StringWriter writer = new StringWriter(); 
		try
		{
		    serializer.write(novel, writer);
			Log.e(TAG, "we got a novel"+novel);
		} catch (Exception e) 
		{ 
		    //todo
			Log.e(TAG, e.getMessage());
		} catch (Throwable t) {
			Log.e(TAG, t.getMessage());
		}
		
		StringBuffer sb = writer.getBuffer();
		String ret = sb.toString();
		
		Log.e(TAG, "noveltostring "+ret);
		
		return ret;
	}
}
