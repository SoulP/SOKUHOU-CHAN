package sokuhou;

public class MainSYS {

	public static void main(String[] args) {
		// メインシステム
		NetWork nw = new NetWork();
		nw.setURL("http://google.com/");
		NetGET nGET = new NetGET(nw);

		DBA dba = null;
		IOsys io = null;
		nGET.start();
		try {
			nGET.join();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		nw = nGET.getNet();
		System.out.println(nw.getURL());
	}
}
