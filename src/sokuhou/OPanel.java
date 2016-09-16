package sokuhou;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class OPanel extends JPanel {
	private Image img;
	private int imgWidth, imgHeight;
	private boolean viewImage;

	public OPanel(){
		img = null;
		viewImage = false;
	}

	public void paint(Graphics g){
		if(img != null && viewImage)g.drawImage(img, 0, 0, imgWidth, imgHeight, null);
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

	public void setImageWidth(int imgWidth){
		this.imgWidth = imgWidth;
	}

	public int getImageWidth(){
		return imgWidth;
	}

	public void setImageHeight(int imgHeight){
		this.imgHeight = imgHeight;
	}

	public int getImageHeight(){
		return imgHeight;
	}

	public void setImageSize(int imgWidth, int imgHeight){
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
	}

}
