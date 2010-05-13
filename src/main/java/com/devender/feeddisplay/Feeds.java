package com.devender.feeddisplay;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class Feeds {
	private final List<SyndFeedWrapper> syndFeeds;
	private int lastFeedRead = 0;
	private final Timer timer = new Timer();

	public SyndEntry getNextItemToRead() {
		SyndEntry syndEntry = syndFeeds.get(lastFeedRead++).getNextItemToRead();
		if (lastFeedRead + 1 > syndFeeds.size()) {
			lastFeedRead = 0;
		}
		return syndEntry;
	}

	public Feeds() {
		syndFeeds = new ArrayList<SyndFeedWrapper>();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				for (SyndFeedWrapper feedWrapper : syndFeeds) {
					feedWrapper.check();
				}

			}
		}, 1000, 1000 * 60 * 60);
	}

	public boolean addNewFeedUrl(URL feedUrl, long howOften) {
		boolean added = false;
		SyndFeedInput input = new SyndFeedInput();
		try {
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			SyndFeedWrapper feedWrapper = new SyndFeedWrapper.Builder().withSyndFeed(feed).withHowOften(howOften).build();
			feedWrapper.update();
			syndFeeds.add(feedWrapper);
			added = true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return added;
	}

	public static void main(String[] args) throws MalformedURLException {
		String[] strings = { "http://news.ycombinator.com/rss", "http://www.reddit.com/r/programming/.rss" };

		Feeds feeds = new Feeds();
		for (String string : strings) {
			URL feedUrl = new URL(string);
			feeds.addNewFeedUrl(feedUrl, 1000 * 60 * 60);
		}

		for (int i = 0; i < 2000; i++) {
			SyndEntry syndEntry = feeds.getNextItemToRead();
			if (syndEntry != null)
				System.out.println(syndEntry.getTitle());
		}
	}

}
