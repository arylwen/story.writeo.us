/* ------------------------------------------------------------------------
 * Copyright 2010 Tim Vernum
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package us.writeo.play.engine.classloader;

import dalvik.system.*;
import java.net.*;
import org.apache.log4j.*;
import us.terebi.lang.lpc.compiler.*;
import us.writeo.*;

/**
 * 
 */
public class AutoCompilingClassLoader extends DexClassLoader
{
    private final Logger LOG = Logger.getLogger(AutoCompilingClassLoader.class);

    private final ObjectCompiler _compiler;
	private static String _base;
	private static String _bootdex;

    public AutoCompilingClassLoader(URL[] urls, ClassLoader parent, ObjectCompiler compiler)
    {
        //super(urls[0].toString().substring(5), parent);
		//super("/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/jar/lib.jar", parent);
		//super( "/sdcard/lib.ds/work/classes.dex", 
		//	  "/data/data/us.writeo/app_dex", null,parent);
		//super( "/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/work/classes.dex", 
		//	  "/data/data/us.writeo/app_dex", null,parent);	  
		super( getDexFileList(urls), 
			  MainActivity.engineContext.getDir("dex", 0).getAbsolutePath(), null,parent);
			  
		for(int i = 0; i< urls.length; i++){
		    LOG.debug("Class loader urls["+i+"] is: "+urls[i]);
		}
        _compiler = compiler;
    }

    public Class< ? > findClass(String name) throws ClassNotFoundException
    {
		LOG.debug("class name:"+name);
        compile(name);
		Class<?> clazz = null;
		try{
           clazz = super.findClass(name);
		} catch (ClassNotFoundException e) {
			LOG.debug("Dexing: " + name);
			dexClasses();
			LOG.debug("Done dexing: " + name);
			clazz = super.findClass(name);
		}
		return clazz;
    }

    private void compile(String name)
    {
        String lpc = ClassNameMapper.getLpcName(name);
        if (lpc == null)
        {
            return;
        }
        try
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Attempt to compile " + lpc);
            }
            if( _compiler.precompile(lpc)){
				LOG.debug("Dexing: " + name);
                dexClasses();
				LOG.debug("Done dexing: " + name);
		    }
        }
        catch (CompileException e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Cannot compile " + lpc + "for " + name + " - " + e);
            }
        }
    }
	
	private static String getDexFileList(URL[] urls){
		_base = urls[0].getPath();
		_bootdex = _base + "classes.dex";
		//return urls[0].toString() + "classes.dex";
		return _bootdex;
	}
	
	private void dexClasses(){
		
		DexClasses dexer = new DexClasses(_base, _bootdex);
		dexer.dexFiles();
	}
}
