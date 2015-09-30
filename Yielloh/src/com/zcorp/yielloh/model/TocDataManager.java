package com.zcorp.yielloh.model;

import java.util.Hashtable;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public class TocDataManager {
	Context ctx;
	Resources res;

	public TocDataManager(Context context) {
		this.ctx = context;
		res = ctx.getResources();
		atocindexHash = new Hashtable<String, String>();
		new Hashtable<String, String>();
		ttocindexHash = new Hashtable<String, String>();
	}

	int apolen;
	boolean isapo;

	private Hashtable<String, String> atocindexHash, ntocindexhash,
			ttocindexHash;

	public String getHashValue(String key) {
		return atocindexHash.get(key);
	}

	public int getHashValueFrom(String key) {
		if (atocindexHash.get(key) != null) {
			return 1;
		} else if (ntocindexhash.get(key) != null) {
			return 0;
		} else {
			return -1;
		}
	}

	int length;

	public int getGreatGrandpaLevels(int which_app) {
		length = mlHeader.length;
		return length;
	}

	public int getGrandpaLevels(int ggp, int which_app) {
		length = slHeader[ggp].length;
		return length;
	}

	public int getParentLevels(int ggp, int gp, int which_app) {
		length = tlHeader[ggp][gp].length;
		return length;
	}

	public int getChildLevels(int ggp, int gp, int p, int which_app) {
		length = flHeader[ggp][gp][p].length;
		return length;
	}

	public String getTherapieindexHash(String key) {
		return ttocindexHash.get(key);
	}

	String[] list;

	public String[] getFirstLevelList(int which_app) {
		list = mlHeader;
		return list;
	}

	public String[] getSecondLevelList(int ggp, int which_app) {
		list = slHeader[ggp];
		return list;
	}

	public String[] getThirdLevelList(int ggp, int gp, int which_app) {
		list = tlHeader[ggp][gp];
		return list;
	}

	public String[] getFourthLevelList(int ggp, int gp, int p, int which_app) {
		list = flHeader[ggp][gp][p];
		return list;
	}

	String header;

	public String getGreatGrandpaHeader(int gp, int which_app) {
		try {
			header = mlHeader[gp]; //
			return header;
		} catch (Exception ex) {
			header = mlHeader[gp];
			return header;
		}
	}

	public String getGrandpaHeader(int gp, int p, int which_app) {
		try {
			header = slHeader[gp][p];
			return header;
		} catch (Exception ex) {
			header = slHeader[gp][p];
			return header;
		}
	}

	public String getParentHeader(int gp, int p, int c, int which_app) {
		try {
			header = tlHeader[gp][p][c];
			return header;
		} catch (Exception ex) {
			header = tlHeader[gp][p][c];
			return header;
		}
	}

	public String getChildHeader(int ggp, int gp, int p, int c, int which_app) {
		try {
			header = flHeader[ggp][gp][p][c];
			return header;
		} catch (Exception ex) {
			header = flHeader[ggp][gp][p][c];//
			return header;
		}
	}

	public String getUltimateChildHeader(int ggp, int gp, int pp, int cp) {
		String header = null;
		if (getChildLevels(ggp, gp, pp, 0) == 0) {
			if (getParentLevels(ggp, gp, 0) == 0) {
				if (getGrandpaLevels(ggp, 0) == 0) {
					header = getGreatGrandpaHeader(ggp, 0);
				} else {
					header = getGrandpaHeader(ggp, gp, 0);
				}
			} else {
				header = getParentHeader(ggp, gp, pp, 0);
			}
		} else {
			header = getChildHeader(ggp, gp, pp, cp, 0);
		}
		return header;
	}

	public String ahtmlfileName;

	public String getTherapieChildHtmlFile(int ggp, int gp, int p, int c,
			int slevels, int tlevels, int flevels, int which_app) {
		try {
			ahtmlfileName = " file:///android_asset/content/";
			if (slevels == 0) {
				ahtmlfileName += String.valueOf(ggp + 1) + " ."
						+ String.valueOf(gp) + " ." + String.valueOf(p) + " ."
						+ String.valueOf(c) + " .html";
				Toast.makeText(ctx, " rony" + ahtmlfileName, Toast.LENGTH_SHORT)
						.show();
			} else {
				if (tlevels == 0) {
					ahtmlfileName += String.valueOf(ggp + 1) + " ."
							+ String.valueOf(gp + 1) + " ." + String.valueOf(p)
							+ " ." + String.valueOf(c) + " .html";
				} else {
					if (flevels == 0) {
						if (c != -1)
							ahtmlfileName += String.valueOf(ggp + 1) + " ."
									+ String.valueOf(gp + 1) + " ."
									+ String.valueOf(p + 1) + " ."
									+ String.valueOf(c) + " .html";
						else
							ahtmlfileName += String.valueOf(ggp + 1) + " ."
									+ String.valueOf(gp + 1) + " ."
									+ String.valueOf(p + 1) + " ."
									+ String.valueOf(0) + " .html";
					} else
						ahtmlfileName += String.valueOf(ggp + 1) + " ."
								+ String.valueOf(gp + 1) + " ."
								+ String.valueOf(p + 1) + " ."
								+ String.valueOf(c + 1) + " .html";
				}
			}
			return (ahtmlfileName);
		} catch (Exception ex) {
			return (ahtmlfileName);
		}
	}

	private String[] mlHeader = new String[] { "Animals and Outdoors", "Arts",
			"Education and Business", "Fashions and Crafts ", "Health",
			"History and Culture ", "Home and Travel ", "Humor", "Literature ",
			"Music ", "News and Politics ", "Relationships and Family ",
			"Religion and Spirituality ", "Science and Mathematics ",
			"Sports and Games ", "TV & Movies ", "Technology " };

	private String[][] slHeader = new String[][] {
			{ "Animals", "Outdoors" },
			{ "Design", "Fine Arts", "Performing Arts" },
			{ "Business and Careers ", "Education", "Finance " },
			{ "Crafts ", "Fashion and Beauty " },
			{ "Genetics and Biology ", "Health Conditions ",
					"Health Solutions " },
			{ "Alternative Culture", "History", "World Culture" },
			{ "Food and Beverages ", "Home and Garden ", "Travel" },
			{ "Humor " },
			{ "Literature" },
			{ "Classical and World ", "Dance Music ", "Jazz and Blues  ",
					"Popular Music ", "Rock Musics ", "Songwriting " },

			{ "Activism and News  ", "Humanitarianism ", "Politics " },
			{ "Family  ", "Relationships  " },
			{ "Religion   ", "Spirituality  " },
			{ "Earth Sciences    ", "Energy   ", "Engineering", "Mathematics " },

			{ "Cars and Transportation", "Extreme Sports    ", "Games ",
					"Leisure Sports  ", "Sports" },
			{ "Animation", "Television and Movies " },
			{ "AI and Robotics ", "Development ", "Gadgets",
					"Information Technology ", "Internet" } };

	private String[][][] tlHeader = new String[][][] {
			{
					{ "Animals", "Bird Watching", "Birds", "Cats", "Dogs",
							"Exotic Pets", "Fish ", "Pets " },
					{ "Agriculture", "Camping", "Climbing", "Environment",
							"Forestry", "Hiking", "Nature", "Outdoors",
							"Scouting", "Survivalist" }, },
			{
					{ "Advertising", "Architecture", "C.A.D.",
							"Computer Graphics", "Design",
							"Desktop Publishing", "Graphic Design",
							"Industrial Design", "Photoshop" },
					{ "Art History", "Arts", "Drawing", "Fine Arts",
							"Painting", "Photography", "Postmodernism",
							"Sculpting" },
					{ "Ballet", "Dancing", "Live Theatre", "Musicals",
							"Performing Arts" } },

			{
					{ "Business", "Career planning", "Consumer Info",
							"Entrepreneurship", "Home Business",
							"Management/HR ", "Marketing" },
					{ "Continuing Education", "Education ", "Homeschooling",
							"Library Resources", "Research",
							"Self Improvement", "University/College" },
					{ "Accounting", "Banking", "Capitalism", "Daytrading",
							"Economics", "Financial planning", "Insurance",
							"Investing", "Mutual Funds", "Options/Futures",
							"Real Estate ", "Taxation" }, },

			/* fourth */
			{
					{ "Antiques", "Christmas", "Collecting", "Crafts ",
							"Crochet", "Dolls/Puppets", "Knitting",
							"Memorabilia", "Quilting ", "Scrapbooking",
							"Sewing ", "Woodworking" },
					{ "Bargains/Coupons ", "Beauty", "Clothing ", "Ecommerce ",
							"Fashion ", "Jewelry ", "Shopping " } },

			/* fifth */
			{
					{ "Anatomy", "Biomechanics ", "Biotech ",
							"Cognitive Science ", "Evolution ", "Genetics ",
							"Microbiology", "Neuroscience ", "Pharmacology ",
							"Physiology " },

					{ "AIDS ", "Aging ", "Arthritis", "Asthma ",
							"Brain Disorders", "Cancer ", "Diabetes",
							"Disabilities", "Eating Disorders", "Glaucoma ",
							"Heart Conditions", "Learning Disorders",
							"Substance Abuse ", },

					{ "Alternative Health ", "Antiaging ", "Dentistry ",
							"Doctors/Surgeons", "Ergonomics ", "Health ",
							"Kinesiology", "Medical Science", "Mental Health ",
							"Nursing ", "Physical Therapy", "Psychiatry ",
							"Psychology ", "Sexual Health", "Weight Loss " }, },

			/* Sixth */
			{
					{ "Alternative News", "Bisexual Culture ", "Conspiracies ",
							"Counterculture ", "Crime ", "Drugs ", "Forensics",
							"Gay Culture", "Goth Culture ", "Hedonism ",
							"Lefthanded ", "Lesbian Culture", "Paranormal ",
							"Subculture ", "Tattoos/Piercing" },
					{ "American History ", "Ancient History ", "Anthropology ",
							"Archaeology ", "Classical Studies", "Cold War ",
							"History ", "Medieval History ", "Philosophy " },
					{ " Africa ", "African Americans", "Asia ", "Australia",
							"Brazil ", "Canada ", "Caribbean",
							"Central America", "China ", "Culture/Ethnicity",
							"Eastern Studies ", "Europe ", "France ",
							"Geography", "Germany ", "India ", "Iraq ",
							"Ireland", "Israel ", "Italy ", "Japan ", "Korea ",
							"Linguistics", "Mexico ", "Middle East",
							"Native Americans", "Netherlands ", "New York ",
							"Oceania ", "Russia ", "South America", "	Spain ",
							"	Terrorism ", "	UK ", "	USA " }, },

			/* seventh */
			{
					{ "Alcoholic Drinks", "Beer ", "Beverages", "Cigars ",
							"Coffee ", "Food/Cooking", "Homebrewing ",
							"Nutrition ", "Restaurants ", "Tea ", "Vegetarian",
							"Wine" },
					{ "Botany ", "Construction", "Gardening",
							"Home Improvement", "Homemaking",
							"Interior Design", "Landscaping", "Restoration " },
					{ "Hotels ", "Luxury", "Nightlife", "Spas ", "Travel " } },

			/* Eight */
			{ { "Bizarre/Oddities ", "Cyberculture", "Humor ", "Satire " } },

			/* nine */

			{ { "American Lit. ", "Biographies", "Books ",
					"British Literature", "Children's Books ",
					"Fantasy Books ", "Humanities ", "Journalism ",
					"Literature ", "Mystery Novels", "Poetry ", "Quotes ",
					"Romance Novels", "Shakespeare ", "Writing ",
					"Open letters", "Letters", "Journals" } },

			{
					{ "Celtic Music", "Christian Music", "Classical Music ",
							"Ethnic Music", "Gospel music ", "Latin Music ",
							"Lounge Music ", "Opera ", "Vocal Music" },

					{ "Ambient Music ", "DJ's/Mixing", "Dance Music ",
							"Disco ", "Drum'n'Bass", "Electronica/IDM",
							"House music ", "Industrial Music",
							"Rave Culture ", "Techno ", "Trance ",
							"TripHop/Downtempo" },
					{ "Blues music", "Funk ", "Jazz ", "Oldies Music",
							"Reggae ", "Soul/R&B" },
					{ "Country music ", "HipHop/Rap", "Karaoke ", "Music ",
							"Pop music " },
					{ "Alternative Rock", "Britpop ", "Classic Rock ",
							"Folk music ", "Heavy metal ", "Indie Rock/Pop ",
							"Punk Rock ", "Rock music " },
					{ "Guitar", "Music Composition ", "Music Instruments ",
							"Music Theory", "Musician Resources ",
							"Percussion ", "Radio Broadcasts ", "Songwriting" } },

			{
					{ "Activism ", "Anarchism ", "Communism ", "Feminism ",
							"Law ", "Liberties/Rights ", "Military ",
							"News(General) ", "Socialism ", "Sociology" },

					{ "Ethics ", "Humanitarianism ", "Int'l Development ",
							"Nonprofit/Charity " },
					{ "Conservative Politics ", "Government ",
							"Liberal Politics ", "Political Science ",
							"Politics " } },

			{
					{ " Babies ", "Divorce ", "Entertaining Guests ",
							"Family ", "For Kids ", "Genealogy ", "Kids ",
							"Married Life ", "Parenting ", "Pregnancy/Birth",
							"Senior Citizens ", "Teen Life ", "Teen Parenting " },

					{ "Dating Tips ", "Matchmaking ", "Men's Issues",
							"Relationships", "Weddings ", "Women's Issues " } },

			{
					{ "Atheist/Agnostic ", "Buddhism ", "Catholic ",
							"Christianity ", "Hinduism ", "Islam ", "Judaism ",
							"Mormon ", "Orthodox ", "Paganism ", "Protestant ",
							"Religion ", "Scientology ", "Sufism ", "Sunni ",
							"Wicca" },

					{ "Astrology/Psychics ", "Mythology ", "New Age ",
							"Spirituality " } },

			{
					{ "Ecology ", "Geoscience ", "Marine Biology ",
							"Meteorology ", "Paleontology ", "Zoology " },

					{ "Alternative Energy ", "Energy Industry ",
							"Mining/Metallurgy ", "Nuclear Science",
							"Petroleum " },
					{ "Chemical Eng. ", "Civil Engineering ",
							"Electrical Eng.", "Machinery ", "Manufacturing ",
							"Mechanical Eng. " },
					{ "Chaos/Complexity ", "Logic ", "Mathematics ",
							"Statistics " },
					{ "Biology", "Chemistry ", "Physics", "Science" },
					{ "Astronomy ", "Space Exploration ", "UFOs" } },

			{
					{ "Car Parts ", "Cars ", "Motor Sports ", "Motorcycles ",
							"Trains/Railroads ", "Transportation ",
							"Vintage Cars ", },

					{ "Extreme Sports ", "Skateboarding", "Skiing ",
							"Skydiving ", "Snowboarding ", "Surfing ",
							"Water Sports ", "Windsurfing " },

					{ "Billiards ", "Board Games ", "Card Games", "Chess ",
							"Gambling ", "Magic/Illusions ", "Online Games ",
							"Poker ", "Puzzles ", "Quizzes ",
							"Roleplaying Games ", "Toys ", "Video Games " },

					{ "Boating ", "Bowling ", "Canoeing/Kayaking ", "Fishing ",
							"Flyfishing ", "Golf ", "Guns ", "Hunting ",
							"Sailing ", "Scuba Diving " },

					{ "American Football ", "Badminton ", "Baseball ",
							"Basketball ", "Bicycling ", "Bodybuilding ",
							"Boxing ", "Cheerleading ", "Cricket ",
							"Equestrian/Horses ", "Figure Skating ",
							"Fitness ", "Gymnastics ", "Hockey ",
							"Martial Arts ", "Racquetball ", "Rodeo ",
							"Rugby ", "Running ", "Soccer ",
							"Sports(General) ", "Squash ", "Swimming ",
							"Tennis ", "Track/Field ", "Volleyball ",
							"Wrestling ", "Yoga" }, },

			{
					{ "Animation ", "Anime ", "Cartoons ", "Comic Books ",
							"Multimedia" },

					{ "Acting ", "Action Movies ", "Babes ", "Celebrities ",
							"Classic Films ", "Comedy Movies ", "Cult Films ",
							"Drama Movies ", "Film Noir ", "Filmmaking ",
							"Foreign Films ", "Horror Movies",
							"Independent Film ", "Movies ", "Science Fiction",
							"Soap Operas ", "Soundtracks ", "Television " },

			},

			{
					{ " A.I. ", "Futurism ", "Nanotech ", "Robotics ",
							"Semiconductors ", "Supercomputing",
							"Virtual Reality " },

					{ "Computer Science ", "Databases ", "Embedded Systems ",
							"Encryption ", "Hacking ", "Java ", "Open Source",
							"PHP ", "Perl ", "Programming ", "Shareware ",
							"Software ", "Web Development", "Windows Dev" },

					{ "Amateur Radio ", "Audio Equipment ", "Cell Phones ",
							"Electronic Devices ", "Electronic Parts ",
							"Gadgets ", "Ipod ", "Mobile Computing ",
							"Peripheral Devices ", "Photo Gear ",
							"Recording Gear ", "Technology ", "Telecom ",
							"Video Equipment " },

					{ "Computer Hardware ", "Computer Security", "Computers",
							"IT ", "Linux/Unix", "MacOS ", "Network Security ",
							"Operating Systems", "Proxy ", "Windows" },

					{ "Facebook", "Firefox ", "Forums ", "Instant Messaging ",
							"Internet ", "Internet Tools ", "P2P ",
							"Personal Sites", "SEO ", "Search ", "facebook",
							"twitter", "Webhosting ", "Weblogs" }

			}

	};

	private String[][][][] flHeader = new String[][][][] {
			{ { {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {} }, },

			{ { {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {} }, { {}, {}, {}, {}, {} } },

			{ { {}, {}, {}, {}, {}, {}, {} }, { {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} } },

			{ { {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {} }, },
			{
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{} } },

			{
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{} },

					{ {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {}, {}, {}, {}, {}, {}, {} }, },

			{ { {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {} }, { {}, {}, {}, {}, {} } },

			{ { {}, {}, {}, {} }, },

			{ { {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
					{}, {} }, },
			{ { {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {} }, { {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {} } },

			{ { {}, {}, {}, {}, {}, {}, {}, {}, {}, {} }, { {}, {}, {}, {} },
					{ {}, {}, {}, {}, {} } },

			{
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {} }, { {}, {}, {}, {}, {}, {} } },

			{
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {} }, { {}, {}, {}, {}, {}, {} } },

			{ { {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {} }, { {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {} }, { {}, {}, {}, {} }, { {}, {}, {} } },

			{
					{ {}, {}, {}, {}, {}, {}, {} },

					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },

					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },

					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{} } },

			{
					{ {}, {}, {}, {}, {} },

					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {}, {}, {} } },

			{
					{ {}, {}, {}, {}, {}, {}, {} },

					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} } },

			{ { {}, {}, {}, {}, {}, {}, {} },

			{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} } }

	};
}