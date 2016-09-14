package sokuhou;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//抽象クラス
public abstract class Net extends Thread {

	private String			URL;// URLアドレス接続先
	private List<String>	listURL;// URLアドレス配列
	private List<String>	html;// HTML文
	private String			title;// タイトル
	private String			site_name;// サイト名
	private String			description;// 説明
	private List<String>	keywords;// キーワード配列
	private String			type;// ページの種類; トップページ = website | 各ページ = article
	private String			imageURL;// サムネイル画像のURLアドレス
	private BufferedImage	buffImage;// サムネイル画像
	private String			iconURL;// アイコンのURLアドレス
	private BufferedImage	buffIcon;// アイコン

	// エンコード種類
	final String sjis = "Shift_JIS", utf8 = "UTF-8", jis = "JISAutoDetect";

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
}
