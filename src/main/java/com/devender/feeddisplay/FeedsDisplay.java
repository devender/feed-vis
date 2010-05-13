package com.devender.feeddisplay;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public class FeedsDisplay extends JApplet {
	private static final long serialVersionUID = -9162862030966415394L;
	private Board board;

	@Override
	public void init() {
		super.init();
		board = new Board();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					setSize(800, 300);
					setVisible(true);
					setContentPane(board);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		super.start();
		board.start();
	}

	@Override
	public void stop() {
		super.stop();
		board.stop();
	}
}
