package xp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class TextScreen extends JDialog implements ActionListener {
	JButton start;

	static boolean isLong = false;
	static int seconds = 60;
	boolean promptForText = false;

	public TextScreen(String text, Frame owner, int x, int y,
			boolean promptForText) {

		super(owner, true);
		this.promptForText = promptForText;
		if (isLong) {
			setUndecorated(true);
		}
		// setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout(20, 20));
		this.setSize(x, y);
		setLocationRelativeTo(null);
		// this.setUndecorated(true);

		JPanel txtfld = new JPanel(new BorderLayout(20, 20));

		JTextPane txta = new JTextPane();
		txta.setContentType("text/html");
		txta.setText(text);
		txta.setFont(new Font(getFont().getFontName(),
				Font.LAYOUT_LEFT_TO_RIGHT, 14));
		txta.setPreferredSize(new Dimension(x - 40, y - 40));

		txta.setBackground(getBackground());
		txta.setEditable(false);
		// txta.setLineWrap(true);
		// txta.setWrapStyleWord(true);

		txtfld.add(txta, BorderLayout.CENTER);
		txtfld.setBorder(new LineBorder(getBackground(), 15));
		this.add(txtfld, BorderLayout.CENTER);

		start = new JButton("OK");
		if (DataObject.demoMode == false && isLong) {

			start.setEnabled(false);

		}
		start.addActionListener(this);
		JPanel bbar = new JPanel();
		bbar.setLayout(new BoxLayout(bbar, BoxLayout.Y_AXIS));
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		if (promptForText) {
			bbar.add(this.makeQuestionPanel());
			bbar.add(Box.createRigidArea(new Dimension(10, 10)));
		}
		bbar.add(start);
		bbar.add(Box.createRigidArea(new Dimension(10, 10)));

		this.add(bbar, BorderLayout.SOUTH);

	}

	JTextPane userText;

	private JPanel makeQuestionPanel() {
		JPanel qPan = new JPanel();
		JLabel lab = new JLabel("What do the icons depict?");
		qPan.add(lab);
		userText = new JTextPane();
		userText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		Dimension size = new Dimension(150, 50);
		userText.setPreferredSize(size);
		qPan.add(userText);

		return qPan;
	}

	private JPanel makePatternPanel() {
		JPanel patternPanel = new JPanel();

		FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 20, 20);
		patternPanel.setLayout(fl);
		Image blueImage = getToolkit().getImage("events/example/blue.gif");
		Image greenImage = getToolkit().getImage("events/example/green.gif");
		Image blueAndGreenImage = getToolkit().getImage(
				"events/example/blueandgreen.gif");

		Image dispersedImage = getToolkit().getImage(
				"events/example/dispersed.gif");

		ImageIcon blueIcon = new ImageIcon(blueImage);
		ImageIcon greenIcon = new ImageIcon(greenImage);
		ImageIcon blueAndGreenIcon = new ImageIcon(blueAndGreenImage);
		ImageIcon dispersedIcon = new ImageIcon(dispersedImage);

		JLabel blueLabel = new JLabel("Only blue is significantly clustered",
				blueIcon, JLabel.CENTER);
		JLabel greenLabel = new JLabel("Only green is significantly clustered",
				greenIcon, JLabel.CENTER);
		JLabel blueAndGreenLabel = new JLabel(
				"Both blue and green are significantly clustered",
				blueAndGreenIcon, JLabel.CENTER);
		JLabel dispersedLabel = new JLabel("Dispersed pattern", dispersedIcon,
				JLabel.CENTER);
		blueLabel.setVerticalTextPosition(JLabel.BOTTOM);
		blueLabel.setHorizontalTextPosition(JLabel.CENTER);

		greenLabel.setVerticalTextPosition(JLabel.BOTTOM);
		greenLabel.setHorizontalTextPosition(JLabel.CENTER);

		blueAndGreenLabel.setVerticalTextPosition(JLabel.BOTTOM);
		blueAndGreenLabel.setHorizontalTextPosition(JLabel.CENTER);

		dispersedLabel.setVerticalTextPosition(JLabel.BOTTOM);
		dispersedLabel.setHorizontalTextPosition(JLabel.CENTER);

		patternPanel.add(blueLabel);
		patternPanel.add(greenLabel);
		patternPanel.add(blueAndGreenLabel);
		patternPanel.add(dispersedLabel);
		return patternPanel;
	}

	public void actionPerformed(ActionEvent event) {

		String cmd = event.getActionCommand();
		if (cmd.equals("OK") && this.userText != null
				&& this.userText.getText().length() <= 0 && this.promptForText
				&& DataObject.demoMode == false) {
			JOptionPane.showMessageDialog(this,
					"Please answer the question before proceeding.");
			return;
		}

		if (cmd.equals("OK")) {
			setVisible(false);
		}

	}

	public void enableButtonAfterDelay(int seconds) {
		runTimer(start, seconds);
	}

	private void runTimer(JButton butt, int seconds) {
		MyTimerTask myTask = new MyTimerTask(butt);
		Timer myTimer = new Timer();

		/*
		 * Set an initial delay of 30 seconds, then repeat every half second.
		 */

		myTimer.schedule(myTask, 1000 * seconds, 500);

	}

	@Override
	public void setVisible(boolean viz) {
		enableButtonAfterDelay(TextScreen.seconds);
		super.setVisible(viz);

	}

	class MyTimerTask extends TimerTask {

		JButton butt;

		public MyTimerTask(JButton butt) {
			this.butt = butt;
		}

		@Override
		public void run() {
			System.out.println("Timer task executed.");

			butt.setEnabled(true);
			cancel();
		}
	}
}
