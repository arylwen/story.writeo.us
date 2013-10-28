package us.writeo.novelgenerator;

import android.content.*;
import android.util.*;
import java.io.*;
import java.text.*;
import java.util.*;
import us.writeo.common.novel.model.*;
import us.writeo.novelgenerator.model.*;
import us.writeo.novelwriter.*;

public class ZipNovelGenerator implements NovelGenerator
{
    private static final String TAG = "NG ZipNovelGenerator";
	private Context context;
	private NovelGeneratorPersistenceManager npm;
	private String novelTemplateFolder;
	
	
	public ZipNovelGenerator(Context aContext, String aTemplateDir){
		context = aContext;		
		//File extFileDir = context.getExternalFilesDir("templates/novels/shadowdancer");
		//novelTemplateFolder = extFileDir.toString();
		novelTemplateFolder = aTemplateDir;
	}

	public String generate()
	{
		long now = Calendar.getInstance().getTime().getTime();
		File extFileDir = context.getExternalFilesDir("novelz");
		String novelName = extFileDir + "/"+"Novel-"+now+".zip";
		
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
		
		classifyByScene(sceneInfoList, characters);
		for(NCharacter character:characters.getCharacters()){
			Log.e(TAG, character.getName()+"-"+character.getCounter());
		}
				
		//affinity
		ArrayList<CharacterPair> lpairs = classifyByAffinity(sceneInfoList);
		
		List<NCharacter> selected = new ArrayList<NCharacter>();
		for(CharacterPair pair:lpairs){
			if(!selected.contains(pair.character1)) selected.add(pair.character1);
			if(!selected.contains(pair.character2)) selected.add(pair.character2);
			if(selected.size() > 6) break;
		}
		
		//select main characters
		ArrayList<NCharacter> mainCharacters = new ArrayList<NCharacter>();
		mainCharacters.add(selected.get(0));
		mainCharacters.add(selected.get(1));
		
		for(NCharacter character:mainCharacters){
			Log.e(TAG, "main "+character.getName()+"-"+character.getCounter());
		}
		
		//select secondary characters
		ArrayList<NCharacter> secondaryCharacters = new ArrayList<NCharacter>();
		secondaryCharacters.add(selected.get(2));
		secondaryCharacters.add(selected.get(3));
		
		for(NCharacter character:secondaryCharacters){
			Log.e(TAG, "secondary "+character.getName()+"-"+character.getCounter());
		}		

		//select supporting characters
		ArrayList<NCharacter> supportingCharacters = new ArrayList<NCharacter>();
		supportingCharacters.add(selected.get(4));
		supportingCharacters.add(selected.get(5));
		
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
			   (secondaryCharacters.contains(sceneInfo.getCharacter1()) && secondaryCharacters.contains(sceneInfo.getCharacter2())) || 
			   (supportingCharacters.contains(sceneInfo.getCharacter1()) && mainCharacters.contains(sceneInfo.getCharacter2())) ||
			   (mainCharacters.contains(sceneInfo.getCharacter1()) && supportingCharacters.contains(sceneInfo.getCharacter2()))
			){
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
		
		StringBuffer cinfo = new StringBuffer();
		cinfo.append("main characters\n");
		cinfo.append(mainCharacters.get(0).getName() + "\n");
		cinfo.append(mainCharacters.get(1).getName() + "\n");
		
		cinfo.append("\nsecondary characters\n");
		cinfo.append(secondaryCharacters.get(0).getName() + "\n");
		cinfo.append(secondaryCharacters.get(1).getName() + "\n");
		
		cinfo.append("\nsuporting characters\n");
		cinfo.append(supportingCharacters.get(0).getName() + "\n");
		cinfo.append(supportingCharacters.get(1).getName() + "\n");
		
		DecimalFormat df = new DecimalFormat(); 
		df.setMaximumFractionDigits(2);
		
		//build novel
		novel.getChapters().clear();
		Chapter toc = new Chapter();
		toc.setName("Novel Info");
		novel.getChapters().add(toc);
		Scene chars = new Scene();
		toc.getScenes().add(chars);
		chars.setName("Table of Contents");
		StringBuffer tocinfo = new StringBuffer();		
		tocinfo.append("\n\n Table of contents");
		int cindex = 1;
		Set<String> chapters = topics.keySet();
		for(String chapterName:chapters){
			Chapter chapter = new Chapter();
			chapter.setName("Chapter "+cindex);
			novel.getChapters().add(chapter);

			int sindex = 1;
			String cname = "";
			for(SceneInfo sceneInfo:topics.get(chapterName)){
				Scene scene = new Scene();
				scene.setName("Scene "+sindex);
				chapter.getScenes().add(scene);
				npm.updateScene(scene.getPath(), sceneInfo.getSecret(), sceneInfo.toString());
				cname = sceneInfo.getSecret();
				sindex++;
			}
			double percent =   (((double)chapter.getScenes().size())/((double)novelSceneInfoList.size()))*100.0;
			tocinfo.append("\n        "+cindex+". "+cname+"..... "+ df.format(percent) +"%");
			cindex++;
		}
		npm.updateScene(chars.getPath(), tocinfo.toString(), cinfo.toString());
		//npm.updateNovel();
		

		
		Scene affinity = new Scene();
		affinity.setName("Affinity");
		toc.addScene(affinity);
		
		StringBuffer aff = new StringBuffer();
		for(CharacterPair pp:lpairs){
		   aff.append(pp.character1.getName()+" and "+pp.character2.getName()+" - "+pp.counter+"\n");
		}
		
		npm.updateScene(affinity.getPath(), aff.toString(), "");
		npm.updateNovel();
		
		return novelName;
	}

	private ArrayList<CharacterPair> classifyByAffinity(ArrayList<SceneInfo> sceneInfoList)
	{
		Map<Integer, CharacterPair> pairs = new HashMap<Integer, CharacterPair>();
		for (int i=0; i < sceneInfoList.size(); i++)
		{
			SceneInfo sceneInfo = sceneInfoList.get(i);
		    CharacterPair cp = new CharacterPair();
			cp.character1 = sceneInfo.getCharacter1();
			cp.character2 = sceneInfo.getCharacter2();
			if (!pairs.containsKey(cp.getKey()))
			{
				pairs.put(cp.getKey(), cp);
			}
			cp = pairs.get(cp.getKey());
			cp.counter++;
		}

		ArrayList<CharacterPair> lpairs = new ArrayList<CharacterPair>(pairs.values());
		Collections.sort(lpairs, new CharacterPairComparator());
		return lpairs;
	}

	private void classifyByScene(ArrayList<SceneInfo> sceneInfoList, Characters characters)
	{
		for (int i=0; i < sceneInfoList.size(); i++)
		{

			SceneInfo sceneInfo = sceneInfoList.get(i);
		    sceneInfo.getCharacter1().incrementCounter();
			sceneInfo.getCharacter2().incrementCounter();
		}

		Collections.sort(characters.getCharacters(), new CharacterComparator());
	}
	
	private class CharacterPair{
		public NCharacter character1;
		public NCharacter character2;
		public int counter;
		
		public boolean equals(Object obj) { 
		   if (obj == null) return false; 
		   if (obj == this) return true; 
		   if (!(obj instanceof CharacterPair)) return false; 
		   CharacterPair rhs = (CharacterPair) obj; 
		   return ( (( character1.getName().equals(rhs.character1.getName())) &&
					 ( character2.getName().equals(rhs.character2.getName()))) ||
					
				    (( character1.getName().equals(rhs.character2.getName())) &&
				     ( character2.getName().equals(rhs.character1.getName())))
				  );
		}
		
		public int hashCode(){
			return character1.getName().hashCode()+character2.getName().hashCode();
		}
		
		public Integer getKey(){
			return new Integer(hashCode());
		}
	}
	
	private Secrets readSecrets() 
	{
		InputStream inputStream1 = null;
		
		Secrets secrets = null;
		try
		{
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_secrets);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_secrets.xml");
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
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_sounds);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_sounds.xml");
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
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_locations);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_locations.xml");
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
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_scents);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_scents.xml");
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
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_location_modifiers);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_location_modifiers.xml");
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
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_verbs);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_verbs.xml");
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
		    //inputStream1 = context.getResources().openRawResource(R.raw.ng_characters);
			inputStream1 = new FileInputStream(this.novelTemplateFolder + "/ng_characters.xml");
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
	
	class CharacterPairComparator implements Comparator
	{

		public int compare(Object p1, Object p2)
		{
			CharacterPair char1 = (CharacterPair)p1;
			CharacterPair char2 = (CharacterPair)p2;

		    return -(new Integer(char1.counter)).compareTo(new Integer(char2.counter));

		}		
	}
}
