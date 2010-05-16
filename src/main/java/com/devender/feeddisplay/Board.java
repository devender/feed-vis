package com.devender.feeddisplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	private static final long serialVersionUID = 7966865481186963441L;
	private static final int SPEED = 20;
	private final Timer timer;
	private final RenderingHints rh;
	private final AllTextsWrapper allTextsWrapper;

	/**
	 * if this is not used, every time getSize is called a new dimension object
	 * will be created.FS
	 */
	private Dimension dimensionOfBoard;

	public Board() {
		super();
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		timer = new Timer(SPEED, this);
		timer.start();
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		allTextsWrapper = new AllTextsWrapper(3);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				allTextsWrapper.mouseClick(e.getPoint(),getPreferredSize());
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				allTextsWrapper.mouseEntered(e.getPoint(),getPreferredSize());
			}
		});
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(rh);
		g2.setColor(Color.LIGHT_GRAY);
		allTextsWrapper.draw(g2, (float) getSize(dimensionOfBoard).getWidth(), (float) getSize(dimensionOfBoard).getHeight());
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}