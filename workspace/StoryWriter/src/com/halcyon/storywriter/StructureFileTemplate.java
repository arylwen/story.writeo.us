package com.halcyon.storywriter;

import java.io.*;

public class StructureFileTemplate implements Serializable
{
	private String name;
	private String file;

	public void setName(String aName)
	{
		name = aName;
	}

	public String getName(){
		return name;
	}

	public void setFile(String aFile){
		file = aFile;
	}

	public String getFile()
	{
		return file;
	}
}
