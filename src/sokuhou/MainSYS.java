package sokuhou;

public class MainSYS {

	public static void main(String[] args) {
		// メインシステム
		NetWork nw = new NetWork();
		nw.setURL("http://qiita.com/taiyop/items/050c6749fb693dae8f82");
		NetWork nGET = new NetGET(nw);

		DBA dba = null;
		IOsys io = new IOsys();

		// テスト-------------------------------------
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
		// テスト; 出力
		System.out.println("--------------------------------------");
		System.out.println("URLアドレス: " + nw.getURL());
		System.out.println("ドメイン名: " + nw.getDomain());
		System.out.println("タイトル: " + nw.getTitle());
		System.out.println("説明: " + nw.getDescription());
		System.out.print("キーワード: ");
		for(String Str : nw.getKeywords()){
			if(Str.equals(""))break;
			System.out.print(Str + ", ");
		}
		System.out.println();
		System.out.println("サイト名: " + nw.getSiteName());
		System.out.println("ページの種類: " + nw.getType());
		System.out.println("サムネイル画像のURLアドレス: " + nw.getImageURL());
		System.out.println("アイコンのURLアドレス: " + nw.getIconURL());
		io.winPOP.setImage2Panel(nw.getBuffImage());
		io.winPOP.setViewImagePanel(true);
		io.winPOP.setVisible(true);

		/* システムトレイアイコン(タスクバー)
		TrayIcon icon = new TrayIcon(nw.getBuffImage());
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		*/
		/* ワンタイムパスワード
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
	    final GoogleAuthenticatorKey key = gAuth.createCredentials();
	    System.out.println(key.getKey());
	    System.out.println(key.getScratchCodes());
	    System.out.println();
	    GoogleAuthenticatorQRGenerator qr = new GoogleAuthenticatorQRGenerator();
	    System.out.println(qr.getOtpAuthURL("速報ちゃん", "ユーザ名", key));
	    System.out.println();
	    System.out.println(gAuth.getTotpPassword(key.getKey()));

	    boolean time = true;
	    while(true){
	    	if(System.currentTimeMillis()%30000 == 0 && time){
	    		System.out.println(gAuth.getTotpPassword(key.getKey()));
	    		time = false;
	    	}
	    	if(System.currentTimeMillis()%30000 != 0 && !time){
	    		time = true;
	    	}
	    }
	    */
		/*
		byte[] b = new byte[2];
		int x = 9627;
		b[0] = (byte)(x & 0xFF);
		b[1] = (byte)(x >>> 8);
		System.out.println(x);
		System.out.printf("%X%X\n",b[1],b[0]);
		int y = 0;
		y = b[1] & 0xFF;
		y = y << 8;
		y += b[0] & 0xFF;
		System.out.println(y);
		*/
	}
}
