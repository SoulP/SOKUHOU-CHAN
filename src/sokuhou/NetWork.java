package sokuhou;

import java.util.ArrayList;

public class NetWork extends Net{
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
}
