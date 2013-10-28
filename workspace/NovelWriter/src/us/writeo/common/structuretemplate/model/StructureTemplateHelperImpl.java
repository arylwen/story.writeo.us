package us.writeo.common.structuretemplate.model;

import android.graphics.*;
import android.text.style.*;
import android.util.*;
import java.io.*;
import java.util.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

public class StructureTemplateHelperImpl implements StructureTemplateHelper
{
    private final static String TAG = "SW StructureTemplateHelperImpl";

	private Map<Integer, SNode> template ;
	private List<Integer> limits;
	private Map<Integer, ForegroundColorSpan> spans;
	
	public StructureTemplateHelperImpl()
	{
		
	}
	
	public StructureTemplateHelperImpl(String aXmlTemplate)
	{
        resetTemplate(aXmlTemplate);
	}
	
	private void resetTemplate(String aXmlTemplate)
	{
		Log.e("SW", aXmlTemplate);
		
		template = new HashMap<Integer, SNode>();
		spans = new HashMap<Integer, ForegroundColorSpan>();
		
		Serializer serializer = new Persister(); 
		Reader reader = new StringReader(aXmlTemplate); 
		try
		{
		    StructureTemplate tmpl = serializer.read(StructureTemplate.class, reader, false);
			int id = 0;
			for(Part p:tmpl.parts)
			{
				SNode node;

				node = new SNode();
				node.setName(p.name);
				node.setColor(Color.parseColor( p.color));	
				node.setId(id); id++;
				template.put(new Integer(p.limit), node);
				spans.put(new Integer(node.getId()), new ForegroundColorSpan(node.getColor()));
			}
			limits = asSortedList(template.keySet());
			Log.e("SW", limits.toString());
		} catch (Exception e) 
		{ 
		    //todo
			Log.e(TAG, e.getMessage());
		} catch (Throwable t) {
			Log.e(TAG, t.getMessage());
		}
	}
	
	public void setTemplate(String aXmlTemplate)
	{
		resetTemplate(aXmlTemplate);
	}
	
	public Map<Integer, SNode> getStructure()
	{
		return template;
	}
	
	public Map<Integer, ForegroundColorSpan> getSpans()
	{
		return spans;
	}
	
	public List<Integer> getLimits()
	{
		  return limits;
	}
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) 
	{ 
		List<T> list = new ArrayList<T>(c); 
		java.util.Collections.sort(list); 

		return list; 
	}
}
