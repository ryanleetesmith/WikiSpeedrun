import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* 
 * Author: Ryan Smith
 * File: WikiScraper.java
 * 
 * Purpose: Access the html of a given wikipedia page, store this HTML as a string.
 * Search through and gather only the names of valid wikipedia pages, and store these in a
 * HashMap before returning the set of strings. 
 * 
 * Usage: Accessed by WikiRacer.java to efficiently form a ladder between two wikipedia pages.
 */

public class WikiScraper {
	
	// cache! So that we dont have to accost wikipedia too much (also speeds things up a LOT)
	private static HashMap<String, Set<String>> memoizer = new HashMap<String, Set<String>>();
	
	/*
	 * This method takes in a string representing a wiki page as a parameter and
	 * utilizes the other methods in this class to return a set of every link
	 * to another page on the wiki page that was passed in.
	 */
	public static Set<String> findWikiLinks(String link) {
		// try cache
		if (memoizer.containsKey(link)) {
			return memoizer.get(link);
		// cache missed, do it the hard way
		} else {
			String html = fetchHTML(link);
			Set<String> links = scrapeHTML(html);
			memoizer.put(link, links);
			return links;
		}
	}
	
	/*
	 * This method uses a URL openstream to get the html content of a webpage,
	 * then reads through this stream and converts it to one very long string
	 * which is then returned.
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			// basically HTTP get the wikipedia page we are looking for
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			// read returned bytestream
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
	 * in fetchHTML(). Allows the user to just type in something they want to try 
	 * without going to wikipedia, assuming they capitalize properly!
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
		// first link is always invalid
		int track = 0;
		// split on valid links to another wikipedia page
		for (String here : html.split("<a href=\"/wiki/")) {
			// multiple types of href, ensure it is the correct kind
			int j = here.indexOf("\"");
			if (j != -1 && track != 0) {
				String maybe = here.substring(0, j);
				// final check to see if this link is correct
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
