package com.halcyon.novelgenerator.model;

	import android.util.*;
	import com.halcyon.storywriter.template.*;
	import java.io.*;
	import org.simpleframework.xml.*;
	import org.simpleframework.xml.core.*;

	public class SecretsHelper
	{
		private static final String TAG = "NW SecretsHelper";

		public static Secrets readSecrets(String secretsXml)
		{
			Secrets secrets = null;

			Serializer serializer = new Persister(); 
			Reader reader = new StringReader(secretsXml); 
			try
			{
				secrets = serializer.read(Secrets.class, reader, false);
				Log.e(TAG, "we got scents"+secrets);
			} catch (Exception e) 
			{ 
				//todo
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			return secrets;
		}

		public static String secretToString(Secrets secrets){

			Serializer serializer = new Persister(); 
			StringWriter writer = new StringWriter(); 
			try
			{
				serializer.write(secrets, writer);
				Log.e(TAG, "we got scents"+secrets);
			} catch (Exception e) 
			{ 
				Log.e(TAG, e.getMessage());
			} catch (Throwable t) {
				Log.e(TAG, t.getMessage());
			}

			StringBuffer sb = writer.getBuffer();
			String ret = sb.toString();

			Log.e(TAG, "secretstostring "+ret);

			return ret;
		}
	}

