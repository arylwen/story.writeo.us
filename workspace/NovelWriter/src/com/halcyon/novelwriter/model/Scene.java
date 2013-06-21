package com.halcyon.novelwriter.model;

import java.io.*;

public class Scene implements Serializable
{
    private String name;

	public void setName(String aName)
	{
		name = aName;
	}
	
	public String getName()
	{
		return name;
	}

}
