package xp;

import java.util.ArrayList;

public class DataObject {

	public DataObject(int pn) {
		participantNr = pn;
		age = 21;
		log = new ArrayList();
		folder = "events";
		likertAnswers = new ArrayList<Integer>();
		likertQuestions = new ArrayList<String>();

	}

	// program state, is this the right place for this?
	static boolean demoMode = false;
	static boolean secondRun = false;
	// another global...
	static int pictureSize = 150;
	public static String fileIdentifier;

	// personal data

	public String gender = "";
	public String subject = "";
	public int age;
	// public String handedness = "";

	// new stuff
	public boolean geoIntelligence;
	public boolean geography;
	public boolean colorBlind;

	// likert preferences
	public ArrayList<String> likertQuestions;
	public ArrayList<Integer> likertAnswers;

	// I/O data
	// public File outputFile;

	public String folder;
	public static int participantNr;

	// results
	public String fileOrder = "";
	public ArrayList groups;
	int maxpic;

	// time
	long startTime;
	long endTime;
	long taskDuration;

	public ArrayList log;

	public void resetResults() {

		groups = new ArrayList();
		maxpic = 0;
		log = new ArrayList();

	}

}
