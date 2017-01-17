package server.socket;

import java.security.PublicKey;
import java.util.List;

import server.cipher.JCipher;
import server.cipher.JCipher.cipher;
import server.cipher.JDecrypt;
import server.cipher.JEncrypt;

public class AccountRegister extends JSocket{
	// インスタンス変数
	private byte[] data;
	protected WatchDogTimer wdt;
	protected PublicKey clientPublicKey;// クライアントの公開鍵

	// コンストラクタ
	public AccountRegister(){
		data = null;
		wdt = null;
	}

	// コンストラクタ
	public AccountRegister(List<String> info){
		data = null;
		setInfo(info);
		wdt = null;
	}

	// コンストラクタ
	public AccountRegister(byte[] data){
		this.data = data;
		wdt = null;
	}

	// コンストラクタ
	public AccountRegister(List<String> info, byte[] data){
		this.data = data;
		setInfo(info);
		wdt = null;
	}

	// スレッド
	public void run(){
		try {
			if(wdt == null) throw new Exception("Error: WatchDogTimer wdt is null.");// ウォッチドッグタイマがnullの場合、エラー発生させる
			// バイト列のデータを文字列に変換
			String strData = bytes2str(data);

			wdt.success();// 監視終了
			if(wdt.isAlive()) wdt.join();// 監視終了待ち

			// 確認
			if(!strData.equals("$REGISTER:USER;")){// 異なる場合
				sendBoolean(false);
				super.check = false;
				return;
			}

			// クライアントにtrue送信
			sendBoolean(true);

			// 受信
			data = recv("0000");// クライアントの公開鍵のバイト列取得

			wdt.start();// 監視開始

			clientPublicKey = JCipher.bytes2publicKey(data);// バイト列を公開鍵に変換
			JEncrypt enc = new JEncrypt(cipher.RSA, clientPublicKey);// 暗号化
			setSecretKey(enc.getSecretKey());// 共通鍵コピー
			JDecrypt dec = new JDecrypt(cipher.AES, getSecretKey());// 復号化
			byte[] key = JCipher.secretKey2bytes(getSecretKey());// 共通鍵をバイト列に変換

			// 暗号化
			enc.setBytes(key);// バイト列 入力
			enc.start();// 暗号化開始
			enc.join();// 暗号化終了待ち

			key = enc.getBytes();// 暗号化したバイト列 出力

			// 共通鍵をクライアントに送信
			createInfoBytes("0000", ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			buildBytes(key);// 公開鍵のバイト列を使って、送信用バイト列に構築する
			wdt.success();
			if(wdt.isAlive()) wdt.join();
			send(getAllBytes());// 構築したバイト列を送信する

			while(!recvBoolean());// クライアント待ち
			wdt.start();// 監視開始

			// 接続番号と接続鍵の発行
			setConnectionNO(randomNO(4));// 接続番号
			setConnectionKEY(randomNO(4));// 接続鍵

			// 送信データ作成
			createInfoBytes("0000", ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			strTEMP = getConnectionNO() + "" + getConnectionKEY();// 接続番号と接続鍵を文字列にし、一時的保管
			buildBytes(str2bytes(strTEMP));// バイト列に変換し、構築する
			wdt.success();// 監視終了
			if(wdt.isAlive()) wdt.join();// 監視終了待ち
			send();// 送信

			setConnectionNO(nextConnect());
			//

			enc = new JEncrypt(cipher.AES, getSecretKey());
			//

		} catch (Exception e) {
			// エラー
			System.out.println(e);// エラー表示
			e.printStackTrace();// エラー原因追跡表示
		}
	}

	// ウォッチドッグタイマ 入力
	public void setWatchDogTimer(WatchDogTimer wdt){
		this.wdt = wdt;
	}

}
