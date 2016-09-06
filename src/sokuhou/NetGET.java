package sokuhou;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class NetGET extends NetWork {
	// URLアドレス先 HTML取得
	final String sjis = "SHIFT_JIS", utf8 = "UTF-8";
	private NetWork nw;

	// コンストラクタ
	public NetGET(){
		nw = null;
	}

	// コンストラクタ
	public NetGET(NetWork nw){
		super(nw);
		this.nw = nw;
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

	// 実行処理
	public void run(){
		try{
			BufferedReader open = new BufferedReader(new InputStreamReader(new URL(super.getURL()).openStream(), sjis));
			String str = null;
			while((str = open.readLine()) != null){
				System.out.println(str);
			}
			nw.setURL("YEAH!");
		}catch(Exception e){
			System.out.println(e);
			System.exit(-1);
		}
	}
}