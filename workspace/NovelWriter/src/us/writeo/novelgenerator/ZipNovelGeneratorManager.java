package us.writeo.novelgenerator;

import java.io.*;
import us.writeo.common.novel.model.*;
import us.writeo.common.novel.persistence.*;

public class ZipNovelGeneratorManager extends NovelZipManager implements NovelGeneratorPersistenceManager
{
	public ZipNovelGeneratorManager(String aZipFileName, Novel aNovel, File aCacheDir){
		super(aZipFileName, aNovel, aCacheDir);
	}
}
