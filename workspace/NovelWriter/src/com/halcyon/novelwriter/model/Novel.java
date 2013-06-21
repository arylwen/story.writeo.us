package com.halcyon.novelwriter.model;
import java.util.*;

public class Novel
{
      private String name;
	  private ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	  
	  public void setName(String aName)
	  {
		  name = aName;
	  }
	 
	 public void addChapter(Chapter aChapter)
	 {
		 chapters.add(aChapter);
	 }
	 
	 public ArrayList<Chapter> getChapters()
	 {
		 return chapters;
	 }
}
