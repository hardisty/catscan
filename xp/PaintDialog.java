package xp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintDialog extends JDialog implements ActionListener {

	PaintPanel pp;

	public PaintDialog(JFrame owner) {
		super(owner, true);

		JPanel cp = new JPanel(new BorderLayout(10, 10));
		setSize(300, 300);
		setResizable(false);
		// setResizable(false); //Hinweis im Text beachten
		Point parloc = owner.getLocation();
		setLocation(parloc.x + 50, parloc.y + 50);

		// Buttonbar
		JPanel bbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton ok = new JButton("Ok");
		JButton reset = new JButton("Reset");
		ok.addActionListener(this);
		reset.addActionListener(this);
		bbar.add(ok);
		bbar.add(reset);

		pp = new PaintPanel();
		// pp.addMouseListener(new MouseListener(this));

		cp.add(pp, BorderLayout.CENTER);
		cp.add(bbar, BorderLayout.SOUTH);

		// image

		// Make this dialog display it.
		setContentPane(cp);

	}

	public void actionPerformed(ActionEvent event) {

		String cmd = event.getActionCommand();

		if (cmd.equals("Ok")) {
			// pp.saveImage();
			setVisible(false);
		}

		if (cmd.equals("Reset")) {
			pp.lines = new ArrayList();
			pp.current = new ArrayList();
			pp.lines.add(pp.current);
			pp.repaint();

		}
	}
}
