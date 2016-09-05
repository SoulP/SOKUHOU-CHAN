package sokuhou;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

public abstract class NetWork extends Net{
	// ネットワーク

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

	// 実行処理
	public void run(){

		System.out.println("Hellow Java");//Hellow Javaと出力


		System.out.println("こんにちは");//こんにちはと出力


		System.out.println("ニーハオ");//ニーハオと出力

		}
	/** Httpプロトコルによるソース表示（HttpURLConnectionクラス利用） **/
	public class HttpUrl {
	  public void main(String args[]) {
	    String url = "http://ash.jp/";
	    String proxyHost = "";
	    int    proxyPort = 8080;

	    URL urlObj;
	    HttpURLConnection urlCon;
	    BufferedReader urlIn;
	    String str;

	    if (args.length > 0) {
	      url = args[0];
	    }
	//  System.out.println("URL: " + url);

	    try {
	      urlObj = new URL("http", proxyHost, proxyPort, url);

	      // URL接続
	      urlCon = (HttpURLConnection)urlObj.openConnection();
	      urlCon.setRequestMethod("GET");
	      urlIn = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

	      // HTMLソースの表示
	      while ((str = urlIn.readLine()) != null) {
	        System.out.println(str);
	      }

	      // URL切断
	      urlIn.close();
	      urlCon.disconnect();

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	  }
	}
}




