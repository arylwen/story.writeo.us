package us.writeo.intents;

public final class WriteoUSIntents
{


		/**
		 * Activity Action: Pick a file through the file manager, or let user
		 * specify a custom file name.
		 * Data is the current file name or file name suggestion.
		 * Returns a new file name as file URI in data.
		 * 
		 * <p>Constant Value: "org.openintents.action.PICK_FILE"</p>
		 */
	public static final String ACTION_PICK_FILE = "us.writeo.filepicker.action.PICK_FILE";

		/**
		 * Activity Action: Pick a directory through the file manager, or let user
		 * specify a custom file name.
		 * Data is the current directory name or directory name suggestion.
		 * Returns a new directory name as file URI in data.
		 * 
		 * <p>Constant Value: "org.openintents.action.PICK_DIRECTORY"</p>
		 */
		public static final String ACTION_PICK_DIRECTORY = "us.writeo.filepicker.action.PICK_DIRECTORY";

		/**
		 * Activity Action: Move, copy or delete after select entries.
		 * Data is the current directory name or directory name suggestion.
		 * 
		 * <p>Constant Value: "org.openintents.action.MULTI_SELECT"</p>
		 */
		public static final String ACTION_MULTI_SELECT = "org.openintents.action.MULTI_SELECT";

		public static final String ACTION_SEARCH_STARTED = "org.openintents.action.SEARCH_STARTED";

		public static final String ACTION_SEARCH_FINISHED = "org.openintens.action.SEARCH_FINISHED";

		/**
		 * The title to display.
		 * 
		 * <p>This is shown in the title bar of the file manager.</p>
		 * 
		 * <p>Constant Value: "org.openintents.extra.TITLE"</p>
		 */
		public static final String EXTRA_TITLE = "org.openintents.extra.TITLE";

		/**
		 * The text on the button to display.
		 * 
		 * <p>Depending on the use, it makes sense to set this to "Open" or "Save".</p>
		 * 
		 * <p>Constant Value: "org.openintents.extra.BUTTON_TEXT"</p>
		 */
		public static final String EXTRA_BUTTON_TEXT = "org.openintents.extra.BUTTON_TEXT";

		/**
		 * Flag indicating to show only writeable files and folders.
		 *
		 * <p>Constant Value: "org.openintents.extra.WRITEABLE_ONLY"</p>
		 */
		public static final String EXTRA_WRITEABLE_ONLY = "org.openintents.extra.WRITEABLE_ONLY";

		/**
		 * The path to prioritize in search. Usually denotes the path the user was on when the search was initiated.
		 * 
		 * <p>Constant Value: "org.openintents.extra.SEARCH_INIT_PATH"</p>
		 */
		public static final String EXTRA_SEARCH_INIT_PATH = "org.openintents.extra.SEARCH_INIT_PATH";

		/**
		 * The search query as sent to SearchService.
		 * 
		 * <p>Constant Value: "org.openintents.extra.SEARCH_QUERY"</p>
		 */
		public static final String EXTRA_SEARCH_QUERY = "org.openintents.extra.SEARCH_QUERY";

		/**
		 * <p>Constant Value: "org.openintents.extra.DIR_PATH"</p>
		 */
		public static final String EXTRA_DIR_PATH = "org.openintents.extra.DIR_PATH";

		/**
		 * Extension by which to filter.
		 * 
		 * <p>Constant Value: "org.openintents.extra.FILTER_FILETYPE"</p>
		 */
		public static final String EXTRA_FILTER_FILETYPE = "org.openintents.extra.FILTER_FILETYPE";

		/**
		 * Mimetype by which to filter.
		 * 
		 * <p>Constant Value: "org.openintents.extra.FILTER_MIMETYPE"</p>
		 */
		public static final String EXTRA_FILTER_MIMETYPE = "org.openintents.extra.FILTER_MIMETYPE";

		/**
		 * Only show directories.
		 * 
		 * <p>Constant Value: "org.openintents.extra.DIRECTORIES_ONLY"</p>
		 */
		public static final String EXTRA_DIRECTORIES_ONLY = "org.openintents.extra.DIRECTORIES_ONLY";

		public static final String EXTRA_DIALOG_FILE_HOLDER = "org.openintents.extra.DIALOG_FILE";

		public static final String EXTRA_IS_GET_CONTENT_INITIATED = "org.openintents.extra.ENABLE_ACTIONS";

		public static final String EXTRA_FILENAME = "org.openintents.extra.FILENAME";
	}
