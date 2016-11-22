package sokuhou.InputOutput;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;

import sokuhou.Window.Window;
import sokuhou.Window.Window_POP;

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

	// 出力: フルスクリーンサイズ
	public Dimension getScreenSizeFull() {
		return screenSizeFull;
	}

	// 出力: デスクトップ画面サイズ
	public Dimension getScreenSize() {
		return screenSize;
	}


}

