package xp;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class LikertPanel extends JPanel {
	int nChoices;
	ButtonGroup bGroup;
	ArrayList<JRadioButton> likertButtons;
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
	String question;

	public LikertPanel(int nChoices, String question) {
		this.question = question;
		GridLayout gl = new GridLayout(1, this.nChoices);
		setBorder(BorderFactory.createTitledBorder(question));
		setLayout(gl);
		this.nChoices = nChoices;
		likertButtons = new ArrayList<JRadioButton>();
		bGroup = new ButtonGroup();
		for (int i = 0; i < nChoices; i++) {

			JRadioButton butt = new JRadioButton();
			likertButtons.add(butt);
			butt.setHorizontalTextPosition(SwingConstants.CENTER);
			butt.setVerticalTextPosition(SwingConstants.BOTTOM);
			butt.setHorizontalAlignment(SwingConstants.CENTER);
			butt.setVerticalAlignment(SwingConstants.TOP);
			bGroup.add(butt);
			this.add(butt);
			butt.setText(" ");
		}

		JRadioButton butt = likertButtons.get(0);

		butt = likertButtons.get(nChoices - 1);

		setButtonText("Strongly agree", 0);
		setButtonText("Agree", 1);
		setButtonText("Neither agree nor disagree", 2);
		setButtonText("Disagree", 3);

		setButtonText("Strongly disagree", 4);

	}

	public void setButtonText(String text, int position) {
		JRadioButton butt = likertButtons.get(position);
		butt.setText(text);
		revalidate();
	}

	public String getQuestion() {
		return question;
	}

	public int getSelection() {

		for (int i = 0; i < nChoices; i++) {
			JRadioButton rButt = likertButtons.get(i);
			if (rButt.isSelected()) {
				return i + 1;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		JFrame app = new JFrame("trying Likert");
		String[] qs = likertQuestions;
		JPanel holder = new JPanel();
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
		for (String q : qs) {
			LikertPanel lp = new LikertPanel(5, q);
			holder.add(lp);
		}

		LikertPanel lp = new LikertPanel(7, "Do you like all kinds of cheese?");
		// lp.setButtonText("Love", 0);
		// lp.setButtonText("Hate", 6);

		app.add(holder);
		app.pack();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}