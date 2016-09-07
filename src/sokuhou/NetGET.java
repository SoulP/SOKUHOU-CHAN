package sokuhou;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetGET extends NetWork {
	// URLアドレス先 HTML取得

	// エンコード種類
	final String sjis = "SHIFT_JIS", utf8 = "UTF-8";

	// コンストラクタ
	public NetGET(){}

	// コンストラクタ
	public NetGET(NetWork nw){super(nw);}

	// 文字列から文字列配列に変換; 入力: 文字列; 出力: 文字列配列
	public List<String> str2strArray(String str){
		List<String> strArray = new ArrayList<String>();
		int a = 0;
		for(int i=1; i < str.length(); i++ ){
			if(str.substring(a,a+1).equals("<")){
				if(str.substring(i,i+1).equals(">")){
					strArray.add(str.substring(a, i+1));
					a=i+1;
				}
			}else{
				if(str.substring(i,i+1).equals("<")){
					strArray.add(str.substring(a,i));
					a=i;
				}
			}
		}
		return strArray;
	}

	// 実行処理
	public void run(){
		try{
			BufferedReader open = new BufferedReader(new InputStreamReader(new URL(super.getURL()).openStream(), sjis));
			String str = "";
			String buff = null;
			while((buff = open.readLine()) != null){
				str += buff;
			}
			super.setHTML(str2strArray(str));
		}catch(Exception e){
			System.out.println(e);
			System.exit(-1);
		}
	}
}
