package xp;

/*
 * PictureTransferHandler.java is used by the 1.4
 * DragPictureDemo.java example.
 */

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

class PictureImportHandler extends TransferHandler {
	DataFlavor pictureFlavor = DataFlavor.imageFlavor;
	DTPicture sourcePic;
	boolean shouldRemove;

	@Override
	public boolean importData(JComponent c, Transferable t) {
		Image image;
		if (canImport(c, t.getTransferDataFlavors())) {
			DTPicture pic = (DTPicture) c;
			// Don't drop on myself.
			if (sourcePic == pic) {
				shouldRemove = false;
				return true;
			}
			if (pic.image != null) {
				shouldRemove = false;
				return false;
			}
			try {
				image = (Image) t.getTransferData(pictureFlavor);
				// Set the component to the new picture.
				pic.setImage(image);
				pic.number = sourcePic.number;
				pic.info.path = sourcePic.info.path;
				pic.info.panel.newEmpty();
				pic.info.panel.validate();
				return true;
			} catch (UnsupportedFlavorException ufe) {
				System.out.println("importData: unsupported data flavor");
			} catch (IOException ioe) {
				System.out.println("importData: I/O exception");
			}
		}
		return false;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		sourcePic = (DTPicture) c;
		shouldRemove = true;
		return new PictureTransferable(sourcePic);
	}

	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	@Override
	protected void exportDone(JComponent c, Transferable data, int action) {
		if (shouldRemove && (action == MOVE)) {
			sourcePic.setImage(null);

			sourcePic.info.panel.removePic(sourcePic.info);
			sourcePic.info.panel.validate();
		}
		sourcePic = null;
	}

	@Override
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		for (DataFlavor element : flavors) {
			if (pictureFlavor.equals(element)) {
				return true;
			}
		}
		return false;
	}

	class PictureTransferable implements Transferable {
		private final Image image;

		PictureTransferable(DTPicture pic) {
			image = pic.image;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return image;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { pictureFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return pictureFlavor.equals(flavor);
		}
	}
}
