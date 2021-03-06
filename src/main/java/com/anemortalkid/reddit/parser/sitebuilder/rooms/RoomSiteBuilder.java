package com.anemortalkid.reddit.parser.sitebuilder.rooms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.anemortalkid.reddit.parser.sitebuilder.BaseSiteBuilderHelper;
import com.anemortalkid.reddit.parser.sitebuilder.ISiteBuilder;
import com.anemortalkid.reddit.scrubber.StrongParagraphEntryScrubber;
import com.anemortalkid.reddit.scrubber.dataobject.ScrubbedDataObject;

public class RoomSiteBuilder implements ISiteBuilder {

	private static String tableHeaderHTML = "<tr><th align=\"center\">Room Name</th><th align=\"center\">Room Description</th></tr>";

	private static String[] urls = {
			"https://www.reddit.com/r/DnDBehindTheScreen/comments/4l55a9/addition_to_10k_things_project_lets_make_10000/" };

	private static String[] ignoreHeaders = {
			"It takes a moment for your eyes to adjust to the flickering torchlight, the chamber walls are not at all what you had been expecting", };
	private StrongParagraphEntryScrubber scrubber;
	private List<ScrubbedDataObject> data;

	public RoomSiteBuilder() {
		Set<String> ignores = new HashSet<String>();
		for (String phrase : ignoreHeaders) {
			ignores.add(phrase);
		}
		scrubber = new StrongParagraphEntryScrubber(ignores, urls);
	}

	@Override
	public String getTitle() {
		return "Rooms";
	}

	@Override
	public String getRedditURL() {
		return urls[urls.length - 1];
	}

	@Override
	public String getTableHeader() {
		return tableHeaderHTML;
	}

	@Override
	public List<ScrubbedDataObject> getScrubbedData() {
		return data;
	}

	public static void main(String[] args) {
		RoomSiteBuilder builder = new RoomSiteBuilder();
		builder.buildSite();
	}

	@Override
	public void scrubData() {
		data = scrubber.scrubData();
	}

	@Override
	public int scrubbedCount() {
		return data == null ? -1 : data.size();
	}

}
