package com.devender.feeddisplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Preferences {
	private static final String PREF_DIR = ".feed-vis";
	private static final String FEED_FILE = "feeds.txt";
	private static final String[] defaultList = { "http://news.ycombinator.com/rss",
			"http://www.reddit.com/r/programming/.rss" };

	public List<String> init() {
		// check if pref dir exists
		File prefDir = new File(System.getProperty("user.home")
				+ System.getProperty("file.separator") + PREF_DIR);
		if (!prefDir.exists()) {
			prefDir.mkdir();
		}
		return initFeedFile(prefDir);
	}

	private List<String> initFeedFile(File prefDir) {
		List<String> list = new ArrayList<String>();
		// check to see if feed.txt exists
		File feeds = new File(prefDir.getAbsoluteFile() + System.getProperty("file.separator")
				+ FEED_FILE);
		try {
			if (feeds.exists()) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(feeds));
				String line = bufferedReader.readLine();
				while (line != null) {
					list.add(line);
					line = bufferedReader.readLine();
				}
				bufferedReader.close();
			} else {
				feeds.createNewFile();
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(feeds));
				for (String string : defaultList) {
					bufferedWriter.write(string);
					bufferedWriter.newLine();
					list.add(string);
				}
				bufferedWriter.flush();
				bufferedWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		Preferences preferences = new Preferences();
		List<String> list = preferences.init();
		for (String string : list) {
			System.out.println(string);
		}
	}
}
