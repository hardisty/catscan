package xp;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PicInfo implements MouseMotionListener {

	DTPicture picdata;
	String path;
	MyPanel panel;

	int filenr;

	// public PicInfo(DTPicture p1, MyPanel p2, PictureTransferHandler
	// picHandler,
	// String pth) {
	// panel = p2;
	// picdata = p1;// new DTPicture(p1.getImage(),this, p1.number);
	// picdata.info = this;
	// picdata.setTransferHandler(picHandler);
	// panel.addPic(this);
	// path = pth;
	// }

	public PicInfo(String p1, MyPanel p2, PictureTransferHandler picHandler,
			int nr) {
		path = p1;
		panel = p2;

		if (p1 != null) {
			// picdata = new DTPicture(DragPictureDemo.createImageIcon(path,
			// path).getImage(), this, nr);
			picdata = new DTPicture(panel.getToolkit().getImage(path), this, nr);
		} else {
			picdata = new DTPicture(null, this, -1);
		}
		picdata.setTransferHandler(picHandler);
		picdata.setPreferredSize(new Dimension(120, 120));
		panel.addPic(this);
		picdata.addMouseMotionListener(this);
	}

	public PicInfo(Image p1, MyPanel p2, PictureTransferHandler picHandler,
			int nr, String pth) {

		panel = p2;

		if (p1 != null) {
			picdata = new DTPicture(p1, this, nr);
		} else {
			picdata = new DTPicture(null, this, -1);
		}
		picdata.setTransferHandler(picHandler);
		picdata.setPreferredSize(new Dimension(120, 120));
		panel.addPic(this);
		path = pth;
		// p2.pix.add(this);
		picdata.addMouseMotionListener(this);
	}

	public void mouseDragged(MouseEvent arg0) {
		// Cursor curse = Toolkit.getDefaultToolkit().createCustomCursor(
		// picdata.image, new Point(10, 10), "");
		// panel.parentFrame.setCursor(curse);

	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
