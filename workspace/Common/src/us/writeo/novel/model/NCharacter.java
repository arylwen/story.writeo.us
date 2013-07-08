package us.writeo.novel.model;

	import java.io.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="character")
	public class NCharacter implements Serializable
	{
		@Element(name="name")
		protected String name = "Untitled";

		@Element(name="description", required=false)
		protected String path = "characters/"+UUID.randomUUID().toString();
		
		protected int counter = 0;

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
		
	public void incrementCounter(){
		counter++;
	}

	public int getCounter(){
		return counter;
	}
	}
