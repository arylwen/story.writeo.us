package us.writeo.novelgenerator.model;

import java.util.*;
import org.simpleframework.xml.*;
import us.writeo.common.novel.model.*;

@Element(name="root")
public class Characters
{
	@ElementList(name="characters")
	protected List<NCharacter> characters = new ArrayList<NCharacter>();
	
	public List<NCharacter> getCharacters(){
		return characters;
	}
}
