package xp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GroupPanel extends MyPanel implements FocusListener, MouseListener {

	DragPictureDemo demo;

	PicInfo emptyPic;
	PictureTransferHandler picHandler;
	Integer number;
	int width;
	// results
	String name;
	String question1;
	String question2;
	BufferedImage image;

	int rows;

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		// g.drawString("that hurts!", 10, 15);

	}

	public GroupPanel(CatScanMainWin pf, DragPictureDemo dm,
			PictureTransferHandler pth, int nr, String label) {

		super(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel groupLabel = new JLabel(label);
		this.add(groupLabel);
		addFocusListener(this);
		addMouseListener(this);

		setTransferHandler(pth);

		pix = new ArrayList();

		parentFrame = pf;
		demo = dm;
		picHandler = pth;
		number = nr;
		name = "Group " + number.toString();
		width = parentFrame.picCatcher.getWidth() - 30;

		// clobber here? 667
		width = 850;
		// showPanel = new JPanel(new GridLayout(rows, columns,10,10));
		showPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

		// this.setSize(this.parentFrame.compl.getWidth(),rows*(picsize+25));
		ajustSize();

		/*
		 * int width = this.parentFrame.compl.getWidth()-50;
		 * 
		 * showPanel.setPreferredSize(new Dimension (width-200,116));
		 * showPanel.setMaximumSize(new Dimension (width-200,116));
		 * this.setMinimumSize(new Dimension (width,136));
		 * this.setPreferredSize(new Dimension (width,136));
		 * this.setMaximumSize(new Dimension (width,136));
		 */
		// showPanel.setBackground(Color.CYAN);
		// emptyPic = new PicInfo((String)null, this, picHandler, -1);
		/*
		 * JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
		 * //JButton cn = new JButton("Change name");
		 * //cn.addActionListener(this); //buttons.add(cn); JButton dg = new
		 * JButton("Delete group"); dg.addActionListener(this); buttons.add(dg);
		 * 
		 * this.add(buttons, BorderLayout.NORTH);
		 */

		this.add(showPanel);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createLineBorder(Color.RED, 5), BorderFactory
				.createLoweredBevelBorder()));
		// BorderFactory.createTitledBorder(
		// BorderFactory.createEtchedBorder(), "Group "+number.toString()));

		requestFocusInWindow();
		parentFrame.focus = this;

		ajustSize();
		validate();
		this.repaint();

	}

	public void actionPerformed(ActionEvent arg0) {

		// String cmd = arg0.getActionCommand();

		// NewGroup-Button

		/*
		 * if (cmd.equals("Change name")) { JComponent grpContainer =
		 * ((JComponent
		 * )((Container)(arg0.getSource())).getParent().getParent()); NameInput
		 * fg = new NameInput(parentFrame,
		 * ((TitledBorder)grpContainer.getBorder()).getTitle());
		 * fg.setVisible(true); fg.pack();
		 * grpContainer.setBorder(BorderFactory.createTitledBorder(
		 * BorderFactory.createEtchedBorder(), fg.getResult()));
		 * //((TitledBorder)grpContainer.getBorder()).setTitle("BLA");
		 * //contentPane.validate(); }
		 */

		/*
		 * if (cmd.equals("Delete group")) { parentFrame.groupcount--;
		 * dragPictureGUI.addPicLst(pix); Container parent = this.getParent();
		 * parent.remove(this); if (parentFrame.groupcount==0) {
		 * parentFrame.newGroup(); } else { parent.setLayout(new
		 * GridLayout(parentFrame.groupcount, 1)); } parent.validate(); }
		 */
	}

	public void mouseClicked(MouseEvent e) {
		// Since the user clicked on us, let's get focus!
		requestFocusInWindow();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void focusGained(FocusEvent e) {
		// Draw the component with a red border
		// indicating that it has focus.
		parentFrame.focus.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.DARK_GRAY, 5),
				BorderFactory.createLoweredBevelBorder()));
		parentFrame.focus = this;
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createLineBorder(Color.RED, 5), BorderFactory
				.createLoweredBevelBorder()));
		// this.setBorder(BorderFactory.createLineBorder(Color.RED));
		validate();
		this.repaint();
	}

	public void focusLost(FocusEvent e) {
		// Draw the component with a black border
		// indicating that it doesn't have focus.
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createLineBorder(Color.DARK_GRAY, 5), BorderFactory
				.createLoweredBevelBorder()));
		// this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		validate();
		this.repaint();
	}

	@Override
	public void addPic(PicInfo pi) {
		System.out.println("adding pic " + pi.path);
		parentFrame.focus.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.DARK_GRAY, 5),
				BorderFactory.createLoweredBevelBorder()));
		parentFrame.focus = this;
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createLineBorder(Color.RED, 5), BorderFactory
				.createLoweredBevelBorder()));
		showPanel.add(pi.picdata);
		demo.pix.trimToSize();
		pix.trimToSize();

		ajustSize();
		validate();
		this.repaint();

	};

	@Override
	public void addPic(Image image, int nr, String path) {
		System.out.println("adding pic nr " + nr + " " + path);
		pix.add(new PicInfo(image, this, picHandler, nr, path));

		parentFrame.data.log.add("Add picture " + path + " to group " + number
				+ " at " + System.currentTimeMillis() + "\n");

		pix.trimToSize();
		demo.pix.trimToSize();
		// System.out.println(rows+"##"+columns);
		// this.setSize(this.parentFrame.compl.getWidth(),rows*(picsize+25));
		ajustSize();
		validate();
		this.repaint();

	}

	@Override
	public void removePic(PicInfo p) {
		pix.remove(p);

		// showPanel.setLayout(new GridLayout(rows,columns,10,10));

		showPanel.remove(p.picdata);
		parentFrame.data.log.add("Remove picture " + p.path + " from group "
				+ number + " at " + System.currentTimeMillis() + "\n");
		pix.remove(p);
		pix.trimToSize();
		ajustSize();
	}

	@Override
	public void newEmpty() {
		pix.add(emptyPic);
		emptyPic = new PicInfo((String) null, this, picHandler, -1);
	}

	public void ajustSize() {

		int columns = (width + 120) / DataObject.pictureSize;

		rows = columns == 0 ? 1 : ((pix.size()) / columns) + 1;

		if (rows == 0) {
			rows = 1;
		}

		System.out.println("" + columns + " " + pix.size() + " = " + rows);

		showPanel.setPreferredSize(new Dimension(width - 20, rows
				* DataObject.pictureSize));
		showPanel.setMaximumSize(new Dimension(width - 20, rows
				* DataObject.pictureSize));

		// this.setMinimumSize(new Dimension(width,rows*116+20));
		setPreferredSize(new Dimension(width, rows * DataObject.pictureSize
				+ 20));
		setMaximumSize(new Dimension(width, rows * DataObject.pictureSize + 20));
		validate();
		// revalidate();
		this.repaint();
		parentFrame.ajustCompl();

		parentFrame.validate();
		parentFrame.repaint();
	}

	public void ajustSize2() {
		revalidate();
		// parentFrame.pack();
		this.repaint();
	}
}
