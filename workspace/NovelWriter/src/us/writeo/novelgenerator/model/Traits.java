package us.writeo.novelgenerator.model;

import java.util.*;
import org.simpleframework.xml.*;

@Element(name="root")
public class Traits
{
	@ElementList(name="traits")
	protected List<String> traits = new ArrayList<String>();

	public List<String> getTraits(){
		return traits;
	}
}
