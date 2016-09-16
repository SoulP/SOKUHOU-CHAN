package sokuhou;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

// 通知フレームのパネル
public class OPanel extends JPanel {
	private Image img;
	private int imgWidth, imgHeight;
	private boolean viewImage, viewBackgroundColor;
	private Color color;

	public OPanel(){
		img = null;
		viewImage = viewBackgroundColor = false;
		color = Color.DARK_GRAY;
	}

	public void paint(Graphics g){
		if(viewBackgroundColor){
			g.setColor(color);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
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

	public void setBackgroundColor(Color color){
		this.color = color;
	}

	public Color getBackgroundColor(){
		return color;
	}

	public void setViewBackgroundColor(boolean viewBackgroundColor){
		this.viewBackgroundColor = viewBackgroundColor;
	}

	public boolean isViewBackgroundColor(){
		return viewBackgroundColor;
	}

}
