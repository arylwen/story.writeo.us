	package us.writeo.novelgenerator.model;

	import android.util.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class TraitsHelper
	{
		private static final String TAG = "NW TraitsHelper";

		public static Traits readTraits(String traitsXml)
		{
			Traits traits = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(traitsXml); 
			try
			{
				traits = serializer.read(Traits.class, reader, false);
				Log.e(TAG, "we got traits: "+traits);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return traits;
		}

		public static String traitsToString(Traits traits){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(traits, writer);
				Log.e(TAG, "we got traits: "+traits);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "traitstostring "+ret);

			return ret;
		}
	}

