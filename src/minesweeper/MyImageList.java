package minesweeper;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class MyImageList {
	public Shell shell;

	public final Image undiscoveredButton;
	public final Image flag;
	public final Image mine;

	public MyImageList(Shell shell) {
		this.shell = shell;
		undiscoveredButton = new Image(shell.getDisplay(), Images.UNDISCOVERED_BUTTON);
		flag = new Image(shell.getDisplay(), Images.FLAG);
		mine = new Image(shell.getDisplay(), Images.BOMB);
	}

}
