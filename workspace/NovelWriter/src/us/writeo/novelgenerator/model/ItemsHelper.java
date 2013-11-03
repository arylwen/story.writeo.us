	package us.writeo.novelgenerator.model;

	import android.util.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class ItemsHelper
	{
		private static final String TAG = "NW ItemsHelper";

		public static Items readItems(String itemsXml)
		{
			Items items = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(itemsXml); 
			try
			{
				items = serializer.read(Items.class, reader, false);
				Log.e(TAG, "we got items: "+items);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return items;
		}

		public static String itemsToString(Items items){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(items, writer);
				Log.e(TAG, "we got items: "+items);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "itemstostring "+ret);

			return ret;
		}
	}

