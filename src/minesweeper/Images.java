package minesweeper;

public final class Images {
	public static final String PLATFORM_SEPARATOR = System.getProperty("file.separator");
	public static final String ICONS_PATH = "Images"+PLATFORM_SEPARATOR;
	
	public static final String BOMB = ICONS_PATH + "bomb.png";
	public static final String UNDISCOVERED_BUTTON = ICONS_PATH + "button.png";
	public static final String FLAG = ICONS_PATH + "flag.png";
	private Images() {}
}
