package us.writeo.novel.persistence;

import java.util.*;
import us.writeo.novel.model.*;

public interface NovelPersistenceManager
{

    public void createNovel();

    /**
	     updates the novel metadata
	**/
    public void updateNovel();
	
	public Novel getNovel();
	
	public void updateScene(String path, String text, String prompt);
	
	public String getScene(String path);
	
	public void deleteEntry(String entryName);
	
	public void deleteEntries(List<String> entryNames);
	
}
