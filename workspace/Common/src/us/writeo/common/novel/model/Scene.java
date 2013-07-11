package us.writeo.common.novel.model;

import java.io.*;
import java.util.*;
import org.simpleframework.xml.*;

@Element(name="scene")
public class Scene implements Serializable
{
	@Element(name="name")
    protected String name = "Untitled";
	
	@Element(name="file")
	protected String path = "scenes/"+UUID.randomUUID().toString();

	public void setName(String aName)
	{
		name = aName;
	}
	
	public String getName()
	{
		return name;
	}

	public void setPath(String aPath)
	{
		path = aPath;
	}

	public String getPath()
	{
		return path;
	}	
}
