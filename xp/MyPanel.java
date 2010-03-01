package xp;

import java.awt.Image;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class MyPanel extends JPanel {

	JPanel showPanel;
	CatScanMainWin parentFrame;
	ArrayList pix;
	public boolean addPic = true;

	public MyPanel(LayoutManager l) {
		super(l);

	}

	public abstract void newEmpty();

	public abstract void removePic(PicInfo p);

	public abstract void addPic(PicInfo p);

	public abstract void addPic(Image im, int nr, String path);

}
