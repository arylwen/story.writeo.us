package us.writeo;

import android.util.*;
import com.android.dx.cf.iface.*;
import com.android.dx.dex.*;
import com.android.dx.dex.cf.*;
import com.android.dx.dex.code.*;
import com.android.dx.dex.file.*;
import java.io.*;
import java.util.*;
import org.apache.commons.io.comparator.*;

	public class DexClasses {
	private static final String TAG = "lib.ds";
		
		/** number of warnings during processing */
		private static int warnings = 0;

		/** number of errors during processing */
		private static int errors = 0;

		/** {@code non-null;} output file in-progress */
		private static DexFile outputDex;

		private final CfOptions cfOptions;
		
		private String base;
		private String outDex;

		public DexClasses(String base, String outDex) {
			DexOptions dopt = new DexOptions();
			dopt.targetApiLevel = 17;
			
			outputDex = new DexFile(dopt);
			cfOptions = new CfOptions();

			cfOptions.positionInfo = PositionList.LINES;
			cfOptions.localInfo = true;
			cfOptions.strictNameCheck = true;
			cfOptions.optimize = true;
			cfOptions.optimizeListFile = null;
			cfOptions.dontOptimizeListFile = null;
			cfOptions.statistics = false;
			
			this.base = base;
			this.outDex = outDex;
		}

		public byte[] classesToDex(Map<String, byte[]> bytecode) {
			byte[] dummy = new byte[0];
			for (String className:bytecode.keySet()) {
				String name = className;
				byte[] byteArray = bytecode.get(name);
				Log.e(TAG, "processing class:"+name);
				processClass(name, byteArray);
				bytecode.put(name, dummy);
				byteArray = null;
			}

			byte[] outputArray = writeDex();

			return outputArray;
		}

		/**
		 * Processes one classfile.
		 *
		 * @param name {@code non-null;} name of the file, clipped such that it
		 * <i>should</i> correspond to the name of the class it contains
		 * @param bytes {@code non-null;} contents of the file
		 * @return whether processing was successful
		 */
		private boolean processClass(String name, byte[] bytes) {
			try {
				ClassDefItem clazz =
					CfTranslator.translate(name, bytes, cfOptions, outputDex.getDexOptions());
				Log.e(TAG, "adding clazz:"+clazz);
				outputDex.add(clazz);
				clazz = null;
				return true;
			} catch (ParseException ex) {
				//StringBuffer context = new StringBuffer();
				
				Log.e(TAG, ex.getMessage()+" context: "+ex.getContext(), ex);
			}

			warnings++;
			return false;
		}

		/**
		 * Converts {@link #outputDex} into a {@code byte[]}, write
		 * it out to the proper file (if any), and also do whatever human-oriented
		 * dumping is required.
		 *
		 * @return {@code null-ok;} the converted {@code byte[]} or {@code null}
		 * if there was a problem
		 */
		private byte[] writeDex() {
			byte[] outArray = null;

			OutputStreamWriter out = new OutputStreamWriter(new ByteArrayOutputStream());
			try {
				outArray = outputDex.toDex(out, false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return outArray;
		}

		/**
		 * Opens and returns the named file for writing, treating "-" specially.
		 *
		 * @param name {@code non-null;} the file name
		 * @return {@code non-null;} the opened file
		 */
		private OutputStream openOutput(String name) throws IOException {
			if (name.equals("-") ||
                name.startsWith("-.")) {
				return System.out;
			}

			return new FileOutputStream(name);
		}

		/**
		 * Flushes and closes the given output stream, except if it happens to be
		 * {@link System#out} in which case this method does the flush but not
		 * the close. This method will also silently do nothing if given a
		 * {@code null} argument.
		 *
		 * @param stream {@code null-ok;} what to close
		 */
		private void closeOutput(OutputStream stream) throws IOException {
			if (stream == null) {
				return;
			}

			stream.flush();

			if (stream != System.out) {
				stream.close();
			}
		}
		
		
	public void dexFiles(){
		dexIt(new File(base), "");
		byte[] dexedbytecode = writeDex();
		
				try
			{
				FileOutputStream  output = new FileOutputStream(outDex);
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
	}
		
	public void dexIt(File node, String path)
	{ 	
		//Map<String, byte[]> ret = new HashMap<String, byte[]>();
		Log.e(TAG, node.getAbsoluteFile().toString() + " path: " + path); 	
		if (node.isDirectory())
		{ 			
			File[] subNote = node.listFiles(); 		
			Arrays.sort(subNote, SizeFileComparator.SIZE_REVERSE);
			for (File file : subNote)
			{ 				
			    dexIt(file, path + "/" + file.getName()); 		
				//ret.putAll(subMap);
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
			//ret.put(filePath, contents);
			processClass(filePath, contents);
			contents = null;
		}
		//return ret;
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
}
