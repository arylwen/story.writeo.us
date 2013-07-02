package com.halcyon.novelgenerator.model;

	import com.halcyon.novelwriter.model.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="root")
	public class LocationModifiers
	{
		@ElementList(name="location_modifiers")
		protected List<String> locationModifiers = new ArrayList<String>();

		public List<String> getLocationModifers(){
			return locationModifiers;
		}
	}
