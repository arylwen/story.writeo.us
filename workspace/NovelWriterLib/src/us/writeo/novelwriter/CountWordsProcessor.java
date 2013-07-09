package us.writeo.novelwriter;

import us.writeo.novel.model.*;
import us.writeo.novel.persistence.*;

public class CountWordsProcessor implements SceneProcessor
{
    private long wordCount = 0;

	public void process(Scene currentScene, Object extra)
	{
		long localwc = NovelColoriser.wordCount((String)extra);
		wordCount = wordCount + localwc;
	}

    public long getWordCount(){
		return wordCount;
	}
}
