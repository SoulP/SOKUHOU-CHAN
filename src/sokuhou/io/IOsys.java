package sokuhou.io;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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

		menu = new WinMenu();
		menu.addLangEventListener(this);
		window = new Window(menu);
		window.addLangEventListener(this);
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
			JMenuItem[] fileItems = menu.fileItems;
			JMenuItem[] editItems = menu.editItems;
			JMenuItem[] settingItems = menu.settingItems;
			JMenuItem[] accountItems = menu.accountItems;
			JMenu[] wMenu = menu.wMenu;
			JCheckBoxMenuItem[] checkItems = menu.checkItems;

			fileItems[0].setText(rb.getString("menu.file.item.save"));
			fileItems[1].setText(rb.getString("menu.file.item.saveas"));
			fileItems[2].setText(rb.getString("menu.file.item.import"));
			fileItems[3].setText(rb.getString("menu.file.item.export"));
			fileItems[4].setText(rb.getString("menu.file.item.exit"));

			editItems[0].setText(rb.getString("menu.edit.item.addreadlater"));// 編集	-> あとで読む追加
			editItems[1].setText(rb.getString("menu.edit.item.removereadlater"));// 	-> あとで読む削除
			editItems[2].setText(rb.getString("menu.edit.item.addbookmark"));// 		-> マイリスト追加
			editItems[3].setText(rb.getString("menu.edit.item.removebookmark"));// 		-> マイリスト削除

			settingItems[0].setText(rb.getString("menu.settings.item.addremovekeywords"));

			checkItems[0].setText(rb.getString("menu.info.item.want"));
			checkItems[1].setText(rb.getString("menu.info.item.notwant"));

			accountItems[0].setText(rb.getString("menu.account.item.name"));// アカウント管理	-> 名前変更
			accountItems[1].setText(rb.getString("menu.account.item.email"));// 				-> メールアドレス変更
			accountItems[2].setText(rb.getString("menu.account.item.password"));// 				-> パスワード変更
			accountItems[3].setText(rb.getString("menu.account.item.otp.regist"));// 			-> ワンタイムパスワード登録
			accountItems[4].setText(rb.getString("menu.account.item.otp.delete"));// 			-> ワンタイムパスワード削除
			accountItems[5].setText(rb.getString("menu.account.item.birthday"));// 				-> 誕生日変更
			accountItems[6].setText(rb.getString("menu.account.item.signout"));// 				-> ログアウト
			accountItems[7].setText(rb.getString("menu.account.item.delete"));// 				-> アカウント削除

			wMenu[0].setText(rb.getString("menu.file"));
			wMenu[1].setText(rb.getString("menu.edit"));
			wMenu[2].setText(rb.getString("menu.settings"));
			wMenu[3].setText(rb.getString("menu.account"));
			menu.info.setText(rb.getString("menu.info"));
			menu.optLang.setText(rb.getString("lang"));
			menu.langItems[0].setText(rb.getString("lang.auto"));

			menu.helpMenu.setText(rb.getString("menu.help"));// ヘルプ
			menu.version.setText(rb.getString("menu.help.version") + "(V)");// バージョン
		}
		if(evt.getSource() == qr){
			qr.setTitle(rb.getString("qr.title"));
			qr.label.setText(rb.getString("qr.authcode"));
			qr.button.setText(rb.getString("qr.confirm"));
			qr.str01 = rb.getString("qr.text01");
			qr.str02 = rb.getString("qr.text02");
			qr.text.setText(qr.str01);
		}
	}


}

