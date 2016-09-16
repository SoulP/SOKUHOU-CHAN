package sokuhou;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

//Input / Output (入出力)
public class IOsys extends IO{

	public Window window;
	public Window_POP winPOP;
	private Dimension screenSize;
	private Rectangle2D frameSize;
	public Toolkit tk;
	public SystemTray tray;
	public GraphicsEnvironment env;

	public IOsys(){
		window = new Window();
		winPOP = new Window_POP();
		tk = Toolkit.getDefaultToolkit();
		tray = (SystemTray.isSupported())? SystemTray.getSystemTray() : null;
		env = (!GraphicsEnvironment.isHeadless())? GraphicsEnvironment.getLocalGraphicsEnvironment() : null;
	}

	public void setWinPOP(Window_POP winPOP){
		this.winPOP = winPOP;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}

	public int getScreenWidth(){
		return new Double(screenSize.getWidth()).intValue();
	}

	public int getScreenHeight(){
		return new Double(screenSize.getHeight()).intValue();
	}


	public Rectangle2D getFrameSize() {
		return frameSize;
	}

	public int getFrameWidth(){
		return new Double(frameSize.getWidth()).intValue();
	}

	public int getFrameHeight(){
		return new Double(frameSize.getHeight()).intValue();
	}

}

