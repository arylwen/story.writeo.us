package com.halcyon.novelgenerator.model;

import com.halcyon.novelwriter.model.*;
import java.util.*;
import org.simpleframework.xml.*;

@Element(name="root")
public class Characters
{
	@ElementList(name="characters")
	protected List<NCharacter> characters = new ArrayList<NCharacter>();
	
	public List<NCharacter> getCharacters(){
		return characters;
	}
}
