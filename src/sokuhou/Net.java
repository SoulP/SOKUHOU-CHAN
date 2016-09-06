package sokuhou;

import java.util.ArrayList;
import java.util.List;

public abstract class Net extends Thread {
	// 抽象クラス
	private String URL;
	private List<String> listURL;

	// コンストラクタ
	public Net(){
		URL = "";
		listURL = new ArrayList<String>();
	}

	// コンストラクタ(文字列: URLアドレス)
	public Net(String URL){
		this.URL = URL;
		listURL = new ArrayList<String>();
	}

	// コンストラクタ(文字列の配列: 各URLアドレス)
	public Net(ArrayList<String> listURL){
		this.listURL = listURL;
		URL = "";
	}

	// コンストラクタ(NetWork: NetWorkオブジェクト)
	public Net(NetWork nw){
		URL = nw.getURL();
		listURL = nw.getListURL();
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

	public void setNet(Net net){
		URL = net.getURL();
		listURL = net.getListURL();
	}
	// 抽象: 実行処理
	public abstract void run();
}
