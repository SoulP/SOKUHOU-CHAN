package sokuhou;

import java.util.ArrayList;
import java.util.List;

public abstract class Net extends Thread {
	// 抽象クラス
	private String URL;
	private List<String> listURL;
	private List<String> html;

	// コンストラクタ
	public Net(){
		URL = "";
		listURL = new ArrayList<String>();
		html = new ArrayList<String>();
	}

	// コンストラクタ(文字列: URLアドレス)
	public Net(String URL){
		this.URL = URL;
		listURL = new ArrayList<String>();
		html = new ArrayList<String>();
	}

	// コンストラクタ(文字列の配列: 各URLアドレス)
	public Net(ArrayList<String> listURL){
		this.listURL = listURL;
		URL = "";
		html = new ArrayList<String>();
	}

	// コンストラクタ(NetWork: NetWorkオブジェクト)
	public Net(NetWork nw){
		URL = nw.getURL();
		listURL = nw.getListURL();
		html = nw.getHTML();
	}

	// 入力: URLアドレス
	public void setURL(String URL){
		this.URL = URL;
	}

	// 出力: URLアドレス
	public String getURL(){
		return URL;
	}

	// 入力: 各URLアドレス
	public void setListURL(ArrayList<String> listURL){
		this.listURL = listURL;
	}

	// 出力: 各URLアドレス
	public List<String> getListURL(){
		return listURL;
	}

	// 入力: HTML情報
	protected void setHTML(List<String> html){
		this.html = html;
	}
<<<<<<< HEAD
=======

	// 出力: HTML情報
	public List<String> getHTML(){
		return html;
	}


	public void setNet(NetWork nw){
		URL = nw.getURL();
		listURL = nw.getListURL();
	}
>>>>>>> branch 'Head' of https://github.com/SoulP/SOKUHOU-CHAN.git
}
