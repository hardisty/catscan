package xp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LikertGroupPanel extends JPanel implements ActionListener {

	static String[] likertQuestions = { "I am very good at reading maps",
			"I am a visual person", "I am good at statistics",
			"I am a fan of Google maps",
			"I am aware of the concept of statistical significance",
			"I am knowledgeable on probability distributions",
			"I like reading maps",
			"I actively gather information by using maps",
			"I am familiar with the concept of randomness",
			"I have knowledge about visual perception",
			"I know about Tobler’s first law of geography",
			"I am familiar with spatial autocorrelation",
			"I am familiar with spatial analysis",
			"I am familiar with join count statistics" };

	JButton finishButton;
	ArrayList<LikertPanel> likPanels;

	public LikertGroupPanel(JFrame owner, DataObject dob) {
		likPanels = new ArrayList<LikertPanel>();
		String[] qs = likertQuestions;
		JPanel holder = new JPanel();
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
		for (String q : qs) {
			LikertPanel lp = new LikertPanel(5, q);
			holder.add(lp);
			likPanels.add(lp);
		}

		this.add(holder);
		JPanel finishButtonHolder = new JPanel();
		finishButton = new JButton("Finish");
		// finishButton.setMaximumSize(new Dimension(200, 100));
		// finishButton.setPreferredSize(new Dimension(200, 100));
		finishButton.addActionListener(this);
		finishButtonHolder.add(finishButton);
		// this.add(finishButtonHolder, BorderLayout.SOUTH);
		// setLocationRelativeTo(null);

	}

	public boolean allChoicesMade() {
		for (LikertPanel likPan : likPanels) {
			if (likPan.getSelection() < 0) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {
		JFrame app = new JFrame("trying Likert");

		app.pack();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DataObject dataO = new DataObject(1);
		LikertGroupPanel lik = new LikertGroupPanel(app, dataO);
		app.add(lik);
		app.pack();

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}