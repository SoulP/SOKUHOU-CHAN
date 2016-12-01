package sokuhou.Window;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.event.EventListenerList;

import sokuhou.Lang;
import sokuhou.JEvent.LangEvent;
import sokuhou.JEvent.EventListener.LangEventListener;

public class WinMenu extends MenuBar implements ActionListener, ItemListener, Runnable{

	// インスタンス変数
	volatile ResourceBundle rb;
	EventListenerList evList;

	public Menu[] wMenu = new Menu[4];
	public MenuItem[] fileItems, editItems, settingItems, accountItems;
	public Menu info, account, optLang;
	public CheckboxMenuItem[] checkItems;
	public CheckboxMenuItem[] langItems;
	public final String lang = "lang";
	public final String[] strLang = {
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

	public WinMenu(){
		rb = sokuhou.MainSYS.lang.getResBundle();
		evList = new EventListenerList();

		fileItems = new MenuItem[5];
		editItems = new MenuItem[2];
		settingItems = new MenuItem[1];
		checkItems = new CheckboxMenuItem[2];
		accountItems = new MenuItem[7];
		langItems = new CheckboxMenuItem[strLang.length];
		info = new Menu();
		account = new Menu();
		optLang = new Menu();

		for(int i = 0; i < wMenu.length; i++){
			wMenu[i] = new Menu();
			add(wMenu[i]);
		}

		for(int i = 0; i < fileItems.length; i++) fileItems[i] = new MenuItem();

		for(int i = 0; i < editItems.length; i++) editItems[i] = new MenuItem();

		for(int i = 0; i < settingItems.length; i++) settingItems[i] = new MenuItem();

		for(int i = 0; i < checkItems.length; i++) checkItems[i] = new CheckboxMenuItem();

		for(int i = 0; i < accountItems.length; i++) accountItems[i] = new MenuItem();

		langItems[0] = new CheckboxMenuItem();
		langItems[0].setState(true);
		langItems[0].addItemListener(this);
		optLang.add(langItems[0]);
		optLang.addSeparator();

		for(int i = 1; i < langItems.length; i++){
			langItems[i] = new CheckboxMenuItem();
			langItems[i].addItemListener(this);
			optLang.add(langItems[i]);
		}

		//info.setEnabled(false);
		ItemsEnabled(fileItems, false);
		ItemsEnabled(editItems, false);
		ItemsEnabled(checkItems, false);
		ItemsEnabled(accountItems, false);
		ItemsAddListener(fileItems);
		ItemsAddListener(editItems);
		ItemsAddListener(settingItems);
		ItemsAddListener(accountItems);
		checkItems[0].addItemListener(this);
		checkItems[1].addItemListener(this);

		fileItems[4].setEnabled(true);

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

		fileItems[0].setLabel(rb.getString("menu.file.item.save"));// ファイル	-> 上書き保存
		fileItems[1].setLabel(rb.getString("menu.file.item.saveas"));// 		-> 名前を付けて保存
		fileItems[2].setLabel(rb.getString("menu.file.item.import"));// 		-> インポート
		fileItems[3].setLabel(rb.getString("menu.file.item.export"));// 		-> エクスポート
		fileItems[4].setLabel(rb.getString("menu.file.item.exit"));// 			-> 終了

		editItems[0].setLabel(rb.getString("menu.edit.item.addreadlater"));// 編集	-> あとで読む追加
		editItems[1].setLabel(rb.getString("menu.edit.item.addbookmark"));// 		-> マイリスト追加

		settingItems[0].setLabel(rb.getString("menu.settings.item.addremovekeywords"));// 設定	-> 単語追加

		checkItems[0].setLabel(rb.getString("menu.info.item.want"));// 設定	-> 情報 -> 欲しい
		checkItems[1].setLabel(rb.getString("menu.info.item.notwant"));// 			-> 欲しくない

		accountItems[0].setLabel(rb.getString("menu.account.item.name"));// アカウント管理	-> 名前変更
		accountItems[1].setLabel(rb.getString("menu.account.item.email"));// 				-> メールアドレス変更
		accountItems[2].setLabel(rb.getString("menu.account.item.password"));// 			-> パスワード変更
		accountItems[3].setLabel(rb.getString("menu.account.item.otp.regist"));// 			-> ワンタイムパスワード登録
		accountItems[4].setLabel(rb.getString("menu.account.item.birthday"));// 			-> 誕生日変更
		accountItems[5].setLabel(rb.getString("menu.account.item.signout"));// 				-> ログアウト
		accountItems[6].setLabel(rb.getString("menu.account.item.delete"));// 				-> アカウント削除

		wMenu[0].setLabel(rb.getString("menu.file"));// ファイル
		wMenu[1].setLabel(rb.getString("menu.edit"));// 編集
		wMenu[2].setLabel(rb.getString("menu.settings"));// 設定
		wMenu[3].setLabel(rb.getString("menu.account"));// アカウント管理
		info.setLabel(rb.getString("menu.info"));// 設定 -> 情報

		optLang.setLabel(rb.getString(lang));// 設定 -> 言語

		// 各言語のラベルとネーム設定
		for(int i = 0; i < langItems.length; i++){
			langItems[i].setLabel(rb.getString(lang + "." + strLang[i]));// ラベル
			langItems[i].setName(strLang[i]);// ネーム
		}
	}

	private void ItemsAddListener(MenuItem[] items){
		for(MenuItem item : items) item.addActionListener(this);
	}

	private void ItemsEnabled(MenuItem[] items, boolean bool){
		for(MenuItem item : items) item.setEnabled(bool);
	}

	public synchronized int getMode(){
		return mode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if(e.getSource() == fileItems[4]) System.exit(0);
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
				for(CheckboxMenuItem item : checkItems) if(item != checkItems[i]) item.setState(false);
				break;
			}
		}
		for(CheckboxMenuItem items : langItems){
			if(e.getSource() == items){
				for(CheckboxMenuItem item : langItems) if(item != items) item.setState(false);
				setLang(items.getName());
				break;
			}
		}
	}

	// 言語変更 入力: 文字列 (例え: "ja_JP")
	public synchronized void setLang(String locale){
		if(locale == null || locale.equals(strLang[0])){
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