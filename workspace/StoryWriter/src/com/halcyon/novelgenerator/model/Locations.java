package com.halcyon.novelgenerator.model;

	import com.halcyon.novelwriter.model.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="root")
	public class Locations
	{
		@ElementList(name="locations")
		protected List<Location> locations = new ArrayList<Location>();

		public List<Location> getLocations(){
			return locations;
		}
	}
