import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* 
 * Author: Ryan Smith
 * File: WikiRacer.java
 * Assignment: Programming Assignment 10 - WikiRacer
 * Course: CSC 210 Fall 2020
 * Usage and Purpose: Forms a "ladder" or pathway from one wikipedia page to another by
 * utilizing WikiScraper.java methods to retrieve all of the links on a given wiki page's HTML,
 * then determining which link has the closest similarity to our end goal. This page is then
 * selected and the process is repeated. Parallelization is implemented to reduce runtime.
 */

public class WikiRacer {

	private static Set<String> visited = new HashSet<String>();
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
	 */
	private static List<String> findWikiLadder(String start, String end) {
		// Starting our queue and ladder out
		List<String> startLad = new ArrayList<String>();
		startLad.add(start);
		ladsQ.enqueue(startLad, 1);
		visited.add(start);
		Set<String> endLinks = WikiScraper.findWikiLinks(end);
		while (!ladsQ.isEmpty()) {
			// take highest priority ladder
			List<String> curLadder = ladsQ.dequeue();
			Set<String> linksHere = WikiScraper.findWikiLinks(curLadder.get(curLadder.size() - 1));
			if (linksHere.contains(end)) {
				curLadder.add(end);
				return curLadder;
			}
			// memoization
			linksHere.parallelStream().forEach(link -> {
				WikiScraper.findWikiLinks(link);
				});
			// link not on current page, make new ladders with correct priorities
			for (String link : linksHere) {
				if (!visited.contains(link)) {
					visited.add(link);
					List<String> ladCopy = new ArrayList<String>(curLadder);
					ladCopy.add(link);
					Set<String> passMe = WikiScraper.findWikiLinks(link);
					Set<String> temp = new HashSet<String>(passMe);
					temp.retainAll(endLinks);
					ladsQ.enqueue(ladCopy, temp.size());
				}
			}
		}
		startLad.clear();
		return startLad;
	}


}
