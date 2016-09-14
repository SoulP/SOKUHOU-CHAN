package sokuhou;

public class MainSYS {

	public static void main(String[] args) {
		// メインシステム
		NetWork nw = new NetWork();
		nw.setURL("http://qiita.com/taiyop/items/050c6749fb693dae8f82");
		NetWork nGET = new NetGET(nw);

		DBA dba = null;
		IOsys io = null;
		nGET.start();
		try {
			nGET.join();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		nw = nGET;
		System.out.println(nw.getURL()+"\n");

		for(String z : nw.getHTML()){
			System.out.println(z);
		}
		System.out.println(nw.getTitle());
	}
}
