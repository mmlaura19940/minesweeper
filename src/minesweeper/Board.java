package minesweeper;

import java.util.Random;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Board {
	private static final int RIGHT_CLICK = 3;
	private final MyImageList imageList;

	private final Position[] relativPositions = { new Position(-1, -1), new Position(-1, 0), new Position(-1, 1),
			new Position(0, -1), new Position(0, 1), new Position(1, -1), new Position(1, 0), new Position(1, 1) };

	private Field[] buttons;
	private int size;
	private int maxCountOfMine;
	private boolean hasStarted = false;
	private boolean firstClick = true;
	private Shell shell;
	private StopWatch timer;
	private int restOfMines;

	private Button timerButton;
	private Button countOfRestMine;

	public Board(int size, int maxCountOfMine, Shell shell, MyImageList imageList, Button timerButton,
			Button countOfRestMine) {
		this.size = size;
		this.maxCountOfMine = maxCountOfMine;
		this.shell = shell;
		this.imageList = imageList;
		this.timerButton = timerButton;
		this.countOfRestMine = countOfRestMine;
		restOfMines = maxCountOfMine;
		timer = new StopWatch(timerButton);
	}

	public void createBoard(Group outerGroup) {
		firstClick = true;
		setMineCounter(0);
		buttons = new Field[size * size];
		for (int i = 0; i < buttons.length; i++) {
			Object layoutData = GridDataFactory.fillDefaults().hint(25, 25).grab(true, true).create();
			buttons[i] = new Field(outerGroup, imageList, layoutData);
		}
	}

	public void addPositions() {
		int iterator = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				buttons[iterator].setPosition(new Position(i, j));
				iterator++;
			}
		}
	}

	public void createMines() {
		final Random generator = new Random();
		for (int i = 0; i < maxCountOfMine; i++) {
			int randomIndex = generator.nextInt(size * size);
			if (buttons[randomIndex].getCellState() != CellStates.MINE) {
				buttons[randomIndex].setCellState(CellStates.MINE);
			} else {
				i--;
			}
		}

	}

	public void createMouseListeners() {
		for (final Field field : buttons) {
			final MouseAdapter adapter = new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					startTimer();
					if (e.button == RIGHT_CLICK) {
						checkFlags(field);
						field.setImage();
					} else {
						if (firstClick && field.isMine()) {
							firstClick = false;
							swapMines(field);
						}

						if (!field.isMine()) {
							field.setDiscovered(true);
							int minesAroundActualPoz = checkAllAdjacentTiles(field.getPosition());
							field.setValue(minesAroundActualPoz);
							if (minesAroundActualPoz == 0) {
								recursiveOpen(field.getPosition());
							}
							if (field.isFlag()) {
								field.updateFlag();
								setMineCounter(-1);
							}
							field.setImage();
							checkIfWin();
						} else {
							timer.setIsRunning(false);
							discoverAll();
							final MessageBox messageBox = new MessageBox(shell, SWT.NONE);
							messageBox.setText("End Of Game:");
							messageBox.setMessage("You Lose!");
							messageBox.open();
						}
					}
				}
			};
			field.addMouseListener(adapter);
		}
	}

	public void disposeAll() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getButton() != null) {
				buttons[i].getButton().dispose();
			}
		}
		resetState();
	}

	public void setMineCounter(int iterator) {
		restOfMines = restOfMines - iterator;
		countOfRestMine.setText("Bombs: " + String.valueOf(restOfMines));
	}

	private void recursiveOpen(Position position) {
		for (int k = 0; k < relativPositions.length; k++) {
			int newXPoz = position.getX() + relativPositions[k].getX();
			int newYPoz = position.getY() + relativPositions[k].getY();
			Position actualPoz = new Position(newXPoz, newYPoz);
			final CellStates tileState = checkTile(actualPoz);

			int actualIndexOfButton = getIndexOfButton(actualPoz);
			if (tileState == CellStates.MINE || tileState == CellStates.OUT_OF_BOUNDS) {
				continue;
			}
			Field actualField = buttons[actualIndexOfButton];
			if (actualField.isDiscovered()) {
				continue;
			}

			int minesAroundActualPoz = checkAllAdjacentTiles(actualPoz);
			actualField.setValue(minesAroundActualPoz);
			actualField.setImage();

			if (minesAroundActualPoz == 0) {
				recursiveOpen(actualPoz);
			}

			if (actualField.isFlag()) {
				actualField.updateFlag();
				actualField.setImage();
				setMineCounter(-1);
			}
		}
	}

	private int getIndexOfButton(Position position) {
		for (int j = 0; j < buttons.length; j++) {
			if (buttons[j].getPosition().getX() == position.getX()
					&& buttons[j].getPosition().getY() == position.getY()) {
				return j;
			}
		}
		return -1;
	}

	private void checkFlags(Field field) {
		if (field.isFlag()) {
			setMineCounter(-1);
			field.updateFlag();
		} else {
			if (restOfMines > 0) {
				setMineCounter(+1);
				field.updateFlag();
			}
		}
	}

	private int checkAllAdjacentTiles(Position position) {
		int countMines = 0;
		for (int k = 0; k < relativPositions.length; k++) {
			int newXPoz = position.getX() + relativPositions[k].getX();
			int newYPoz = position.getY() + relativPositions[k].getY();
			Position newPoz = new Position(newXPoz, newYPoz);
			if (CellStates.MINE == checkTile(newPoz)) {
				countMines++;
			}
		}

		return countMines;
	}

	private CellStates checkTile(Position position) {
		int indexOfButton = getIndexOfButton(position);
		if (indexOfButton == -1) {
			return CellStates.OUT_OF_BOUNDS;
		} else {
			if (buttons[indexOfButton].isMine()) {
				return CellStates.MINE;
			}
		}
		return CellStates.NOT_MINE;
	}

	private boolean checkIfWin() {
		int countOfGoodChoices = 0;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isDiscovered() == true) {
				countOfGoodChoices++;
			}
		}
		if (countOfGoodChoices == size * size - maxCountOfMine) {
			MessageBox messageBox = new MessageBox(shell, SWT.NONE);
			messageBox.setText("End Of Game:");
			messageBox.setMessage("You Win!");
			messageBox.open();
			timer.setIsRunning(false);
			return true;
		}
		return false;
	}

	private void discoverAll() {
		for (int j = 0; j < buttons.length; j++) {
			buttons[j].setDiscovered(true);
			if (buttons[j].isFlag()) {
				buttons[j].updateFlag();
				setMineCounter(-1);
			}
			if (!buttons[j].isMine()) {
				buttons[j].setValue(checkAllAdjacentTiles(buttons[j].getPosition()));
			}
			buttons[j].setImage();

		}
	}

	private void swapMines(Field field) {
		field.setCellState(CellStates.NOT_MINE);
		final Random generator = new Random();
		boolean isMine = true;

		while (isMine) {
			int randomIndex = generator.nextInt(size * size);
			Field randomField = buttons[randomIndex];
			if (randomField != field && !randomField.isMine()) {
				randomField.setCellState(CellStates.MINE);
				isMine = false;
			}
		}
	}

	private void resetState() {
		timer.setIsRunning(false);
		timer = new StopWatch(timerButton);
		hasStarted = false;
	}

	private void startTimer() {
		if (!timer.getIsRunning()) {
			if (hasStarted) {
				timer.setIsRunning(true);
			}
			hasStarted = true;
			timer.start();
		}
	}
}
