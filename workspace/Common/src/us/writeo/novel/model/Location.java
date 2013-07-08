
	package us.writeo.novel.model;

	import java.io.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="location")
	public class Location implements Serializable
	{
		@Element(name="name")
		protected String name = "Untitled";

		@Element(name="description", required=false)
		protected String path = "locations/"+UUID.randomUUID().toString();

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
