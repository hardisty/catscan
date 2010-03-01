package xp;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ConfigDialog extends JDialog implements ActionListener,
		ChangeListener, KeyListener, ItemListener {

	JFrame parentFrame;

	DataObject data;

	File dir = new File("events"); // initial image folder
	// File txt; //= new File("participant"+meo.participantNr+".txt");
	int retV = -1;

	// dialog components
	JSlider participantNrSlider;
	JTextField slText;
	JTextField cdText = new JTextField(dir.getAbsolutePath(), 30);
	JTextField cfText;// = new JTextField(txt.getAbsolutePath(), 30);
	JCheckBox demoModeCBox;
	JCheckBox secondRunModeCBox;

	public ConfigDialog(JFrame owner, DataObject d) {

		super(owner, true);

		parentFrame = owner;
		data = d;

		slText = new JTextField("00" + data.participantNr);
		slText.addKeyListener(this);

		File outputFile = new File("participant" + data.participantNr + ".txt");

		int counter = 0;
		while (outputFile.exists() || counter > 100) {
			data.participantNr = (data.participantNr % 100) + 1;
			outputFile = new File("participant" + data.participantNr + ".txt");
		}

		if (outputFile.exists()) {
			OkDialog dia = new OkDialog(parentFrame, "Warning",
					"File already exists!", "OK");
			dia.setVisible(true);
		}

		cfText = new JTextField("participant" + data.participantNr, 30);

		if (data.participantNr < 10) {
			slText.setText("00" + data.participantNr);
		} else if (data.participantNr < 100) {
			slText.setText("0" + data.participantNr);
		} else {
			slText.setText("" + data.participantNr);
		}

		slText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"check");

		/*
		 * slText.getActionMap().put("check", new AbstractAction() { public void
		 * actionPerformed(ActionEvent e) { try {
		 * participantNrSlider.setValue(Integer
		 * .valueOf(slText.getText()).intValue()); } catch (Exception ex) {} }
		 * });
		 */

		Container c;
		c = getContentPane();

		GridBagLayout gbl = new GridBagLayout();
		c.setLayout(gbl);

		JLabel slLabelT = new JLabel("Participant Number:");
		JLabel cdLabelT = new JLabel("Current Image Directory: ");
		JLabel cfLabelT = new JLabel("Current Output File Identifier:");
		gbl.setConstraints(slLabelT, mkGBC(0, 0, 2, 1));
		gbl.setConstraints(cdLabelT, mkGBC(0, 2, 2, 1));
		gbl.setConstraints(cfLabelT, mkGBC(0, 4, 2, 1));

		gbl.setConstraints(slText, mkGBC(3, 0, 2, 1));
		gbl.setConstraints(cdText, mkGBC(3, 2, 4, 1));
		gbl.setConstraints(cfText, mkGBC(3, 4, 4, 1));

		demoModeCBox = new JCheckBox("Demo Mode?");
		demoModeCBox.addItemListener(this);

		secondRunModeCBox = new JCheckBox("Second run?");
		secondRunModeCBox.addItemListener(this);

		GridBagConstraints gbc;

		participantNrSlider = new JSlider(JSlider.HORIZONTAL, 0, 100,
				data.participantNr);
		participantNrSlider.setMinorTickSpacing(1);
		participantNrSlider.setMajorTickSpacing(10);
		participantNrSlider.setPaintTicks(true);
		participantNrSlider.setPaintLabels(true);
		participantNrSlider.setSnapToTicks(true);
		participantNrSlider.addChangeListener(this);
		participantNrSlider.setPreferredSize(new Dimension(350, 42));
		gbc = mkGBC(5, 0, 5, 1);
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(participantNrSlider, gbc);

		gbc = mkGBC(0, 7, 1, 1);
		gbc.anchor = GridBagConstraints.SOUTH;
		gbl.setConstraints(demoModeCBox, gbc);

		gbc = mkGBC(0, 8, 1, 1);
		gbc.anchor = GridBagConstraints.SOUTH;
		gbl.setConstraints(secondRunModeCBox, gbc);

		gbc = mkGBC(6, 7, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		JButton startButton = new JButton("START");
		gbl.setConstraints(startButton, gbc);

		gbc = mkGBC(9, 2, 1, 1);
		gbc.anchor = GridBagConstraints.EAST;
		JButton choosedirButton = new JButton("dir ");
		gbl.setConstraints(choosedirButton, gbc);

		/*
		 * gbc = mkGBC(9,4,1,1); gbc.anchor = GridBagConstraints.EAST; JButton
		 * choosetxtButton = new JButton("file");
		 * gbl.setConstraints(choosetxtButton, gbc);
		 */

		JLabel emptyLabel = new JLabel(" \n ");
		gbc = mkGBC(5, 5, 1, 1);
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(emptyLabel, gbc);

		startButton.addActionListener(this);
		choosedirButton.addActionListener(this);
		// choosetxtButton.addActionListener(this);

		JLabel emptyLabel2 = new JLabel(" ");
		gbl.setConstraints(emptyLabel2, mkGBC(0, 1, 5, 1));

		JLabel emptyLabel3 = new JLabel(" ");
		gbl.setConstraints(emptyLabel3, mkGBC(0, 3, 5, 1));

		JLabel emptyLabel4 = new JLabel(" ");
		gbl.setConstraints(emptyLabel4, mkGBC(0, 6, 5, 1));

		c.add(emptyLabel2);
		c.add(emptyLabel3);
		c.add(emptyLabel4);
		c.add(slLabelT);
		c.add(cdLabelT);
		c.add(cfLabelT);
		c.add(slText);
		c.add(cdText);
		c.add(cfText);
		c.add(startButton);
		// c.add(choosetxtButton);
		c.add(choosedirButton);
		c.add(participantNrSlider);
		c.add(emptyLabel);

		c.add(demoModeCBox);
		c.add(secondRunModeCBox);

		addWindowListener(new WinAdapter());

		this.setSize(600, 400);
		setLocationRelativeTo(null);

		// cd.setVisible(true);

		// pack();
		// this.setVisible(true);
	}

	public void keyTyped(KeyEvent e) {
		try {
			participantNrSlider.setValue(Integer.valueOf(slText.getText())
					.intValue());
		} catch (Exception ex) {
		}
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		try {
			participantNrSlider.setValue(Integer.valueOf(slText.getText())
					.intValue());
		} catch (Exception ex) {
		}

	}

	public void stateChanged(ChangeEvent event) {
		File outputFile;

		JSlider sl = (JSlider) event.getSource();
		data.participantNr = sl.getValue();

		String participantNr = Integer.toString(data.participantNr);
		if (participantNr.length() == 1) {
			participantNr = "00" + participantNr;
		}
		if (participantNr.length() == 2) {
			participantNr = "0" + participantNr;
		}
		slText.setText(participantNr);
		outputFile = new File("participant" + data.participantNr + ".txt");
		cfText.setText("participant" + data.participantNr);
	}

	public void actionPerformed(ActionEvent event) {

		String cmd = event.getActionCommand();

		if (cmd.equals("START")) {
			File outputFile;
			try {
				data.participantNr = Integer.valueOf(slText.getText())
						.intValue();
				// data.lst = MepFile.openFiles(dir);
				outputFile = new File(FileIO.ResultsDir + "participant"
						+ data.participantNr + ".txt");

				if (outputFile.exists()) {

					OkDialog dia = new OkDialog(parentFrame, "Warning",
							"File already exists!", "OK");
					dia.setVisible(true);
				} else {
					data.fileIdentifier = cfText.getText();
					data.folder = dir.getName();
					System.out.println(dir.getName());

					setVisible(false);
				}
			} catch (Exception e) {
			}

			/*
			 * } else if (cmd.equals("file")) {
			 * 
			 * JFileChooser chooser = new JFileChooser();
			 * chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); retV =
			 * chooser.showOpenDialog(this); if (retV ==
			 * JFileChooser.APPROVE_OPTION) { data.outputFile =
			 * chooser.getSelectedFile();
			 * cfText.setText(data.outputFile.getAbsolutePath()); if
			 * (data.outputFile.exists()) { OkDialog dia = new
			 * OkDialog(parentFrame, "Warning", "File already exists!", "OK");
			 * dia.setVisible(true); } }
			 */

		} else if (cmd.equals("dir ")) {

			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			retV = chooser.showOpenDialog(this);
			if (retV == JFileChooser.APPROVE_OPTION) {
				dir = chooser.getSelectedFile();
				cdText.setText(dir.getAbsolutePath());
			}

		}
	}

	@Override
	protected void finalize() {
		System.out.println("ENDE...");
	}

	private GridBagConstraints mkGBC(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		return gbc;
	}

	static class WinAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);

		}
	}

	public void itemStateChanged(ItemEvent e) {
		// We got something from our checkbox!!
		if (e.getSource() != demoModeCBox && e.getSource() != secondRunModeCBox) {
			System.err.println("got unexpected itemStateChanged event");
			return;
		}
		if (demoModeCBox.isSelected()) {
			DataObject.demoMode = true;
		} else {
			DataObject.demoMode = false;
		}
		if (secondRunModeCBox.isSelected()) {
			DataObject.secondRun = true;
		} else {
			DataObject.secondRun = false;
		}

	}

}