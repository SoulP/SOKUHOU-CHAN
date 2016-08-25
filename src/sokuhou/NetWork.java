package sokuhou;

import java.util.ArrayList;
import java.util.List;

public class NetWork {
	// ネットワーク
	private String URL;
	private List<String> listURL;

	// コンストラクタ
	public NetWork(){
		URL = "";
		listURL = new ArrayList<String>();
	}

	// コンストラクタ(文字列: URLアドレス)
	public NetWork(String URL){
		this.URL = URL;
		listURL = new ArrayList<String>();
	}

	// コンストラクタ(文字列の配列: 各URLアドレス)
	public NetWork(ArrayList<String> listURL){
		this.listURL = listURL;
		URL = "";
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

}
