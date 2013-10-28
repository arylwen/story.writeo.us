package us.writeo.common.novel.model;
import java.util.*;
import org.simpleframework.xml.*;

@Root
public class Novel
{
	  @Element(name="name")
      protected String name;
	  
	  @ElementList(name="chapters")
	  protected List<Chapter> chapters = new ArrayList<Chapter>();
	  
	  @ElementList(name="counters", required=false)
	  protected List<Counter> counters = new ArrayList<Counter>();
	  
	  @ElementList(name="characters", required=false)
	  protected List<NCharacter> characters = new ArrayList<NCharacter>();
	  
	  public void setName(String aName)
	  {
		  name = aName;
	  }
	  

	  public String getName(){
		 return name;
	 }
	 
	 
	 public void addChapter(Chapter aChapter)
	 {
		 chapters.add(aChapter);
	 }
	 
	public void insertChapter(Chapter aChapter, int position)
	{
		chapters.add(position, aChapter);
	}
	 
	 public void removeChapter(int pos)
	 {
		 chapters.remove(pos);
	 }

	 public List<Chapter> getChapters()
	 {
		 return chapters;
	 }
	 
	 public String toString()
	 {
		 StringBuffer ret = new StringBuffer();
		 
		 ret.append("[name="+name);
		 for(Chapter chapter: chapters){
			 ret.append(";"+chapter.toString());
		 }
		 ret.append("]");
		
		 return ret.toString();
	 }
}
