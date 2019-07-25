package minesweeper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class GameMaster {
	private final Shell shell;
	private final Button timerButton;
	private final Button countOfRestMine;

	private int size;
	private int maxCountOfMine;
	private Board board;
	private Group outerGroup;
	private boolean isDisposed = true;

	private static GameMaster instance = null;

	private GameMaster(Shell shell, Button timerButton, Button countOfRestMine) {
		this.shell = shell;
		this.timerButton = timerButton;
		this.countOfRestMine = countOfRestMine;

	}

	public static GameMaster instance(Shell shell, Button timerButton, Button countOfRestMine) {
		if (instance == null) {
			synchronized (GameMaster.class) {
				if (instance == null) {
					instance = new GameMaster(shell, timerButton, countOfRestMine);
				}
			}
		}
		return instance;
	}

	public void setMaxCountOfMine(int maxCountOfMine) {
		this.maxCountOfMine = maxCountOfMine;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void createGame() {
		outerGroup = new Group(shell, SWT.NONE);
		outerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outerGroup.setLayout(new GridLayout(size, true));

		final MyImageList imageList = new MyImageList(shell);

		board = new Board(size, maxCountOfMine, shell, imageList, timerButton, countOfRestMine);
		board.createBoard(outerGroup);
		board.addPositions();
		board.createMines();
		board.createMouseListeners();
		isDisposed = false;
	}

	public void disposeAll() {
		board.disposeAll();
		outerGroup.dispose();
		isDisposed = true;
	}

	public boolean getIsDisposed() {
		return isDisposed;
	}
}
