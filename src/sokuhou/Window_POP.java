package sokuhou;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Window_POP extends JFrame {
	private Image img;
	private OPanel oPanel;
	Dimension screenSize;
	Dimension traySize;
	Point location;
	public Window_POP(){
		setTitle("速報");
		setUndecorated(true);
		setSize(300, 100);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		traySize = SystemTray.getSystemTray().getTrayIconSize();
		location = new Point();
		location.setLocation(screenSize.width - getWidth(), screenSize.height - getHeight() - (int)(traySize.getHeight()*2.5));;
		setLocation(location);
		setResizable(false);
		oPanel = new OPanel();
		int temp = Math.min(getWidth(), getHeight());
		oPanel.setSize(temp, temp);
		add(oPanel);
	}

	public void setImage(Image img){
		this.img = img;
	}

	public Image getImage(){
		return img;
	}

	public void setImage2Panel(Image img){
		oPanel.setImage(img);
	}

	public void setViewImagePanel(boolean b){
		oPanel.setViewImage(b);
	}

	public boolean isViewImagePanel(){
		return oPanel.isViewImage();
	}

}
