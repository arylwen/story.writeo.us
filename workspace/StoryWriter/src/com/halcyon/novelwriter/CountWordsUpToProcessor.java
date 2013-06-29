package com.halcyon.novelwriter;
import android.util.*;
import com.halcyon.novelwriter.model.*;

public class CountWordsUpToProcessor implements SceneProcessor
{
    private static final String TAG = "NW CountWordsUpToProcessor";
	private long wordCount = 0;
	private Novel novel;
	private Scene targetScene;

	public CountWordsUpToProcessor(Novel aNovel, Scene aTargetScene){
		novel = aNovel;
		targetScene = aTargetScene;
	}
		
	public void process(Scene currentScene, Object extra)
	{
		boolean isBefore = isBefore(currentScene);
		Log.e(TAG, "isBefore "+isBefore);
		if(isBefore){
		    long localwc = NovelColoriser.wordCount((String)extra);
		    wordCount = wordCount + localwc;
		}
	}

	public long getWordCount(){
		return wordCount;
	}
	
	private boolean isBefore(Scene currentScene){
		boolean before = true;
		//boolean found = false;
		
		chapters:
		for(Chapter chapter:novel.getChapters()){
			for(Scene scene:chapter.getScenes()){
				if(scene.getPath().equals(targetScene.getPath())){
					before = false;
					break chapters;
				} else {
					if(scene.getPath().equals(currentScene.getPath())){
						//found = true;
						break chapters;
					}
				}
			}
		}
		
		return before;
	}
	
}
