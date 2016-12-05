package sokuhou.window;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MenuBar;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;

import javax.swing.event.EventListenerList;

import sokuhou.event.LangEvent;
import sokuhou.event.EventListener.LangEventListener;

// メインフレーム
public class Window extends Frame implements WindowListener, Runnable{
	// インスタンス変数
	volatile ResourceBundle rb;
	EventListenerList evList;

	WinPanel win;
	WinMenu menu;
	QRcodeViewer qr;

	// コンストラクタ
	public Window(){
		rb = sokuhou.MainSYS.lang.getResBundle();
		evList = new EventListenerList();

		setTitle(rb.getString("title"));
		setSize(640, 360);
		setPreferredSize(new Dimension(640, 360));
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);
		setResizable(false);
		addWindowListener(this);

		win = new WinPanel();
		add(win);

		pack();

		/*
		if(qr == null) qr = new QRcodeViewer();
		qr.setImage("https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2F%25E9%2580%259F%25E5%25A0%25B1%25E3%2581%25A1%25E3%2582%2583%25E3%2582%2593%3AtestUSER%40sokuhou.soulp.moe%3Fsecret%3DGK3R4GZYC4G4TRC5%26issuer%3D%25E9%2580%259F%25E5%25A0%25B1%25E3%2581%25A1%25E3%2582%2583%25E3%2582%2593");
		qr.setVisible(true);
		if(!qr.isVisible()) qr = null;
		*/

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

	public synchronized void setMenuBar(MenuBar menu){
		this.menu = (WinMenu) menu;
		super.setMenuBar(menu);
	}

	public synchronized MenuBar getMenuBar(){
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
}
