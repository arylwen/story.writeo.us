package us.writeo.novelgenerator.model;

import java.util.*;
import org.simpleframework.xml.*;
import us.writeo.common.novel.model.*;

	@Element(name="root")
	public class Locations
	{
		@ElementList(name="locations")
		protected List<Location> locations = new ArrayList<Location>();

		public List<Location> getLocations(){
			return locations;
		}
	}
