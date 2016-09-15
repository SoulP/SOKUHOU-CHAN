package sokuhou;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Window_POP extends JFrame {
	public Image img;
	Dimension screenSize;
	Dimension traySize;
	Point location;
	public Window_POP(){
		setTitle("速報");
		setUndecorated(true);
		setSize(300, 200);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		traySize = SystemTray.getSystemTray().getTrayIconSize();
		location = new Point();
		location.setLocation(screenSize.width - getWidth(), screenSize.height - getHeight() - (int)(traySize.getHeight()*2.5));;
		setLocation(location);
		setResizable(false);
	}
}
