package xp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class ResultsCollector {

	// public enum IconTypes {
	public enum TopoClass {
		DC1, EC1, PO1, TPP1, NTPP, TPP2, PO2, EC2, DC2, NONE;
	}

	TopoClass findClassFromName(String name) {
		if (name.equals("DC1")) {
			return TopoClass.DC1;
		} else if (name.equals("EC1")) {
			return TopoClass.EC1;
		} else if (name.equals("PO1")) {
			return TopoClass.PO1;
		} else if (name.equals("TPP1")) {
			return TopoClass.TPP1;
		} else if (name.equals("NTPP")) {
			return TopoClass.NTPP;
		} else if (name.equals("PP")) {
			return TopoClass.NTPP;
		} else if (name.equals("PP1")) {
			return TopoClass.NTPP;
		} else if (name.equals("TPP2")) {

			return TopoClass.TPP2;
		} else if (name.equals("PO2")) {
			return TopoClass.PO2;
		} else if (name.equals("EC2")) {
			return TopoClass.EC2;
		} else if (name.equals("DC2")) {
			return TopoClass.DC2;
		}

		else {
			logger.severe("unkown topo name: " + name);
			System.exit(0);

		}
		return null;
	}

	TopoClass findClassFromCode(String code) {
		if (code.equals("A")) {
			return TopoClass.DC1;
		} else if (code.equals("B")) {
			return TopoClass.EC1;
		} else if (code.equals("C")) {
			return TopoClass.PO1;
		} else if (code.equals("D")) {
			return TopoClass.TPP1;
		} else if (code.equals("E")) {
			return TopoClass.NTPP;
		} else if (code.equals("F")) {

			return TopoClass.TPP2;
		} else if (code.equals("G")) {
			return TopoClass.PO2;
		} else if (code.equals("H")) {
			return TopoClass.EC2;
		} else if (code.equals("I")) {
			return TopoClass.DC2;
		} else {
			logger.severe("unkown topo code: " + code);
			System.exit(0);

		}
		return null;
	}

	String findCode(TopoClass tClass) {
		if (tClass.equals(TopoClass.DC1)) {
			return "A";
		} else if (tClass.equals(TopoClass.EC1)) {
			return "B";
		} else if (tClass.equals(TopoClass.PO1)) {
			return "C";
		} else if (tClass.equals(TopoClass.TPP1)) {
			return "D";
		} else if (tClass.equals(TopoClass.NTPP)) {
			return "E";
		} else if (tClass.equals(TopoClass.TPP2)) {

			return "F";
		} else if (tClass.equals(TopoClass.PO2)) {
			return "G";
		} else if (tClass.equals(TopoClass.EC2)) {
			return "H";
		} else if (tClass.equals(TopoClass.DC2)) {
			return "I";
		} else {
			logger.severe("unkown topo tClass: " + tClass);
			System.exit(0);

		}

		return null;
	}

	final static Logger logger = Logger.getLogger(ResultsCollector.class
			.getName());

	HashMap<Integer, ClusterType> iconClusterType = new HashMap<Integer, ClusterType>();
	HashMap<Integer, String> iconStrength = new HashMap<Integer, String>();
	HashMap<Integer, String> iconColorProportion = new HashMap<Integer, String>();
	HashMap<Integer, String> iconThreeCluster = new HashMap<Integer, String>();
	HashMap<Integer, String> clusterNames = new HashMap<Integer, String>();;

	HashMap<ClusterType, Integer> countsOverall = new HashMap<ClusterType, Integer>();

	HashMap<ClusterType, Integer> correctCounts001 = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> correctCounts01 = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> correctCounts05 = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> correctCountsRandom = new HashMap<ClusterType, Integer>();

	HashMap<ClusterType, Integer> totalCounts001 = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> totalCounts01 = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> totalCounts05 = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> totalCountsRandom = new HashMap<ClusterType, Integer>();

	HashMap<ClusterType, Integer> counts60Green = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> counts50Green = new HashMap<ClusterType, Integer>();
	HashMap<ClusterType, Integer> counts40Green = new HashMap<ClusterType, Integer>();

	HashMap<ClusterType, Integer> totalCounts40Green = new HashMap<ClusterType, Integer>();

	HashMap<Integer, Icon> iconInfo = new HashMap<Integer, Icon>();

	HashMap<Integer, Participant> participantInfo = new HashMap<Integer, Participant>();

	HashMap<String, List<Placement>> placements = new HashMap<String, List<Placement>>();
	int nExperiments = 0;

	private class Placement implements Comparable {
		String icon;
		ResultsCollector.TopoClass topoClass;
		double timing;// in seconds
		long timestamp;
		int group;
		int order;
		int orderInGroup;

		@Override
		public int compareTo(Object otherObject) {
			Placement otherPlace = (Placement) otherObject;
			if (timestamp < otherPlace.timestamp) {
				return -1;
			} else if (timestamp > otherPlace.timestamp) {
				return 1;
			}
			return 0;
		}

		public String toString() {
			return icon + ", ts = " + timestamp + ", timing = " + timing
					+ ", group = " + group + ", topoClass = " + topoClass;
		}
	}

	// Pattern intsOnly = Pattern.compile("\\d+");
	private String placementListToTopoString(List<Placement> pList) {

		StringBuilder topoString = new StringBuilder();

		for (Placement place : pList) {
			String code = this.findCode(place.topoClass);
			topoString.append(code);
		}

		return topoString.toString();
	}

	private TopoClass findTopoClass(String fileName) {
		// logger.info("going to match " + fileName);
		// find first character that is numeric
		// Matcher intMatch = intsOnly.matcher(fileName);
		// String theInt = intMatch.group();
		// int place = fileName.indexOf(theInt);
		int place = this.findFirstPlace(fileName);
		if (place < 0) {
			return TopoClass.NONE;
		}
		String theGroup = fileName.substring(0, place);
		logger.info("theGroup is :" + theGroup);
		if (theGroup.contains("Desert-")) {
			theGroup = theGroup.substring("Desert-".length());

		}
		if (theGroup.contains("Geometry-")) {
			theGroup = theGroup.substring("Geometry-".length());

		}
		if (theGroup.contains("Lake-")) {
			theGroup = theGroup.substring("Lake-".length());

		}
		if (theGroup.contains("Oil-")) {
			theGroup = theGroup.substring("Oil-".length());

		}

		TopoClass tClass = this.findClassFromName(theGroup);
		return tClass;

	}

	private static char getUnderscore() {
		String under = "_";
		char[] chars = under.toCharArray();
		return chars[0];
	}

	static char underscore = getUnderscore();

	private int findFirstPlace(String input) {
		int answer = 0;
		char[] chars = input.toCharArray();

		for (int i = 0; i < input.length(); i++) {
			if (chars[i] == underscore) {
				return i;

			}
		}
		return -1;
	}

	private class Participant {

		ArrayList<Icon> icons;
		int[] counts;

		void bumpCount(Icon type) {
			int position = icons.indexOf(type);
			counts[position]++;

		}

	}

	private class Icon {

		ClusterType type;
		ClusterStrength strength;
		ClusterProportion proportion;
		int count;

		String getDescription() {
			return type + "--" + strength + "--" + proportion;
		}

		boolean isSameType(Icon ic) {
			if (ic.type == type && ic.strength == strength
					&& ic.proportion == proportion) {
				return true;
			}

			return false;
		}

	}

	private class ConfusionPair {
		ClusterType asserted;
		ClusterType actual;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((actual == null) ? 0 : actual.hashCode());
			result = prime * result
					+ ((asserted == null) ? 0 : asserted.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ConfusionPair other = (ConfusionPair) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (actual == null) {
				if (other.actual != null) {
					return false;
				}
			} else if (!actual.equals(other.actual)) {
				return false;
			}
			if (asserted == null) {
				if (other.asserted != null) {
					return false;
				}
			} else if (!asserted.equals(other.asserted)) {
				return false;
			}
			return true;
		}

		private ResultsCollector getOuterType() {
			return ResultsCollector.this;
		}

	}

	private final HashMap<ConfusionPair, Integer> confusionIndexesAll = new HashMap<ConfusionPair, Integer>();
	private final HashMap<ConfusionPair, Integer> confusionIndexGreen40 = new HashMap<ConfusionPair, Integer>();
	private final HashMap<ConfusionPair, Integer> confusionIndexGreen50 = new HashMap<ConfusionPair, Integer>();
	private final HashMap<ConfusionPair, Integer> confusionIndexGreen60 = new HashMap<ConfusionPair, Integer>();

	private static void printConfusionIndexesAssertedFirst(
			HashMap<ConfusionPair, Integer> confusionIndexes, String kind) {
		System.out.println(kind + "Asserted," + kind + "Actual," + kind
				+ "Count");
		Set<ConfusionPair> cpSet = confusionIndexes.keySet();
		Object[] cpArray = cpSet.toArray();
		// Arrays.sort(cpArray);
		for (Object obj : cpArray) {
			ConfusionPair cp = (ConfusionPair) obj;
			System.out.println(cp.asserted + "," + cp.actual + ","
					+ confusionIndexes.get(cp));

		}
	}

	private static void printConfusionIndexesActualFirst(
			HashMap<ConfusionPair, Integer> confusionIndexes, String kind) {
		System.out.println(kind + "Actual," + kind + "Asserted," + kind
				+ "Count");
		Set<ConfusionPair> cpSet = confusionIndexes.keySet();
		Object[] cpArray = cpSet.toArray();
		// Arrays.sort(cpArray);
		for (Object obj : cpArray) {
			ConfusionPair cp = (ConfusionPair) obj;
			System.out.println(cp.actual + "," + cp.asserted + ","
					+ confusionIndexes.get(cp));

		}
	}

	private void incrementConfusionIndexes(
			HashMap<ConfusionPair, Integer> confusionIndexes,
			ClusterType asserted, ClusterType actual) {
		ConfusionPair cp = new ConfusionPair();
		cp.asserted = asserted;
		cp.actual = actual;
		Integer count = confusionIndexes.get(cp);
		if (count == null) {
			count = 1;
		} else {
			count = count + 1;
		}
		confusionIndexes.put(cp, count);

	}

	private enum ClusterType {
		BLUE, GREEN, BLUEGREEN, DISPERSED, RANDOM, CLUSTERED, ALL_TYPES
	}

	private enum ClusterStrength {
		POINT_ONE, POINT_ZERO_FIVE, POINT_ZERO_ONE, ZERO, ALL_STRENGTHS
	}

	private enum ClusterProportion {
		GREEN_40, GREEN_50, GREEN_60, ALL_PROPORTIONS
	}

	static ArrayList<Icon> getIconTypes() {
		ResultsCollector col = new ResultsCollector();
		ClusterType[] clusterTypes = ClusterType.values();
		ClusterStrength[] strengths = ClusterStrength.values();
		ClusterProportion[] props = ClusterProportion.values();
		ArrayList<Icon> iconTypes = new ArrayList<Icon>();
		for (ClusterType clusterType : clusterTypes) {
			for (ClusterStrength strength : strengths) {
				for (ClusterProportion prop : props) {
					Icon ic = col.new Icon();
					ic.type = clusterType;
					ic.strength = strength;
					ic.proportion = prop;
					iconTypes.add(ic);
				}
			}
		}

		return iconTypes;

	}

	public ResultsCollector() {
		clusterNames.put(0, "Blue");
		clusterNames.put(1, "Green");
		clusterNames.put(2, "BlueGreen");
		clusterNames.put(3, "Dispersed");
		clusterNames.put(4, "Random");

		initCounter(countsOverall);

		initCounter(correctCounts001);
		initCounter(correctCounts01);
		initCounter(correctCounts05);
		initCounter(correctCountsRandom);

		initCounter(totalCounts001);
		initCounter(totalCounts01);
		initCounter(totalCounts05);
		initCounter(totalCountsRandom);

		initCounter(counts60Green);
		initCounter(counts50Green);
		initCounter(counts40Green);

		initCounter(totalCounts40Green);

	}

	private void initCounter(HashMap<ClusterType, Integer> counter) {
		counter.put(ClusterType.BLUE, 0);
		counter.put(ClusterType.GREEN, 0);
		counter.put(ClusterType.BLUEGREEN, 0);
		counter.put(ClusterType.DISPERSED, 0);
		counter.put(ClusterType.RANDOM, 0);
		counter.put(ClusterType.CLUSTERED, 0);
	}

	private void readFileDetails(File detailsFile) {
		Scanner scan = null;
		try {
			scan = new Scanner(detailsFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String line = "";
		while (scan.hasNext()) {
			line = scan.next();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(",");
			Integer iconNumber = Integer.valueOf(lineScanner.next());

			// logger.finest("num " + iconNumber);
			String name = lineScanner.next();
			// logger.finest(name);
			String clusterName = lineScanner.next();
			ClusterType type = findClusterType(clusterName);
			iconClusterType.put(iconNumber, type);

			String strength = lineScanner.next();
			iconStrength.put(iconNumber, strength);

			String colorProp = lineScanner.next();
			iconColorProportion.put(iconNumber, colorProp);

			String threeCluster = lineScanner.next();
			iconThreeCluster.put(iconNumber, threeCluster);
			// logger.finest(threeCluster);

		}

	}

	private String collectDifferentSizeResults(String dir) {
		String delimiter = "\t";
		String condition = dir;
		File baseDir = new File(dir);
		if (baseDir.isDirectory() == false) {
			return "Need to pass in a directory";
		}

		File[] files = baseDir.listFiles();
		HashMap<String, File> fileHash = new HashMap<String, File>();
		for (File f : files) {
			fileHash.put(f.getName(), f);
		}

		File participantsResultsFile = new File(baseDir
				+ "/results_summary.prn");
		File firstIconFile = new File(baseDir + "/times_first.csv");
		File iconRemovalsFile = new File(baseDir + "/icon_removals.csv");

		FileWriter participantsResultsWriter = null;
		FileWriter firstIconWriter = null;
		FileWriter iconRemovalsWriter = null;

		try {
			participantsResultsWriter = new FileWriter(participantsResultsFile);
			firstIconWriter = new FileWriter(firstIconFile);
			iconRemovalsWriter = new FileWriter(iconRemovalsFile);

		} catch (IOException e1) {

			e1.printStackTrace();
		}
		String firstLine = "Nr" + delimiter + "File-Nr" + delimiter
				+ "Gender (0=male)" + delimiter + "age" + delimiter
				+ "nr. Groups" + delimiter + "time to group" + delimiter
				+ "subject group descriptions" + "\n";

		String returnString = "";
		ArrayList<Icon> participantCounts = getIconTypes();
		for (Icon ic : participantCounts) {
			returnString = returnString + ic.getDescription() + ",";
		}
		returnString = returnString + "\n";

		try {
			File FASTAFile = new File(baseDir + "/FASTA.txt");
			FileWriter FASTAWriter = new FileWriter(FASTAFile);
			participantsResultsWriter.write(firstLine);
			firstLine = "Icon Name" + delimiter + "Times first in group" + "\n";
			firstIconWriter.write(firstLine);
			iconRemovalsWriter.write("Icon Name" + "," + "Removals" + "\n");

			int nr = 1;
			HashMap<String, Integer> removalCounts = new HashMap<String, Integer>();
			HashMap<String, Integer> firstIconCounts = new HashMap<String, Integer>();
			HashMap<String, Float> iconPlaces = new HashMap<String, Float>();
			HashMap<String, Float> iconTimings = new HashMap<String, Float>();
			for (File f : files) {
				// logger.finest(f.getName());
				String name = f.getName();
				// if (name.contains("participant") && name.contains("csv")) {
				// Scanner scan = new Scanner(f);
				//
				// while (scan.hasNext()) {
				// System.out.println(scan.nextLine());
				// }
				// }
				boolean thisToo = true;
				if (thisToo && name.contains("participant")
						&& name.contains(".csv")) {

					String line = parseParticipantFile(delimiter, baseDir, nr,
							f);
					nr++;

					participantsResultsWriter.write(line);

				}

				if (name.contains("participant") && name.contains(".log")) {
					findRemovals(removalCounts, f);

					findFirstIcon(firstIconCounts, f);
					List<Placement> filePlaces = findPlacesAndTimings(f);
					this.nExperiments++;
					this.placements.put(String.valueOf(nExperiments),
							filePlaces);
					String topoList = this
							.placementListToTopoString(filePlaces);
					logger.info(topoList);
					FASTAWriter.write(">" + name + "\n");
					FASTAWriter.write(topoList + "\n");

				}
			}// next file
			for (String name : removalCounts.keySet()) {
				Integer count = removalCounts.get(name);
				iconRemovalsWriter.write(name + "," + count + "\n");
			}
			for (String name : firstIconCounts.keySet()) {
				Integer count = firstIconCounts.get(name);
				firstIconWriter.write(name + "," + count + "\n");
			}

			FASTAWriter.close();
			participantsResultsWriter.close();
			iconRemovalsWriter.close();
			firstIconWriter.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
		this.compileAndWritePlacements(baseDir.getAbsolutePath());
		return "";
	}

	private void compileAndWritePlacements(String baseDir) {

		// XXX add timing code here
		HashMap<String, Double> timeToChose = new HashMap<String, Double>();
		HashMap<String, Double> overallOrder = new HashMap<String, Double>();
		HashMap<String, Double> orderInGroup = new HashMap<String, Double>();
		HashMap<ResultsCollector.TopoClass, Double> timeToChoseTopo = new HashMap<ResultsCollector.TopoClass, Double>();
		HashMap<ResultsCollector.TopoClass, Double> overallOrderTopo = new HashMap<ResultsCollector.TopoClass, Double>();
		HashMap<ResultsCollector.TopoClass, Double> orderInGroupTopo = new HashMap<ResultsCollector.TopoClass, Double>();

		for (List<Placement> placeList : this.placements.values()) {

			for (Placement place : placeList) {
				if (timeToChose.containsKey(place.icon) == false) {
					timeToChose.put(place.icon, place.timing);

				} else {
					Double totalTime = timeToChose.get(place.icon);
					totalTime = totalTime + place.timing;
					timeToChose.put(place.icon, totalTime);
					logger.info("timing " + place.timing);
				}
				if (timeToChoseTopo.containsKey(place.topoClass) == false) {
					timeToChoseTopo.put(place.topoClass, place.timing);

				} else {
					Double totalTime = timeToChoseTopo.get(place.topoClass);
					totalTime = totalTime + place.timing;
					timeToChoseTopo.put(place.topoClass, totalTime);
					logger.info("topo timing " + place.timing);
				}

				if (overallOrder.containsKey(place.icon) == false) {
					overallOrder.put(place.icon, (double) place.order);
				} else {
					Double totalPlace = overallOrder.get(place.icon);
					totalPlace = totalPlace + place.order;
					overallOrder.put(place.icon, totalPlace);
					logger.info("order " + place.order);
				}

				if (overallOrderTopo.containsKey(place.topoClass) == false) {
					overallOrderTopo.put(place.topoClass, (double) place.order);
				} else {
					Double totalPlace = overallOrderTopo.get(place.topoClass);
					totalPlace = totalPlace + place.order;
					overallOrderTopo.put(place.topoClass, totalPlace);
					logger.info("topo order " + place.order);
				}

			}
		}

		for (String icon : timeToChose.keySet()) {
			Double total = timeToChose.get(icon);
			Double average = total / this.placements.size();
			timeToChose.put(icon, average);
		}
		for (String icon : overallOrder.keySet()) {
			Double total = overallOrder.get(icon);
			Double average = total / this.placements.size();
			overallOrder.put(icon, average);
		}

		for (TopoClass tClass : timeToChoseTopo.keySet()) {
			Double total = timeToChoseTopo.get(tClass);
			Double average = total / (this.placements.size() * 8);
			timeToChoseTopo.put(tClass, average);
		}
		for (TopoClass tClass : overallOrderTopo.keySet()) {
			Double total = overallOrderTopo.get(tClass);
			Double average = total / (this.placements.size() * 8);
			overallOrderTopo.put(tClass, average);
		}

		File timeToChoseFile = new File(baseDir + "/timeToChose.csv");
		File overallOrderFile = new File(baseDir + "/overall_order.csv");

		File timeToChoseFileTopo = new File(baseDir
				+ "/timeToChoseTopoClass.csv");
		File overallOrderFileTopo = new File(baseDir
				+ "/overall_orderTopoClass.csv");

		FileWriter timeToChoseWriter = null;
		FileWriter overallOrderWriter = null;

		FileWriter timeToChoseWriterTopo = null;
		FileWriter overallOrderWriterTopo = null;

		try {
			timeToChoseWriter = new FileWriter(timeToChoseFile);
			overallOrderWriter = new FileWriter(overallOrderFile);

			timeToChoseWriterTopo = new FileWriter(timeToChoseFileTopo);
			overallOrderWriterTopo = new FileWriter(overallOrderFileTopo);

			for (String icon : timeToChose.keySet()) {
				timeToChoseWriter.write(icon + "," + timeToChose.get(icon)
						+ "\n");
			}
			for (String icon : overallOrder.keySet()) {
				overallOrderWriter.write(icon + "," + overallOrder.get(icon)
						+ "\n");

			}

			for (TopoClass tClass : timeToChoseTopo.keySet()) {
				timeToChoseWriterTopo.write(tClass + ","
						+ timeToChoseTopo.get(tClass) + "\n");
			}
			for (TopoClass tClass : overallOrderTopo.keySet()) {
				overallOrderWriterTopo.write(tClass + ","
						+ overallOrderTopo.get(tClass) + "\n");

			}

			timeToChoseWriter.close();
			overallOrderWriter.close();

			timeToChoseWriterTopo.close();
			overallOrderWriterTopo.close();

		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	private String parseParticipantFile(String delimiter, File baseDir, int nr,
			File f) throws FileNotFoundException {
		String line;
		Scanner scan = new Scanner(f);
		scan.useDelimiter(",");
		String fileNum = scan.next();
		String age = scan.next();
		String gender = scan.next();
		if (gender.equals("female")) {
			gender = "1";
		} else {
			gender = "0";
		}
		File assignFile = new File(baseDir + "/" + fileNum + "assignment.csv");
		logger.info(assignFile.toString());

		// String correctResults = "ble";
		int nGroups = findNGroups(assignFile);
		// int nGroups = 0;
		File participantFile = new File(baseDir + "/" + "participant" + fileNum
				+ ".log");

		double time = findTime(participantFile);
		File batchFile = new File(baseDir + "/" + fileNum + "batch.csv");

		String subjectComments = findCommentsForSubject(batchFile);

		line = nr + delimiter + fileNum + delimiter + gender + delimiter + age
				+ delimiter + nGroups + delimiter + time + delimiter
				+ subjectComments + "\n";
		return line;
	}

	private void findRemovals(HashMap<String, Integer> removalCounts, File f)
			throws FileNotFoundException {

		Scanner fileScan = new Scanner(f);
		while (fileScan.hasNext()) {
			String line = fileScan.nextLine();

			if (line.contains("Remove")) {
				String[] subStrings = line.split(" ");
				String iconName = findIconName(subStrings);
				Integer count = removalCounts.get(iconName);
				if (count == null) {
					count = 1;
				} else {
					count = count + 1;
				}
				removalCounts.put(iconName, count);
			}
		}
		fileScan.close();

	}

	@SuppressWarnings("unchecked")
	private List<Placement> findPlacesAndTimings(File f)
			throws FileNotFoundException {
		int currPlace;
		Scanner fileScan = new Scanner(f);

		long currTime = 0l;
		HashMap<String, Placement> filePlacements = new HashMap<String, Placement>();
		while (fileScan.hasNext()) {

			String line = fileScan.nextLine();
			String[] subStrings = line.split(" ");
			if (line.contains("Started at")) {
				if (currTime > 0) {
					logger.severe("hit non-zero start time");
				}
				currTime = Long.valueOf(subStrings[subStrings.length - 1]);

			}

			if (line.contains("Add")) {

				String iconName = findIconName(subStrings);
				Integer groupNum = null;
				try {
					groupNum = this.findGroupNumber(subStrings);
				} catch (NumberFormatException e) {
					logger.info("problem with " + f);
					e.printStackTrace();

					break;
				}
				long newTime = Long.valueOf(subStrings[subStrings.length - 1]);

				Placement place = new Placement();
				place.group = groupNum;
				place.icon = iconName;
				place.timestamp = newTime;
				place.timing = (newTime - currTime) / 1000d;
				place.topoClass = this.findTopoClass(iconName);
				currTime = newTime;
				filePlacements.put(iconName, place);
				logger.info(place.toString());

			}
		}

		fileScan.close();

		List<Placement> places = new ArrayList<Placement>(
				filePlacements.values());
		Collections.sort(places);
		for (int i = 0; i < places.size(); i++) {
			Placement place = places.get(i);
			place.order = i;
		}

		findOrderInGroup(places);

		return places;
	}

	private void findOrderInGroup(List<Placement> places) {

		HashMap<Integer, ArrayList<Placement>> orderedPlacements = new HashMap<Integer, ArrayList<Placement>>();
		// places must be pre-sorted
		for (Placement place : places) {
			int group = place.group;
			int order = place.order;
			ArrayList<Placement> placeList = orderedPlacements.get(group);
			if (placeList == null) {
				placeList = new ArrayList<Placement>();
				orderedPlacements.put(group, placeList);
			}

			placeList.add(place);

		}

		for (ArrayList<Placement> groupList : orderedPlacements.values()) {

			for (int i = 0; i < groupList.size(); i++) {
				Placement place = groupList.get(i);
				place.orderInGroup = i + 1;// count from 1, not 0
				i++;
			}
		}

	}

	private void findFirstIcon(HashMap<String, Integer> firstIconCounts, File f)
			throws FileNotFoundException {

		Scanner fileScan = new Scanner(f);
		HashMap<Integer, String> firstIcons = new HashMap<Integer, String>();
		while (fileScan.hasNext()) {
			String line = fileScan.nextLine();

			if (line.contains("Add")) {
				String[] subStrings = line.split(" ");
				String iconName = findIconName(subStrings);
				Integer groupNum = null;
				try {
					groupNum = this.findGroupNumber(subStrings);
				} catch (NumberFormatException e) {
					logger.info("problem with " + f);
					e.printStackTrace();

					break;
				}
				boolean alreadyIn = firstIcons.containsKey(groupNum);
				if (alreadyIn == false) {
					firstIcons.put(groupNum, iconName);
				}

			}
		}
		for (String iconName : firstIcons.values()) {
			if (firstIconCounts.containsKey(iconName)) {
				firstIconCounts
						.put(iconName, firstIconCounts.get(iconName) + 1);
			} else {
				firstIconCounts.put(iconName, 1);
			}
		}

		fileScan.close();

	}

	// Remove picture events\WW01C2.gif from group 3 at 1239920132268
	private String findIconName(String[] subStrings) {
		for (String s : subStrings) {
			if (s.contains("events")) {
				s = s.substring(7);// strip off "events\"
				s = s.substring(0, s.length() - 4);// remove ".gif"
				return s;
			}
		}
		return null;
	}

	// Add picture events\BB001B1.gif to group 3 at 1239920142111
	private int findGroupNumber(String[] subStrings) {
		for (int i = 0; i < subStrings.length; i++) {
			// need to look for "to" otherwise we pick up group deletion
			if (subStrings[i].equals("group") && subStrings[i - 1].equals("to")) {
				int groupNum = Integer.parseInt(subStrings[i + 1]);
				return groupNum;
			}
		}
		return -99;
	}

	private int findGroupNumber(String line) {
		int groupNum = -99;

		return groupNum;
	}

	private String collectResults(String dir) {

		String condition = dir;
		File baseDir = new File(dir);
		if (baseDir.isDirectory() == false) {
			return "Need to pass in a directory";
		}

		File detailsFile = new File(baseDir + "/file_details.csv");
		if (detailsFile.exists()) {
			readFileDetails(detailsFile);
		}
		File[] files = baseDir.listFiles();
		HashMap<String, File> fileHash = new HashMap<String, File>();
		for (File f : files) {
			fileHash.put(f.getName(), f);
		}
		File correctFile = new File(baseDir + "/correct_counts.csv");
		File outputFile = new File(baseDir + "/results_summary.csv");
		File catCountsFile = new File(baseDir + "/cat_results_summary.csv");
		FileWriter output = null;
		FileWriter correctOutput = null;
		FileWriter catCounts = null;
		try {
			output = new FileWriter(outputFile);
			correctOutput = new FileWriter(correctFile);
			catCounts = new FileWriter(catCountsFile);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		String line = "Nr,File-Nr,Gender (0=male),age,nr. Groups,Condition,Comment,time to group,subject group descriptions\n";
		String correctLine = "participant, Blue001, Blue01, Blue05, Green001, Green01, Green05, BlueGreen001, BlueGreen01, BlueGreen05, Random, Clustered001, Clustered01, Clustered05";
		correctLine = "participant, Blue, Green, BlueGreen, Dispersed, Random, Clustered";
		correctLine = correctLine + "\n";
		String returnString = "";
		ArrayList<Icon> participantCounts = getIconTypes();
		for (Icon ic : participantCounts) {
			returnString = returnString + ic.getDescription() + ",";
		}
		returnString = returnString + "\n";
		correctLine = "participant," + returnString;

		try {
			output.write(line);
			correctOutput.write(correctLine);

			int nr = 1;
			for (File f : files) {
				// logger.finest(f.getName());
				String name = f.getName();
				// if (name.contains("participant") && name.contains("csv")) {
				// Scanner scan = new Scanner(f);
				//
				// while (scan.hasNext()) {
				// System.out.println(scan.nextLine());
				// }
				// }
				boolean thisToo = true;
				if (thisToo && name.contains("participant")
						&& name.contains(".csv")) {

					try {

						Scanner scan = new Scanner(f);
						scan.useDelimiter(",");
						String fileNum = scan.next();
						String age = scan.next();
						String gender = scan.next();
						if (gender.equals("female")) {
							gender = "1";
						} else {
							gender = "0";
						}
						File assignFile = new File(baseDir + "/" + fileNum
								+ "assignment.csv");
						logger.info(assignFile.toString());
						String correctResults = findNCorrect(assignFile);
						// String correctResults = "ble";
						int nGroups = findNGroups(assignFile);
						// int nGroups = 0;
						File participantFile = new File(baseDir + "/"
								+ "participant" + fileNum + ".log");

						double time = findTime(participantFile);
						File batchFile = new File(baseDir + "/" + fileNum
								+ "batch.csv");

						String subjectComments = findCommentsForSubject(batchFile);

						line = nr + "," + fileNum + "," + gender + "," + age
								+ "," + nGroups + "," + condition + ",," + time
								+ "," + subjectComments + "\n";
						nr++;
						correctResults = fileNum + "," + correctResults;
						correctOutput.write(correctResults);
						logger.info(correctResults);
						output.write(line);

					} catch (FileNotFoundException e) {

						e.printStackTrace();
					}

					// logger.finest("part!!!!!!!!!!");
				}
			}
			catCounts
					.write("category, 0.1, 0.05, 0.01, Random, 40Green, 50Green, 60Green\n");
			catCounts.write(assembleString(ClusterType.BLUE));
			catCounts.write(assembleString(ClusterType.GREEN));
			catCounts.write(assembleString(ClusterType.BLUEGREEN));
			catCounts.write(assembleString(ClusterType.RANDOM));
			catCounts.write(assembleString(ClusterType.DISPERSED));
			catCounts.write(assembleString(ClusterType.CLUSTERED));

			output.close();
			correctOutput.close();
			catCounts.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		printConfusionIndexesAssertedFirst(confusionIndexesAll, "All");
		printConfusionIndexesAssertedFirst(confusionIndexGreen40, "Green40");
		printConfusionIndexesAssertedFirst(confusionIndexGreen50, "Green50");
		printConfusionIndexesAssertedFirst(confusionIndexGreen60, "Green60");

		printConfusionIndexesActualFirst(confusionIndexesAll, "All");
		printConfusionIndexesActualFirst(confusionIndexGreen40, "Green40");
		printConfusionIndexesActualFirst(confusionIndexGreen50, "Green50");
		printConfusionIndexesActualFirst(confusionIndexGreen60, "Green60");

		return "";
	}

	private String assembleString(ClusterType type) {
		String returnString = "";
		returnString = returnString + type + ",";
		float result;
		if (totalCounts01.get(type) == 0) {
			result = 0;
		} else {
			result = correctCounts01.get(type) / totalCounts01.get(type);
		}
		returnString = returnString + correctCounts01.get(type) + ",";
		returnString = returnString + correctCounts05.get(type) + ",";
		returnString = returnString + correctCounts001.get(type) + ",";
		returnString = returnString + correctCountsRandom.get(type) + ",";
		returnString = returnString + counts40Green.get(type) + ",";
		returnString = returnString + counts50Green.get(type) + ",";
		returnString = returnString + counts60Green.get(type) + "\n";
		return returnString;
	}

	private String findCommentsForSubject(File batchFile) {
		StringBuffer fileBuff = new StringBuffer();
		Scanner fileScan = null;
		try {
			fileScan = new Scanner(batchFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		// fileScan.useDelimiter(",");
		while (fileScan.hasNext()) {
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",");
			if (lineScan.hasNext()) {
				String participant = lineScan.next();
			} else {
				break;
			}
			if (lineScan.hasNext()) {
				String group = lineScan.next();
			} else {
				break;
			}
			String name = "";
			if (lineScan.hasNext()) {
				name = lineScan.next();
			} else {
				break;
			}

			fileBuff.append(name + ",");
			String description = lineScan.next();
			// if there are any more, separate with semis
			while (lineScan.hasNext()) {
				description = description + ";" + lineScan.next();
			}
			fileBuff.append(description + ",");
		}

		// logger.finest(fileBuff.toString());

		return fileBuff.toString();
	}

	private double findTime(File participantFile) {
		Scanner scan = null;
		try {
			scan = new Scanner(participantFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String line = "";
		while (scan.hasNext()) {
			line = scan.next();
		}
		Scanner lineScanner = new Scanner(line);
		double time = lineScanner.nextDouble();
		// logger.finest(time);
		return time;
	}

	private ClusterType findClusterType(String name) {
		ClusterType type = null;
		if (name.equals("Blue")) {
			type = ClusterType.BLUE;
		} else if (name.equals("Green")) {
			type = ClusterType.GREEN;
		} else if (name.equals("BlueGreen")) {
			type = ClusterType.BLUEGREEN;
		} else if (name.equals("Dispersed")) {
			type = ClusterType.DISPERSED;
		} else if (name.equals("Random")) {
			type = ClusterType.RANDOM;
		} else if (name.equals("Clustered")) {
			type = ClusterType.CLUSTERED;
		} else {
			throw new IllegalArgumentException("unknown cluster type: " + name);
		}

		return type;
	}

	private ClusterStrength findClusterStrength(String name) {
		ClusterStrength str = null;

		if (name.equals("0.01")) {
			str = ClusterStrength.POINT_ONE;
		} else if (name.equals("0.05")) {
			str = ClusterStrength.POINT_ZERO_FIVE;
		} else if (name.equals("0.001")) {
			str = ClusterStrength.POINT_ZERO_ONE;
		} else if (name.equals("0")) {
			str = ClusterStrength.ZERO;
		} else {
			throw new IllegalArgumentException("unknown cluster str: " + name);
		}

		return str;
	}

	private ClusterProportion findClusterProportion(String name) {
		ClusterProportion prop = null;

		if (name.equals("40Green")) {
			prop = ClusterProportion.GREEN_40;
		} else if (name.equals("50Green")) {
			prop = ClusterProportion.GREEN_50;
		} else if (name.equals("60Green")) {
			prop = ClusterProportion.GREEN_60;
		} else {
			throw new IllegalArgumentException("unknown cluster proportion: "
					+ name);
		}

		return prop;
	}

	private String findNCorrect(File assignFile) {
		ArrayList<Icon> participantCounts = getIconTypes();
		Scanner scan = null;
		try {
			scan = new Scanner(assignFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String participant = null;

		while (scan.hasNext()) {

			String line = scan.next();
			logger.finest("line: " + line);
			Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",");

			participant = lineScan.next();

			int assertedGroup = lineScan.nextInt();
			int fileNum = lineScan.nextInt();

			findCorrectByIcon(participantCounts, assertedGroup, fileNum);

		}
		String returnString = "";
		for (Icon ic : participantCounts) {
			returnString = returnString + ic.count + ",";
		}
		returnString = returnString + "\n";
		return returnString;
	}

	private void findCorrectByIcon(ArrayList<Icon> participantCounts,
			int assertedGroup, int fileNum) {
		ClusterType correctCluster = iconClusterType.get(fileNum);
		String assertedClusterName = clusterNames.get(assertedGroup);
		ClusterType assertedCluster = findClusterType(assertedClusterName);

		String significance = iconStrength.get(fileNum);
		ClusterStrength str = findClusterStrength(significance);

		String proportion = iconColorProportion.get(fileNum);
		ClusterProportion prop = findClusterProportion(proportion);

		incrementConfusionIndexes(confusionIndexesAll, assertedCluster,
				correctCluster);
		if (prop.equals(ClusterProportion.GREEN_40)) {
			incrementConfusionIndexes(confusionIndexGreen40, assertedCluster,
					correctCluster);

		} else if (prop.equals(ClusterProportion.GREEN_50)) {
			incrementConfusionIndexes(confusionIndexGreen50, assertedCluster,
					correctCluster);

		} else if (prop.equals(ClusterProportion.GREEN_60)) {
			incrementConfusionIndexes(confusionIndexGreen60, assertedCluster,
					correctCluster);

		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.info("Correct cluster: " + correctCluster);
			logger.info("Asserted cluster: " + assertedCluster);
			logger.info("Significance: " + significance);
			logger.info("Proportion: " + proportion);
		}
		boolean isCorrect = correctCluster.equals(assertedCluster);
		boolean isCorrectClustered = findIfCluster(correctCluster,
				assertedCluster);
		if (isCorrect == false && isCorrectClustered == false) {
			return;
		}

		for (Icon ic : participantCounts) {
			if (ic.proportion.equals(prop)
					|| ic.proportion.equals(ClusterProportion.ALL_PROPORTIONS)) {
				if (ic.strength.equals(str)
						|| ic.strength.equals(ClusterStrength.ALL_STRENGTHS)) {
					if (isCorrect && ic.type.equals(assertedCluster)
							|| isCorrect
							&& ic.type.equals(ClusterType.ALL_TYPES)) {
						ic.count++;
					} else if (isCorrectClustered
							&& ic.type.equals(ClusterType.CLUSTERED)) {
						ic.count++;
					}

				}

			}
		}

	}

	private String findNCorrect_old(File assignFile) {

		String resultString = "";
		Scanner scan = null;

		HashMap<ClusterType, Integer> fiveCatCountsSubject = new HashMap<ClusterType, Integer>();
		initCounter(fiveCatCountsSubject);

		try {
			scan = new Scanner(assignFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String participant = null;

		while (scan.hasNext()) {

			String line = scan.next();
			logger.finest("line: " + line);
			Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",");

			participant = lineScan.next();

			int group = lineScan.nextInt();
			int file = lineScan.nextInt();

			ClusterType correctCluster = iconClusterType.get(file);
			String assertedClusterName = clusterNames.get(group);
			ClusterType assertedCluster = findClusterType(assertedClusterName);
			String significance = iconStrength.get(file);
			String proportion = iconColorProportion.get(file);
			logger.finest("Correct cluster: " + correctCluster);
			logger.finest("Asserted cluster: " + assertedCluster);
			logger.finest("Significance: " + significance);
			logger.finest("Proportion: " + proportion);
			boolean isCluster;

			if (correctCluster != null && assertedCluster != null) {
				if (significance.equals("0.01")) {
					bumpCount(correctCluster, totalCounts01);
				} else if (significance.equals("0.05")) {
					bumpCount(correctCluster, totalCounts05);
				} else if (significance.equals("0.001")) {
					bumpCount(correctCluster, totalCounts001);
				} else if (significance.equals("0")) {
					bumpCount(correctCluster, totalCountsRandom);
				} else {
					throw new IllegalArgumentException("unknown significance: "
							+ significance);
				}

				if (proportion.equals("40Green")) {
					bumpCount(correctCluster, totalCounts40Green);
				}
				isCluster = findIfCluster(correctCluster, assertedCluster);

				if (isCluster) {
					if (significance.equals("0.01")) {
						bumpClusterCount(correctCounts01);
					} else if (significance.equals("0.05")) {
						bumpClusterCount(correctCounts05);
					} else if (significance.equals("0.001")) {
						bumpClusterCount(correctCounts001);
					} else if (significance.equals("0")) {
						bumpClusterCount(correctCountsRandom);
					} else {
						throw new IllegalArgumentException(
								"unknown significance: " + significance);
					}

					if (proportion.equals("40Green")) {
						bumpClusterCount(counts40Green);
					} else if (proportion.equals("50Green")) {
						bumpClusterCount(counts50Green);
					} else if (proportion.equals("60Green")) {
						bumpClusterCount(counts60Green);
					} else {
						throw new IllegalArgumentException(
								"unknown proportion: " + proportion);
					}
				}
				if (assertedCluster.equals(correctCluster)) {

					Integer count = fiveCatCountsSubject.get(correctCluster);
					count++;
					fiveCatCountsSubject.put(correctCluster, count);

					count = countsOverall.get(correctCluster);
					count++;
					countsOverall.put(correctCluster, count);

					if (significance.equals("0.01")) {
						bumpCount(correctCluster, correctCounts01);
					} else if (significance.equals("0.05")) {
						bumpCount(correctCluster, correctCounts05);
					} else if (significance.equals("0.001")) {
						bumpCount(correctCluster, correctCounts001);
					} else if (significance.equals("0")) {
						bumpCount(correctCluster, correctCountsRandom);
					} else {
						throw new IllegalArgumentException(
								"unknown significance: " + significance);
					}

					if (proportion.equals("40Green")) {
						bumpCount(correctCluster, counts40Green);
					} else if (proportion.equals("50Green")) {
						bumpCount(correctCluster, counts50Green);
					} else if (proportion.equals("60Green")) {
						bumpCount(correctCluster, counts60Green);
					} else {
						throw new IllegalArgumentException(
								"unknown proportion: " + proportion);
					}

				}

			}

		}

		resultString = participant + ","
				+ fiveCatCountsSubject.get(ClusterType.BLUE) + ","
				+ fiveCatCountsSubject.get(ClusterType.GREEN) + ","
				+ fiveCatCountsSubject.get(ClusterType.BLUEGREEN) + ","
				+ fiveCatCountsSubject.get(ClusterType.DISPERSED) + ","
				+ fiveCatCountsSubject.get(ClusterType.RANDOM) + ","
				+ fiveCatCountsSubject.get(ClusterType.CLUSTERED) + "\n";
		return resultString;
	}

	private void bumpCount(ClusterType correctCluster,
			HashMap<ClusterType, Integer> counter) {
		Integer count;
		count = counter.get(correctCluster);
		count++;
		counter.put(correctCluster, count);

	}

	private void bumpClusterCount(HashMap<ClusterType, Integer> counter) {
		Integer count;
		count = counter.get(ClusterType.CLUSTERED);
		count++;
		counter.put(ClusterType.CLUSTERED, count);
	}

	private boolean findIfCluster(ClusterType correctCluster,
			ClusterType assertedCluster) {
		boolean isCluster;
		isCluster = false;
		if (assertedCluster.equals(ClusterType.BLUE)
				|| assertedCluster.equals(ClusterType.GREEN)
				|| assertedCluster.equals(ClusterType.BLUEGREEN)) {
			if (correctCluster.equals(ClusterType.BLUE)
					|| correctCluster.equals(ClusterType.GREEN)
					|| correctCluster.equals(ClusterType.BLUEGREEN)) {
				isCluster = true;
			}
		}
		return isCluster;
	}

	private int findNGroups(File assignFile) {

		Scanner scan = null;
		try {
			scan = new Scanner(assignFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		// scan.useDelimiter(",");
		int group = -1;
		while (scan.hasNext()) {
			String line = scan.next();
			Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",");

			String participant = lineScan.next();
			group = lineScan.nextInt();
			String file = lineScan.next();

			// logger.finest(group);
		}

		return group + 1;
	}

	public static void main(String[] args) {
		ResultsCollector col = new ResultsCollector();
		ArrayList<Icon> types = ResultsCollector.getIconTypes();
		Integer counter = 0;
		for (Icon ic : types) {
			// logger.info(++counter + ": " + ic.getDescription());
		}

		String dir = "";
		if (args.length != 1) {
			dir = "./COLOR/";
			dir = "C:\\data\\publications\\joincount_annals\\results\\expert\\";
			dir = "C:\\data\\grants\\nsf_klippel09\\different_size\\";
			dir = "./results/";
			dir = "C:\\data\\publications\\joincount_annals\\results\\non-expert\\";
			dir = "C:/Users\\Frank\\Documents\\My Dropbox\\ConCat\\data_frank\\canon\\";
			dir = "C:\\Users\\Frank\\Desktop\\participant15\\";
			args = new String[1];
			args[0] = dir;
			// printUsage();
			// System.exit(0);
		} else {
			File testDir = new File(args[0]);
			if (testDir.exists() == false || testDir.isDirectory() == false) {
				printUsage();
				logger.info("found: " + args[0]);
				System.exit(0);
			}
			dir = args[0];
		}
		dir = "C:\\Users\\Frank\\Desktop\\participant15\\";
		ResultsCollector collector = new ResultsCollector();

		String result = null;
		// dir =
		// "C:\\Users\\Frank\\Documents\\My Dropbox\\ConCat\\Experiment_Data\\data_frank\\tornado"
		// + "\\";
		// result = collector.collectDifferentSizeResults(dir);

		result = collector.collectDifferentSizeResults(dir);

		if (result.equals("")) {
			logger.info("Success!");
		} else {
			logger.info("There was a problem:" + result);
		}

	}

	private static void printUsage() {
		System.out.println("first arg should be directory");
	}

}
