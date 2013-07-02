

	package com.halcyon.novelgenerator.model;

	import com.halcyon.novelwriter.model.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="root")
	public class Verbs
	{
		@ElementList(name="verbs")
		protected List<String> verbs = new ArrayList<String>();

		public List<String> getVerbs(){
			return verbs;
		}
	}
