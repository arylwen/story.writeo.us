	package com.halcyon.novelgenerator.model;

	import android.util.*;
	import com.halcyon.storywriter.template.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class VerbsHelper
	{
		private static final String TAG = "NW VerbsHelper";

		public static Verbs readVerbs(String verbsXml)
		{
			Verbs verbs = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(verbsXml); 
			try
			{
				verbs = serializer.read(Verbs.class, reader, false);
				Log.e(TAG, "we got verbs"+verbs);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return verbs;
		}

		public static String charactersToString(Verbs verbs){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(verbs, writer);
				Log.e(TAG, "we got verbs"+verbs);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "characterstostring "+ret);

			return ret;
		}
	}

