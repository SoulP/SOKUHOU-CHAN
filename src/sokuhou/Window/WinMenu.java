package sokuhou.Window;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class WinMenu extends MenuBar implements ActionListener{

	// インスタンス変数
	ResourceBundle rb;

	Menu[] wMenu = new Menu[4];
	MenuItem[] fileItem;
	MenuItem[] editItem;
	MenuItem[] optionItem;

	int mode;

	WinMenu(){

		rb = ResourceBundle.getBundle("lang");

		fileItem = new MenuItem[5];
		editItem = new MenuItem[4];
		optionItem = new MenuItem[2];

		for(int i = 0; i < wMenu.length; i++){
			wMenu[i] = new Menu();
			add(wMenu[i]);
		}

		for(int i = 0; i < fileItem.length; i++) fileItem[i] = new MenuItem();

		for(int i = 0; i < editItem.length; i++) editItem[i] = new MenuItem();

		for(int i = 0; i < optionItem.length; i++) optionItem[i] = new MenuItem();

		ItemsEnabled(fileItem, false);
		ItemsEnabled(editItem, false);
		ItemsAddListener(fileItem);
		ItemsAddListener(editItem);
		ItemsAddListener(optionItem);

		fileItem[4].setEnabled(true);

		fileItem[0].setLabel(rb.getString("menu.file.item.save"));
		fileItem[1].setLabel(rb.getString("menu.file.item.saveas"));
		fileItem[2].setLabel(rb.getString("menu.file.item.import"));
		fileItem[3].setLabel(rb.getString("menu.file.item.export"));
		fileItem[4].setLabel(rb.getString("menu.file.item.exit"));

		editItem[0].setLabel(rb.getString("menu.edit.item.addreadlater"));
		editItem[1].setLabel(rb.getString("menu.edit.item.addbookmark"));
		editItem[2].setLabel(rb.getString("menu.edit.item.need"));
		editItem[3].setLabel(rb.getString("menu.edit.item.notneed"));

		optionItem[0].setLabel(rb.getString("menu.option.item.addremovekeywords"));
		optionItem[1].setLabel(rb.getString("menu.option.item.accountmanagement"));

		wMenu[0].setLabel(rb.getString("menu.file"));
		wMenu[0].add(fileItem[0]);
		wMenu[0].add(fileItem[1]);
		wMenu[0].addSeparator();
		wMenu[0].add(fileItem[2]);
		wMenu[0].add(fileItem[3]);
		wMenu[0].addSeparator();
		wMenu[0].add(fileItem[4]);

		wMenu[1].setLabel(rb.getString("menu.edit"));
		wMenu[1].add(editItem[0]);
		wMenu[1].addSeparator();
		wMenu[1].add(editItem[1]);
		wMenu[1].addSeparator();
		wMenu[1].add(editItem[2]);
		wMenu[1].add(editItem[3]);

		wMenu[2].setLabel(rb.getString("menu.option"));
		wMenu[2].add(optionItem[0]);
		wMenu[2].addSeparator();
		wMenu[2].add(optionItem[1]);

		wMenu[3].setLabel(rb.getString("menu.signout"));
		wMenu[3].addActionListener(this);

		mode = 0;
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
		if(e.getSource() == fileItem[4]) System.exit(0);
		for(int i = 0; i < fileItem.length -1; i++) if(e.getSource() == fileItem[i]) mode = i + 0x01;
		for(int i = 0; i < editItem.length; i++) if(e.getSource() == editItem[i]) mode = i + 0x11;
		for(int i = 0; i < optionItem.length; i++) if(e.getSource() == optionItem[i]) mode = i + 0x21;
	}
}
