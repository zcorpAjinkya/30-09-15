
package com.zcorp.yielloh.model;

import java.util.Hashtable;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public class TocListDataManager
{
	Context ctx;
	Resources res;

	public TocListDataManager(Context context)
	{
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

	public String getHashValue(String key)
	{
		return atocindexHash.get(key);
	}

	public int getHashValueFrom(String key)
	{
		if (atocindexHash.get(key) != null)
		{
			return 1;
		}
		else if (ntocindexhash.get(key) != null)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

	int length;

	public int getGreatGrandpaLevels(int which_app)
	{
		length = mlHeader.length;
		return length;
	}

	public int getGrandpaLevels(int ggp, int which_app)
	{
		length = slHeader[ggp].length;
		return length;
	}

	public int getParentLevels(int ggp, int gp, int which_app)
	{
		length = tlHeader[ggp][gp].length;
		return length;
	}

	public int getChildLevels(int ggp, int gp, int p, int which_app)
	{
		length = flHeader[ggp][gp][p].length;
		return length;
	}

	public String getTherapieindexHash(String key)
	{
		return ttocindexHash.get(key);
	}

	String[] list;

	public String[] getFirstLevelList(int which_app)
	{
		list = mlHeader;
		return list;
	}

	public String[] getSecondLevelList(int ggp, int which_app)
	{
		list = slHeader[ggp];
		return list;
	}

	public String[] getThirdLevelList(int ggp, int gp, int which_app)
	{
		list = tlHeader[ggp][gp];
		return list;
	}

	public String[] getFourthLevelList(int ggp, int gp, int p, int which_app)
	{
		list = flHeader[ggp][gp][p];
		return list;
	}

	String header;

	public String getGreatGrandpaHeader(int gp, int which_app)
	{
		try
		{
			header = mlHeader[gp]; //
			return header;
		}
		catch (Exception ex)
		{
			header = mlHeader[gp];
			return header;
		}
	}

	public String getGrandpaHeader(int gp, int p, int which_app)
	{
		try
		{
			header = slHeader[gp][p];
			return header;
		}
		catch (Exception ex)
		{
			header = slHeader[gp][p];
			return header;
		}
	}

	public String getParentHeader(int gp, int p, int c, int which_app)
	{
		try
		{
			header = tlHeader[gp][p][c];
			return header;
		}
		catch (Exception ex)
		{
			header = tlHeader[gp][p][c];
			return header;
		}
	}

	public String getChildHeader(int ggp, int gp, int p, int c, int which_app)
	{
		try
		{
			header = flHeader[ggp][gp][p][c];
			return header;
		}
		catch (Exception ex)
		{
			header = flHeader[ggp][gp][p][c];//
			return header;
		}
	}

	public String getUltimateChildHeader(int ggp, int gp, int pp, int cp)
	{
		String header = null;
		if (getChildLevels(ggp, gp, pp, 0) == 0)
		{
			if (getParentLevels(ggp, gp, 0) == 0)
			{
				if (getGrandpaLevels(ggp, 0) == 0)
				{
					header = getGreatGrandpaHeader(ggp, 0);
				}
				else
				{
					header = getGrandpaHeader(ggp, gp, 0);
				}
			}
			else
			{
				header = getParentHeader(ggp, gp, pp, 0);
			}
		}
		else
		{
			header = getChildHeader(ggp, gp, pp, cp, 0);
		}
		return header;
	}

	public String ahtmlfileName;

	public String getTherapieChildHtmlFile(int ggp, int gp, int p, int c,
			int slevels, int tlevels, int flevels, int which_app)
	{
		try
		{
			ahtmlfileName = " file:///android_asset/content/";
			if (slevels == 0)
			{
				ahtmlfileName += String.valueOf(ggp + 1) + " ."
						+ String.valueOf(gp) + " ." + String.valueOf(p) + " ."
						+ String.valueOf(c) + " .html";
				// Toast.makeText(ctx, " rony" +ahtmlfileName,
				// Toast.LENGTH_SHORT).show();
			}
			else
			{
				if (tlevels == 0)
				{
					ahtmlfileName += String.valueOf(ggp + 1) + " ."
							+ String.valueOf(gp + 1) + " ." + String.valueOf(p)
							+ " ." + String.valueOf(c) + " .html";
				}
				else
				{
					if (flevels == 0)
					{
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
					}
					else
						ahtmlfileName += String.valueOf(ggp + 1) + " ."
								+ String.valueOf(gp) + " ." + String.valueOf(p)
								+ " ." + String.valueOf(c) + " .html";
				}
			}
			return (ahtmlfileName);
		}
		catch (Exception ex)
		{
			return (ahtmlfileName);
		}
	}

	private String[] mlHeader = new String[] {
			"Venenthrombose und Lungenembolie", "Embolien",
			"Extremit&auml;tenarterienverschluss",
			"Mesenterialarterienverschluss",
			"Management akutes Koronarsyndrom (STEMI/NSTEMI)",
			"Management Schlaganfall", "Kardiale Emboliequellen",
			"Medikamente", "Anhang" };

	private String[][] slHeader = new String[][] {
			{ "Definitionen/Fakten", "Diagnose einer tiefen Venenthrombose",
					"Diagnose einer Lungenembolie (LE)",
					"Risikogruppen, Wahrscheinlichkeiten",
					"Thromboembolie-Prophylaxe: allgemein",
					/* firstChapter */"Spezielle Prophylaxe-Empfehlungen",
					"Perioperatives Management", "Therapie bei Thromboembolie",
					"Therapie bei Lungenembolie",
					"Vorgehen bei Verdacht auf Thrombophilie",
					"Vorgehen bei Verdacht auf Neoplasien",
					"Sonderformen der Thrombosen" },
			{

			"Embolien",
			/* SecondChapter */"Risikofaktoren",
					"Grunds&auml;tzliche Therapiema&#223;nahmen bei arterieller Embolie" },
			{ "Quellenangaben", "Klinik", "Diagnostischer Algorithmus",
			/* thirdChapter */"Therapie", "Kompartment-/Reperfusionssyndrom",
					"Rezidivprophylaxe nach Revaskularisation",
					"Weitere Informationen" },
			{ "Allgemeines",
			/* fourth */"Diagnostik", "Therapie" },
			{ "Quellenangaben", "Definition und Diagnostik",
			/* fifth chapter */"Einteilung STEMI/NSTEMI",
					"Therapiealgorithmus STEMI", "Therapiealgorithmus NSTEMI",
					"Weiteres zu STEMI/NSTEMI" },
			{
					"Quellenangaben",
					"Vorgehen im zeitlichen Verlauf nach AWMF 030/046  ",
					/* sixthchapter */"Pr&auml;klinische Diagnostik und Monitoring ",
					"Pr&Auml;klinische Therapie", "Klinische Akutdiagnostik",
					"Klinische Therapie", "Stenosen der A. carotis",
					"Komplikationen", "Sekund&auml;rprophylaxe",
					"Weitere Informationen" },
			{ "Quellenangaben",
			/* seventhChapter */"Management von Vorhofflimmern",
					"Management Klappenvitien" },
			{ "Information", "Mechanismen der Pharmakotherapie",
			/* eightChapter */"Arzneimittel, die auf die Gerinnung einwirken",
					"Weitere Medikamente" }, { "Quellenverzeichnis",
			/* nineChapter */"Liste der Abk&uuml;rzungen" } };

	private String[][][] tlHeader = new String[][][] {
			{
					{},
					{ "Klinik und Wahrscheinlichkeit: Wells-Score",
							"Unsichere Zeichen der tiefen Venenthrombose",
							"Algorithmus bei Verdacht auf Venenthrombose" },
					{
							"Klinik und Wahrscheinlichkeit: Wells-Score",
							"Klinische Charakteristik: Geneva-Score",
							"Weitere Hinweise auf eine Lungenembolie",
							"Diagnostische Algorithmen bei Verdacht auf Lungenembolie",
							/* firstChapter */"Bildgebende Verfahren bei Verdacht auf Lungenembolie",
							"Risiko: Expositionell/dispositionell/individuell", },
					{
							"Dispositionelle Risikofaktoren nach relativer Bedeutung",
							"Expositionelle Risikofaktoren",
							"Abh&auml;ngigkeit der VTE-/LE-H&auml;ufigkeit von der Risikogruppe",
							"Beispielhafte Risikokategorien" },
					{
							"Grunds&auml;tzliches",
							"Klinische Wahrscheinlichkeit, m&ouml;gliche Prophylaxe und Risiko einer tiefen Venenthrombose",
							"Allgemeine Basisma&#223;nahmen ",
							"Physikalische Ma&#223;nahmen",
							"Medikament&ouml;se Ma&#223;nahmen",
							"Nebenwirkungen und Anwendungseinschr&auml;nkungen der medikament&ouml;sen Prophylaxe",
							"Beginn und Dauer der medikament&ouml;sen Prophylaxe" },
					{
							"Eingriffe im Kopf- und Halsbereich",
							"Neurochirurgische Eingriffe",
							"Herz-, thorax- und gef&auml;&#223;chirurgische Eingriffe",
							"Eingriffe im Bauch- oder Beckenbereich",
							"Operationen und Verletzungen der oberen Extremit&auml;t",
							"Operationen und Verletzungen der unteren Extremit&auml;t",
							"Operationen und Verletzungen an der Wirbels&auml;ule, Polytrauma, Verbrennungen",
							"Innere Medizin/Neurologie", "Schlaganfall",
							"Intensivmedizin",
							"Geburtshilfe und Gyn&auml;kologie",
							"P&auml;diatrie und Neonatologie", "Urologie",
							"Prophylaxe in der ambulanten Medizin",
							"Zusammenfassung: Prophylaxe in den speziellen Fachbereichen" },
					{ "Vorgehen bei Therapie mit Vitamin-K-Antagonisten",
							"Vorgehen bei Therapie mit neuen oralen Antikoagulanzien" },
					{ "Soforttherapie", "Therapeutisches Vorgehen bei TVT",
							"Dauer der Sekund&auml;rprophylaxe" },
					{ "Grunds&auml;tzliche Vorgehensweise",
							"Behandlung der Lungenembolie",
							"Heparindosierungsanpassung (f&uuml;r UFH)",
							"Kontraindikationen zur Fibrinolyse" },
					{ "Suche nach Thrombophilie bei folgenden Besonderheiten",
							"Laborparameter " }, {}, {} },
			{ {}, {},/* Second chapter */
			{} },

			{
					{},
					{
							"Schweres Isch&auml;miesyndrom: 6 P nach Pratt (pain, pallor, pulselessness, paresthesia, paralysis, prostation)",
							"Klinische Einteilung SVS/ISCVS-Klassifikation",
							"Klinische Untersuchung" },
					{},
					/* ThirdChapter */{ "&Uuml;bersicht",
							"Sofortma&#223;nahmen",
							"Revaskularisierungsma&#223;nahmen in Abh7auml;ngigkeit vom Stadium" },
					{},
					{ "Nach infrainguinalen Eingriffen" },
					{ "M&ouml;gliche Differenzialdiagnosen",
							"M&ouml;gliche Ursachen" } },

			/* fourth */
			{
					{},
					{
							"Diagnostischer Algorithmus",
							"Vorgehen nach Verschlusstyp/angiografischem Befund",
							"Vorgehen nach Klinik/peritonitischen Zeichen" },
					{ "Basisma&#223;nahmen", "Nach Angiografie", "Operation",
							"Vorgehen nach Darmbefund",
							"Second-Look-Operation",
							"Komplikation Reperfusionssyndrom",
							"Postoperatives Vorgehen" } },

			/* fifth */
			{
					{},
					{ "Definition", "EKG", "Labor", "Weitere Diagnostik",
							"Risiko bestimmen" },
					{ "Differenzierung der Formen des akuten Koronarsyndroms",
							"Entscheidungsalgorithmus beim akuten Koronarsyndrom" },
					{},
					{},
					{ "Ursachen/Risiko/Mortalit&auml;t/Komplikationen",
							"Medikament&ouml;se Therapie bei STEMI",
							"Medikament&ouml;se Therapie bei NSTEMI",
							"Begleittherapie/Sekund&auml;rpr&auml;vention",
							"Mortalit&auml;tsrisiko: GRACE-Risk-Score ",
							"CRUSADE-Blutungsscore" } },

			/* Sixth */
			{
					{},
					{},
					{ "Hauptsymptome des Schlaganfalls" },
					{},
					{ "&Uuml;bersicht ", "Ausschluss Stroke Mimics",
							"NIH Stroke Scale",
							"Weiterf&auml;hrende Diagnostik " },
					{ "Basisma&#223;nahmen  ",
							"Weiterf&uuml;hrende Therapie: Fibrinolyse" },
					{
							"Einteilung   ",
							" Therapie der ACI-Stenose  ",
							"Klassifikation des akuten Hirninfarkts nach den TOAST-Kriterien nach Adams 1993 " },
					{},
					{ "Allgemein/Gerinnungshemmung", "Rehabilitation", },
					{

					"Prim&auml;rpr&auml;vention",
							"Versorgungsgebiete und Segmenteinteilung der Hirnarterien  " } },

			/* seventh */
			{
					{},
					{
							"Definition, Epidemiologie, Pathophysiologie, &Aum;tiologie",
							"Klinik", "Diagnostik",
							"Thromboembolie (TE)- und Blutungsrisiko",
							"Therapie" },
					{ "Erworbene Vitien", "Angeborene Vitien", "Endokarditis" } },

			/* Eight */
			{
					{},
					{
							"Gerinnungskaskade und Hemmmechanismen modifiziert nach DGK 2012a",
							"Angriffspunkte der Thrombozytenaggregationshemmer nach Patrono 2008 und Fachinformationen" },
					{ "Unfraktioniertes Heparin (UFH)", "Heparinantidot",
							"Niedermolekulare Heparine (NMH)",
							"Indirekter Faktor-Xa-Inhibitor",
							"Direkte Faktor-Xa-Inhibitoren",
							"Direkte Thrombininhibitoren", "Heparinoide",
							"Cumarinderivate", "Fibrinolytika", "Protein C",
							"Antifibrinolytika",
							"Thrombozytenaggregationshemmer",
							"Durchblutungsf&auml;rdernde Mittel",
							"Gerinnungsfaktoren", "Thrombininhibitoren",
							"Enzyminhibitoren" },
					{ "Sympathomimetika", "Parasympatholytika", "ACE-Hemmer",
							"Angiotensin-II-Blocker (Sartane)", "Betablocker",
							"Kalziumantagonisten (Non-Dihydropyridine)",
							"Kalziumantagonisten (Dihydropyridine)",
							"Zentral angreifende Alpha-2-Rezeptoragonisten",
							"Alphablocker", "Direkte Vasodilatatoren",
							"Mineralokortikoid-Rezeptor-Antagonisten",
							"Nitrate", "Klasse-Ia-Antiarrhythmika",
							"Klasse-Ic-Antiarrhythmika",
							"Klasse-III-Antiarrhythmika", "Mehrkanalblocker",
							"Digitalisglykoside", "Vitamin K" } },

			/* nine */

			{ {}, {} } };

	private String[][][][] flHeader = new String[][][][] {
			{
					{ {} },
					{ {}, {}, {} },
					{ {}, {}, {}, {}, {}, {} },
					{ {}, {}, {}, {} },
					{
							{ "&Uuml;bersicht", "Klinisches Vorgehen" },
							{ "Thromboseprophylaxe in Schwangerschaft/Wochenbett" },
							{},
							{},
							{ "Heparine", "Faktor-Xa-Hemmer", "Thrombinhemmer",
									"Vitamin-K-Antagonisten", "Danaparoid" },
							{
									"Blutungskomplikationen",
									"Heparininduzierte Thrombozytopenie (HIT II)",
									"Osteoporose" },
							{ "Beginn", "Dauer",
									"Medikament&ouml;se Prophylaxe und r&uuml;ckenmarknahe An&auml;sthesie" } },
					{
							{},
							{},
							{ "Herz- und thoraxchirurgische Eingriffe",
									"Gef&auml;&#220;chirurgische Eingriffe" },
							{},
							{},
							{
									"H&uuml;ftgelenkendoprothetik und h&uuml;ftgelenknahe Frakturen und Osteotomien, Beckenfrakturen",
									"Kniegelenkendoprothetik und kniegelenknahe Frakturen und Osteotomien",
									"Immobilisation an der unteren Extremität und Eingriffe an Sprunggelenk oder Fu&#223;",
									"Arthroskopische Eingriffe an der unteren Extremität" },
							{ "Elektive Eingriffe an der Wirbels&auml;ule",
									"Wirbels&auml;ulenverletzungen",
									"Polytrauma", "Verbrennungen" },
							{ "Akute internistische Erkrankungen",
									"Maligne Erkrankungen (nichtoperative Behandlung)" },
							{},
							{},
							{
									"Antikoagulation w&auml;hrend und nach der Geburt",
									"Gyn&auml;kologische Eingriffe",
									"Hormonelle Kontrazeption und postmenopausale Hormontherapie" },
							{},
							{},
							{
									"Medikament&ouml;se Prophylaxe in der ambulanten Medizin",
									"Aufkl&auml;rung des Patienten" }, {} },
					{
							{
									"Definition &#8222;Bridging&#8220;",
									"M&ouml;gliche Medikamente",
									" Vorgehen",
									"Mindestanforderungen hinsichtlich der Laborkontrollen",
									"Empfehlungen zur Durchf&uuml;hrung" },
							{ "Vorgehen", "Gerinnungsdiagnostik unter NOAKs" } },

					{ {}, {}, {} },

					{
							{
									"Einteilung nach h&auml;modynamischer Stabilit&auml;t",
									"Therapieempfehlungen",
									"Weitere Risikostratifizierung durch Biomarker" },
							{}, {}, {} },

					{ {}, {} }, { {} }, { {} } },

			{ { {} }, { {} }, { {} } },

			{
					{ {} },
					{ {}, {}, {} },
					{ {} },
					{
							{},
							{},
							{
									"Therapie bei akuter Extremit&auml;tenischämie DGK 2012c",
									"Interventionelle Therapie",
									"Chirurgische Therapieans&auml;tze",
									"Fibrinolysetherapie" }, {}, {}, {} },
					{ {} },
					{ {} },
					{
							{},
							{ "Emboliequelle Herz", "Lokalisation",
									"Gewebesch&auml;digung" } } },

			{
					{ {} },
					{ {}, {}, {} },
					{
							{},
							{},
							{},
							{},
							{},
							{ "Therapie" },
							{
									"Normalisierung der Verdauungsfunktionen nach 4–6 Wochen",
									"Verlauf" } } },
			{
					{ {} },
					{ {}, {}, {}, {}, {} },
					{ {}, {} },
					{ {} },
					{ {} },
					{ {}, { "Fibrinolyse", "Antithrombotische Therapie" }, {},
							{}, {}, {} },

			},

			{
					{ {} },
					{ {} },
					{ {} },
					{ {} },
					{ {}, {}, {}, {} },
					{
							{},
							{
									"Durchf&uuml;hrung der Fibrinolyse",
									"&Uuml;berwachung bei systemischer Fibrinolyse",
									"Ein- und Ausschlusskriterien" },

					},

					{ {}, {}, {} },

					{ {} }, { {}, {}, {}, {}, {} }, { {}, {} } },

			{
					{ {} },
					{
							{},
							{},
							{},
							{ "&Uuml;bersicht ", "CHA2DS2-VASc-Score",
									" HAS-BLED-Bleeding-Risk-Score" },
							{
									"Ziele der Therapie",
									"Therapieans&auml;tze in Abh&auml;ngigkeit von der Form des VHF",
									"Akuttherapie",
									"Antithrombotische Therapie bei nicht valvul&auml;rem VHF",
									"Antiarrhythmische Therapie",
									" Frequenzkontrolle", "Rhythmuskontrolle",
									"Therapie in speziellen Fällen" } },
					{
							{
									"Therapie bei speziellen Herzklappenerkrankungen ESC 2012d ",
									"Mechanischer Klappenersatz: Ziel-INR " },
							{}, {} }, { {} }, { {} }, { {} } },
			{
					{ {} },
					{ {}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {} },
					{ {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
							{}, {}, {}, {} } }, { { {} }, { {} } } };
}