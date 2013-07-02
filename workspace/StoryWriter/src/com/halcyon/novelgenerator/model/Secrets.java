package com.halcyon.novelgenerator.model;

	import com.halcyon.novelwriter.model.*;
	import java.util.*;
	import org.simpleframework.xml.*;

	@Element(name="root")
	public class Secrets
	{
		@ElementList(name="secrets")
		protected List<String> secrets = new ArrayList<String>();

		public List<String> getSecrets(){
			return secrets;
		}
	}
