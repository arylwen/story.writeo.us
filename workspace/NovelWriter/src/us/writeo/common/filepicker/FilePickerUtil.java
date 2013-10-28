package us.writeo.common.filepicker;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import java.lang.reflect.*;

public class FilePickerUtil
{
     private static final String TAG = "WRS FilePickerUtil";
	 
     public static FilePicker getFilePicker(Activity activity){

		 FilePicker fpk = null;
		 try
		 {
			 ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), 
																		 PackageManager.GET_META_DATA);
			 if(ai != null){
				 Bundle metadata = ai.metaData;
				 String value = metadata.getString("filePicker");	

				 if(value != null){
					 Class clazz = Class.forName(value);

					 Constructor[] constructors = clazz.getDeclaredConstructors();
					 Constructor c = constructors[0];
					 Object[] params = new Object[1];
					 params[0] = activity;

					 fpk = (FilePicker)c.newInstance(params);
					 Log.e(TAG, "obtained a filepicker "+fpk.getClass().getName());
				 }
			 }

		 }
		 catch (PackageManager.NameNotFoundException e)
		 {
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		 } 
		 catch (ClassNotFoundException e)
		 {
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		 }
		 catch (InstantiationException e)
		 {
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		 }
		 catch (InvocationTargetException e)
		 {
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		 }
		 catch (IllegalAccessException e)
		 {
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		 }
		 catch (IllegalArgumentException e)
		 {
			 Log.e(TAG, "couldn't get a file picker "+e.toString());
		 }
		 finally{
			 //if (fpk == null ) fpk = new SWFilePicker(this);
		 }
		 
		 return fpk;
	 }

}
