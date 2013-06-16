package com.halcyon.storywriter;

/**
       processes the xml string of a strcture template
	   and transforms it in something easy to process
	   for the Coloriser
**/
import android.text.style.*;
import java.util.*;

public interface StructureTemplateHelper
{
    /**
	      Changes the template represented by this object.
		  The templates is parsed and the necessary data
		  structures are put in place
	      @param the new template in the xml format
	**/
	public void setTemplate(String aXmlTemplate);
	

	public Map<Integer, SNode> getStructure();
	
	public Map<Integer, ForegroundColorSpan> getSpans();
	
	public List<Integer> getLimits();
}
