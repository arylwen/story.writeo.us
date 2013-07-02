package com.halcyon.novelgenerator.model;

import com.halcyon.novelwriter.model.*;

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
		sceneStr.append(". ");
		sceneStr.append(secret);
		return sceneStr.toString();
	}
}
