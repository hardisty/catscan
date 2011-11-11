package xp;

/*
 * DragPictureDemo.java is a 1.4 example that
 * requires the following files:
 *     Picture.java
 *     DTPicture.java
 *     PictureTransferHandler.java
 *     images/Maya.jpg
 *     images/Anya.jpg
 *     images/Laine.jpg
 *     images/Cosmo.jpg
 *     images/Adele.jpg
 *     images/Alexi.jpg
 */
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DragPictureDemo extends MyPanel {

	PictureTransferHandler picHandler;
	PicInfo emptyPic;

	public DragPictureDemo(DataObject dob, PictureTransferHandler ph,
			CatScanMainWin parent) {
		super(new GridLayout(1, 1));
		picHandler = ph;
		setTransferHandler(ph);
		parentFrame = parent;

		showPanel = new JPanel();

		// showPanel.setBackground(Color.CYAN);

		if (parent.trail) {
			pix = openFiles(dob.folder + "/trail", this, picHandler,
					dob.participantNr);
		} else {
			pix = openFiles(dob.folder, this, picHandler, dob.participantNr);
		}

		showPanel.setLayout(new GridLayout(pix.size() / 7 + 1, 7, 10, 10));

		// emptyPic = new PicInfo((String)null, this, picHandler, -1);

		// setPreferredSize(new Dimension(450, 630));
		add(showPanel);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		dob.maxpic = pix.size();
		// this.setMinimumSize(this.getSize());
		// this.setMaximumSize(this.getSize());
		// this.setPreferredSize(this.getSize());
	}

	public void addPicLst(ArrayList news) {
		System.out.println("addPicLst(ArrayList news)");
		int i;
		int len = news.size();
		// showPanel.remove(emptyPic.picdata);

		// showPanel.setLayout(new GridLayout(((int)(news.size()/3)),3, 10,10));

		for (i = 0; i < len; ++i) {
			PicInfo buf = (PicInfo) news.get(i);
			addPic(buf.picdata.image, buf.picdata.number, buf.path);
		}
		// emptyPic = new PicInfo((String)null, this, picHandler, -1);

		parentFrame.validate();
		parentFrame.repaint();
	}

	@Override
	public void addPic(PicInfo p) {
		// System.out.println("addPic(PicInfo p)");

		Component[] comps = showPanel.getComponents();
		ArrayList<Component> nullComps = new ArrayList<Component>();

		for (Component comp : comps) {

			DTPicture picHolder = (DTPicture) comp;
			if (picHolder.image == null) {
				nullComps.add(comp);

			}

		}
		int nullPlace = 0;
		Component nearest = null;
		if (nullComps.size() > 0) {
			nearest = findNearestComp(nullComps);
			for (Component comp : comps) {
				if (comp == nearest) {
					break;
				}
				nullPlace++;

			}
		}

		if (nullComps.size() > 0) {
			showPanel.remove(nearest);
			showPanel.add(p.picdata, nullPlace);
		} else {

			showPanel.add(p.picdata);
		}

	}

	private Component findNearestComp(ArrayList<Component> comps) {
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point p = pi.getLocation();
		Component returnComp = null;
		int minDist = Integer.MAX_VALUE;

		for (Component comp : comps) {
			Point location = comp.getLocation();
			int midX = location.x + (comp.getWidth() / 2);
			int midY = location.y + (comp.getHeight() / 2);

			int xDist = Math.abs(p.x - midX);
			int yDist = Math.abs(p.y - midY);
			int dist = xDist + yDist;
			if (dist < minDist) {
				minDist = dist;
				returnComp = comp;
			}
		}

		return returnComp;
	}

	@Override
	public void addPic(Image image, int nr, String path) {
		System.out.println("addPic(Image image, int nr, String path)");

		pix.add(new PicInfo(image, this, picHandler, nr, path));

		revalidate();
		this.repaint();
	}

	void compactPics() {
		Component[] comps = showPanel.getComponents();
		for (Component comp : comps) {
			DTPicture picHolder = (DTPicture) comp;
			if (picHolder.image == null) {
				showPanel.remove(comp);
			}

		}
		showPanel.revalidate();
		showPanel.repaint();
	}

	@Override
	public void removePic(PicInfo p) {
		pix.remove(p);
		// System.out.println(this.getLayout().getClass().getCanonicalName());

		// if (!(showPanel.getLayout() instanceof FlowLayout)) {
		// showPanel.setLayout(new GridLayout(
		// showPanel.getComponentCount() / 7 + 1, 7, 10, 10));
		// }

		// showPanel.remove(p.picdata);
		pix.remove(p);
		pix.trimToSize();

		// int columns = (int)((this.getWidth()-20)/(100+10));

		// int rows = columns == 0 ? 1 : (int)(pix.size()/columns)+1;

		// showPanel.setLayout(new GridLayout(24,7,10,10));
		// showPanel.setLayout(new GridLayout(pix.size()/7,7,10, 10));
		// validate();
		this.repaint();
	}

	@Override
	public void newEmpty() {
		pix.add(emptyPic);
		emptyPic = new PicInfo((String) null, this, picHandler, -1);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imageURL = DragPictureDemo.class.getResource(path);
		// getToolkit().getImage(((MepObj)imgLst.get(r)).filename).
		// getScaledInstance(500,500,Image.SCALE_FAST);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {

			ImageIcon img = new ImageIcon(imageURL, description);
			img.setImage(img.getImage().getScaledInstance(100, 100,
					Image.SCALE_FAST));

			return img;
		}
	}

	public ImageIcon createImageIcon(String path) {
		return new ImageIcon(getToolkit().getImage(path));
	}

	public static ArrayList openFiles(String dir, MyPanel panel,
			PictureTransferHandler ph, int seed) {

		String[] filenames = (new File(dir)).list(new MyFilter());
		Arrays.sort(filenames, new MyComp());

		String x = "";
		// PicInfo[] mos = new PicInfo[filenames.length];
		ArrayList lst = new ArrayList();
		ArrayList buf = new ArrayList();
		Random rnd = new Random(seed);
		int rndnr;
		int rnr;
		System.out.println("");
		System.out.println("");
		System.out.println("Collecting icon names from: " + dir);
		System.out.println("");
		for (int i = 0; i < filenames.length; ++i) {
			System.out.println(filenames[i]);
		}
		System.out.println("");
		System.out.println("");

		for (int i = 0; i < filenames.length; ++i) {
			x = x + filenames[i] + "\n";
			buf.add(new Integer(i));
		}

		panel.parentFrame.data.fileOrder = x;

		while (!buf.isEmpty()) {
			rndnr = rnd.nextInt(buf.size());

			rnr = (Integer) (buf.get(rndnr));
			// System.out.println(filenames[rnr]+" "+rnr);
			lst.add(new PicInfo((dir + "/" + filenames[rnr]), panel, ph, rnr));
			buf.remove(rndnr);
		}

		return lst;
	}

}

class MyComp implements Comparator {

	public int compare(Object o1, Object o2) {

		String s1 = o1.toString();
		String s2 = o2.toString();

		return s1.compareToIgnoreCase(s2);
	}

}

class MyFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		String lowerCase = name.toLowerCase();

		return (lowerCase.endsWith(".gif") || lowerCase.endsWith(".png"));
	}

}