package com.devender.feeddisplay;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;

public class TextLayoutWrapper {
	private float xForText, yForText = 0;
	private TextLayout textLayout;
	private boolean finishedScrollingText = false;

	public TextLayoutWrapper(TextLayout textLayout, float xForText, float yForText) {
		this.textLayout = textLayout;
		this.yForText = yForText;
		this.xForText = xForText;
	}

	public TextLayout getTextLayout() {
		return textLayout;
	}

	public boolean isFinishedScrollingText() {
		return finishedScrollingText;
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
}
