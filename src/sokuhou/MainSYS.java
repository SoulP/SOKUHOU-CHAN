package sokuhou;

public class MainSYS {

	public static void main(String[] args) {
		// メインシステム
		NetWork nw = new NetWork();
		nw.setURL("http://qiita.com/taiyop/items/050c6749fb693dae8f82");
		NetWork nGET = new NetGET(nw);

		DBA dba = null;
		IOsys io = null;
		// テスト; 接続開始
		nGET.start();
		try {
			nGET.join();// テスト; 接続完了を待つ
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// テスト; 受信した内容をコピー
		nw = nGET;
		// テスト; URLアドレス出力
		System.out.println(nw.getURL()+"\n");
		// テスト; HTML文出力
		for(String z : nw.getHTML()){
			System.out.println(z);
		}
		// テスト; タイトル出力
		System.out.println("\n" + nw.getTitle());
	}
}
