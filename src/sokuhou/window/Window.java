package sokuhou.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.event.EventListenerList;

import sokuhou.event.LangEvent;
import sokuhou.event.EventListener.LangEventListener;

// メインフレーム
public class Window extends JFrame implements WindowListener, KeyListener, Runnable{
	// インスタンス変数
	volatile ResourceBundle rb;
	EventListenerList evList;

	volatile WinPanel win;
	volatile WinMenu menu;
	Container p;
	volatile QRcodeViewer qr;

	// コンストラクタ
	public Window(JMenuBar menu){
		rb = sokuhou.MainSYS.lang.getResBundle();
		evList = new EventListenerList();

		win = new WinPanel();
		p = getContentPane();
		p.setBackground(Color.WHITE);
		p.add(win);

		/*
		if(qr == null) qr = new QRcodeViewer();
		qr.setImage("https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2F%25E9%2580%259F%25E5%25A0%25B1%25E3%2581%25A1%25E3%2582%2583%25E3%2582%2593%3AtestUSER%40sokuhou.soulp.moe%3Fsecret%3DGK3R4GZYC4G4TRC5%26issuer%3D%25E9%2580%259F%25E5%25A0%25B1%25E3%2581%25A1%25E3%2582%2583%25E3%2582%2593");
		qr.setVisible(true);
		if(!qr.isVisible()) qr = null;
		*/

		InputStream stream = ClassLoader.getSystemResourceAsStream("resource/img/dummy.png");
		Image icon = null;
		try {
			icon = ImageIO.read(stream);
		}catch (Exception e){
		}
		setTitle(rb.getString("title"));
		setSize(640, 360);
		setPreferredSize(new Dimension(640, 360));
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);
		setResizable(false);
		setJMenuBar(menu);
		setIconImage(icon);
		addWindowListener(this);
		addKeyListener(this);
		pack();
	}

	public void run(){
		int mode = 0;
		while(true){
			if(menu.getMode() != mode){
				mode = menu.getMode();
				if(mode == 0x21){
					win.setVisible(true);
				}
			}
			if(!rb.equals(sokuhou.MainSYS.lang.getResBundle())) {
				fireUpdateLang();
				rb = sokuhou.MainSYS.lang.getResBundle();
			}
		}
	}

	public synchronized void setJMenuBar(JMenuBar menu){
		this.menu = (WinMenu) menu;
		super.setJMenuBar(menu);
	}

	public synchronized JMenuBar getJMenuBar(){
		return menu;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}


	public void addLangEventListener(LangEventListener l){
		if(l == null) return;
		evList.add(LangEventListener.class, l);
	}

	public void removeLangEventListener(LangEventListener l){
		if(l == null) return;
		evList.remove(LangEventListener.class, l);
	}

	private void fireUpdateLang(){
		LangEvent evt = new LangEvent(this);
		for(LangEventListener listener : evList.getListeners(LangEventListener.class)){
			listener.updateLang(evt);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		if(e.getKeyCode() == KeyEvent.VK_ALT){
			menu.wMenu[0].getMouseListeners()[0].mousePressed(
					new MouseEvent(menu.wMenu[0], MouseEvent.MOUSE_PRESSED,
							System.currentTimeMillis(), 0, 0, 0, 0, false, MouseEvent.BUTTON1));
		}
		*/
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}
