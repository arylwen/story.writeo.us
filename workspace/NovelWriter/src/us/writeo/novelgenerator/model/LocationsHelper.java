package us.writeo.novelgenerator.model;

import android.util.*;
import java.io.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

	public class LocationsHelper
	{
		private static final String TAG = "NW LocationsHelper";

		public static Locations readLocations(String locationsXml)
		{
			Locations locations = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(locationsXml); 
			try
			{
				locations = serializer.read(Locations.class, reader, false);
				Log.e(TAG, "we got a novel"+locations);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return locations;
		}

		public static String locationsToString(Locations locations){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(locations, writer);
				Log.e(TAG, "we got a locatons"+locations);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "locationstostring "+ret);

			return ret;
		}
	}

