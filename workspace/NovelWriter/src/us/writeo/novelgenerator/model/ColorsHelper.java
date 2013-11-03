	package us.writeo.novelgenerator.model;

	import android.util.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class ColorsHelper
	{
		private static final String TAG = "NW ColorsHelper";

		public static Colors readColors(String colorsXml)
		{
			Colors colors = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(colorsXml); 
			try
			{
				colors = serializer.read(Colors.class, reader, false);
				Log.e(TAG, "we got colors: "+colors);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return colors;
		}

		public static String colorsToString(Colors colors){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(colors, writer);
				Log.e(TAG, "we got colors: " + colors);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "colorstostring "+ret);

			return ret;
		}
	}

