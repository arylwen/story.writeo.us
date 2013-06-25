package com.halcyon.novelwriter.model;

import java.util.*;
import org.simpleframework.xml.*;

@Element(name="chapter")
public class Chapter
{
	@Element(name="name")
    protected String name = "Untitled";
	
	@ElementList(name="scenes")
	protected List<Scene> scenes = new ArrayList<Scene>();

	public void setName(String aName)
	{
		name = aName;
	}
	

	public String getName()
	{
		return name;
	}

	public void addScene(Scene aScene)
	{
		scenes.add(aScene);
	}
	
	public void removeScene(int pos)
	{
		scenes.remove(pos);
	}
	
	public List<Scene> getScenes()
	{
		return scenes;
	}
}
