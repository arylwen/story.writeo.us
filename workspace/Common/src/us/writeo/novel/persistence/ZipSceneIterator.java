package us.writeo.novel.persistence;

import android.util.*;
//import com.halcyon.novelwriter.*;
import java.io.*;
import java.util.zip.*;
import us.writeo.novel.model.*;

public class ZipSceneIterator implements SceneIterator
{
    private static final String TAG = "NW ZipSceneIterator";
	private String zipFileName;
	private SceneProcessor sceneProcessor;

    public ZipSceneIterator(String aZipFileName){
		zipFileName = aZipFileName;
	}

	public void setProcessor(SceneProcessor processor, Object args)
	{		
	    sceneProcessor = processor;
	}

	public void iterate(SceneProcessor processor, Object args)
	{
		sceneProcessor = processor;
		
		InputStream is = null;
		ZipInputStream zis = null;
		String ret = null;
		try{
			Log.e(TAG, "zipFileName" + zipFileName);
			is = new FileInputStream(zipFileName);	   
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				String filename = ze.getName();
				if(isScene(filename)){
				    ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    byte[] buffer = new byte[1024];
					int count;
					while ((count = zis.read(buffer)) != -1) {
						baos.write(buffer, 0, count);
					}

					byte[] bytes = baos.toByteArray();
					ret = new String(bytes);
					
					Scene s = new Scene();
					s.setPath(filename);
					sceneProcessor.process(s, ret);
				}	
			}			
		}catch(IOException e) {
			Log.e(TAG, e.getMessage());
		}  finally {
			try{
				if(zis != null) {			    
			        zis.close();
				}
			} catch(IOException e) {
				Log.e(TAG, e.getMessage());
		    } 		
			try{
				if(is != null) {
					is.close();
				}
			} catch(IOException e) {
				Log.e(TAG, e.getMessage());
		    } 			
		}
		
	}
	
	private boolean isScene(String path){
		if(path.contains("scenes") && !path.contains("."))
			return true;
		
		return false;
	}
}
