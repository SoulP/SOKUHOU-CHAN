package sokuhou;

import java.util.ArrayList;

public class NetWork extends Net{
	// ネットワーク

	private NetWork nw;

	// コンストラクタ
	public NetWork(){
		super();
	}

	// コンストラクタ(文字列: URLアドレス)
	public NetWork(String URL){
		super(URL);
	}

	// コンストラクタ(文字列の配列: 各URLアドレス)
	public NetWork(ArrayList<String> listURL){
		super(listURL);
	}

	// コンストラクタ(NetWork: NetWorkオブジェクト)
	public NetWork(NetWork nw){
		super(nw);
	}

	// 入力: NetWorkオブジェクト
	public void setNet(NetWork nw){
		super.setNet(nw);
		this.nw = nw;
	}

	// 出力: NetWorkオブジェクト
	public NetWork getNet(){
		return nw;
	}
}