package us.writeo.structuretemplate.model;

import java.util.List;
import org.simpleframework.xml.Element; 
import org.simpleframework.xml.ElementList; 
import org.simpleframework.xml.Root;

@Root
public class StructureTemplate
{
    @Element(name="name")
    public String name;
  
    @Element(name="mode")
	public String mode;
	
	@ElementList (name="parts")
	public List<Part> parts; 
}

