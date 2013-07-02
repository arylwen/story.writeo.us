package com.halcyon.novelgenerator.model;

	import com.halcyon.novelwriter.model.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="root")
	public class Scents
	{
		@ElementList(name="scents")
		protected List<String> scents = new ArrayList<String>();

		public List<String> getScents(){
			return scents;
		}
	}
