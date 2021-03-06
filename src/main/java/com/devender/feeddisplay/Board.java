package com.devender.feeddisplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	private static final long serialVersionUID = 7966865481186963441L;
	private static final int SPEED = 20;
	private final Timer timer;
	private final RenderingHints rh;
	private final AllTextsWrapper allTextsWrapper;
	private final Feeds feeds;
	private final Preferences preferences;

	/**
	 * if this is not used, every time getSize is called a new dimension object
	 * will be created.FS
	 */
	private Dimension dimensionOfBoard;

	public Board() {
		super();
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setFocusable(true);

		feeds = new Feeds();
		preferences = new Preferences();

		List<String> strings = preferences.init();

		for (String string : strings) {
			addToFeeds(string);
		}
		timer = new Timer(SPEED, this);
		timer.start();
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		allTextsWrapper = new AllTextsWrapper(3, feeds);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				allTextsWrapper.mouseClick(e.getPoint());
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					showAdd();
					break;

				case KeyEvent.VK_D:
					showDelete();
					break;
				}
			}
		});
	}

	private void addToFeeds(String string) {
		URL feedUrl;
		try {
			feedUrl = new URL(string);
			feeds.addNewFeedUrl(feedUrl, 1000 * 60 * 60);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showAdd() {
		String s = JOptionPane.showInputDialog("Enter the feed url");
		addToFeeds(s);
	}

	private void showDelete() {
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
		allTextsWrapper.draw(g2, (float) getSize(dimensionOfBoard).getWidth(), (float) getSize(
				dimensionOfBoard).getHeight());
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}