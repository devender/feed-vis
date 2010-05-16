package com.devender.feeddisplay;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;

import com.sun.syndication.feed.synd.SyndEntry;

public class TextLayoutWrapper {
	private float xForText, yForText = 0;
	private final TextLayout textLayout;
	private boolean finishedScrollingText = false;
	private final SyndEntry syndEntry;

	public TextLayoutWrapper(TextLayout textLayout, float xForText, float yForText,
			SyndEntry syndEntry) {
		this.textLayout = textLayout;
		this.yForText = yForText;
		this.xForText = xForText;
		this.syndEntry = syndEntry;
	}

	public boolean isFinishedScrollingText() {
		return finishedScrollingText;
	}

	public SyndEntry getSyndEntry() {
		return syndEntry;
	}

	public float getyForText() {
		return yForText;
	}

	public void draw(Graphics2D g2) {
		textLayout.draw(g2, xForText, yForText);

		if (textLayout.getBounds().getX() + xForText + textLayout.getBounds().getWidth() < 0) {
			finishedScrollingText = true;
		} else {
			xForText--;
		}
	}

	public boolean contains(Point point) {
		return textLayout.getOutline(null).contains(point.x - xForText, point.y - yForText);
	}
}
