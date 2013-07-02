	package com.halcyon.novelgenerator.model;

	import android.util.*;
	import com.halcyon.storywriter.template.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class SoundsHelper
	{
		private static final String TAG = "NW SoundsHelper";

		public static Sounds readSounds(String soundsXml)
		{
			Sounds sounds = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(soundsXml); 
			try
			{
				sounds = serializer.read(Sounds.class, reader, false);
				Log.e(TAG, "we got sounds"+sounds);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return sounds;
		}

		public static String soundsToString(Sounds sounds){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(sounds, writer);
				Log.e(TAG, "we got sounds"+sounds);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "soundstostring "+ret);

			return ret;
		}
	}

