/*
 * LstLineFileLoader.java
 * Copyright 2003 (C) David Hibbs <sage_sam@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on November 17, 2003, 12:00 PM
 *
 * Current Ver: $Revision$ <br>
 * Last Editor: $Author$ <br>
 * Last Edited: $Date$
 */
package pcgen.persistence.lst;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.Campaign;
import pcgen.core.SettingsHandler;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.util.Logging;
import pcgen.system.LanguageBundle;
import pcgen.system.PCGenSettings;

/**
 * This class is an extension of the LstFileLoader that loads items
 * that are CDOMObjects and have a source campaign associated with them.
 * Objects loaded by implementations of this class inherit the core
 * MOD/COPY/FORGET funcationality needed for core CDOMObjects used
 * to directly create characters.
 *
 * <p>
 * Current Ver: $Revision$ <br>
 * Last Editor: $Author$ <br>
 * Last Edited: $Date$
 *
 * @author AD9C15
 * @author boomer70 <boomer70@yahoo.com>
 */
public abstract class LstObjectFileLoader<T extends CDOMObject> extends Observable
{
	/** The String that separates fields in the file. */
	public static final String FIELD_SEPARATOR = "\t"; //$NON-NLS-1$
	/** The String that separates individual objects */
	public static final String LINE_SEPARATOR = "\r\n"; //$NON-NLS-1$

	/** Tag used to include an object */
	public static final String INCLUDE_TAG = "INCLUDE"; //$NON-NLS-1$

	/** Tag used to exclude an object */
	public static final String EXCLUDE_TAG = "EXCLUDE"; //$NON-NLS-1$

	/** The suffix used to indicate this is a copy operation */
	public static final String COPY_SUFFIX = ".COPY"; //$NON-NLS-1$
	/** The suffix used to indicate this is a mod operation */
	public static final String MOD_SUFFIX = ".MOD"; //$NON-NLS-1$
	/** The suffix used to indicate this is a forget operation */
	public static final String FORGET_SUFFIX = ".FORGET"; //$NON-NLS-1$

	private List<ModEntry> copyLineList = new ArrayList<ModEntry>();
	private List<String> forgetLineList = new ArrayList<String>();
	private List<List<ModEntry>> modEntryList = new ArrayList<List<ModEntry>>();
	private boolean processComplete = true;
	/** A list of objects that will not be included. */
	protected List<String> excludedObjects = new ArrayList<String>();

	/**
	 * LstObjectFileLoader constructor.
	 */
	public LstObjectFileLoader()
	{
		super();
	}

	/**
	 * This method loads the given list of LST files.
	 * @param fileList containing the list of files to read
	 * @throws PersistenceLayerException 
	 */
	public void loadLstFiles(LoadContext context, List<CampaignSourceEntry> fileList) throws PersistenceLayerException
	{
		processComplete = true;
		// Track which sources have been loaded already
		Set<CampaignSourceEntry> loadedFiles = new HashSet<CampaignSourceEntry>();

		// Load the files themselves as thoroughly as possible
		for (CampaignSourceEntry sourceEntry : fileList)
		{
			if (sourceEntry == null)
			{
				continue;
			}

			// Check if the CSE has already been loaded before loading it
			if (!loadedFiles.contains(sourceEntry))
			{
				loadLstFile(context, sourceEntry);
				loadedFiles.add(sourceEntry);
			}
		}

		// Next we perform copy operations
		processCopies(context);

		// Now handle .MOD items
		processComplete = false;
		processMods(context);

		// Finally, forget the .FORGET items
		processForgets(context);
	}

	/**
	 * This method parses the LST file line, applying it to the provided target
	 * object.  If the line indicates the start of a new target object, a new
	 * CDOMObject of the appropriate type will be created prior to applying the
	 * line contents.  Because of this behavior, it is necessary for this
	 * method to return the new object.  Implementations of this method also
	 * MUST call <code>completeObject</code> with the original target prior to 
	 * returning the new value.
	 * @param context TODO
	 * @param target CDOMObject to apply the line to, barring the start of a
	 *         new object
	 * @param lstLine String LST formatted line read from the source URL
	 * @param source SourceEntry indicating the file that the line was
	 *         read from as well as the Campaign object that referenced the file
	 *
	 * @return CDOMObject that was either created or modified by the provided
	 *         LST line
	 * @throws PersistenceLayerException if there is a problem with the LST syntax
	 */
	public abstract T parseLine(LoadContext context, T target,
		String lstLine, SourceEntry source) throws PersistenceLayerException;

	/**
	 * This method is called by the loading framework to signify that the
	 * loading of this object is complete and the object should be added to the
	 * system.
	 * 
	 * <p>This method will check that the loaded object should be included via
	 * a call to <code>includeObject</code> and if not add it to the list of
	 * excluded objects.
	 * 
	 * <p>Once the object has been verified the method will call
	 * <code>finishObject</code> to give each object a chance to complete 
	 * processing.
	 * 
	 * <p>The object is then added to the system if it doesn't already exist.
	 * If the object exists, the object sources are compared by date and if the
	 * System setting allowing over-rides is set it will use the object from the
	 * newer source.
	 * @param context TODO
	 * @param pObj The object that has just completed loading.
	 * 
	 * @see pcgen.persistence.lst.LstObjectFileLoader#includeObject(SourceEntry, CDOMObject)
	 * @see pcgen.persistence.lst.LstObjectFileLoader#finishObject(CDOMObject)
	 * @see pcgen.core.SettingsHandler#isAllowOverride()
	 * 
	 * @throws PersistenceLayerException 
	 * @since 5.11
	 */
	public void completeObject(LoadContext context, SourceEntry source,
		final T pObj) throws PersistenceLayerException
	{
		if (!processComplete || pObj == null)
		{
			return;
		}

		if (includeObject(source, pObj))
		{
			storeObject(context, pObj);
		}
		else
		{
			excludedObjects.add(pObj.getKeyName());
			context.ref.forget(pObj);
		}
	}

	protected void storeObject(LoadContext context, T pObj)
	{
		final T currentObj = getMatchingObject(context, pObj);

		if (!context.consolidate() || currentObj == null
				|| !pObj.equals(currentObj))
		{
			addGlobalObject(pObj);
		}
		else
		{
			//Yes, this is instance equality, NOT .equals!!!!!
			if (currentObj != pObj)
			{
				if (SettingsHandler.isAllowOverride())
				{
					// If the new object is more recent than the current
					// one, use the new object
					final Date pObjDate = pObj.get(ObjectKey.SOURCE_DATE);
					final Date currentObjDate = currentObj
							.get(ObjectKey.SOURCE_DATE);
					if ((pObjDate != null)
						&& ((currentObjDate == null) || ((pObjDate
							.compareTo(currentObjDate) > 0))))
					{
						performForget(context, currentObj);
						addGlobalObject(pObj);
					}
					else
					{
						/*
						 * This does not use performForget since this is only
						 * forgetting something that is local to the context
						 * (was never "added" to the 5.x system)
						 */
						context.ref.forget(pObj);
					}
				}
				else
				{
					// Duplicate loading error
					Logging.errorPrintLocalised(
						"Warnings.LstFileLoader.DuplicateObject", //$NON-NLS-1$
						pObj.getKeyName(), currentObj.getSourceURI(), pObj
							.getSourceURI());
				}
			}
		}
	}

	/**
	 * Adds an object to the global repository.
	 * 
	 * @param cdo The object to add.
	 * 
	 * @since 5.11
	 */
	protected void addGlobalObject(final CDOMObject cdo)
	{
	}

	/**
	 * This method should be called by finishObject implementations in
	 * order to check if the parsed object is affected by an INCLUDE or
	 * EXCLUDE request.
	 *
	 * @param cdo CDOMObject to determine whether to include in
	 *         Globals etc.
	 * @return boolean true if the object should be included, else false
	 *         to exclude it
	 */
	protected boolean includeObject(SourceEntry source, CDOMObject cdo)
	{
		// Null check; never add nulls or objects without a name/key name
		if ((cdo == null) || (cdo.getDisplayName() == null)
			|| (cdo.getDisplayName().trim().length() == 0)
			|| (cdo.getKeyName() == null)
			|| (cdo.getKeyName().trim().length() == 0))
		{
			return false;
		}

		// If includes were present, check includes for given object
		List<String> includeItems = source.getIncludeItems();

		if (!includeItems.isEmpty())
		{
			return includeItems.contains(cdo.getKeyName());
		}
		// If excludes were present, check excludes for given object
		List<String> excludeItems = source.getExcludeItems();

		if (!excludeItems.isEmpty())
		{
			return !excludeItems.contains(cdo.getKeyName());
		}

		return true;
	}

	/**
	 * This method retrieves a CDOMObject from globals by its key.
	 * This is used to avoid duplicate loads, get objects to forget or
	 * modify, etc.
	 * @param context TODO
	 * @param aKey String key of CDOMObject to retrieve
	 * @return CDOMObject of the given key
	 */
	protected abstract T getObjectKeyed(LoadContext context, String aKey);

	/**
	 * This method retrieves a CDOMObject from the global list, attempting to match (by key
	 * and category, if necessary), the given object. This is used to avoid
	 * duplicate loads
	 * @param context TODO
	 * @param key The CDOMObject containing the key to retrieve (for which there may be a duplicate)
	 * 
	 * @return CDOMObject from Globals
	 */
	protected T getMatchingObject(LoadContext context, CDOMObject key)
	{
		return getObjectKeyed(context, key.getKeyName());
	}
	
	/**
	 * This method loads a single LST formatted file.
	 * @param sourceEntry CampaignSourceEntry containing the absolute file path
	 * or the URL from which to read LST formatted data.
	 */
	protected void loadLstFile(LoadContext context, CampaignSourceEntry sourceEntry)
	{
		setChanged();
		URI uri = sourceEntry.getURI();
		notifyObservers(uri);

		StringBuilder dataBuffer;

		try
		{
			dataBuffer = LstFileLoader.readFromURI(uri);
		}
		catch (PersistenceLayerException ple)
		{
			String message = LanguageBundle.getFormattedString(
				"Errors.LstFileLoader.LoadError", //$NON-NLS-1$
				uri, ple.getMessage());
			Logging.errorPrint(message);
			setChanged();
			return;
		}

		String aString = dataBuffer.toString();
		if (context != null)
		{
			context.setSourceURI(uri);
		}
		T target = null;
		ArrayList<ModEntry> classModLines = null;

		boolean allowMultiLine =
				PCGenSettings.OPTIONS_CONTEXT.initBoolean(
					PCGenSettings.OPTION_SOURCES_ALLOW_MULTI_LINE, false);
		if (allowMultiLine)
		{
			// Support the new file type. All lines that start with a tab belong to the previous line.
			aString = aString.replaceAll("\r?\n\t", "\t");
		}
		
		String[] fileLines = aString.split(LstFileLoader.LINE_SEPARATOR_REGEXP);

		for (int i = 0; i < fileLines.length; i++)
		{
			String line = fileLines[i];
			if ((line.length() == 0)
				|| (line.charAt(0) == LstFileLoader.LINE_COMMENT_CHAR))
			{
				continue;
			}
			int sepLoc = line.indexOf(FIELD_SEPARATOR);
			String firstToken;
			if (sepLoc == -1)
			{
				firstToken = line;
			}
			else
			{
				firstToken = line.substring(0, sepLoc);
			}

			// Check for continuation of class mods
			if (classModLines != null)
			{
				// TODO - Figure out why we need to check CLASS: in this file.
				if (firstToken.startsWith("CLASS:")) //$NON-NLS-1$
				{
					modEntryList.add(classModLines);
					classModLines = null;
				}
				else
				{
					// Add the line to the class mod and don't process it yet.
					classModLines.add(new ModEntry(sourceEntry, line, i + 1));
					continue;
				}
			}

			// check for copies, mods, and forgets
			// TODO - Figure out why we need to check SOURCE in this file
			if (line.startsWith("SOURCE")) //$NON-NLS-1$
			{
				SourceLoader.parseLine(context, line, uri);
			}
			else if (line.trim().length()==0)
			{
				// Ignore the line
			}
			else if (firstToken.indexOf(COPY_SUFFIX) > 0)
			{
				copyLineList.add(new ModEntry(sourceEntry, line,
					i + 1));
			}
			else if (firstToken.indexOf(MOD_SUFFIX) > 0)
			{
				// TODO - Figure out why we need to check CLASS: in this file.
				if (firstToken.startsWith("CLASS:")) //$NON-NLS-1$
				{
					// As CLASS:abc.MOD can be followed by level lines, we place the
					// lines into a list for processing in a group afterwards
					classModLines = new ArrayList<ModEntry>();
					classModLines.add(new ModEntry(sourceEntry, line, i + 1));
				}
				else
				{
					List<ModEntry> modLines = new ArrayList<ModEntry>(1);
					modLines.add(new ModEntry(sourceEntry, line, i + 1));
					modEntryList.add(modLines);
				}
			}
			else if (firstToken.indexOf(FORGET_SUFFIX) > 0)
			{
				forgetLineList.add(line);
			}
			else
			{
				try
				{
					target = parseLine(context, target, line, sourceEntry);
				}
				catch (PersistenceLayerException ple)
				{
					String message =
							LanguageBundle.getFormattedString(
								"Errors.LstFileLoader.ParseError", //$NON-NLS-1$
								uri, i + 1, ple.getMessage());
					Logging.errorPrint(message);
					setChanged();
					Logging.debugPrint("Parse error:", ple); //$NON-NLS-1$
				}
				catch (Throwable t)
				{
					String message =
							LanguageBundle.getFormattedString(
								"Errors.LstFileLoader.ParseError", //$NON-NLS-1$
								uri, i + 1, t.getMessage());
					Logging.errorPrint(message, t);
					setChanged();
					Logging.errorPrint(LanguageBundle
						.getString("Errors.LstFileLoader.Ignoring: " + t.getMessage()));
					if  (Logging.isDebugMode())
					{
						Logging.errorPrint(LanguageBundle
								.getString("Errors.LstFileLoader.Ignoring"), t);
						t.printStackTrace();
					}
				}
			}
		}

		if (classModLines != null)
		{
			modEntryList.add(classModLines);
		}
		if (target != null)
		{
			try
			{
				completeObject(context, sourceEntry, target);
			}
			catch (PersistenceLayerException ple)
			{
				Logging.errorPrint("Error in completing "
					+ target.getClass().getSimpleName() + " "
					+ target.getKeyName());
				setChanged();
				Logging.debugPrint("Parse error:", ple); //$NON-NLS-1$
			}
		}
	}

	/**
	 * This method, when implemented, will perform a single .FORGET
	 * operation.
	 * @param context TODO
	 * @param objToForget containing the object to forget
	 */
	protected void performForget(LoadContext context, T objToForget)
	{
		context.ref.forget(objToForget);
	}

	/**
	 * This method will perform a single .COPY operation based on the LST
	 * file content.
	 * @param lstLine String containing the LST source for the
	 * .COPY operation
	 * @throws PersistenceLayerException 
	 */
	private void performCopy(LoadContext context, ModEntry me) throws PersistenceLayerException
	{
		String lstLine = me.getLstLine();
		int sepLoc = lstLine.indexOf(FIELD_SEPARATOR);
		String name;
		if (sepLoc != -1)
		{
			name = lstLine.substring(0, sepLoc);
		}
		else
		{
			name = lstLine;
		}
		final int nameEnd = name.indexOf(COPY_SUFFIX);
		final String baseName = name.substring(0, nameEnd);
		final String copyName = name.substring(nameEnd + 6);
		T copy = getCopy(context, baseName, copyName.intern());
		if (copy != null)
		{
			if (sepLoc != -1)
			{
				String restOfLine = me.getLstLine().substring(nameEnd + 6);
				parseLine(context, copy, restOfLine, me.getSource());
			}
			completeObject(context, me.getSource(), copy);
		}
	}

	protected T getCopy(LoadContext context, final String baseName,
			final String copyName) throws PersistenceLayerException
	{
		T object = getObjectKeyed(context, baseName);

		if (object == null)
		{
			String message = LanguageBundle.getFormattedString(
				"Errors.LstFileLoader.CopyObjectNotFound", //$NON-NLS-1$
				baseName);
			Logging.errorPrint(message);
			setChanged();

			return null;
		}

		T obj = context.ref.performCopy(object, copyName);
		if (obj == null)
		{
			setChanged();
		}
		return obj;
	}

	/**
	 * This method will perform a multi-line .MOD operation. This is used
	 * for example in MODs of CLASSES which can have multiple lines. Loaders
	 * can [typically] use the name without checking
	 * for (or stripping off) .MOD due to the implementation of
	 * CDOMObject.setName()
	 * @param entryList
	 */
	private void performMod(LoadContext context, List<ModEntry> entryList)
	{
		ModEntry entry = entryList.get(0);
		// get the name of the object to modify, trimming off the .MOD
		int nameEnd = entry.getLstLine().indexOf(MOD_SUFFIX);
		String key = entry.getLstLine().substring(0, nameEnd);

		// remove the leading tag, if any (i.e. CLASS:Druid.MOD
		int nameStart = key.indexOf(':');

		if (nameStart > 0)
		{
			key = key.substring(nameStart + 1);
		}

		// get the actual object to modify
		T object = context.ref.performMod(getObjectKeyed(context, key));
		
		if (object == null)
		{
			if (excludedObjects.contains(key))
			{
				return;
			}

			String message = LanguageBundle.getFormattedString(
				"Errors.LstFileLoader.ModObjectNotFound", //$NON-NLS-1$
				entry.getSource().getURI(), entry.getLineNumber(), key);
			Logging.log(Logging.LST_ERROR, message);
			setChanged();
			return;
		}

		// modify the object
		try
		{
			for (ModEntry element : entryList)
			{
				context.setSourceURI(element.source.getURI());
				try
				{
					Campaign origCampaign = object.get(ObjectKey.SOURCE_CAMPAIGN);
					
					parseLine(context, object, element.getLstLine(), element.getSource());

					if (origCampaign != null)
					{
						object.put(ObjectKey.SOURCE_CAMPAIGN, origCampaign);
					}
				}
				catch (PersistenceLayerException ple)
				{
					String message = LanguageBundle.getFormattedString(
						"Errors.LstFileLoader.ModParseError", //$NON-NLS-1$
						element.getSource().getURI(), element.getLineNumber(),
						ple.getMessage());
					Logging.errorPrint(message);
					setChanged();
				}
			}
			completeObject(context, entry.getSource(), object);
		}
		catch (PersistenceLayerException ple)
		{
			String message = LanguageBundle.getFormattedString(
				"Errors.LstFileLoader.ModParseError", //$NON-NLS-1$
				entry.getSource().getURI(), entry.getLineNumber(), ple
					.getMessage());
			Logging.errorPrint(message);
			setChanged();
		}
	}

	/**
	 * This method will process the lines containing a .COPY directive
	 * @throws PersistenceLayerException 
	 */
	private void processCopies(LoadContext context) throws PersistenceLayerException
	{
		for (ModEntry me : copyLineList)
		{
			context.setSourceURI(me.source.getURI());
			performCopy(context, me);
		}
		copyLineList.clear();
	}

	/**
	 * This method will process the lines containing a .FORGET directive
	 * @param context TODO
	 */
	private void processForgets(LoadContext context)
	{

		for (String forgetKey : forgetLineList)
		{
			forgetKey =
					forgetKey.substring(0, forgetKey.indexOf(FORGET_SUFFIX));

			if (excludedObjects.contains(forgetKey))
			{
				continue;
			}
			// Commented out so that deprcated method no longer used
			// performForget(forgetName);

			T objToForget = getObjectKeyed(context, forgetKey);
			if (objToForget != null)
			{
				performForget(context, objToForget);
			}
		}
		forgetLineList.clear();
	}

	/**
	 * This method will process the lines containing a .MOD directive
	 */
	private void processMods(LoadContext context)
	{
		for (List<ModEntry> modEntry : modEntryList)
		{
			performMod(context, modEntry);
		}
		modEntryList.clear();
	}

	/**
	 * This class is an entry mapping a mod to its source.
	 * Once created, instances of this class are immutable.
	 */
	public static class ModEntry
	{
		private CampaignSourceEntry source = null;
		private String lstLine = null;
		private int lineNumber = 0;

		/**
		 * ModEntry constructor.
		 * @param aSource CampaignSourceEntry containing the MOD line
		 *         [must not be null]
		 * @param aLstLine LST syntax modification
		 *         [must not be null]
		 * @param aLineNumber
		 * 
		 * @throws IllegalArgumentException if aSource or aLstLine is null.
		 */
		public ModEntry(final CampaignSourceEntry aSource,
			final String aLstLine, final int aLineNumber)
		{
			super();

			// These are programming errors so the msgs don't need to be 
			// internationalized.
			if (aSource == null)
			{
				throw new IllegalArgumentException("source must not be null"); //$NON-NLS-1$
			}

			if (aLstLine == null)
			{
				throw new IllegalArgumentException("lstLine must not be null"); //$NON-NLS-1$
			}

			this.source = aSource;
			this.lstLine = aLstLine;
			this.lineNumber = aLineNumber;
		}

		/**
		 * This method gets the LST formatted source line for the .MOD
		 * @return String in LST format, unmodified from the source file
		 */
		public String getLstLine()
		{
			return lstLine;
		}

		/**
		 * This method gets the source of the .MOD operation
		 * @return CampaignSourceEntry indicating where the .MOD came from
		 */
		public CampaignSourceEntry getSource()
		{
			return source;
		}

		/**
		 *
		 * @return The line number of the original file for this MOD entry
		 */
		public int getLineNumber()
		{
			return lineNumber;
		}
	}
}