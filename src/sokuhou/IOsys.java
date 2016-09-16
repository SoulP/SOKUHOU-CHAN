package sokuhou;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;

//Input / Output (入出力)
public class IOsys extends IO{

	public Window window;
	public Window_POP winPOP;
	private Dimension screenSizeFull;
	private Dimension screenSize;
	public Toolkit tk;
	public SystemTray tray;
	public GraphicsEnvironment env;

	// コンストラクタ
	public IOsys(){
		window = new Window();
		winPOP = new Window_POP();
		tk = Toolkit.getDefaultToolkit();
		tray = (SystemTray.isSupported())? SystemTray.getSystemTray() : null;
		env = (!GraphicsEnvironment.isHeadless())? GraphicsEnvironment.getLocalGraphicsEnvironment() : null;
		screenSizeFull = (env != null)? env.getDefaultScreenDevice().getDefaultConfiguration().getBounds().getSize(): null;
		screenSize = (env != null)? env.getMaximumWindowBounds().getBounds().getSize(): null;
	}

	// 出力: デスクトップ画面サイズ
	public Dimension getScreenSize() {
		return screenSize;
	}

	public int getScreenWidth(){
		return new Double(screenSize.getWidth()).intValue();
	}

	public int getScreenHeight(){
		return new Double(screenSize.getHeight()).intValue();
	}


	public Dimension getScreenSizeFull() {
		return screenSizeFull;
	}



}

