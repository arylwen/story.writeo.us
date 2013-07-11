package us.writeo.common.structuretemplate.model;

import org.simpleframework.xml.Element; 
import org.simpleframework.xml.ElementList; 

@Element(name="part")
public class Part
{
	@Element(name="name")
    public String name;

    @Element(name="color")
	public String color;

	@Element(name="limit")
    public int limit;

}
