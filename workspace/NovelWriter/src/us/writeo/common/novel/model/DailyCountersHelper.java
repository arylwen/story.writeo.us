package us.writeo.common.novel.model;

import android.util.*;
import java.io.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

	public class DailyCountersHelper
	{
		private static final String TAG = "NW DailyCountersHelper";

		public static DailyCounters readDailyCounters(String locationsXml)
		{
			DailyCounters dailyCounters = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(locationsXml); 
			try
			{
				dailyCounters = serializer.read(DailyCounters.class, reader, false);
				Log.e(TAG, "we got counters"+dailyCounters);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return dailyCounters;
		}

		public static String dailyCountersToString(DailyCounters dailyCounters){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(dailyCounters, writer);
				Log.e(TAG, "we got a dailyCounters"+dailyCounters);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "dailyCoubterstostring "+ret);

			return ret;
		}
	}

