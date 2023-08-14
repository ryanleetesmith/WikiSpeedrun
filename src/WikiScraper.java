import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* 
 * Author: Ryan Smith
 * File: WikiScraper.java
 * Assignment: Programming Assignment 10 - WikiRacer
 * Course: CSC 210 Fall 2020
 * Usage and Purpose: Access the html of a given wikipedia page, store this HTML as a string.
 * Search through and gather only the names of valid wikipedia pages, and store these in a
 * HashMap before returning the set of strings. Accessed by WikiRacer.java to efficiently form
 * a ladder between two wikipedia pages.
 */

public class WikiScraper {
			
	private static HashMap<String, Set<String>> memoizer = new HashMap<String, Set<String>>();
	/*
	 * This method takes in a string representing a wiki page as a parameter and
	 * utilizes the other methods in this class to return a set of all links to
	 * other wiki pages on the given page. Uses a hashmap to perform memoization if
	 * possible for each function call.
	 */
	public static Set<String> findWikiLinks(String link) {
		if (memoizer.containsKey(link)) {
			return memoizer.get(link);
		} else {
			String html = fetchHTML(link);
			Set<String> links = scrapeHTML(html);
			memoizer.put(link, links);
			return links;
		}
	}
	
	/*
	 * This method opens the internet and accesses the html of the wiki link passed
	 * in as a parameter.
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	/*
	 * This method takes the string parameter that represents a wiki page, and
	 * returns the full link to that page so that wikipedia can be properly accessed
	 * in fetchHTML()
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/*
	 * Takes in a string parameter that is all of the the untouched HTML from a wiki
	 * page. This method searches through all of this HTML and retrieves only the
	 * names of valid wikipedia pages, which it adds to a set and returns.
	 */
	private static Set<String> scrapeHTML(String html) {
		Set<String> retSet = new HashSet<String>();
		int track = 0;
		// split on what we need
		for (String here : html.split("<a href=\"/wiki/")) {
			// make sure its the right kind of href
			int j = here.indexOf("\"");
			// take following info
			if (j != -1 && track != 0) {
				String maybe = here.substring(0, j);
				// check if it contains stuff we dont need
				if (!maybe.contains("#") && !maybe.contains(":")) {
					retSet.add(maybe);
				}
			}
			track++;
		}
		// one weird broken page, testing shows it must be removed
		if (retSet.contains("Flag_of_the_Caribbean_Community")) {
			retSet.remove("Flag_of_the_Caribbean_Community");
			System.out.println("Removed!");
		}
		return retSet;
	}
	
}
