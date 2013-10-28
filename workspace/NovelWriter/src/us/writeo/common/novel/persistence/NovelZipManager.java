package us.writeo.common.novel.persistence;

import android.util.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import us.writeo.common.novel.model.*;

public class NovelZipManager implements NovelPersistenceManager
{
   private static final String TAG="NW NovelZipManager";
   private String zipFileName;
   private Novel novel;
   private File cacheDir;

   public NovelZipManager(String aZipFileName, Novel aNovel, File aCacheDir)
   {
	   zipFileName = aZipFileName;
	   novel = aNovel;
	   cacheDir = aCacheDir;
	   
	   //if(novel == null){
	   //	   String novelXml = getScene("novel.xml");
	   //   novel = NovelHelper.readNovel(novelXml);
	   //}
   }
    
  /**
   * Lazy instantiation, to optimize when only scene 
   * operations are required
   **/
  public Novel getNovel()
  {
	  if(novel == null){
		  String novelXml = getScene("novel.xml");
		  novel = NovelHelper.readNovel(novelXml);
	  }
	  return novel;
  }
   
   public void updateNovel(){
	   if(novel != null){
	       String filename = "novel.xml";
	       String novelXml = NovelHelper.novelToString(novel);
	       updateEntry(filename, novelXml);
	   } else {
		   Log.e(TAG, "You must call getNovel at least once before attempting to save it.");
	   }
   }
   
   public void createNovel(){
	   OutputStream os = null;
	   ZipOutputStream zos = null;
	   try{

		   os = new FileOutputStream(zipFileName);
		   zos = new ZipOutputStream(new BufferedOutputStream(os));

		   String filename = "novel.xml";
		   String novelXml = NovelHelper.novelToString(novel);
		   byte[] bytes = novelXml.getBytes();
		   ZipEntry entry = new ZipEntry(filename);
		   zos.putNextEntry(entry);
		   zos.write(bytes);
		   zos.closeEntry();

	   } catch(IOException e) {
		   Log.e(TAG, e.getMessage());
	   } finally {
		   try{
		       if(zos != null) {			   
				   zos.close();
			   }
			   if(os != null ){
				   os.close();
			   }
			} catch (IOException e) { 
				   Log.e(TAG, e.getMessage());
			}
	   }
   }
   
	public void updateScene(String path, String text, String prompt){
		List<String> texts = new ArrayList<String>();
		texts.add(text);
		texts.add(prompt);
		updateEntry(path, texts);
	}
   
   /*public void updateSceneText(String path, String text){
	   OutputStream os = null;
	   ZipOutputStream zos = null;
	   try{
		   os = new FileOutputStream(zipFileName);
		   zos = new ZipOutputStream(new BufferedOutputStream(os));

		   String filename = path;
		   byte[] bytes = text.getBytes();
		   ZipEntry entry = new ZipEntry(filename);
		   zos.putNextEntry(entry);
		   zos.write(bytes);
		   zos.closeEntry();

	   }catch(IOException e) {
		   Log.e(TAG, e.getMessage());
	   } finally {
		   try{
		        if(zos != null) {			   
				   zos.close();
				}
			   if(os != null) {			   
				   os.close();
			   }
			} catch (IOException e) { 
				   Log.e(TAG, e.getMessage());
			}		   
	   }
   }*/
   
   public String getScene(String entryName)
   {
	   InputStream is = null;
	   ZipInputStream zis = null;
	   String ret = null;
	   try{
	      is = new FileInputStream(zipFileName);	   
	      zis = new ZipInputStream(new BufferedInputStream(is));
		  ZipEntry ze;
		  while ((ze = zis.getNextEntry()) != null) {
			  String filename = ze.getName();
			  if(filename.equals(entryName)){
			      ByteArrayOutputStream baos = new ByteArrayOutputStream();
			      byte[] buffer = new byte[1024];
			      int count;
			      while ((count = zis.read(buffer)) != -1) {
				      baos.write(buffer, 0, count);
			      }

			      byte[] bytes = baos.toByteArray();
			      ret = new String(bytes);
				  break;
			  }
		   }
	   }catch(IOException e) {
	       Log.e(TAG, e.getMessage());
       }  finally {
		  
			if(zis != null) {	
               try{		    
			        zis.close();
			   } catch(IOException e) {
			       Log.e(TAG, e.getMessage());
			   } 		
			}
		    if(is != null) {
			    try{
				   is.close();				   
			   } catch(IOException e) {
			       Log.e(TAG, e.getMessage());
			   } 	
		   }		
		}
		
		return ret;
    }
	
	
	public void updateEntry(String entryName, String text)  { 
	
	    ZipInputStream zin = null;
		ZipOutputStream out = null;
		File tempFile = null;
	    
	    try{
		  // get a tempfile
	      tempFile = File.createTempFile(UUID.randomUUID().toString(), null, cacheDir); 
		  // delete it, otherwise you cannot rename your existing zip to it. 
		  tempFile.delete(); 
		  
		  File zipFile = new File(zipFileName);
		  /*boolean renameOk=zipFile.renameTo(tempFile); 
		  
		  if (!renameOk) { 
		      throw new RuntimeException("could not rename the file "+
		        zipFile.getAbsolutePath()+" to "+tempFile.getAbsolutePath()); 
		  } */
		  
		  copy(zipFile, tempFile);
				
		  Log.e(TAG, "after rename"+tempFile.getName());
				
			
	      byte[] buf = new byte[1024]; 
		  zin = new ZipInputStream(new FileInputStream(tempFile)); 
		  out = new ZipOutputStream(new FileOutputStream(zipFile)); 
		  ZipEntry entry = zin.getNextEntry(); 
		  
		  while (entry != null) { 
		     String name = entry.getName(); 
			 
			  if (!name.equals(entryName)) {
				  // Add ZIP entry to output stream. 
				  out.putNextEntry(new ZipEntry(name)); 
				  
				  // Transfer bytes from the ZIP file to the output file 
				  int len; 
				  while ((len = zin.read(buf)) > 0) { 
				    out.write(buf, 0, len); 
				  } 
			  } 
			  
			  entry = zin.getNextEntry(); 
		   } 		   
		   // Add ZIP entry to output stream. 
		   out.putNextEntry(new ZipEntry(entryName)); 		   
		   // Transfer bytes 
		   out.write(text.getBytes()); 
		   // Complete the entry 		 		 
		   out.closeEntry(); 
		 } catch (IOException e){
			 Log.e(TAG, e.getMessage());
		 } finally {
			 if(zin != null){
				 try{
		            zin.close();
				 } catch (IOException e){
					 Log.e(TAG, e.getMessage());
				 }
			 }
			 if(out != null){
                 try{		 
		           out.close(); 
				 } catch (IOException e){
					 Log.e(TAG, e.getMessage());
				 }  
			 }	  
			 if(tempFile != null){
		         tempFile.delete(); 
			 }
		 }
	}
	
	public void updateEntry(String entryName, List<String> texts)  { 

	    ZipInputStream zin = null;
		ZipOutputStream out = null;
		File tempFile = null;

	    try{
			// get a tempfile
			tempFile = File.createTempFile(UUID.randomUUID().toString(), null, cacheDir); 
			// delete it, otherwise you cannot rename your existing zip to it. 
			tempFile.delete(); 

			File zipFile = new File(zipFileName);
			copy(zipFile, tempFile);

			byte[] buf = new byte[1024]; 
			zin = new ZipInputStream(new FileInputStream(tempFile)); 
			out = new ZipOutputStream(new FileOutputStream(zipFile)); 
			ZipEntry entry = zin.getNextEntry(); 

			while (entry != null) { 
				String name = entry.getName(); 

				if (!name.contains(entryName)) {
					// Add ZIP entry to output stream. 
					out.putNextEntry(new ZipEntry(name)); 

					// Transfer bytes from the ZIP file to the output file 
					int len; 
					while ((len = zin.read(buf)) > 0) { 
						out.write(buf, 0, len); 
					} 
				} 

				entry = zin.getNextEntry(); 
			} 	
			int i = 0;
			String fname = entryName;
			for(String text:texts){
			   if(i > 0) {
				   fname  = entryName + "."+i;
			   }
			   // Add ZIP entry to output stream. 
			   out.putNextEntry(new ZipEntry(fname)); 		   
			   // Transfer bytes 
			   out.write(text.getBytes()); 
			   // Complete the entry 		 		 
			   out.closeEntry(); 
			   i++;
			}
		} catch (IOException e){
			Log.e(TAG, e.getMessage());
		} finally {
			if(zin != null){
				try{
		            zin.close();
				} catch (IOException e){
					Log.e(TAG, e.getMessage());
				}
			}
			if(out != null){
				try{		 
					out.close(); 
				} catch (IOException e){
					Log.e(TAG, e.getMessage());
				}  
			}	  
			if(tempFile != null){
				tempFile.delete(); 
			}
		}
	}
	
	private void copy(File src, File dst) throws IOException { 
	    InputStream in = new FileInputStream(src); 
		OutputStream out = new FileOutputStream(dst); 
		// Transfer bytes from in to out 
		byte[] buf = new byte[1024]; 
		int len; 
		while ((len = in.read(buf)) > 0) { out.write(buf, 0, len); } 
		in.close(); 
		out.close(); 
	}
	
	public void deleteEntry(String entryName)  { 

	    ZipInputStream zin = null;
		ZipOutputStream out = null;
		File tempFile = null;

	    try{
			// get a tempfile
			tempFile = File.createTempFile(UUID.randomUUID().toString(), null, cacheDir); 
			// delete it, otherwise you cannot rename your existing zip to it. 
			tempFile.delete(); 

			File zipFile = new File(zipFileName);
			copy(zipFile, tempFile);

			byte[] buf = new byte[1024]; 
			zin = new ZipInputStream(new FileInputStream(tempFile)); 
			out = new ZipOutputStream(new FileOutputStream(zipFile)); 
			ZipEntry entry = zin.getNextEntry(); 

			while (entry != null) { 
				String name = entry.getName(); 

				if (!name.contains(entryName)) {
					// Add ZIP entry to output stream. 
					out.putNextEntry(new ZipEntry(name)); 

					// Transfer bytes from the ZIP file to the output file 
					int len; 
					while ((len = zin.read(buf)) > 0) { 
						out.write(buf, 0, len); 
					} 
				} 

				entry = zin.getNextEntry(); 
			} 	
		} catch (IOException e){
			Log.e(TAG, e.getMessage());
		} finally {
			if(zin != null){
				try{
		            zin.close();
				} catch (IOException e){
					Log.e(TAG, e.getMessage());
				}
			}
			if(out != null){
				try{		 
					out.close(); 
				} catch (IOException e){
					Log.e(TAG, e.getMessage());
				}  
			}	  
			if(tempFile != null){
				tempFile.delete(); 
			}
		}
	}
	
	public void deleteEntries(List<String> entryNames)  { 

	    ZipInputStream zin = null;
		ZipOutputStream out = null;
		File tempFile = null;

	    try{
			// get a tempfile
			tempFile = File.createTempFile(UUID.randomUUID().toString(), null, cacheDir); 
			// delete it, otherwise you cannot rename your existing zip to it. 
			tempFile.delete(); 

			File zipFile = new File(zipFileName);
			copy(zipFile, tempFile);

			byte[] buf = new byte[1024]; 
			zin = new ZipInputStream(new FileInputStream(tempFile)); 
			out = new ZipOutputStream(new FileOutputStream(zipFile)); 
			ZipEntry entry = zin.getNextEntry(); 

			while (entry != null) { 
				String name = entry.getName(); 

				if (!isToBeRemoved(name, entryNames)) {
					// Add ZIP entry to output stream. 
					out.putNextEntry(new ZipEntry(name)); 

					// Transfer bytes from the ZIP file to the output file 
					int len; 
					while ((len = zin.read(buf)) > 0) { 
						out.write(buf, 0, len); 
					} 
				} 

				entry = zin.getNextEntry(); 
			} 	
		} catch (IOException e){
			Log.e(TAG, e.getMessage());
		} finally {
			if(zin != null){
				try{
		            zin.close();
				} catch (IOException e){
					Log.e(TAG, e.getMessage());
				}
			}
			if(out != null){
				try{		 
					out.close(); 
				} catch (IOException e){
					Log.e(TAG, e.getMessage());
				}  
			}	  
			if(tempFile != null){
				tempFile.delete(); 
			}
		}
	}
	
	private boolean isToBeRemoved(String entry, List<String> removedEntries){
		boolean ret = false;
		
		for(String removedEntry:removedEntries){
			if(entry.contains(removedEntry)){
				ret = true;
				break;
			}
		}
		
		return ret;
	}
}
