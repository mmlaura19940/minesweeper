package minesweeper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class Field {

	private CellStates cellState = CellStates.NOT_MINE;
	private MyImageList imageList;

	private Position position;
	private Button button;

	private boolean isFlag = false;
	private boolean isDiscovered = false;

	public Field(Composite parent, MyImageList imageList, Object layoutData) {
		this.button = new Button(parent, SWT.PUSH);
		this.button.setLayoutData(layoutData);
		this.imageList = imageList;
		setImage();
	}

	public void setImage() {
		if (!isDiscovered) {
			button.setImage(imageList.undiscoveredButton);
			if (isFlag) {
				button.setImage(imageList.flag);
			}
		}
		if (isDiscovered) {
			if (cellState == CellStates.MINE) {
				button.setImage(imageList.mine);

			} else {
				button.setRedraw(false);
				try {
					button.setImage(null);
					button.setEnabled(false);
				} finally {
					button.setRedraw(true);
				}
			}
		}
	}

	public void setValue(int minesAroundActualPoz) {
		isDiscovered = true;
		try {
			button.setText(String.valueOf(minesAroundActualPoz));
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	public void updateFlag() {
		if (isFlag) {
			isFlag = false;
		} else {
			isFlag = true;
		}
	}

	public boolean isMine() {
		return cellState == CellStates.MINE;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Button getButton() {
		return button;
	}

	public CellStates getCellState() {
		return cellState;
	}

	public void setCellState(CellStates cellState) {
		this.cellState = cellState;
	}

	public boolean isDiscovered() {
		return isDiscovered;
	}

	public void setDiscovered(boolean isDiscovered) {
		this.isDiscovered = isDiscovered;
	}

	public boolean isFlag() {
		return isFlag;
	}

	public void setIsFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}

	public void addMouseListener(MouseListener listener) {
		this.button.addMouseListener(listener);
	}

}
