package xp;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class OkDialog
    extends Dialog
    implements ActionListener

{
    String result;

    public OkDialog(JFrame owner,
		       String title,
		       String msg,
		       String btxt)
    {
	super(owner, title, true);
	setLayout(new BorderLayout());
	setResizable(false);
	setLocation(300,300);

	add(new Label(msg), BorderLayout.CENTER);

	Panel panel = new Panel();
	panel.setLayout(new FlowLayout(FlowLayout.CENTER));
	Button button = new Button(btxt);
	button.addActionListener(this);
	panel.add(button);
	add(panel, BorderLayout.SOUTH);
	pack();
    }


    public void actionPerformed(ActionEvent event)
    {
	result = event.getActionCommand();
	setVisible(false);
	dispose();
    }

}
