package us.writeo.novelgenerator.model;

	import java.util.*;
	import org.simpleframework.xml.*;
	import us.writeo.common.novel.model.*;

	@Element(name="root")
	public class Items
	{
		@ElementList(name="items")
		protected List<Item> items = new ArrayList<Item>();

		public List<Item> getItems(){
			return items;
		}
	}
