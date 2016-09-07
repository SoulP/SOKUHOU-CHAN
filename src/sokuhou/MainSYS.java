package sokuhou;

public class MainSYS {

	public static void main(String[] args) {
		// メインシステム
		NetWork nw = new NetWork();
<<<<<<< HEAD
		nw.setURL("http://google.com/");
=======
		nw.setURL("http://google.co.jp/");
>>>>>>> branch 'Head' of https://github.com/SoulP/SOKUHOU-CHAN.git
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
<<<<<<< HEAD
		System.out.println(nw.getURL());
=======
		System.out.println(nw.getURL()+"\n");

		for(String z : nw.getHTML()){
			System.out.println(z);
		}
>>>>>>> branch 'Head' of https://github.com/SoulP/SOKUHOU-CHAN.git
	}
}
