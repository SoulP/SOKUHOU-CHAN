package sokuhou;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import io.JSocket;
import sokuhou.io.IOsys;
import sokuhou.network.NetGET;
import sokuhou.network.NetWork;

public class MainSYS {
	public static JSocket socket;
	public volatile static Lang lang;

	public static void main(String[] args) {
		// メインシステム

		// デフォルトエンコードをUTF-8に変更する
		try {
			System.setProperty("file.encoding", "UTF-8");
			Field charset;
			charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		} catch (Exception e){
			e.printStackTrace();
		}

		lang = new Lang();

		NetWork nw = new NetWork();
		nw.setURL("http://qiita.com/taiyop/items/050c6749fb693dae8f82");
		NetWork nGET = new NetGET(nw);

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

		/*
		try {
			String test = "test0123";
			String test1 = "テスト０１２３";
			Cipher ci = Cipher.getInstance("RSA");
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			keygen.initialize(2048);
			KeyPair key = keygen.generateKeyPair();
			ci.init(Cipher.ENCRYPT_MODE, key.getPublic());

			byte[] bTest = ci.doFinal(test.getBytes("UTF-8"));
			byte[] bTest1 = ci.doFinal(test1.getBytes("UTF-8"));

			System.out.println();
			System.out.println(test + ": " + new String(bTest, "UTF-8"));
			System.out.println(test1 + ": " + new String(bTest1, "UTF-8"));
			System.out.println();

			ci.init(Cipher.DECRYPT_MODE, key.getPrivate());

			byte[] dTest = ci.doFinal(bTest);
			byte[] dTest1 = ci.doFinal(bTest1);

			System.out.println("dTest: " + new String(dTest, "UTF-8"));
			System.out.println("dTest1: " + new String(dTest1, "UTF-8"));

			System.out.println(bTest.length);
			System.out.println(bTest1.length);

			byte[] privateKeyBytes = key.getPrivate().getEncoded();// 秘密鍵からバイト列
			byte[] publicKeyBytes = key.getPublic().getEncoded();// 公開鍵からバイト列

			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));// バイト列から秘密鍵
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));// バイト列から公開鍵

		} catch (Exception e) {
			System.out.println(e);
		}
		*/
		/*
		try {
			String test = "test";
			byte[] testB;
			JEncrypt enc = new JEncrypt();
			enc.generateRSA_KEY();
			enc.setType(JCipher.cipher.RSA);
			enc.setKey(enc.getPublicKey());
			enc.setBytes(test.getBytes("UTF-8"));
			enc.start();
			enc.join();

			testB = enc.getBytes();
			System.out.println(new String(testB, "UTF-8"));

			JDecrypt dec = new JDecrypt();
			dec.setKey(enc.getPrivateKey());
			dec.setType(JCipher.cipher.RSA);
			dec.setBytes(testB);
			dec.start();
			dec.join();

			testB = dec.getBytes();
			System.out.println(new String(testB, "UTF-8"));

			testB = null;

			enc = new JEncrypt(JCipher.cipher.AES);
			enc.generateAES_KEY();
			enc.setKey(enc.getSecretKey());
			enc.setBytes(test.getBytes("UTF-8"));
			enc.start();
			enc.join();

			testB = enc.getBytes();
			System.out.println(new String(testB, "UTF-8"));

			dec = new JDecrypt(JCipher.cipher.AES, enc.getKey(), testB);
			dec.start();
			dec.join();

			testB = dec.getBytes();
			System.out.println(new String(testB, "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		/*
		BigInteger big = BigInteger.valueOf(1234567890);
		String hash = JCipher.toHashCode(JCipher.hash.SHA512, big.toString());
		System.out.println();
		System.out.println(hash);
		*/
		/*
		String hash = JCipher.toHashCode(JCipher.hash.SHA1, "aaa@aaa.aaa");
		System.out.println();
		System.out.println(hash);
		*/
	}
}
