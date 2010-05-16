package com.devender.feeddisplay;

import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class SyndFeedWrapper {
	private SyndFeed syndFeed;
	private List<SyndEntry> entries;
	private long lastUpdated = 0;
	private long howOften;
	private int lastEntryRead = 0;

	public void setSyndFeed(SyndFeed syndFeed) {
		this.syndFeed = syndFeed;
	}

	public void setHowOften(long howOften) {
		this.howOften = howOften;
	}

	private boolean needsToBeUpdated() {
		return System.currentTimeMillis() - lastUpdated > howOften;
	}

	@SuppressWarnings("unchecked")
	public void update() {
		entries = syndFeed.getEntries();
		for (SyndEntry entry : entries) {
			entry.setSource(syndFeed);
		}

		lastUpdated = System.currentTimeMillis();
	}

	public void check() {
		if (needsToBeUpdated()) {
			update();
		}
	}

	public SyndEntry getNextItemToRead() {
		SyndEntry syndEntry = entries.get(lastEntryRead++);
		if (lastEntryRead + 1 > entries.size()) {
			lastEntryRead = 0;
		}
		return syndEntry;
	}

	static class Builder {
		private SyndFeedWrapper syndFeedWrapper = new SyndFeedWrapper();

		public Builder withSyndFeed(SyndFeed syndFeed) {
			syndFeedWrapper.setSyndFeed(syndFeed);
			return this;
		}

		public Builder withHowOften(long howOften) {
			syndFeedWrapper.setHowOften(howOften);
			return this;
		}

		public SyndFeedWrapper build() {
			return syndFeedWrapper;
		}
	}
}
