package com.halcyon.novelgenerator.model;

	import android.util.*;
	import com.halcyon.storywriter.template.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class CharactersHelper
	{
		private static final String TAG = "NW CharactersHelper";

		public static Characters readCharacters(String charactersXml)
		{
			Characters characters = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(charactersXml); 
			try
			{
				characters = serializer.read(Characters.class, reader, false);
				Log.e(TAG, "we got a novel"+characters);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return characters;
		}

		public static String charactersToString(Characters characters){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(characters, writer);
				Log.e(TAG, "we got a novel"+characters);
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

