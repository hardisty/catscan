package xp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class CatScanMainWin extends JFrame implements ActionListener {

	public final int picsize = 120;

	boolean trail;

	// Container c;
	JPanel picCatcher;
	JSplitPane split;
	Container contentPane;
	PictureTransferHandler picHandler;
	JLabel actionTxt;
	JScrollPane jsp;

	DragPictureDemo dragPictureGUI;

	// focus is on...
	GroupPanel focus;

	// results
	int groupcount = 0;
	DataObject data;
	ArrayList<GroupPanel> groups;

	// flags
	boolean userNamesGroups = true;

	static String TimeStamp = "Last Modified = 2010-1-29";
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());

	}

	// contructor
	public CatScanMainWin(boolean istrail, DataObject dob) {

		super("XPeriment");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		trail = istrail;

		// first participant//////////////////////////////////
		if (trail) {
			data = dob;
		} else {
			data = dob;
			data.resetResults();
			data.log.add(CatScanMainWin.TimeStamp + "\n");
			data.log.add("Run date = " + now());
			data.log.add("Started at " + System.currentTimeMillis() + "\n");
		}
		// initialising variables
		groups = new ArrayList<GroupPanel>();
		picHandler = new PictureTransferHandler();

		// start main window/////////////////////////////////

		this.setSize(100, 100);
		this.setLocation(0, 0);
		// this.setLocationRelativeTo(null);

		setExtendedState(MAXIMIZED_BOTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);

		if (trail) {
			runDialogs();

		}

		setupFrame();

	}

	public void runDialogs() {

		// show config dialog
		ConfigDialog cd = new ConfigDialog(this, data);

		// cd.setLocation(200,200);
		// cd.setLocationRelativeTo(null);
		// cd.setSize(600,300);
		cd.setVisible(true);
		// show personal data dialog
		showPersonalDataDialog();
		// this.setExtendedState(ICONIFIED);
		TextScreen.isLong = true;
		String oldText = "Dear Participant, In the following experiment we will show you   icons, known as star plots. A star plot visualizes several   variables   of entities at the same time. In this experiment the entities   are   cars. Where each variable is located in the star plot is   explained on   the sheet of paper in front of your computer. The cars are   design   studies of future cars and do not correspond to current   models!   Your task is to sort the animated icons that we will present   you with   on the left side of the screen into groups on the right.   Please sort   them based on how similar they are. You can create as many   groups as   you think are appropriate. Just use the button NEW GROUP to   create a   more groups. Drag items into this group by holding down the   left   mouse button. You may delete a group by using the DELETE   button. All   icons within that group will be placed on the left side   again. You   can also sort icons directly from one category into   another. You   decide which items belong together. There is no right or   wrong group   for the star plot icons. This task should take 20-30 minutes   and you   should do it in one sitting. Your first task is completed   when all   icons (the star plots) from the left side have been placed   into   groups on the right side. If you have further questions   please ask   the experimenter. We have prepared a short example to   acquaint you   with the interface. When you feel comfortable with the   interface,   press FINISH. Please press OK.";
		String firstOldText = "Dear Participant,\n\n In the following experiment we will show you animated icons depicting paths of hurricanes.\n\nYour task is to sort the animated icons that we will present you with on the left side of the screen into groups on the right. Please sort them based on how similar they are. You can create as many groups as you think are appropriate. Just use the button NEW GROUP to create a more groups. Drag items into this group by holding down the left mouse button. You may delete a group by using the DELETE button. All icons within that group will be placed on the left side again. You can also sort icons directly from one category into another. \n\n You decide which items belong together. There is no right or wrong group for the hurricane path icons. This task should take 20-30 minutes and you should do it in one sitting. Your first task is completed when all animated icons from the left side have been placed into groups on the right side. If you have further questions please ask the experimenter. We have prepared a short example to acquaint you with the interface. When you feel comfortable with the interface, press FINISH.\n\n Please press OK after you have read the instructions.";
		String secondOldText = "Dear Participant,\n\n In the following experiment we are asking you to sort patterns into five groups. The patterns consist of simple two color (green and blue) maps, and four examples are provided below. The maps consists of simplified regions (squares) and you might want to think of an elections map (which, of course would be blue and red). The five groups are predefined and it is important that you sort the patterns according to the following five criteria:\n\n   * Only the blue regions (squares) are statistically significantly clustered\n   * Only the green regions (squares) are statistically significantly clustered\n   * Both, blue and green regions (squares) are statistically significantly clustered\n   * The pattern of blue and green regions (squares) is statistically significantly dispersed\n   * The pattern of blue and green regions (squares) is random\n\n The following four examples are extreme cases of clustered and dispersed patterns. These patterns would rarely be found as the result of a random process but should give you an idea of what to look for.";
		String secondText = "In the second part of this experiment, we ask you to perform the same grouping task again. Please keep in mind that you have to name the groups afterwards.";
		String firstText = "Dear Participant,\n\n In the following experiment we are asking you to sort patterns into groups, groups that seem natural to you. The patterns consist of simple two color (green and blue) maps. The maps consist of simplified regions (squares) and you might want to think of election maps (which, of course would be blue and red). There is no right or wrong! You can create as many groups as you think are appropriate.";
		String anotherOld = "Dear Participant,\n\nIn the following experiment we will show you animated icons on a computer screen. These icons represent 2 geographic regions (2 circles) in an abstract way. The animations represent the ‘behaviours’ of these regions. You may think, for example, of an oil slick approaching a coastline, a high pressure zone, clouds and so forth. Your task is to sort the animated icons that we will present you on the left side of the screen into groups on the right. Please sort them based on how similar they are. You can create as many groups as you think are appropriate. Just use the button NEW GROUP. Drag items into this category by holding down the left mouse button. You may delete a category by using the DELETE button. All icons will be placed on the left side again. You can also sort icons directly from one category into another. You decide which items belong together. There is no right or wrong group for the animated icons. This task should take 20-30 minutes and you should do it in one sitting. Your task is completed when all animated icons from the left side have been placed into groups on the right side. If you have further questions please asked the experimenter. We have prepared a short example to acquaint you with the interface. When you feel comfortable with the interface, press OK EXPERIMENT.\n\nPlease press OK.";

		String useThis = getInstructions();
		TextScreen bla = null;
		if (DataObject.secondRun == false) {
			TextScreen.seconds = 60;
			bla = new TextScreen(useThis, this, 600, 600);
		} else {
			TextScreen.seconds = 5;
			bla = new TextScreen(secondText, this, 600, 600);
			trail = false;
		}

		// "Dear Participant,\n\nIn the following experiment we will
		// show you icons, known as star plots. A star plot visualizes
		// several variables of entities at the same time. In this
		// experiment the entities are cars. Where each variable is
		// located in the star plot is explained on the sheet of paper
		// in front of your computer. The cars are design studies of
		// future cars and do not correspond to current models!\nYour
		// task is to sort the icons (cars) that we will present you
		// with on the left side of the screen into groups on the right.
		// Please sort them based on how similar the cars are. You can
		// create as many groups as you think are appropriate. Just use
		// the button NEW GROUP to create more groups. Drag items into
		// this group by holding down the left mouse button. You may
		// delete a group by using the DELETE button. All icons within
		// that group will be placed on the left side again. You can
		// also sort icons directly from one category into another.\nYou
		// decide which cars belong together. There is no right or wrong
		// group for the star plot icons. Your first task is completed
		// when all icons (the star plots) from the left side have been
		// placed into groups on the right side. We have prepared a
		// short example to acquaint you with the interface.",

		// "Dear Participant,\n\nIn the following experiment we will
		// show you icons of geometric figures. Your task is to sort the
		// icons that we will present you with on the left side of the
		// screen into groups on the right. Please sort them based on
		// how similar they are. You can create as many groups as you
		// think are appropriate. Just use the button NEW GROUP to
		// create more groups. Drag items into this group by holding
		// down the left mouse button. You may delete a group by using
		// the DELETE button. All icons within that group will be placed
		// on the left side again. You can also sort icons directly from
		// one category into another.\nYou decide which geometric
		// figures belong together. There is no right or wrong group for
		// the geometric icons. Your first task is completed when all
		// icons from the left side have been placed into groups on the
		// right side. We have prepared a short example to acquaint you
		// with the interface.",

		bla.setVisible(true);
		TextScreen.isLong = false;
		bla.dispose();
	}

	public void setupFrame() {

		// setting up dragPictureGUI
		dragPictureGUI = new DragPictureDemo(data, picHandler, this);
		// dragPictureGUI.setBackground(Color.red);
		dragPictureGUI.setDoubleBuffered(true);
		if (trail) {
			// this.setMaximumSize(new Dimension(500,500));
			dragPictureGUI.showPanel.setLayout(new FlowLayout(15, 15, 15));// GridLayout
			// (
			// 2
			// ,
			// 7
			// ,
			// 10
			// ,
			// 10
			// )
			// )
			// ;
			// dragPictureGUI.showPanel.setMaximumSize(new Dimension (837,300));
			dragPictureGUI.showPanel.setPreferredSize(new Dimension(500, 300));
		} else {
			dragPictureGUI.showPanel.setLayout(new FlowLayout(15, 15, 15));
			dragPictureGUI.showPanel.setPreferredSize(new Dimension(500,
					120 * dragPictureGUI.pix.size()));

			// dragPictureGUI.showPanel.setLayout(new
			// GridLayout(dragPictureGUI.showPanel.getComponentCount()/7+1,7,10,10
			// ));
		}
		// dragPictureGUI.setOpaque(true); //content panes must be opaque

		// setting up groups
		picCatcher = new JPanel(new FlowLayout(5, 5, 5));
		picCatcher.setDoubleBuffered(true);
		picCatcher.setBackground(Color.DARK_GRAY);
		// GroupPanel firstGrp = new GroupPanel(this, dragPictureGUI,
		// picHandler, ++groupcount);
		// picCatcher.add(firstGrp);
		// groups.add(firstGrp);
		// focus = firstGrp;
		// picCatcher.setMinimumSize(new Dimension(837,1015));
		// picCatcher.setMaximumSize(new Dimension(837,1015));
		// picCatcher.setPreferredSize(new Dimension(837,1015));

		// ButtonBar
		JToolBar buttonBar = new JToolBar();
		buttonBar.setFloatable(false);
		buttonBar.setRollover(true);
		JButton ng = new JButton("NEW GROUP");
		JButton ry = new JButton("FINISH");
		JButton del = new JButton("DELETE ACTIVE GROUP");
		JButton compact = new JButton("COMPACT ICONS");
		ng.addActionListener(this);
		ry.addActionListener(this);
		del.addActionListener(this);
		compact.addActionListener(this);
		buttonBar.add(ry);
		buttonBar.add(ng);
		buttonBar.add(del);
		buttonBar.add(compact);

		// dragPictureGUI.setPreferredSize(new Dimension(600,2530));

		JPanel split2 = new JPanel(new GridLayout(1, 2));
		// JScrollPane dsp = new JScrollPane(dragPictureGUI);
		// dsp.setWheelScrollingEnabled(false);

		JScrollPane dpScroll = new JScrollPane(dragPictureGUI,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		split2.add(dpScroll);

		// jsp = new JScrollPane(picCatcher);
		// jsp.setWheelScrollingEnabled(false);

		JScrollPane picCatcherScroll = new JScrollPane(picCatcher,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		split2.add(picCatcherScroll);
		// ContentPane
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(split2, BorderLayout.CENTER);
		// contentPane.add(new JScrollPane(dragPictureGUI), BorderLayout.WEST);
		// contentPane.add(new JScrollPane(picCatcher), BorderLayout.EAST);
		contentPane.add(buttonBar, BorderLayout.PAGE_END);

		setContentPane(contentPane);

		// this.pack();

		// this.setLocation(0,0);
		// this.setSize(500,500);
		// this.setExtendedState(NORMAL);
		setExtendedState(MAXIMIZED_BOTH);
		// this.setLocation(0,0);
		// this.pack();
		// this.validate();
		if (trail) {
			if (userNamesGroups == false) {
				addGroup("Cats");
				addGroup("Dogs");
			} else {
				// addGroup("");
			}

		}
	}

	public void actionPerformed(ActionEvent arg0) {

		TextScreen bla;
		String cmd = arg0.getActionCommand();

		// NewGroup-Button
		if (cmd.equals("NEW GROUP")) {
			addGroup("");

		}
		if (cmd.equals("COMPACT ICONS")) {
			data.log.add("Icons compacted \n");
			dragPictureGUI.compactPics();

		}
		// Ready-Button
		if (cmd.equals("FINISH")
				&& (DataObject.demoMode || dragPictureGUI.pix.isEmpty())) {
			GroupPanel grp = null;
			if (trail) {
				bla = new TextScreen(
						"Please press OK to begin with the main experiment!",
						this, 300, 200);
				bla.setVisible(true);
				bla.dispose();
				CatScanMainWin xpmf = new CatScanMainWin(false, data);

				if (userNamesGroups == false) {
					xpmf
							.addGroup("Statistically significant clustering of ONLY blue squares");
					xpmf
							.addGroup("Statistically significant clustering of ONLY green squares");
					xpmf
							.addGroup("Statistically significant clustering of both blue squares and green squares");
					xpmf
							.addGroup("Statistically significant dispersed pattern");
					xpmf.addGroup("Random pattern");
				} else {
					// xpmf.addGroup("");
				}
				data.startTime = System.currentTimeMillis();
				// addGroup();
				// grp.validate();
				// xpmf.pack();
				// XXX is above line OK?
			} else {

				data.log
						.add("Finished at " + System.currentTimeMillis() + "\n");
				data.endTime = System.currentTimeMillis();
				data.taskDuration = data.endTime - data.startTime;
				double timeInSeconds = (data.taskDuration / 1000d);
				data.log
						.add("Sorting took this many seconds: " + timeInSeconds);
				// int grpsnr = groups.size();
				data.groups = groups;

				String oldTransitionText = "Dear Participant,\n\nCongratulations! You have successfully completed the first part of the experiment. We have one follow up task that consists of two smaller tasks. You will see the groups of geometric icons that you have created again.\n\nPlease press OK to start the second part of the experiment.";
				String transitionText = "Dear Participant,\n\nCongratulations! You have successfully completed the first part of the experiment. We have one follow up task.";

				bla = new TextScreen(transitionText, this, 500, 400);

				bla.setVisible(true);
				bla.dispose();
				ArrayList rem = new ArrayList();

				for (int i = 0; i < groups.size(); i++) {
					grp = (groups.get(i));

					if ((grp.pix.isEmpty())) {

						rem.add(grp);
					}
				}

				for (Object r : rem) {
					groups.remove(r);
				}

				for (int i = 0; i < groups.size(); i++) {
					grp = (groups.get(i));
					grp.number = i + 1;
				}

				dragPictureGUI.removeAll();
				dragPictureGUI.pix = null;
				dragPictureGUI.showPanel = null;
				picCatcher.removeAll();
				groups.trimToSize();

				// skip the following loop if user does not name groups
				if (userNamesGroups == false) {
					return;
				}

				GroupNameDialog ign;
				// ask group questions for each group
				for (int i = 0; i < groups.size(); i++) {

					grp = (groups.get(i));
					// grp.number = i;
					grp.addPic = false;

					ign = new GroupNameDialog(this, grp);
					// ign.pack();
					ign.setVisible(true);

					ign.dispose();
					boolean writeImage = false;
					if (writeImage) {
						try {
							ImageIO.write(grp.image, "JPEG",
									new File((data.fileIdentifier + "_"
											+ grp.number + ".jpg")));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// JAI.create("filestore",((GroupPanel)dob.groups.get(i)).
					// image
					// ,(dob.fileIdentifier+"_"+(((GroupPanel)dob.groups.get
					// (i)).number)+".jpg"),
					// "JPEG", null);
					grp.image = null;

					// PaintDialog pd = new PaintDialog(this);
					// pd.setVisible(true);

				}
				// includes personal data and likert data
				// XXX moving
				FileIO.writeResults(data);

				// data = null;

				bla = new TextScreen(
						"Thank you!\n\nYou have now finished the experiment. Please remain quietly seated and wait for the experimentor to give you further instructions.",
						this, 400, 300);
				bla.setVisible(true);
				System.exit(0);
				bla.dispose();

				nextParticipant(data.participantNr + 1);

			}
		}

		if (cmd.equals("DELETE ACTIVE GROUP")) {
			if (focus != null) {
				groupcount--;
				data.log.add("Delete group " + focus.number);
				dragPictureGUI.addPicLst(focus.pix);
				picCatcher.remove(focus);
				groups.remove(focus);
				if (!groups.isEmpty()) {
					focus = groups.get(0);
				}
				focus.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(Color.RED, 5),
						BorderFactory.createLoweredBevelBorder()));

				validate();
				this.repaint();
				// contentPane.validate();
			}
		}
	}

	private void addGroup(String label) {
		if (!trail) {
			data.log.add("NEW GROUP");
		}
		if (focus != null) {
			focus.setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createLineBorder(Color.DARK_GRAY, 5), BorderFactory
					.createLoweredBevelBorder()));
		}
		groups.add(newGroup(label));
		// jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().
		// getMaximum());

		contentPane.validate();
		contentPane.repaint();
		// this.setContentPane(contentPane);
	}

	public void nextParticipant(int pn) {

		trail = true;
		resetFrame(pn);

		// this.setVisible(false);

		// this.demo.showPanel.removeAll();

		// show config dialog
		ConfigDialog cd = new ConfigDialog(this, data);
		cd.setLocation(200, 200);
		cd.setSize(600, 300);
		cd.setVisible(true);

		String instructions = getInstructions();

		TextScreen bla = new TextScreen(instructions, this, 700, 1000);
		bla.setVisible(true);
		bla.dispose();
		// dragPictureGUI.showPanel = new JPanel(new GridLayout(24, 7,10,10));
		dragPictureGUI.add(dragPictureGUI.showPanel, BorderLayout.CENTER);

		if (trail) {
			dragPictureGUI.showPanel.setLayout(new FlowLayout(15, 15, 15));
			dragPictureGUI.pix = DragPictureDemo.openFiles(data.folder
					+ "/trail", dragPictureGUI, picHandler, pn);
			dragPictureGUI.showPanel.setPreferredSize(new Dimension(500, 300));
		} else {
			dragPictureGUI.pix = DragPictureDemo.openFiles(data.folder,
					dragPictureGUI, picHandler, pn);
		}
		data.maxpic = dragPictureGUI.pix.size();

		setVisible(true);

	}

	private void showPersonalDataDialog() {
		// show personal data dialog
		if (DataObject.secondRun == false) {
			PDDialog pdd = new PDDialog(this, data);
			// pdd.pack();
			// pdd.setLocation(200,200);
			// pdd.setSize(600,400);
			pdd.setVisible(true);
			// int pdX = (this.getWidth()/2) -(pdd.getWidth()/2);
			// int pdY = (this.getHeight()/2) - (pdd.getHeight()/2);
			// pdd.setLocation(pdX,pdY);
		}
	}

	private String getInstructions() {
		InputStream is = this.getClass()
				.getResourceAsStream("instructions.txt");
		BufferedReader input = new BufferedReader(new InputStreamReader(is));
		String line = "";
		String all = "";
		while (line != null) {
			try {
				line = input.readLine();
				if (line != null) {
					all = all + "\n" + line;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return all;
	}

	public void resetFrame(int pn) {
		data = new DataObject(pn);
		groupcount = 0;
		groups = new ArrayList();

		// picCatcher.removeAll();
		// dragPictureGUI.removeAll();

		newGroup("user group");

		validate();
		this.repaint();
	}

	public GroupPanel newGroup(String label) {
		++groupcount;

		GroupPanel lst = new GroupPanel(this, dragPictureGUI, picHandler,
				groupcount, label);
		focus = lst;
		// add first
		picCatcher.add(lst, 0);
		// picCatcher.add(lst);
		lst.ajustSize();

		return lst;
	}

	protected static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imageURL = DragPictureDemo.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			ImageIcon img = new ImageIcon(imageURL, description);
			img.setImage(img.getImage().getScaledInstance(125, 125,
					Image.SCALE_FAST));
			return img;
		}
	}

	static class WinAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			CatScan.wait = false;
			System.exit(0);
		}
	}

	public void ajustCompl() {
		int h = groups.size() * 50;
		for (GroupPanel gp : groups) {
			h = (int) gp.getSize().getHeight() + h;
		}
		int dim = picCatcher.getWidth();

		picCatcher.setMinimumSize(new Dimension(dim, h));
		picCatcher.setMaximumSize(new Dimension(dim, h));
		picCatcher.setPreferredSize(new Dimension(dim, h));
	}

}
