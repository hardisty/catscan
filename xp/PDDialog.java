package xp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PDDialog extends JDialog implements ActionListener,
		ChangeListener, KeyListener {

	DataObject data;

	int retV = -1;

	JSlider ageSlider;
	JTextField ageText;
	JTextField sText = new JTextField("", 20);
	JRadioButton maleRB = new JRadioButton("Male", false);
	JRadioButton femaleRB = new JRadioButton("Female", true);
	JRadioButton leftHandedRB = new JRadioButton("Left", false);
	JRadioButton rightHandedRB = new JRadioButton("Right", true);
	JFrame owner;
	LikertGroupPanel lik;
	ButtonGroup geoSpatialGroup;
	JRadioButton geoSpatialYes;
	JRadioButton geoSpatialNo;
	JRadioButton geographyYes;
	JRadioButton geographyNo;
	JRadioButton colorBlindYes;
	JRadioButton colorBlindNo;

	public PDDialog(JFrame owner, DataObject dob) {

		super(owner, true);
		setUndecorated(true);
		this.owner = owner;
		data = dob;

		JPanel newQuestionHolder = new JPanel();
		newQuestionHolder.setLayout(new BoxLayout(newQuestionHolder,
				BoxLayout.Y_AXIS));

		JPanel geoSpatialIntPanel = new JPanel();
		geoSpatialYes = new JRadioButton("Yes");
		geoSpatialNo = new JRadioButton("No");
		geoSpatialGroup = new ButtonGroup();
		geoSpatialGroup.add(geoSpatialNo);
		geoSpatialGroup.add(geoSpatialYes);
		geoSpatialIntPanel
				.add(new JLabel(
						"Have you taken courses in the Geo-Spatial Intelligence Program?"));
		geoSpatialIntPanel.add(geoSpatialNo);
		geoSpatialIntPanel.add(geoSpatialYes);
		geoSpatialIntPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		newQuestionHolder.add(geoSpatialIntPanel);

		JPanel geographyPanel = new JPanel();
		geographyYes = new JRadioButton("Yes");
		geographyNo = new JRadioButton("No");
		ButtonGroup geographyGroup = new ButtonGroup();
		geographyGroup.add(geographyNo);
		geographyGroup.add(geographyYes);
		geographyPanel.add(new JLabel(
				"Have you taken courses in the Geography?"));
		geographyPanel.add(geographyNo);
		geographyPanel.add(geographyYes);
		geographyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		newQuestionHolder.add(geographyPanel);

		JPanel colorBlindPanel = new JPanel();
		colorBlindYes = new JRadioButton("Yes");
		colorBlindNo = new JRadioButton("No");
		ButtonGroup colorBlindGroup = new ButtonGroup();
		colorBlindGroup.add(geoSpatialNo);
		colorBlindGroup.add(geoSpatialYes);
		colorBlindPanel.add(new JLabel("Are you aware of being color blind?"));
		colorBlindPanel.add(colorBlindNo);
		colorBlindPanel.add(colorBlindYes);
		colorBlindPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		newQuestionHolder.add(colorBlindPanel);

		JPanel oldQuestionPanel = new JPanel();
		GridBagLayout gbLayout = new GridBagLayout();
		oldQuestionPanel.setLayout(gbLayout);

		ageText = new JTextField("" + data.age, 3);
		ageText.addKeyListener(this);

		JLabel ageLabel = new JLabel("Age: ");
		JLabel genderLabel = new JLabel("Gender: ");
		JLabel handedLabel = new JLabel("Handedness: ");
		JLabel fieldOfStudyLabel = new JLabel("Field of study: ");
		gbLayout.setConstraints(ageLabel, makeGBC(0, 3, 2, 1));
		gbLayout.setConstraints(genderLabel, makeGBC(0, 5, 2, 1));
		gbLayout.setConstraints(handedLabel, makeGBC(0, 7, 2, 1));
		gbLayout.setConstraints(fieldOfStudyLabel, makeGBC(0, 9, 2, 1));

		maleRB.setActionCommand("m");
		femaleRB.setActionCommand("f");
		leftHandedRB.setActionCommand("l");
		rightHandedRB.setActionCommand("r");
		gbLayout.setConstraints(ageText, makeGBC(3, 3, 2, 1));
		gbLayout.setConstraints(maleRB, makeGBC(3, 5, 1, 1));
		gbLayout.setConstraints(femaleRB, makeGBC(4, 5, 1, 1));
		gbLayout.setConstraints(leftHandedRB, makeGBC(3, 7, 1, 1));
		gbLayout.setConstraints(rightHandedRB, makeGBC(4, 7, 1, 1));
		gbLayout.setConstraints(sText, makeGBC(3, 9, 3, 1));

		GridBagConstraints gbc;

		ageSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
		ageSlider.setMinimum(10);
		ageSlider.setMaximum(100);
		ageSlider.setValue(21);
		ageSlider.setMinorTickSpacing(1);
		ageSlider.setMajorTickSpacing(10);
		ageSlider.setPaintTicks(true);
		ageSlider.setPaintLabels(true);
		ageSlider.setSnapToTicks(true);
		ageSlider.addChangeListener(this);
		ageSlider.setPreferredSize(new Dimension(350, 42));
		gbc = makeGBC(4, 3, 5, 1);
		gbc.anchor = GridBagConstraints.EAST;
		gbLayout.setConstraints(ageSlider, gbc);

		gbc = makeGBC(5, 10, 1, 1);
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel emptyLabel = new JLabel(" ");
		gbLayout.setConstraints(emptyLabel, gbc);

		gbc = makeGBC(5, 12, 1, 1);
		gbc.anchor = GridBagConstraints.CENTER;
		JButton startButton = new JButton("Finish");
		gbLayout.setConstraints(startButton, gbc);

		startButton.addActionListener(this);
		maleRB.addActionListener(this);
		femaleRB.addActionListener(this);
		rightHandedRB.addActionListener(this);
		leftHandedRB.addActionListener(this);

		JLabel emptyLabel2 = new JLabel(" ");
		gbLayout.setConstraints(emptyLabel2, makeGBC(0, 4, 5, 1));

		JLabel emptyLabel3 = new JLabel(" ");
		gbLayout.setConstraints(emptyLabel3, makeGBC(0, 6, 5, 1));

		JLabel emptyLabel4 = new JLabel(" ");
		gbLayout.setConstraints(emptyLabel4, makeGBC(0, 8, 5, 1));

		JLabel emptyLabel5 = new JLabel(" ");
		gbLayout.setConstraints(emptyLabel5, makeGBC(0, 11, 5, 1));

		JLabel emptyLabel6 = new JLabel(" ");
		gbLayout.setConstraints(emptyLabel6, makeGBC(0, 1, 5, 2));

		// intro.setBorder(BorderFactory.createEtchedBorder());

		// c.add(intro);
		oldQuestionPanel.add(emptyLabel6);
		oldQuestionPanel.add(emptyLabel5);
		oldQuestionPanel.add(emptyLabel4);
		oldQuestionPanel.add(emptyLabel2);
		oldQuestionPanel.add(emptyLabel3);
		oldQuestionPanel.add(ageLabel);
		oldQuestionPanel.add(genderLabel);
		// oldQuestionPanel.add(handedLabel);
		oldQuestionPanel.add(ageText);
		oldQuestionPanel.add(maleRB);
		oldQuestionPanel.add(femaleRB);
		// oldQuestionPanel.add(leftHandedRB);
		// oldQuestionPanel.add(rightHandedRB);
		oldQuestionPanel.add(startButton);
		oldQuestionPanel.add(ageSlider);
		oldQuestionPanel.add(emptyLabel);
		oldQuestionPanel.add(fieldOfStudyLabel);
		oldQuestionPanel.add(sText);

		JPanel oldAndNewHolder = new JPanel();
		oldAndNewHolder.setLayout(new BoxLayout(oldAndNewHolder,
				BoxLayout.Y_AXIS));

		oldAndNewHolder.add(newQuestionHolder);
		oldAndNewHolder.add(oldQuestionPanel);
		oldAndNewHolder.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));

		this.add(oldAndNewHolder);
		lik = new LikertGroupPanel(owner, data);
		lik.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// XXX remove likert panel
		// this.add(lik, BorderLayout.EAST);
		JTextArea intro = new JTextArea(
				"  Please answer the following questions. \n  The answers to these questions, like all the data we collect, are not linked to your identity, that is, they are anonymous.");

		intro.setBackground(getBackground());

		intro.setEditable(false);
		intro.setFont(new Font(getFont().getFontName(), Font.BOLD, 14));
		gbLayout.setConstraints(intro, makeGBC(0, 0, 9, 1));
		// intro.setBorder(BorderFactory.createLineBorder(Color.red));
		intro.setMaximumSize(new Dimension(200, 50));
		JPanel introPan = new JPanel(new FlowLayout(FlowLayout.CENTER));
		introPan.add(intro);
		introPan.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.add(introPan, BorderLayout.NORTH);
		addWindowListener(new WinAdapter());
		// setSize(600, 400);
		// setLocationRelativeTo(null);

		JPanel spacer = new JPanel();
		spacer.setMinimumSize(new Dimension(10, 10));
		this.add(spacer, BorderLayout.WEST);

		pack();
	}

	public void keyTyped(KeyEvent e) {
		try {
			ageSlider.setValue(Integer.valueOf(ageText.getText()).intValue());
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		try {
			if (ageText.getText().length() > 3) {
				ageText.setText(ageText.getText(0, 2));
			}
			ageSlider.setValue(Integer.valueOf(ageText.getText()).intValue());
		} catch (Exception ex) {
		}

	}

	public void stateChanged(ChangeEvent event) {
		JSlider sl = (JSlider) event.getSource();
		data.age = sl.getValue();

		String number = Integer.toString(data.age);
		// if (number.length() == 1) number = "00"+number;
		// if (number.length() == 2) number = "0"+number;
		ageText.setText(number);
	}

	private boolean dataEntered() {

		if (DataObject.demoMode == true) {
			return true;
		}

		if (sText.getText().equals("")) {

			return false;
		}
		if (lik.allChoicesMade() == false) {
			return true;
		}

		return true;
	}

	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		if (cmd.equals("Finish") && dataEntered() == false) {
			JOptionPane.showMessageDialog(this,
					"Plese complete all the questions.");
		}

		if (cmd.equals("Finish") && dataEntered()) {

			try {
				data.age = Integer.valueOf(ageText.getText()).intValue();
				System.out.println("#" + sText.getText() + "#");
				data.subject = sText.getText();
				if (maleRB.isSelected()) {
					data.gender = "male";
				} else {
					data.gender = "female";
				}
				// if (leftHandedRB.isSelected()) {
				// data.handedness = "left";
				// } else {
				// data.handedness = "right";
				// }
				if (geoSpatialYes.isSelected()) {
					data.geoIntelligence = true;
				} else {
					data.geoIntelligence = false;
				}

				if (geographyYes.isSelected()) {
					data.geography = true;
				} else {
					data.geography = false;
				}

				if (colorBlindYes.isSelected()) {
					data.colorBlind = true;
				} else {
					data.colorBlind = false;
				}

				for (LikertPanel likPanel : lik.likPanels) {
					System.out.println(likPanel.getQuestion()
							+ likPanel.getSelection());
					// data.likertQuestions.add(likPanel.getQuestion());
					// data.likertAnswers.add(likPanel.getSelection());

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Nur Ziffern!");
			}
			// MepWin frame = new MepWin(data);
			// frame.setLocation(162, 0);
			// frame.setSize(700, 740);

			setVisible(false);
			// frame.setVisible(true);

		} else if (cmd.equals("m")) {
			maleRB.setSelected(true);
			femaleRB.setSelected(false);

		} else if (cmd.equals("f")) {
			femaleRB.setSelected(true);
			maleRB.setSelected(false);
		} else if (cmd.equals("l")) {
			leftHandedRB.setSelected(true);
			rightHandedRB.setSelected(false);

		} else if (cmd.equals("r")) {
			rightHandedRB.setSelected(true);
			leftHandedRB.setSelected(false);
		}

	}

	private GridBagConstraints makeGBC(int x, int y, int width, int height) {
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
			// System.exit(0);

		}
	}

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DataObject dataO = new DataObject(0);
		PDDialog dia = new PDDialog(app, dataO);
		dia.setVisible(true);
	}

}
