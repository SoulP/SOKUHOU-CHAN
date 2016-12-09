package sokuhou.window;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import sokuhou.Lang;
import sokuhou.event.LangEvent;
import sokuhou.event.EventListener.LangEventListener;

public class WinMenu extends JMenuBar implements ActionListener, ItemListener, Runnable{

	// インスタンス変数
	volatile ResourceBundle rb;
	EventListenerList evList;

	public JMenu[] wMenu = new JMenu[4];
	public JMenu helpMenu, info, account, optLang;
	public JMenuItem[] fileItems, editItems, settingItems, accountItems;
	public JMenuItem up, down;
	public JCheckBoxMenuItem[] checkItems;
	public JCheckBoxMenuItem[] langItems;
	public final String LANG = "lang";
	public final String[] STR_LANG = {
			"auto",// 自動
			"en_US", "fr_FR", "es_ES", "ar_EG", "zh_CN", "ru_RU",	// 英語、			フランス語、		スペイン語、	アラビア語、	中国語、		ロシア語
			"pt_PT", "de_DE", "ja_JP", "hi_IN", "ms_MY", "it_IT",	// ポルトガル語、	ドイツ語、			日本語、		ヒンディー語、	マレー語、		イタリア語
			"tr_TR", "vi_VN", "pl_PL", "th_TH", "sq_AL", "be_BY",	// トルコ語、		ベトナム語、		ポーランド語、	タイ語、		アルバニア語、	ベラルーシ語
			"bg_BG", "ca_ES", "hr_HR", "cs_CZ", "da_DK", "nl_NL",	// ブルガリア語、	カタロニア語、		クロアチア語、	チェコ語、		デンマーク語、	オランダ語
			"et_EE", "fi_FL", "el_GR", "iw_IL", "hu_HU", "is_IS",	// エストニア語、	フィンランド語、	ギリシャ語、	ヘブライ語、	ハンガリー語、	アイスランド語
			"in_ID", "ga_IE", "ko_KR", "lv_LV", "lt_LT", "mk_MK",	// インドネシア語、	アイルランド語、	韓国語、		ラトビア語、	リトアニア語、	マケドニア語
			"mt_MT", "no_NO", "ro_RO", "sr_RS", "sk_SK", "sl_SI",	// マルタ語、		ノルウェー語、		ルーマニア語、	セルビア語、	スロバキア語、	スロベニア語
			"sv_SE", "uk_UA"										// スウェーデン語、	ウクライナ語
	};

	int mode;
	int langMin, langMax;

	public WinMenu(){

		rb = sokuhou.MainSYS.lang.getResBundle();
		evList = new EventListenerList();

		langMin = 1;
		langMax = 10;
		langMax += langMin;


		fileItems = new JMenuItem[5];
		editItems = new JMenuItem[2];
		settingItems = new JMenuItem[1];
		checkItems = new JCheckBoxMenuItem[2];
		accountItems = new JMenuItem[7];
		langItems = new JCheckBoxMenuItem[STR_LANG.length];
		info = new JMenu();
		account = new JMenu();
		optLang = new JMenu();
		helpMenu = new JMenu();

		up = new JMenuItem();
		down = new JMenuItem();

		for(int i = 0; i < wMenu.length; i++){
			wMenu[i] = new JMenu();
			add(wMenu[i]);
		}

		add(helpMenu);// メニューバーにヘルプ追加

		for(int i = 0; i < fileItems.length; i++) fileItems[i] = new JMenuItem();

		for(int i = 0; i < editItems.length; i++) editItems[i] = new JMenuItem();

		for(int i = 0; i < settingItems.length; i++) settingItems[i] = new JMenuItem();

		for(int i = 0; i < checkItems.length; i++) checkItems[i] = new JCheckBoxMenuItem();

		for(int i = 0; i < accountItems.length; i++) accountItems[i] = new JMenuItem();

		langItems[0] = new JCheckBoxMenuItem();
		langItems[0].setState(true);
		langItems[0].addItemListener(this);

		optLang.add(up);
		optLang.addSeparator();
		optLang.add(langItems[0]);
		optLang.addSeparator();

		for(int i = 1; i < langItems.length; i++){
			langItems[i] = new JCheckBoxMenuItem();
			langItems[i].addItemListener(this);
			optLang.add(langItems[i]);
		}

		optLang.addSeparator();
		optLang.add(down);

		// 選択可否初期設定
		//info.setEnabled(false);
		for(JMenuItem item : fileItems) item.setEnabled(false);
		for(JMenuItem item : editItems) item.setEnabled(false);
		for(JMenuItem item : accountItems) item.setEnabled(false);
		for(JCheckBoxMenuItem item : checkItems) item.setEnabled(false);
		fileItems[4].setEnabled(true);

		// リスナー割当
		for(JMenuItem item : fileItems) item.addActionListener(this);
		for(JMenuItem item : editItems) item.addActionListener(this);
		for(JMenuItem item : settingItems) item.addActionListener(this);
		for(JMenuItem item : accountItems) item.addActionListener(this);
		for(JCheckBoxMenuItem item : checkItems) item.addActionListener(this);
		up.addActionListener(this);
		down.addActionListener(this);

		wMenu[0].add(fileItems[0]);
		wMenu[0].add(fileItems[1]);
		wMenu[0].addSeparator();
		wMenu[0].add(fileItems[2]);
		wMenu[0].add(fileItems[3]);
		wMenu[0].addSeparator();
		wMenu[0].add(fileItems[4]);

		wMenu[1].add(editItems[0]);
		wMenu[1].addSeparator();
		wMenu[1].add(editItems[1]);
		wMenu[1].addSeparator();
		wMenu[1].add(info);

		info.add(checkItems[0]);
		info.add(checkItems[1]);

		wMenu[2].add(settingItems[0]);
		wMenu[2].add(optLang);

		wMenu[3].add(accountItems[0]);
		wMenu[3].add(accountItems[1]);
		wMenu[3].add(accountItems[2]);
		wMenu[3].add(accountItems[3]);
		wMenu[3].add(accountItems[4]);
		wMenu[3].addSeparator();
		wMenu[3].add(accountItems[5]);
		wMenu[3].addSeparator();
		wMenu[3].add(accountItems[6]);

		mode = 0;

		fileItems[0].setText(rb.getString("menu.file.item.save"));// ファイル	-> 上書き保存
		fileItems[1].setText(rb.getString("menu.file.item.saveas"));// 		-> 名前を付けて保存
		fileItems[2].setText(rb.getString("menu.file.item.import"));// 		-> インポート
		fileItems[3].setText(rb.getString("menu.file.item.export"));// 		-> エクスポート
		fileItems[4].setText(rb.getString("menu.file.item.exit"));// 			-> 終了

		editItems[0].setText(rb.getString("menu.edit.item.addreadlater"));// 編集	-> あとで読む追加
		editItems[1].setText(rb.getString("menu.edit.item.addbookmark"));// 		-> マイリスト追加

		settingItems[0].setText(rb.getString("menu.settings.item.addremovekeywords"));// 設定	-> 単語追加

		checkItems[0].setText(rb.getString("menu.info.item.want"));// 設定	-> 情報 -> 欲しい
		checkItems[1].setText(rb.getString("menu.info.item.notwant"));// 			-> 欲しくない

		accountItems[0].setText(rb.getString("menu.account.item.name"));// アカウント管理	-> 名前変更
		accountItems[1].setText(rb.getString("menu.account.item.email"));// 				-> メールアドレス変更
		accountItems[2].setText(rb.getString("menu.account.item.password"));// 			-> パスワード変更
		accountItems[3].setText(rb.getString("menu.account.item.otp.regist"));// 			-> ワンタイムパスワード登録
		accountItems[4].setText(rb.getString("menu.account.item.birthday"));// 			-> 誕生日変更
		accountItems[5].setText(rb.getString("menu.account.item.signout"));// 				-> ログアウト
		accountItems[6].setText(rb.getString("menu.account.item.delete"));// 				-> アカウント削除

		wMenu[0].setText(rb.getString("menu.file"));// ファイル
		wMenu[1].setText(rb.getString("menu.edit"));// 編集
		wMenu[2].setText(rb.getString("menu.settings"));// 設定
		wMenu[3].setText(rb.getString("menu.account"));// アカウント管理
		info.setText(rb.getString("menu.info"));// 設定 -> 情報

		optLang.setText(rb.getString(LANG));// 設定 -> 言語

		// 各言語のラベルとネーム設定
		for(int i = 0; i < langItems.length; i++){
			String str = rb.getString(LANG + "." + STR_LANG[i]);
			try {
				byte[] b = str.getBytes("UTF-16");
				str = new String(b, "UTF-16");
			} catch (UnsupportedEncodingException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			langItems[i].setText(str);// ラベル
			langItems[i].setName(STR_LANG[i]);// ネーム
		}

		helpMenu.setText(rb.getString("menu.help"));// ヘルプ

		up.setText(rb.getString("menu.up"));
		down.setText(rb.getString("menu.down"));

		// ニーモニック割当
		wMenu[0].setMnemonic(KeyEvent.VK_F);
		wMenu[1].setMnemonic(KeyEvent.VK_E);
		wMenu[2].setMnemonic(KeyEvent.VK_S);
		wMenu[3].setMnemonic(KeyEvent.VK_A);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		fileItems[0].setMnemonic(KeyEvent.VK_S);
		fileItems[1].setMnemonic(KeyEvent.VK_A);
		fileItems[2].setMnemonic(KeyEvent.VK_I);
		fileItems[3].setMnemonic(KeyEvent.VK_O);
		fileItems[4].setMnemonic(KeyEvent.VK_X);

		// 言語表示数制限(画面外の表示対策)
		for(int i = 1; i < langItems.length; i++) langItems[i].setVisible(false);
		for(int i = langMin; i < langMax; i++ ) langItems[i].setVisible(true);
		up.setEnabled(langMin > 1);
		down.setEnabled(langMax+1 < langItems.length);
	}

	// 出力: メニュー選択の値
	public synchronized int getMode(){
		return mode;
	}

	// 言語選択 上
	private void goUP(){
		if(langMin <= 1) return;
		langItems[--langMin].setVisible(true);
		langItems[langMax--].setVisible(false);
		Container c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) wMenu[2].getComponentPopupMenu());
        if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(true);
        wMenu[2].setSelected(true);
		optLang.setSelected(true);
		up.setEnabled(langMin > 1);
		down.setEnabled(langMax+1 < langItems.length);
	}

	// 言語選択 下
	private void goDOWN(){
		if(langMax >= langItems.length) return;
		langItems[langMin++].setVisible(false);
		langItems[++langMax].setVisible(true);
		wMenu[2].setSelected(true);
		Container c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) wMenu[2].getComponentPopupMenu());
        if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(true);
        wMenu[2].setSelected(true);
		optLang.setSelected(true);
		up.setEnabled(langMin > 1);
		down.setEnabled(langMax+1 < langItems.length);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if(e.getSource() == fileItems[4]) System.exit(0);
		if(e.getSource() == up){
            Container c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) e.getSource());
            if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(true);
            c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) optLang);
            if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(true);
            goUP();
		}
		if(e.getSource() == down){
			Container c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) e.getSource());
            if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(true);
            c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) optLang);
            if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(true);
            goDOWN();
		}
		for(int i = 0; i < fileItems.length -1; i++){
			if(e.getSource() == fileItems[i]){
				mode = i + 0x01;
				break;
			}
		}
		for(int i = 0; i < editItems.length; i++){
			if(e.getSource() == editItems[i]){
				mode = i + 0x11;
				break;
			}
		}
		for(int i = 0; i < settingItems.length; i++){
			if(e.getSource() == settingItems[i]){
				mode = i + 0x21;
				break;
			}
		}
	}

	@Override
	public void run() {
		while(true){
			if(!rb.equals(sokuhou.MainSYS.lang.getResBundle())) {
				fireUpdateLang();
				rb = sokuhou.MainSYS.lang.getResBundle();
			}
		}
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
	public void itemStateChanged(ItemEvent e) {
		for(int i = 0; i < checkItems.length; i++){
			if(e.getSource() == checkItems[i]){
				mode = i + 0x31;
				for(JCheckBoxMenuItem item : checkItems) if(item != checkItems[i]) item.setState(false);
				break;
			}
		}
		for(JCheckBoxMenuItem items : langItems){
			if(e.getSource() == items && items.getState()){
				for(JCheckBoxMenuItem item : langItems) if(!item.getName().equals(items.getName())) item.setState(false);
				setLang(items.getName());
				Container c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) e.getSource());
	            if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(false);
	            c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) optLang);
	            if (c instanceof JPopupMenu) ((JPopupMenu) c).setVisible(false);
	            optLang.setSelected(false);
	            wMenu[2].setSelected(false);
				break;
			}
		}
	}

	// 言語変更 入力: 文字列 (例え: "ja_JP")
	public synchronized void setLang(String locale){
		if(locale == null || locale.equals(STR_LANG[0])){
			sokuhou.MainSYS.lang.setResBundle(new Lang().getResBundle());
		}else{
			String str = "";
			str += locale.substring(0, 2);
			str += "-";
			str += locale.substring(3, 5);
			sokuhou.MainSYS.lang.changeLocale(Locale.forLanguageTag(str));
		}
	}
}
