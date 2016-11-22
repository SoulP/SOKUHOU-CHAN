package sokuhou.Window;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinMenu extends MenuBar implements ActionListener{
	String[] menuJP = {"ファイル", "編集", "設定","ログアウト"};
	String[] menuUS = {"File", "Edit", "Option", "Logout"};
	String[][] menuLang = {menuJP, menuUS};

	String[] fileItemJP = { "上書き保存", "名前を付けて保存", "インポート", "エクスポート", "終了"};
	String[] fileItemUS = {"Save", "Save as", "Import", "Export", "Exit"};
	String[][] fileItemLang = {fileItemJP, fileItemUS};

	String[] editItemJP = {"あとで読む登録", "あとで読む解除", "お気に入り登録", "お気に入り解除", "良い", "悪い"};
	String[] editItemUS = {"Add Read later", "Remove Read later", "Add Bookmark", "Remove Bookmark", "GOOD", "BAD"};
	String[][] editItemLang = {editItemJP, editItemUS};

	String[] optionItemJP = {"単語登録", "名前変更", "メールアドレス変更", "パスワード変更", "保護者管理", "退会"};
	String[] optionItemUS = {"Add Keywords", "Change Name", "Change Email address", "Change Password", "Guardian Management", "Withdrawal"};
	String[][] optionItemLang = {optionItemJP, optionItemUS};

	Menu[] wMenu = new Menu[4];
	MenuItem[] fileItem;
	MenuItem[] editItem;
	MenuItem[] optionItem;


	WinMenu(){
		for(int i = 0; i < wMenu.length; i++){
			wMenu[i] = new Menu();
			wMenu[i].setLabel(menuLang[0][i]);
			add(wMenu[i]);
		}

		fileItem = new MenuItem[fileItemLang[0].length];

		for(int i = 0; i < fileItem.length; i++) fileItem[i] = new MenuItem(fileItemLang[0][i]);

		editItem = new MenuItem[4];
		for(int i = 0; i < editItem.length; i++) editItem[i] = new MenuItem();
		editItem[0].setLabel(editItemLang[0][0]);
		editItem[1].setLabel(editItemLang[0][2]);
		editItem[2].setLabel(editItemLang[0][4]);
		editItem[3].setLabel(editItemLang[0][5]);

		ItemsEnabled(fileItem, false);
		ItemsEnabled(editItem, false);
		ItemsAddListener(fileItem);
		ItemsAddListener(editItem);

		fileItem[4].setEnabled(true);

		wMenu[0].add(fileItem[0]);
		wMenu[0].add(fileItem[1]);
		wMenu[0].addSeparator();
		wMenu[0].add(fileItem[2]);
		wMenu[0].add(fileItem[3]);
		wMenu[0].addSeparator();
		wMenu[0].add(fileItem[4]);

		wMenu[1].add(editItem[0]);
		wMenu[1].addSeparator();
		wMenu[1].add(editItem[1]);
		wMenu[1].addSeparator();
		wMenu[1].add(editItem[2]);
		wMenu[1].add(editItem[3]);
	}

	private void ItemsAddListener(MenuItem[] items){
		for(MenuItem item : items) item.addActionListener(this);
	}

	private void ItemsEnabled(MenuItem[] items, boolean bool){
		for(MenuItem item : items) item.setEnabled(bool);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if(e.getSource() == fileItem[4]) System.exit(0);
	}
}