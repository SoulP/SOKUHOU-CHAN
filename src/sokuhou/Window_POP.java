package sokuhou;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.Toolkit;

import javax.swing.JFrame;

// 通知フレーム
public class Window_POP extends JFrame {
	private Image img;
	private OPanel oPanel, imgPanel;
	Dimension screenSize;
	Dimension traySize;
	Point location;

	// コンストラクタ
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
		imgPanel = new OPanel();
		int temp = Math.min(getWidth(), getHeight());
		imgPanel.setSize(temp, temp);
		imgPanel.setImageSize(temp, temp);
		oPanel.setSize(getWidth() - imgPanel.getWidth(), getHeight());
		oPanel.setLocation(imgPanel.getWidth(), 0);
		add(imgPanel);
		add(oPanel);
		setBackground(Color.DARK_GRAY);
	}

	// 入力: 画像
	public void setImage(Image img){
		this.img = img;
	}

	// 出力: 画像
	public Image getImage(){
		return img;
	}

	// 入力: 画像
	public void setImage2Panel(Image img){
		imgPanel.setImage(img);
	}

	// 出力: 画像
	public Image getImageFromPanel(){
		return imgPanel.getImage();
	}

	// 入力: パネルの画像 表示・非表示
	public void setViewImagePanel(boolean b){
		imgPanel.setViewImage(b);
	}

	// 出力: パネルの画像 表示・非表示
	public boolean isViewImagePanel(){
		return imgPanel.isViewImage();
	}

}
