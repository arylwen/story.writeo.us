package org.openintents.filemanager.util;

//GC noclassdeffound error
//import org.openintents.filemanager.R;

import android.app.*;
import android.preference.*;
import org.openintents.filemanager.*;


public abstract class UIUtils {

	//public static void setThemeFor(Activity act) {
	//	if (PreferenceManager.getDefaultSharedPreferences(act).getBoolean("usedarktheme", true)) {
	//		act.setTheme(R.style.Theme_Dark);
	//	} else {
	//		act.setTheme(R.style.Theme_Light_DarkTitle);
	//	}
	//}
	
	public static boolean shouldDialogInverseBackground(Activity act){
		return !PreferenceManager.getDefaultSharedPreferences(act).getBoolean("usedarktheme", true);
	}
}
