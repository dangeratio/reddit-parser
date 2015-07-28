package com.anemortalkid.reddit.parser.mysteries;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrub10KMysteries {

	private static final String redditLink = "https://www.reddit.com/r/DnDBehindTheScreen/comments/3evxgl/lets_make_10000_mysteries/";
	private static List<MysteryDataObject> dataPoints = new ArrayList<MysteryDataObject>();

	private static final int LAST_KNOWN_COUNT = 194;

	public static void main(String[] args) {

		try {
			Document redditDoc = Jsoup.connect(redditLink).userAgent("Mozilla")
					.get();
			if (redditDoc != null) {
				Elements comments = redditDoc.getElementsByClass("thing");
				// System.out.println("Comments:" + comments.size());

				List<Element> nonChildOnly = new ArrayList<Element>();

				for (Element element : comments) {
					nonChildOnly.add(element);
				}

				// System.out.println("Top-Level:" + allElements.size());

				for (Element topLevel : nonChildOnly) {
					Element md = topLevel.getElementsByClass("md").first();

					// Process the first one
					Elements mdElems = md.getAllElements();
					String bold, regular = "";
					bold = "";
					regular = "";

					for (Element elem : mdElems) {

						String tagName = elem.tagName();
						if (tagName.equals("p")) {
							Element p = elem;
							Elements strongElems = p.getElementsByTag("strong");
							Elements emElems = p.getElementsByTag("em");

							if (!strongElems.isEmpty()) {
								Element strong = strongElems.first();
								bold = strong.text();
							} else {
								regular += " " + p.text();
							}
						} else if (tagName.equals("hr")) {
							constructIfRequiredPartsAreThere(bold, regular);
							bold = "";
							regular = "";
						}
						if (tagName.equals("strong")) {
							bold = elem.text();
						}
					}

					constructIfRequiredPartsAreThere(bold, regular);
					bold = "";
					regular = "";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// XXX: Print everything
		// dataPoints.forEach(x -> System.out.println(x.toCsvFormat()));

		// XXX: Print just count
		System.out.println(dataPoints.size());
		writeToFile();
	}

	private static void writeToFile() {
		File outFile = new File(
				"J:\\Workspaces\\redditparser\\reddit-parser\\src\\main\\resources\\mysteries.csv");

		int dataWritten = 0;
		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			PrintWriter printWritter = new PrintWriter(outFile);
			for (MysteryDataObject data : dataPoints) {
				System.out.println("DataName: " + data.getBold());
				printWritter.println(data.toGooleSpreadsheet());
				dataWritten++;
				printWritter.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Wrote " + dataWritten + " data");
	}

	private static void markupParser(String markupText) {
		List<Integer> tripStarIndeces = new ArrayList<Integer>();
		if (!markupText.contains("Sharom"))
			return;
		int currIndex = markupText.indexOf("***");
		while (currIndex != -1) {
			System.out.println("Found *** @ " + currIndex);
			currIndex = markupText.indexOf("***");
		}
	}

	private static void constructIfRequiredPartsAreThere(String bold,
			String regular) {
		if (bold == null || bold.isEmpty())
			return;

		if (regular == null || regular.isEmpty())
			return;

		/*
		 * Excludes the header
		 */
		if (bold.contains("WELCOME"))
			return;

		MysteryDataObject sd = new MysteryDataObject(bold, regular);
		if (!dataPoints.contains(sd)) {
			dataPoints.add(sd);
		}
	}

	private static void printValues(String bold, String italic, String text) {
		System.out.println("Name: " + bold + "\t Sex-Race-Ocupation:" + italic);
		System.out.println("Description: " + text);
	}

}
