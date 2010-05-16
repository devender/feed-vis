package com.devender.feeddisplay;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AllTextsWrapper {
	private final Font font;
	private final Feeds feeds;
	private final Random random;
	private final int numberOfLines;
	private List<TextLayoutWrapper> list;
	private static final int MIN_V_DISTANCE = 10;

	public AllTextsWrapper(int numberOfLines) {
		this.numberOfLines = numberOfLines;
		font = new FontFinder().chooseFont();
		feeds = new Feeds();
		// -----------------------TODO -------------------------
		String[] strings = { "http://news.ycombinator.com/rss",
				"http://www.reddit.com/r/programming/.rss" };
		for (String string : strings) {
			URL feedUrl;
			try {
				feedUrl = new URL(string);
				feeds.addNewFeedUrl(feedUrl, 1000 * 60 * 60);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		// ------------------------------------------------
		random = new Random();
	}

	public void mouseClick(Point point, Dimension size) {
		System.out.println("mouse click @ "+ point.x + " "+ point.y);
		for (TextLayoutWrapper layoutWrapper : list) {
			System.out.println(layoutWrapper.getTextLayout().getOutline(null).getBounds2D().contains(point.x, point.y));
			boolean contains = layoutWrapper.getTextLayout().getOutline(null).contains(point);
			System.out.println(contains);

		}
	}

	/**
	 * For each text in the list, checks to see if it has scrolled off the
	 * board, if so resets it and assigns a new y cord
	 * 
	 * @param g2
	 * @param boardWidth
	 * @param boardHeight
	 */
	public void draw(final Graphics2D g2, final float boardWidth, final float boardHeight) {
		if (list == null) {
			list = new ArrayList<TextLayoutWrapper>();
			for (int i = 0; i < numberOfLines; i++) {
				list.add(createNewTextLayoutWrapper(g2, boardWidth, boardHeight));
			}
		}

		for (int i = 0; i < list.size(); i++) {
			final TextLayoutWrapper layoutWrapper = list.get(i);
			if (layoutWrapper.isFinishedScrollingText()) {
				list.remove(i);
				list.add(createNewTextLayoutWrapper(g2, boardWidth, boardHeight));
			}
			layoutWrapper.draw(g2);
		}
	}

	private TextLayoutWrapper createNewTextLayoutWrapper(final Graphics2D g2,
			final float boardWidth, final float boardHeight) {
		return new TextLayoutWrapper(new TextLayout(feeds.getNextItemToRead().getTitle(), font, g2
				.getFontRenderContext()), Math.round(boardWidth), getNextY(boardHeight));
	}

	private float getNextY(final float boardHeight) {
		boolean foundY = false;
		int y = random.nextInt(Math.round(boardHeight));

		while (!foundY) {
			// the next y should be > 1
			if (y > MIN_V_DISTANCE) {
				if (list.isEmpty()) {
					// list does not have any items and y is already > 1,
					foundY = true;
				} else {
					// if list already has items, then compare with each
					foundY = compareAgainstEachtItem(y);
					if (foundY) {
						break;
					}
				}
			}
			y = random.nextInt(Math.round(boardHeight));
		}
		return y;
	}

	private boolean compareAgainstEachtItem(int y) {
		int howManyChecked = 0;

		for (TextLayoutWrapper layoutWrapper : list) {
			final int presentY = Math.round(layoutWrapper.getyForText());
			if (Math.abs(presentY - y) < MIN_V_DISTANCE) {
				break;
			} else {
				howManyChecked++;
			}
		}

		if (howManyChecked == list.size()) {
			return true;
		}
		return false;
	}

	class FontFinder {
		private static final String PREFERRED_FONT = "Monaco";
		private static final String FALLBACK_FONT = "Lucida Console";

		public Font chooseFont() {
			if (isPreferredFontAvailable()) {
				return new Font(PREFERRED_FONT, Font.BOLD, 18);
			}
			return new Font(FALLBACK_FONT, Font.BOLD, 18);
		}

		private boolean isPreferredFontAvailable() {
			for (Font localFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
				if (localFont.getFontName().equals(PREFERRED_FONT)) {
					return true;
				}
			}
			return false;
		}
	}
}
