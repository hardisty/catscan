package xp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class ResultsCollector {

	// public enum IconTypes {

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

		File outputFile = new File(baseDir + "/results_summary.prn");

		FileWriter output = null;

		try {
			output = new FileWriter(outputFile);

		} catch (IOException e1) {

			e1.printStackTrace();
		}
		String line = "Nr" + delimiter + "File-Nr" + delimiter
				+ "Gender (0=male)" + delimiter + "age" + delimiter
				+ "nr. Groups" + delimiter + "Condition" + delimiter
				+ "time to group" + delimiter + "subject group descriptions"
				+ "\n";
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

						// String correctResults = "ble";
						int nGroups = findNGroups(assignFile);
						// int nGroups = 0;
						File participantFile = new File(baseDir + "/"
								+ "participant" + fileNum + ".log");

						double time = findTime(participantFile);
						File batchFile = new File(baseDir + "/" + fileNum
								+ "batch.csv");

						String subjectComments = findCommentsForSubject(batchFile);

						line = nr + delimiter + fileNum + delimiter + gender
								+ delimiter + age + delimiter + nGroups
								+ delimiter + time + delimiter
								+ subjectComments + "\n";
						nr++;

						output.write(line);

					} catch (FileNotFoundException e) {

						e.printStackTrace();
					}

					// logger.finest("part!!!!!!!!!!");
				}
			}

			output.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return "";
	}

	private String collectResults(String dir) {

		String condition = dir;
		File baseDir = new File(dir);
		if (baseDir.isDirectory() == false) {
			return "Need to pass in a directory";
		}

		File detailsFile = new File(baseDir + "/file_details.csv");

		readFileDetails(detailsFile);

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
			String group = lineScan.next();
			String name = lineScan.next();
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

		return group;
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
			// printUsage();
			// System.exit(0);
		} else {
			File testDir = new File(args[0]);
			if (testDir.exists() == false || testDir.isDirectory() == false) {
				printUsage();
				System.exit(0);
			}
			dir = args[0];
		}

		ResultsCollector collector = new ResultsCollector();

		String result = collector.collectDifferentSizeResults(dir);

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
