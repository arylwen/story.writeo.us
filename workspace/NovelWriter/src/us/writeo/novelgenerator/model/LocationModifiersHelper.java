package us.writeo.novelgenerator.model;

import android.util.*;
import java.io.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

	public class LocationModifiersHelper
	{
		private static final String TAG = "NW LocationModifiersHelper";

		public static LocationModifiers readLocationModifers(String locationModifiersXml)
		{
			LocationModifiers locationModifiers = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(locationModifiersXml); 
			try
			{
				locationModifiers = serializer.read(LocationModifiers.class, reader, false);
				Log.e(TAG, "we got location modifiers"+locationModifiers);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return locationModifiers;
		}

		public static String locationModifiersToString(LocationModifiers locationModifiers){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(locationModifiers, writer);
				Log.e(TAG, "we got location modifiers"+locationModifiers);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "locationmodifierstostring "+ret);

			return ret;
		}
	}

