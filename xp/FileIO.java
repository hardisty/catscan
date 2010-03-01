package xp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileIO {

	static String ResultsDir = "results/";

	public static void writeResults(DataObject dob) {

		FileWriter fw;
		// DataObject current;

		try {
			int grpsize = dob.groups.size();
			int pixsize;

			// write main output

			fw = new FileWriter(ResultsDir + dob.fileIdentifier + ".txt");

			fw.write("participant number: " + dob.participantNr + "\n");
			fw.write("age: " + dob.age + "\n");
			fw.write("gender: " + dob.gender + "\n");
			// fw.write("handedness: " + dob.handedness + "\n");
			fw.write("geo spatial:" + dob.geoIntelligence + "\n");
			fw.write("geography:" + dob.geography + "\n");
			fw.write("color blind:" + dob.colorBlind + "\n");
			for (int i = 0; i < dob.likertQuestions.size(); i++) {
				fw.write(dob.likertQuestions.get(i) + ":"
						+ dob.likertAnswers.get(i) + "\n");
			}

			fw.write("subject: " + dob.subject + "\n\n");

			for (int i = 0; i < grpsize; i++) {
				GroupPanel grp = (GroupPanel) dob.groups.get(i);
				if (!grp.pix.isEmpty()) {
					fw.write(grp.name + "\n\n");
					fw.write(grp.question1 + "\n\n");
					fw.write(grp.question2 + "\n\n");
					for (int j = 0; j < grp.pix.size(); j++) {
						fw.write(((PicInfo) grp.pix.get(j)).path + "nr: "
								+ ((PicInfo) grp.pix.get(j)).picdata.number
								+ "\n");
					}
					fw.write("\n");
				}
			}

			fw.close();

			// write matrix
			fw = new FileWriter(ResultsDir + dob.fileIdentifier + ".mtrx");

			ArrayList pix = null;
			// ArrayList matrix = new ArrayList();
			char[][] matrix = new char[dob.maxpic][dob.maxpic * 2 - 1];
			char[] matrixln = new char[dob.maxpic * 2 - 1];

			for (int l = 0; l < dob.maxpic; l++) {
				for (int h = 0; h < dob.maxpic * 2 - 1; h = h + 2) {
					matrix[l][h] = '0';
					if (h < dob.maxpic * 2 - 2) {
						matrix[l][h + 1] = ' ';
					}
				}
			}

			for (int i = 0; i < grpsize; i++) {

				pix = ((GroupPanel) dob.groups.get(i)).pix;
				pixsize = pix.size();

				for (int j = 0; j < pixsize; j++) {

					for (int k = 0; k < pixsize; k++) {
						matrix[((PicInfo) pix.get(j)).picdata.number][((PicInfo) pix
								.get(k)).picdata.number * 2] = '1';
						// if (k < dob.maxpic-1)
						// matrix[j][k+1] = ' ';
					}

				}

				// w.write((((GroupPanel)dob.groups.get(i)).name)+"\n");

			}
			for (int m = 0; m < dob.maxpic; m++) {
				fw.write((new String(matrix[m])) + "\n");
			}
			fw.write("\n");
			fw.write("order of files:\n" + dob.fileOrder);
			fw.close();

			// write log
			fw = new FileWriter(ResultsDir + dob.fileIdentifier + ".log");
			for (int i = 0; i < dob.log.size(); i++) {
				fw.write((String) dob.log.get(i));
			}
			fw.close();

			fw = null;

			// write data for Chris
			fw = new FileWriter(ResultsDir + dob.participantNr
					+ "participant.csv");
			String likertString = "";
			for (int i = 0; i < dob.likertQuestions.size(); i++) {
				likertString = likertString + dob.likertAnswers.get(i) + ",";
			}
			fw.write(dob.participantNr + "," + dob.age + "," + dob.gender + ","
					+ dob.geoIntelligence + "," + dob.geography + ","
					+ dob.colorBlind + "," + likertString + dob.subject + "\n");
			fw.close();
			fw = null;

			fw = new FileWriter(ResultsDir + dob.participantNr + "batch.csv");
			for (int i = 0; i < grpsize; i++) {
				GroupPanel grp = (GroupPanel) dob.groups.get(i);
				if (!grp.pix.isEmpty()) {
					fw.write(dob.participantNr + "," + i + "," + grp.name + ","
							+ grp.question1 + "," + grp.question2 + "\n");

				}
			}

			fw.close();
			fw = null;

			fw = new FileWriter(ResultsDir + dob.participantNr
					+ "assignment.csv");
			for (int i = 0; i < grpsize; i++) {
				GroupPanel grp = (GroupPanel) dob.groups.get(i);
				if (!grp.pix.isEmpty()) {

					for (int j = 0; j < grp.pix.size(); j++) {
						fw.write(dob.participantNr + ",");
						fw.write(i + ",");

						fw.write(((PicInfo) grp.pix.get(j)).picdata.number
								+ "\n");
					}
				}
			}
			fw.close();
			fw = null;

			fw = new FileWriter(ResultsDir + dob.participantNr + "icon.csv");
			for (int i = 0; i < grpsize; i++) {
				GroupPanel grp = (GroupPanel) dob.groups.get(i);

				if (!grp.pix.isEmpty()) {
					for (int j = 0; j < grp.pix.size(); j++) {
						fw.write(((PicInfo) grp.pix.get(j)).picdata.number
								+ "," + ((PicInfo) grp.pix.get(j)).path + "\n");
					}

				}
			}
			fw.close();
			fw = null;

			File dir = new File(FileIO.ResultsDir);

			// These are the files to include in the ZIP file
			String[] filenames = dir.list();
			// Create a buffer for reading the files
			byte[] buf = new byte[1024];
			// Create the ZIP file
			String outFilename = "results.zip";
			File zipFile = new File(outFilename);
			if (zipFile.exists()) {
				// zipFile.delete();
			}
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					outFilename));
			// Compress the files
			for (String filename : filenames) {
				String dirFilename = FileIO.ResultsDir + filename;
				if (filename.contains(".svn")) {
					continue;
				}
				FileInputStream in = new FileInputStream(dirFilename);
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(filename));
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			}
			out.close();
			// write pictures

			// for (int i = 0; i < grpsize; i++) {
			// GroupPanel grp = (GroupPanel) dob.groups.get(i);
			// if (!grp.pix.isEmpty() && grp.image != null) {
			// String fileName = dob.fileIdentifier + "_"
			// + (((GroupPanel) dob.groups.get(i)).number)
			// + ".jpg";
			// ImageIO.write(grp.image, "JPEG", new File(fileName));
			// ((GroupPanel) dob.groups.get(i)).image = null;
			// } else {
			// if (grp.pix.isEmpty()) {
			// System.out.println(i + " pix is empty");
			// }
			// if (grp.image == null) {
			// System.out.println(i + " image is null");
			//
			// }
			// }
			//
			// }

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("bad things happened writing out the data");
		}
	}

	static int findUniqueParticipantNumber() {
		int pNum = 0;
		File dir = new File(FileIO.ResultsDir);

		String[] filenames = dir.list();

		for (String filename : filenames) {
			System.out.println("filename = " + filename);
			String dirFilename = FileIO.ResultsDir + filename;
			if (filename.contains(".svn")) {
				continue;
			}
			if (filename.contains("participant") && filename.contains(".log")) {
				String intString = filename.substring("participant".length());
				intString = intString.substring(0, intString.length() - 4);
				Integer intVal = Integer.valueOf(intString);
				System.out.println("intVal = " + intVal + ", pNum = " + pNum);
				if (intVal >= pNum) {
					pNum = intVal + 1;
				}
			}

		}
		System.out.println("pNum = " + pNum);
		return pNum;
	}
}
