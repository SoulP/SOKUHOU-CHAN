package sokuhou.NETWORK;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//抽象クラス
public abstract class Net extends Thread {

	private String			URL;// URLアドレス接続先
	private List<String>	listURL;// URLアドレス配列
	private String			domain;// ドメイン名
	private List<String>	html;// HTML文
	private String			title;// タイトル
	private String			site_name;// サイト名
	private String			description;// 説明文
	private List<String>	keywords;// キーワード配列
	private String			type;// ページの種類; トップページ = website | 各ページ = article | 他にも色々ある
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

	// 入力: ドメイン名
	public void setDomain(String domain){
		this.domain = domain;
	}

	// 出力: ドメイン名
	public String getDomain(){
		return domain;
	}

	// 入力: HTML情報
	public void setHTML(List<String> html){
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

    // 入力: サイト名
    public void setSiteName(String site_name){
    	this.site_name = site_name;
    }

    // 出力: サイト名
    public String getSiteName(){
    	return site_name;
    }

    // 入力: 説明文
    public void setDescription(String description){
    	this.description = description;
    }

    // 出力: 説明文
    public String getDescription(){
    	return description;
    }

    // 入力: キーワード配列
    public void setKeywords(List<String> keywords){
    	this.keywords = keywords;
    }

    // 出力: キーワード配列
    public List<String> getKeywords(){
    	return keywords;
    }

    // 入力: ページの種類
    public void setType(String type){
    	this.type = type;
    }

    // 出力: ページの種類
    public String getType(){
    	return type;
    }

    // 入力: サムネイル画像のURLアドレス
    public void setImageURL(String imageURL){
    	this.imageURL = imageURL;
    }

    // 出力: サムネイル画像のURLアドレス
    public String getImageURL(){
    	return imageURL;
    }

    // 入力: サムネイル画像
    public void setBuffImage(BufferedImage buffImage){
    	this.buffImage = buffImage;
    }

    // 出力: サムネイル画像
    public BufferedImage getBuffImage(){
    	return buffImage;
    }

    // 入力: アイコンのURLアドレス
    public void setIconURL(String iconURL){
    	this.iconURL = iconURL;
    }

    // 出力: アイコンのURLアドレス
    public String getIconURL(){
    	return iconURL;
    }

    // 入力: アイコン
    public void setBuffIcon(BufferedImage buffIcon){
    	this.buffIcon = buffIcon;
    }

    // 出力: アイコン
    public BufferedImage getBuffIcon(){
    	return buffIcon;
    }

}
