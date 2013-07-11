package us.writeo.common.filepicker;

public interface FilePicker
{
	public final static int NEW_FILE_REQUEST_CODE = 30;
	public final static int SAVE_FILE_REQUEST_CODE = 10;
	public final static int OPEN_FILE_REQUEST_CODE = 20;
	public final static int PICK_DIRECTORY_REQUEST_CODE = 20;
	
	public void pickFileForNew();
	
	public void pickFileForSave();
	
	public void pickFileForOpen();
	
	public void pickDirectory();
}
