package com.halcyon.novelgenerator;

import android.content.*;
import android.util.*;
import com.halcyon.novelgenerator.model.*;
import com.halcyon.novelwriter.model.*;
import com.halcyon.storywriter.*;
import java.io.*;
import java.util.*;

public class ZipNovelGenerator implements NovelGenerator
{
    private static final String TAG = "NG ZipNovelGenerator";
	private Context context;
	private NovelGeneratorPersistenceManager npm;
	
	
	public ZipNovelGenerator(Context aContext){
		context = aContext;		
	}

	public String generate()
	{
		long now = Calendar.getInstance().getTime().getTime();
		String novelName = "/sdcard/"+"Novel-"+now+".zip";
		
        Novel novel = readNovel();		
		if(novel == null) return null;
		
		npm = new ZipNovelGeneratorManager(novelName, novel, context.getCacheDir());
		npm.createNovel();
		
		Characters characters = readCharacters();		
		if (characters == null) return null;
		
		Verbs verbs = readVerbs();
		if(verbs == null) return null;
				
		LocationModifiers locationModifiers = readLocationModifiers();
		if(locationModifiers == null) return null;
	
		Locations locations = readLocations();		
		if (locations == null) return null;
	
		Scents scents = readScents();
		if(scents == null) return null;
	
		Sounds sounds = readSounds();
		if(sounds == null) return null;	
	
		Secrets secrets = readSecrets();
		if(secrets == null) return null;	    
	
		ArrayList<SceneInfo> sceneInfoList = new ArrayList<SceneInfo>();
		
		for(int i=0; i<700; i++){
			SceneInfo sceneInfo = new SceneInfo();
			Random r = new Random();
			int index1 = r.nextInt(21);
			int index2 = r.nextInt(21);
			
			//we want different characters in a scene
			while(index1 == index2){
				index1 = r.nextInt(21);
				index2 = r.nextInt(21);				
			}
			
			sceneInfo.setCharacter1(characters.getCharacters().get(index1));
			sceneInfo.setCharacter2(characters.getCharacters().get(index2));
			
			//generate verb
			int index3 = r.nextInt(21);
			sceneInfo.setVerb(verbs.getVerbs().get(index3));
			
			//generate location modifier
			int index4 = r.nextInt(21);
			sceneInfo.setLocationModifier(locationModifiers.getLocationModifers().get(index4));
			
			//generate location 
			int index5 = r.nextInt(21);
			sceneInfo.setLocation(locations.getLocations().get(index5));
			
			//generate scent
			int index6 = r.nextInt(21);
			sceneInfo.setScent(scents.getScents().get(index6));
			
			//generate sound
			int index7 = r.nextInt(21);
			sceneInfo.setSound(sounds.getSounds().get(index7));			
			
			//generate secret
			int index8 = r.nextInt(21);
			sceneInfo.setSecret(secrets.getSecrets().get(index8));			
			
			sceneInfoList.add(sceneInfo);
		}
		
		for(int i=0; i<sceneInfoList.size(); i++){
			
			SceneInfo sceneInfo = sceneInfoList.get(i);
		    sceneInfo.getCharacter1().incrementCounter();
			sceneInfo.getCharacter2().incrementCounter();
		}
		
		Collections.sort(characters.getCharacters(), new CharacterComparator());
		
		for(NCharacter character:characters.getCharacters()){
			Log.e(TAG, character.getName()+"-"+character.getCounter());
		}
		
		//select main characters
		ArrayList<NCharacter> mainCharacters = new ArrayList<NCharacter>();
		mainCharacters.add(characters.getCharacters().get(0));
		mainCharacters.add(characters.getCharacters().get(1));
		//mainCharacters.add(characters.getCharacters().get(2));
		//mainCharacters.add(characters.getCharacters().get(3));
		
		for(NCharacter character:mainCharacters){
			Log.e(TAG, "main "+character.getName()+"-"+character.getCounter());
		}
		

		//select main characters
		ArrayList<NCharacter> secondaryCharacters = new ArrayList<NCharacter>();
		secondaryCharacters.add(characters.getCharacters().get(2));
		secondaryCharacters.add(characters.getCharacters().get(3));
		//mainCharacters.add(characters.getCharacters().get(2));
		//mainCharacters.add(characters.getCharacters().get(3));

		for(NCharacter character:secondaryCharacters){
			Log.e(TAG, "secondary "+character.getName()+"-"+character.getCounter());
		}
				
		//select scenes
		ArrayList<SceneInfo> novelSceneInfoList = new ArrayList<SceneInfo>();
		for(int i=0; i<sceneInfoList.size(); i++){

			SceneInfo sceneInfo = sceneInfoList.get(i);
			if((mainCharacters.contains(sceneInfo.getCharacter1()) && mainCharacters.contains(sceneInfo.getCharacter2())) ||
			   (mainCharacters.contains(sceneInfo.getCharacter1()) && secondaryCharacters.contains(sceneInfo.getCharacter2())) ||
			   (secondaryCharacters.contains(sceneInfo.getCharacter1()) && mainCharacters.contains(sceneInfo.getCharacter2())) ||
			   (secondaryCharacters.contains(sceneInfo.getCharacter1()) && secondaryCharacters.contains(sceneInfo.getCharacter2()))  ){
				novelSceneInfoList.add(sceneInfo);
			}
		}
		
		/*for(int i=0; i<novelSceneInfoList.size(); i++){

			SceneInfo sceneInfo = novelSceneInfoList.get(i);
			Log.e(TAG, i+". "+sceneInfo.toString());
		}*/
		
		//form topic
		Map<String, List<SceneInfo>> topics = new HashMap<String, List<SceneInfo>>();
		for(int i=0; i<novelSceneInfoList.size(); i++){

			SceneInfo sceneInfo = novelSceneInfoList.get(i);
			if(!topics.containsKey(sceneInfo.getSecret())){			
			    topics.put(sceneInfo.getSecret(), new ArrayList<SceneInfo>());
			}
			topics.get(sceneInfo.getSecret()).add(sceneInfo);
			Log.e(TAG, i+". "+sceneInfo.toString());
		}
		
		//build novel
		novel.getChapters().clear();
		int cindex = 1;
		Set<String> chapters = topics.keySet();
		for(String chapterName:chapters){
			Chapter chapter = new Chapter();
			chapter.setName("Chapter "+cindex);
			novel.getChapters().add(chapter);
			int sindex = 1;
			for(SceneInfo sceneInfo:topics.get(chapterName)){
				Scene scene = new Scene();
				scene.setName("Scene "+sindex);
				chapter.getScenes().add(scene);
				npm.updateScene(scene.getPath(), sceneInfo.getSecret(), sceneInfo.toString());
				sindex++;
			}
			cindex++;
		}
		npm.updateNovel();
		
		return novelName;
	}
	
	private Secrets readSecrets() 
	{
		InputStream inputStream1 = null;
		
		Secrets secrets = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_secrets);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1)
			{};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    secrets = SecretsHelper.readSecrets(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return secrets;
	}
	
	private Sounds readSounds() 
	{
		InputStream inputStream1 = null;
		
		Sounds sounds = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_sounds);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1)
			{};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    sounds = SoundsHelper.readSounds(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return sounds;
	}
	
	private Locations readLocations() 
	{
		InputStream inputStream1 = null;
		//CharactersHelper ch = new CharactersHelper();
		Locations locations = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_locations);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1)
			{};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    locations = LocationsHelper.readLocations(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return locations;
	}

	private Scents readScents() 
	{
		InputStream inputStream1 = null;
		Scents scents = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_scents);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1){};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    scents = ScentsHelper.readScents(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return scents;
	}
	
	private LocationModifiers readLocationModifiers() 
	{
		InputStream inputStream1 = null;
		//CharactersHelper ch = new CharactersHelper();
		LocationModifiers locationModifiers = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_location_modifiers);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1)
			{};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    locationModifiers = LocationModifiersHelper.readLocationModifers(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return locationModifiers;
	}
	
	private Verbs readVerbs() 
	{
		InputStream inputStream1 = null;
		//CharactersHelper ch = new CharactersHelper();
		Verbs verbs = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_verbs);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1)
			{};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    verbs = VerbsHelper.readVerbs(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return verbs;
	}
	

	private Characters readCharacters() 
	{
		InputStream inputStream1 = null;
		CharactersHelper ch = new CharactersHelper();
		Characters characters = null;
		try
		{
		    inputStream1 = context.getResources().openRawResource(R.raw.ng_characters);
		    byte[] reader = new byte[inputStream1.available()];
		    while (inputStream1.read(reader) != -1)
			{};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    characters = ch.readCharacters(xml);	
		}
		catch (IOException e)
		{
		    Log.e(TAG, e.getMessage());
	    }
		finally
		{
		    if (inputStream1 != null)
			{
				try
				{ 
					inputStream1.close();
				}
				catch (IOException e)
				{ 
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return characters;
	}

	private Novel readNovel(){
		NovelHelper nh = new NovelHelper();
		Novel novel = null;
		InputStream inputStream = null;
		try{
		    inputStream = context.getResources().openRawResource(R.raw.new_novel);
		    byte[] reader = new byte[inputStream.available()];
		    while (inputStream.read(reader) != -1) {};						
		    String xml = new String(reader);						
		    //Log.e(TAG, xml);						
		    novel = nh.readNovel(xml);	
		}catch(IOException e) {
		    Log.e(TAG, e.getMessage());
	    } finally {
		    if(inputStream != null) {
				try { 
					inputStream.close();
				} catch (IOException e) { 
					Log.e(TAG, e.getMessage());
				}
		    }
		}
		
		return novel;
	}
	
	class CharacterComparator implements Comparator
	{

		public int compare(Object p1, Object p2)
		{
			NCharacter char1 = (NCharacter)p1;
			NCharacter char2 = (NCharacter)p2;
		
		    return -(new Integer(char1.getCounter())).compareTo(new Integer(char2.getCounter()));
			
		}

		
	}
}
