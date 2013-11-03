package us.writeo.novelgenerator.model;

import java.util.*;
import us.writeo.common.novel.model.*;

public class SceneInfo
{
     protected NCharacter character1;
	 protected NCharacter character2;
	 protected String verb;
	 protected String locationModifier;
	 protected Location location;
	 protected String scent;
	 protected String sound;
	 protected String secret;
	protected List<Item> items = new ArrayList<Item>();
	protected List<String> colors = new ArrayList<String>();
	protected List<String> traits = new ArrayList<String>();

	public void setTraits(List<String> traits)
	{
		this.traits = traits;
	}

	public List<String> getTraits()
	{
		return traits;
	}

	public void setColors(List<String> colors)
	{
		this.colors = colors;
	}

	public List<String> getColors()
	{
		return colors;
	}

	public void setItems(List<Item> items)
	{
		this.items = items;
	}

	public List<Item> getItems()
	{
		return items;
	}
	 
	 public NCharacter getCharacter1(){
		 return character1;
	 }
	 
	 public void setCharacter1(NCharacter aCharacter){
		 character1 = aCharacter;
	 }
	 
	public NCharacter getCharacter2(){
		return character2;
	}

	public void setCharacter2(NCharacter aCharacter){
		character2 = aCharacter;
		
	}
	
	public String getLocationModifier(){
		return locationModifier;
	}
	
	public void setLocationModifier(String aLocationModifier){
		locationModifier = aLocationModifier;
	}
	
	public String getVerb(){
		return verb;
	}

	public void setVerb(String aVerb){
		verb = aVerb;
	}
	

	public Location getLocation(){
		return location;
	}

	public void setLocation(Location aLocation){
		location = aLocation;
	}
		

	public String getSound(){
		return sound;
	}

	public void setSound(String aSound){
		sound = aSound;
	}
	
	public String getScent(){
		return scent;
	}

	public void setScent(String aScent){
		scent = aScent;
	}
	
	public String getSecret(){
		return secret;
	}

	public void setSecret(String aSecret){
		secret = aSecret;
	}
		
    public String toString(){
		StringBuffer sceneStr = new StringBuffer();
		
		sceneStr.append(secret);
		sceneStr.append(character1.getName());
		sceneStr.append(" and ");
		sceneStr.append(character2.getName());
		sceneStr.append(" ");
		sceneStr.append(verb);
		sceneStr.append(" in a ");
		sceneStr.append(locationModifier);
		sceneStr.append(" place in ");
		sceneStr.append(location.getName());
		sceneStr.append(". ");
		sceneStr.append("They heard a ");
		sceneStr.append(sound);
		sceneStr.append(" and smelled ");
		sceneStr.append(scent);
		sceneStr.append(".\n ");
	
		sceneStr.append("Items of interest: ");
		for(int i = 0; i< items.size(); i++ ){
			sceneStr.append(items.get(i).getName() + " ");
		}
		sceneStr.append(".\n ");
		
		sceneStr.append("Colors: ");
		for(int i = 0; i< colors.size(); i++ ){
			sceneStr.append(colors.get(i)+ " ");
		}
		sceneStr.append(".\n ");
		
		sceneStr.append("Traits: ");
		for(int i = 0; i< traits.size(); i++ ){
			sceneStr.append(traits.get(i) + " ");
		}
		sceneStr.append(".\n ");
		
		
		return sceneStr.toString();
	}
}
