package minesweeper;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public final class MineSweeperApp {

	private final Display display = Display.getDefault();
	private Shell shell;
	private final MenuItem[] menuItems = new MenuItem[3];

	public static void main(String[] args) {
		try {
			MineSweeperApp window = new MineSweeperApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		createContents();
		shell.open();
		shell.layout();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	protected void createContents() {
		shell = new Shell(display);
		shell.setText("Minesweeper");
		shell.setLayout(new GridLayout(1, false));

		final Group outerGroup = new Group(shell, SWT.NONE);
		outerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outerGroup.setLayout(new GridLayout(3, true));

		final Button countOfRestMine = new Button(outerGroup, SWT.PUSH);
		countOfRestMine.setText("Bombs: ");
		countOfRestMine.setEnabled(false);
		countOfRestMine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Button reset = new Button(outerGroup, SWT.PUSH);
		reset.setText("Reset");
		reset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Button time = new Button(outerGroup, SWT.PUSH);
		time.setText("0");
		time.setEnabled(false);
		time.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GameMaster gameMaster = GameMaster.instance(shell, time, countOfRestMine);
		createMenu(gameMaster);

		reset.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (!gameMaster.getIsDisposed()) {
					gameMaster.disposeAll();

				}
				time.setText("0");
				gameMaster.createGame();
				shell.layout(true);
				shell.pack();
			}
		});
	}

	private void createMenu(GameMaster gameMaster) {
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		final MenuItem MenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		MenuHeader.setText("Game");

		final Menu menu = new Menu(shell, SWT.DROP_DOWN);
		MenuHeader.setMenu(menu);

		final MenuItem easyGame = new MenuItem(menu, SWT.CHECK);
		easyGame.setText("Easy");
		menuItems[0] = easyGame;
		final MenuItem normalGame = new MenuItem(menu, SWT.CHECK);
		normalGame.setText("Normal");
		menuItems[1] = normalGame;
		final MenuItem hardGame = new MenuItem(menu, SWT.CHECK);
		hardGame.setText("Hard");
		menuItems[2] = hardGame;
		createMenuListeners(gameMaster);
	}

	private void createMenuListeners(GameMaster gameMaster) {
		for (int i = 0; i < menuItems.length; i++) {
			int iter = i;
			menuItems[i].addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					setMenuSelection(menuItems, iter);

					Difficulty difficulty = Difficulty.EASY;
					try {
						difficulty = Difficulty.getByName(menuItems[iter].getText());
					} catch (IllegalArgumentException ex) {
						ex.printStackTrace();
					}

					gameMaster.setSize(difficulty.getSize());
					gameMaster.setMaxCountOfMine(difficulty.getMaxCountOfMines());

					if (!gameMaster.getIsDisposed()) {
						gameMaster.disposeAll();

					}
					gameMaster.createGame();
					shell.layout(true);
					shell.pack();
				}
			});
		}
	}

	private void setMenuSelection(MenuItem[] menuItems, int iter) {
		for (int j = 0; j < menuItems.length; j++) {
			menuItems[j].setSelection(false);
		}
		menuItems[iter].setSelection(true);
	}
}
