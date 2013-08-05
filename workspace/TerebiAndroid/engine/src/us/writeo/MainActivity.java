package us.writeo;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import us.terebi.engine.*;

public class MainActivity extends Activity
{
    private static final String TAG = "lib.ds";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// Watch for button clicks. 
		Button button;

		button = (Button)findViewById(R.id.start); 		
		button.setOnClickListener(mStartListener); 
		button = (Button)findViewById(R.id.stop); 
		button.setOnClickListener(mStopListener);



    }

	private OnClickListener mStartListener = new OnClickListener() { 
	    @Override
		public void onClick(View v)
		{ 
	    	Log.e(TAG,"start clicked");
			String[] args = new String[1];
			//args[0] = "/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/etc/ds.terebi.config";
			args[0] = "/sdcard/lib.ds/etc/ds.terebi.config";
			

			File dexDir = MainActivity.this.getDir("dex",0);
			Log.e(TAG,"dex dir is: "+dexDir);
			
			try
			{
				Log.e("lib.ds", "before main 123");
				Main.main(args);
				Log.e("lib.ds", "after main 123");
			}
			catch (Exception e)
			{

				Log.e("lib.ds", e.getMessage(), e);
			}
		} 

	}; 

	private OnClickListener mStopListener = new OnClickListener() { 
		@Override
		public void onClick(View v)
		{ 
			Log.e(TAG,"stop clicked");
            //Shutdown.token().notifyAll();
			//String base = "/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/work";
			//String dump = "/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/work/classes.dex";
			String base = "/sdcard/lib.ds/work";
			String dump = "/sdcard/lib.ds/work/classes.dex";

			//Map<String, byte[]> bytecode = dexIt(new File(base), "");
			DexClasses dexer = new DexClasses(base, dump);
			dexer.dexFiles();
			//byte[] dexedbytecode = dexer.classesToDex(bytecode);
			/*try
			{
				FileOutputStream  output = new FileOutputStream(dump);
				output.write(dexedbytecode);
				output.flush();
				output.close();
			}
			catch (FileNotFoundException e)
			{
				Log.e("lib.ds", e.getMessage(), e);
			}
			catch (IOException e)
			{
				Log.e("lib.ds", e.getMessage(), e);
			}
			
			try
			{
				jarIt(base);
			}
			catch (IOException e)
			{
				Log.e(TAG, e.getMessage(), e);
			}*/
	    } 
	}; 

	public Map<String, byte[]> dexIt(File node, String path)
	{ 	
		Map<String, byte[]> ret = new HashMap<String, byte[]>();
		Log.e(TAG, node.getAbsoluteFile().toString() + " path: " + path); 	
		if (node.isDirectory())
		{ 			
			String[] subNote = node.list(); 			
			for (String filename : subNote)
			{ 				
			    Map<String, byte[]> subMap = dexIt(new File(node, filename), path + "/" + filename); 		
				ret.putAll(subMap);
			} 		
		}
		else
		{
			//String filePath = path + "/" + node.getName();
			String filePath = path;
			//filePath = filePath.substring(1, filePath.length() - 6);
			filePath = filePath.substring(1);
			Log.e(TAG, "filepath: " + filePath);
			byte[] contents = readAssetFile(node.getAbsolutePath());
			ret.put(filePath, contents);
		}
		return ret;
	}

	public byte[] readAssetFile(String fileName)
	{
		InputStream inputStream = null; 

		try
		{ 
		    inputStream = new FileInputStream(fileName); 
		}
		catch (IOException e)
		{ 
			Log.e("cannot open " + fileName, e.getMessage()); 
		}

		return readTextFile(inputStream);
	}

	private byte[] readTextFile(InputStream inputStream)
	{ 
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		byte buf[] = new byte[1024]; int len; 
		try
		{ 
		    while ((len = inputStream.read(buf)) != -1)
			{ 
			    outputStream.write(buf, 0, len); 
			} 
			outputStream.close(); 
			inputStream.close(); 
		}
		catch (IOException e)
		{ } 

		return outputStream.toByteArray(); 
	}

	public void jarIt(String base) throws IOException
	{ 
	    Manifest manifest = new Manifest(); 
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0"); 
		JarOutputStream target = new JarOutputStream(new FileOutputStream(
		      "/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/jar/lib.jar"), manifest); 
		add(new File(base), base, target); 
		target.close(); 
	} 

	private void add(File source, String base, JarOutputStream target) throws IOException
	{ 
	    BufferedInputStream in = null; 
		try
		{ 
		    if (source.isDirectory())
			{ 
				String name = source.getPath().replace("\\", "/");
				if (!name.isEmpty())
				{ 
					if (!name.endsWith("/")) 
						name += "/"; 
					JarEntry entry = new JarEntry(name.substring(base.length())); 
					entry.setTime(source.lastModified()); 
					target.putNextEntry(entry); 
					target.closeEntry(); 
				} 

				for (File nestedFile: source.listFiles()) 
					add(nestedFile, base, target); return; 
		    } 

			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").substring(base.length())); 
			entry.setTime(source.lastModified()); 
			target.putNextEntry(entry); 
			in = new BufferedInputStream(new FileInputStream(source)); 
			byte[] buffer = new byte[1024]; 
			while (true)
			{ 
				int count = in.read(buffer); 
				if (count == -1) 
					break; 
				target.write(buffer, 0, count); 
			} 
			target.closeEntry(); 
		}
		finally
		{ 
			if (in != null) in.close(); 
		} 
	}
}
