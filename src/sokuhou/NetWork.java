package sokuhou;

import java.util.ArrayList;
import java.util.List;

public class NetWork {
	// ネットワーク
	String URL;
	List<String> listURL;

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

}
