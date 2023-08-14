import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* 
 * Author: Ryan Smith
 * File: WikiRacer.java
 * 
 * Purpose: Forms a "ladder" or pathway from one wikipedia page to another by
 * utilizing WikiScraper.java methods to retrieve all of the links on a given wiki page's HTML,
 * then determining which link has the closest similarity to our end goal by comparing the number
 * of common links. This page is then selected and the process is repeated. 
 * 
 * Usage:
 * Two command line arguments are necessary: the start and end wikipedia pages respectively.
 * Wikipedia links are structured as so - https://en.wikipedia.org/wiki/Strawberry
 * The arguments should be everything after the .../wiki/
 * Due to certain words having multiple pages, the arguments *ARE* case sensitive.
 * 
 */

public class WikiRacer {

	// visited links are put here so we dont get caught in a loop
	private static Set<String> visited = new HashSet<String>();
	// Prio queue that will help us determine which page to branch down
	private static MaxPQ ladsQ = new MaxPQ();

	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/*
	 * This method takes in a String representing a wiki start page, and a string
	 * representing a wiki end page as parameters. It creates a path of wiki pages
	 * from the start to the end through the links on each wiki page. For each page,
	 * it takes every link on the page and creates a new potential pathway to the
	 * end. It then compares the number of identical links that the last visited
	 * wiki page has to the end page, and adds it to a priority queue accordingly.
	 * The highest priority element is dequeued and worked with during every
	 * iteration of the loop, which ensures our time to reach the desired result is
	 * not infinite.
	 * 
	 * Think of each ladder as the pathway we have taken to get where we are, plus
	 * the number of common links where we currently are. They are mapped to each other
	 * so that we can traverse backwards if need be, but realistically the ladder could 
	 * just be the current link.
	 */
	private static List<String> findWikiLadder(String start, String end) {
		// Starting our queue and ladder out. (a) Ladder will be our end result.
		List<String> startLad = new ArrayList<String>();
		// ladder begins as just start page
		startLad.add(start);
		ladsQ.enqueue(startLad, 1);
		visited.add(start);
		// get the links on the end page to be compared to each page
		Set<String> endLinks = WikiScraper.findWikiLinks(end);
		while (!ladsQ.isEmpty()) { // starts with initial page
			// take highest priority ladder from queue and go down that pathway
			List<String> curLadder = ladsQ.dequeue();
			// get links from the last link in the ladder, i.e. the most recently added page
			Set<String> linksHere = WikiScraper.findWikiLinks(curLadder.get(curLadder.size() - 1));
			// if one of the links is our end result, we have a finalized ladder to return
			if (linksHere.contains(end)) {
				curLadder.add(end);
				return curLadder;
			}
			// end result is not on this page. For every link from this page, get all of its links
			linksHere.parallelStream().forEach(link -> {
				WikiScraper.findWikiLinks(link);
				});
			// for every link we have from this page
			for (String link : linksHere) {
				// if it is not a repeat
				if (!visited.contains(link)) {
					// make it unavailable for future additions
					visited.add(link);
					// make a new ladder with this as the most recent "step"
					List<String> ladCopy = new ArrayList<String>(curLadder);
					ladCopy.add(link);
					// retrieve links in common with end result from cache
					Set<String> passMe = WikiScraper.findWikiLinks(link);
					Set<String> temp = new HashSet<String>(passMe);
					temp.retainAll(endLinks);
					// add this ladder to the queue based on the amount this one had in common
					ladsQ.enqueue(ladCopy, temp.size());
				}
			}
		}
		startLad.clear();
		return startLad;
	}


}
