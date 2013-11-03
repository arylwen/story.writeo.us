    package us.writeo.common.novel.model;

	import java.util.*;
	import org.simpleframework.xml.*;
	import us.writeo.common.novel.model.*;

	@Element(name="root")
	public class DailyCounters
	{
		@ElementList(name="dailyCounters")
		protected List<Counter> dailyCounters = new ArrayList<Counter>();

		public List<Counter> getWordCounters(){
			return dailyCounters;
		}
	}
