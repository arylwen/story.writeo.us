package com.halcyon.novelgenerator;

import com.halcyon.novelwriter.*;
import com.halcyon.novelwriter.model.*;
import java.io.*;

public class ZipNovelGeneratorManager extends NovelZipManager implements NovelGeneratorPersistenceManager
{
	public ZipNovelGeneratorManager(String aZipFileName, Novel aNovel, File aCacheDir){
		super(aZipFileName, aNovel, aCacheDir);
	}
}
