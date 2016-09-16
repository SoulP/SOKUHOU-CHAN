package sokuhou;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class OPanel extends JPanel {
	private Image img;
	private boolean viewImage;

	public OPanel(){
		img = null;
		viewImage = false;
	}

	public void paint(Graphics g){
		if(img != null && viewImage)g.drawImage(img, 0, 0, 100, 100, null);
	}

	public void setImage(Image img){
		this.img = img;
	}

	public Image getImage(){
		return img;
	}

	public void setViewImage(boolean viewImage){
		this.viewImage = viewImage;
	}

	public boolean isViewImage(){
		return viewImage;
	}
}
