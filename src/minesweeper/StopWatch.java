package minesweeper;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class StopWatch extends Thread {
	final private Button timerButton;
	private int time;
	private boolean isRunning = false;

	public StopWatch(Button timerButton) {
		this.timerButton = timerButton;
		setDaemon(true);
	}

	@Override
	public void run() {
		time = 0;
		while (isRunning) {
			time++;
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!timerButton.isDisposed()) {
						timerButton.setText(String.valueOf(time));
					}
				}
			});

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void start() {
		if (!isRunning) {
			isRunning = true;
			super.start();
		}
	}

	public boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

}
