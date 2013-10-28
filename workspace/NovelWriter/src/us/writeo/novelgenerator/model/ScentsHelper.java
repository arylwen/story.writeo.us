package us.writeo.novelgenerator.model;

import android.util.*;
import java.io.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

	public class ScentsHelper
	{
		private static final String TAG = "NW ScentsHelper";

		public static Scents readScents(String scentsXml)
		{
			Scents scents = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(scentsXml); 
			try
			{
				scents = serializer.read(Scents.class, reader, false);
				Log.e(TAG, "we got scents"+scents);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return scents;
		}

		public static String scentsToString(Scents scents){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(scents, writer);
				Log.e(TAG, "we got scents"+scents);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "scentstostring "+ret);

			return ret;
		}
	}

