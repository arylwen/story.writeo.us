package us.writeo.novel.model;

import java.io.*;
import java.util.*;
import org.simpleframework.xml.*;

	@Element(name="counter")
	public class Counter implements Serializable
	{
		@Element(name="date")
		protected Date date;

		@Element(name="value")
		protected int value;

		public void setDate(Date aDate)
		{
			date = aDate;
		}

		public Date getDate()
		{
			return date;
		}

		public void setValue(int aValue)
		{
			value = aValue;
		}

		public int getValue()
		{
			return value;
		}	
	}
