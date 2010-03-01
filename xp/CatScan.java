/**
 * 
 */
package xp;

/**
 * @author Andrae
 * 
 */
public class CatScan {

	public static boolean wait = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int partNum = FileIO.findUniqueParticipantNumber();
		CatScanMainWin frame = new CatScanMainWin(true, new DataObject(partNum));
		// frame.setLocation(162, 0);
		// frame.setSize(700, 740);
		frame.setVisible(true);
		while (wait) {
		}
		;
		// frame.dispose();
		System.exit(0);
	}
}
