package us.writeo.novelgenerator.model;

import java.util.*;
import org.simpleframework.xml.*;

	@Element(name="root")
	public class Sounds
	{
		@ElementList(name="sounds")
		protected List<String> sounds = new ArrayList<String>();

		public List<String> getSounds(){
			return sounds;
		}
	}
