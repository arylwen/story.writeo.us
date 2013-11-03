package us.writeo.novelgenerator.model;

import java.util.*;
import org.simpleframework.xml.*;

@Element(name="root")
public class Colors
{
	@ElementList(name="colors")
	protected List<String> colors = new ArrayList<String>();

	public List<String> getColors(){
		return colors;
	}
}
