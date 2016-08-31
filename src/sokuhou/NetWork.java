package sokuhou;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public abstract class NetWork extends Net{
	// ネットワーク
//テスト
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
		}
			
		


