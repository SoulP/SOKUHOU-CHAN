package sokuhou;

import java.util.ArrayList;
import java.util.List;

public abstract class Net extends Thread {
	// 抽象クラス
	private String URL;// URLアドレス接続先
	private List<String> listURL;// URLアドレス配列
	private List<String> html;// HTML文
	private String title;// タイトル
	private String description;// 説明
	private String imageURL;// 画像(ロゴやアイコンなど)のURLアドレス


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

	// 出力: HTML情報
	public List<String> getHTML(){
		return html;
	}

    // 入力: タイトル
    public void setTitle(String title){
    	this.title = title;
    }

    // 出力: タイトル
    public String getTitle(){
    	return title;
    }


	public void setNet(NetWork nw){
		URL = nw.getURL();
		listURL = nw.getListURL();
	}
}
