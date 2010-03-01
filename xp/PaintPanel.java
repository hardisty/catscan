package xp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
 
class PaintPanel
extends JPanel
implements MouseMotionListener, MouseListener
{

 
BufferedImage image;
Graphics g;
ArrayList lines = new ArrayList();
ArrayList current;

public PaintPanel() {
	addMouseMotionListener(this);
	addMouseListener(this);
	current = new ArrayList();
	lines.add(current);
	this.setSize(new Dimension(1000,700));
	
    image = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
    g = image.getGraphics();
    
    g.setColor(Color.WHITE);
    g.fillRect(0,0,this.getWidth(),this.getHeight());   
    g.setColor(Color.BLACK);
			
}

public void mousePressed(MouseEvent e) {
    //Don't bother to drag if there is no image.
	//current = new ArrayList();
	//current.add(e.getPoint());
	//lines.add(current);
    //e.consume();
    this.repaint();
}

public void mouseDragged(MouseEvent e) {
	//current = new ArrayList();
	//lines.add(current);
 	current.add(e.getPoint());
	//e.consume();
 	this.validate();
	this.repaint();
}

public void mouseMoved(MouseEvent e) {
	this.repaint();   	
}



protected void paintComponent(Graphics graphics) {
    
    int ls = lines.size();
    int sls = 0;
    ArrayList line;
    Point p1, p2;
    

	g.setColor(Color.WHITE);
	g.clearRect(0,0,this.getWidth(),this.getHeight());
    g.fillRect(0,0,this.getWidth(),this.getHeight());   
    g.setColor(Color.BLACK);              
    
    
    for(int i=0;i<ls; i++) {
    	line = ((ArrayList)lines.get(i));
    	sls = line.size();
    	
    	
    	for(int j=1; j<sls; j++) {
    		p1 = ((Point)line.get(j-1)); 
    		p2 = ((Point)line.get(j));  
    		g.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY()); 
    	}	
    }
    
    Graphics gg = graphics.create();
    gg.drawImage(image,0,0,this);
    gg.dispose();
}

public void mouseClicked(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	
	current.add(e.getPoint());
	//e.consume();
	this.repaint();
	current = new ArrayList();	
	lines.add(current);
}

public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	this.repaint();
}

public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
	this.repaint();
}
}
