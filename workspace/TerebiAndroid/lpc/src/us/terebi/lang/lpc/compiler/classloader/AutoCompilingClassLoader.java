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

package us.terebi.lang.lpc.compiler.classloader;

import dalvik.system.*;
import java.net.*;
import org.apache.log4j.*;
import us.terebi.lang.lpc.compiler.*;

/**
 * 
 */
public class AutoCompilingClassLoader extends DexClassLoader
{
    private final Logger LOG = Logger.getLogger(AutoCompilingClassLoader.class);

    private final ObjectCompiler _compiler;

    public AutoCompilingClassLoader(URL[] urls, ClassLoader parent, ObjectCompiler compiler)
    {
        //super(urls[0].toString().substring(5), parent);
		//super("/storage/extSdCard/aprojects/story.writeo.us/workspace/lib.ds/jar/lib.jar", parent);
		super( "/sdcard/lib.ds/work/classes.dex", 
			  "/data/data/us.writeo/app_dex", null,parent);
		for(int i = 0; i< urls.length; i++){
		    LOG.debug("Class loader urls["+i+"] is: "+urls[i].toString().substring(5));
		}
        _compiler = compiler;
    }

    public Class< ? > findClass(String name) throws ClassNotFoundException
    {
		LOG.debug("class name:"+name);
        compile(name);
        return super.findClass(name);
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
            _compiler.precompile(lpc);
        }
        catch (CompileException e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Cannot compile " + lpc + "for " + name + " - " + e);
            }
        }
    }
	
}
