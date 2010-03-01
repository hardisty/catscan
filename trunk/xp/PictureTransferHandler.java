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

class PictureTransferHandler extends TransferHandler {
    DataFlavor pictureFlavor = DataFlavor.imageFlavor;
    DTPicture sourcePic;
    boolean shouldRemove;

    public boolean importData(JComponent c, Transferable t) {
        Image image;
        if (canImport(c, t.getTransferDataFlavors())) {
            
        	if (c.getClass().getName().equals("xp.DTPicture")) {
        	        	
        		DTPicture pic = (DTPicture)c;
        		
        		
        		//Don't drop on myself. 
        		if (sourcePic == pic || !sourcePic.info.panel.addPic) {
        			shouldRemove = false;
        			return true;
        		}
        		/*if (pic.image!=null) {
        			shouldRemove = false;
        			return false;
        		}*/
        		try {
        			/*image = (Image)t.getTransferData(pictureFlavor);
        			//Set the component to the new picture. 
        			pic.setImage(image);
        			pic.number = sourcePic.number;
        			pic.info.path = sourcePic.info.path;
        			pic.info.panel.newEmpty();
        			pic.info.panel.validate();
        			return true;*/
        			MyPanel gp = pic.info.panel;
        			image = (Image)t.getTransferData(pictureFlavor);
        			//Set the component to the new picture.
        			//System.out.println(sourcePic.number);
        			gp.addPic(image, sourcePic.number, sourcePic.info.path);//(image, sourcePic.number, sourcePic.info.path);
        			            			
        			gp.validate();
        			return true;
        		} catch (UnsupportedFlavorException ufe) {
        			System.out.println("importData: unsupported data flavor");
        		} catch (IOException ioe) {
        			System.out.println("importData: I/O exception");
        		}
        	} else {
        		if (c.getClass().getName().equals("xp.GroupPanel")) {
            		try {
            			GroupPanel gp = (GroupPanel)c;
            			image = (Image)t.getTransferData(pictureFlavor);
            			//Set the component to the new picture.
            			//System.out.println(sourcePic.number);
            			gp.addPic(image, sourcePic.number, sourcePic.info.path);
            			            			
            			gp.validate();
            			return true;
            		} catch (UnsupportedFlavorException ufe) {
            			System.out.println("importData: unsupported data flavor");
            		} catch (IOException ioe) {
            			System.out.println("importData: I/O exception");
            		}
        		} else {
            		try {
            			DragPictureDemo gp = (DragPictureDemo)c;
            			image = (Image)t.getTransferData(pictureFlavor);
            			//Set the component to the new picture.
            			
            			System.out.println(sourcePic.number);
            			gp.addPic(image, sourcePic.number, sourcePic.info.path);
            			            			
            			gp.validate();
            			
            			return true;
            		} catch (UnsupportedFlavorException ufe) {
            			System.out.println("importData: unsupported data flavor");
            		} catch (IOException ioe) {
            			System.out.println("importData: I/O exception");
            		}
        		}
        	}
        }
        return false;
    }

    protected Transferable createTransferable(JComponent c) {
        sourcePic = (DTPicture)c;
        shouldRemove = true;
        return new PictureTransferable(sourcePic);
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        if (shouldRemove && (action == MOVE)) {
            sourcePic.setImage(null);
        
        sourcePic.info.panel.removePic(sourcePic.info);
        sourcePic.info.panel.validate();}
        sourcePic = null;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (pictureFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    class PictureTransferable implements Transferable {
        private Image image;

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

