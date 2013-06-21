package com.halcyon.novelwriter.model;

import java.util.*;

public class Chapter
{
    private String name;
	private ArrayList<Scene> scenes = new ArrayList<Scene>();

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
	
	public ArrayList<Scene> getScenes()
	{
		return scenes;
	}
}
