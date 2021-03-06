package sokuhou.io;

import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import sokuhou.event.LangEvent;
import sokuhou.event.EventListener.LangEventListener;
import sokuhou.window.QRcodeViewer;
import sokuhou.window.WinMenu;
import sokuhou.window.Window;
import sokuhou.window.Window_POP;


//Input / Output (入出力)
public class IOsys extends IO implements LangEventListener{
	// インスタンス変数
	volatile ResourceBundle rb;

	public static Window window;
	public static Window_POP winPOP;
	private static WinMenu menu;
	public static QRcodeViewer qr;
	private Dimension screenSizeFull;
	private Dimension screenSize;
	public Toolkit tk;
	public SystemTray tray;
	public GraphicsEnvironment env;

	private enum thread{
		win(new Thread(window)),
		win_POP(new Thread(winPOP)),
		win_Menu(new Thread(menu)),
		win_QR(new Thread(qr));

		private final Thread th;

		private thread(final Thread th){
			this.th = th;
		}

		public void start(){
			th.start();
		}

		public Thread getThread(){
			return th;
		}
	};

	// コンストラクタ
	public IOsys(){

		rb = sokuhou.MainSYS.lang.getResBundle();

		window = new Window();
		window.addLangEventListener(this);
		menu = new WinMenu();
		menu.addLangEventListener(this);
		window.setMenuBar(menu);
		winPOP = new Window_POP();
		qr = new QRcodeViewer();
		qr.addLangEventListener(this);

		thread.win.start();
		thread.win_POP.start();
		thread.win_Menu.start();
		thread.win_QR.start();


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

	@Override
	public void updateLang(LangEvent evt) {
		rb = sokuhou.MainSYS.lang.getResBundle();

		if(evt.getSource() == window) window.setTitle(rb.getString("title"));
		if(evt.getSource() == menu) {
			MenuItem[] fileItems = menu.fileItems;
			MenuItem[] editItems = menu.editItems;
			MenuItem[] settingItems = menu.settingItems;
			MenuItem[] accountItems = menu.accountItems;
			Menu[] wMenu = menu.wMenu;
			CheckboxMenuItem[] checkItems = menu.checkItems;

			fileItems[0].setLabel(rb.getString("menu.file.item.save"));
			fileItems[1].setLabel(rb.getString("menu.file.item.saveas"));
			fileItems[2].setLabel(rb.getString("menu.file.item.import"));
			fileItems[3].setLabel(rb.getString("menu.file.item.export"));
			fileItems[4].setLabel(rb.getString("menu.file.item.exit"));

			editItems[0].setLabel(rb.getString("menu.edit.item.addreadlater"));
			editItems[1].setLabel(rb.getString("menu.edit.item.addbookmark"));

			settingItems[0].setLabel(rb.getString("menu.settings.item.addremovekeywords"));

			checkItems[0].setLabel(rb.getString("menu.info.item.want"));
			checkItems[1].setLabel(rb.getString("menu.info.item.notwant"));

			accountItems[0].setLabel(rb.getString("menu.account.item.name"));
			accountItems[1].setLabel(rb.getString("menu.account.item.email"));
			accountItems[2].setLabel(rb.getString("menu.account.item.password"));
			accountItems[3].setLabel(rb.getString("menu.account.item.otp.regist"));
			accountItems[4].setLabel(rb.getString("menu.account.item.birthday"));
			accountItems[5].setLabel(rb.getString("menu.account.item.signout"));
			accountItems[6].setLabel(rb.getString("menu.account.item.delete"));

			wMenu[0].setLabel(rb.getString("menu.file"));
			wMenu[1].setLabel(rb.getString("menu.edit"));
			wMenu[2].setLabel(rb.getString("menu.settings"));
			wMenu[3].setLabel(rb.getString("menu.account"));
			menu.info.setLabel(rb.getString("menu.info"));
			menu.optLang.setLabel(rb.getString("lang"));
			menu.langItems[0].setLabel(rb.getString("lang.auto"));
		}
		if(evt.getSource() == qr){
			qr.setTitle(rb.getString("qr.title"));
			qr.label.setText(rb.getString("qr.authcode"));
			qr.button.setLabel(rb.getString("qr.confirm"));
			qr.str01 = rb.getString("qr.text01");
			qr.str02 = rb.getString("qr.text02");
			qr.text.setText(qr.str01);
		}
	}


}

