package sokuhou.JSocket;

import sokuhou.cipher.JCipher.cipher;
import sokuhou.cipher.JDecrypt;
import sokuhou.cipher.JEncrypt;

public class Login extends JSocket{
	private boolean check;// 接続処理確認

	// コンストラクタ
	public Login(){
		super();
		check = false;
	}

	// 接続処理確認 true = 接続完了, false = 接続失敗
	public boolean check(){
		return check;
	}

	public void run(){
		try{
			// 値がない場合は、エラーとして発生させる
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");

			// 初期化
			byte[] rData = null;// バイト列のデータ(主に受信)

			open(); // 接続を開く

			// 公開鍵と秘密鍵を生成する
			JEncrypt enc = new JEncrypt();// 暗号化
			enc.generateRSA_KEY();// RSA用の公開鍵と秘密鍵を生成する
			JDecrypt dec = new JDecrypt(cipher.RSA, enc.getPrivateKey());// 復号化
			byte[] publicKEY = enc.publicKey2bytes(enc.getPublicKey()); // 公開鍵のバイト列

			// アカウントの登録を要求する
			createInfoBytes("9999", ctrl.READ, type.USER);// 接続情報をバイト列に出力する
			setDataBytes(str2bytes("$LOGIN:USER;"));// 文字列をバイト列に出力する
			buildBytes();// 送信用バイト列に構築する
			send(getAllBytes());// 構築したバイト列を送信する

			// サーバが応じるかどうか確認する true = OK, false = NG
			if(!recvBoolean()) return;// falseの場合、終了

			setConnectionNO(randomNO(4));

			// クライアント(自分)の公開鍵を送信
			createInfoBytes("" + getConnectionNO(), ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			buildBytes(publicKEY);// 公開鍵のバイト列を使って、送信用バイト列に構築する

			send(getAllBytes());// 構築したバイト列を送信する



			// 接続を閉じる
			close();
		} catch (Exception e){
		// エラーが起きた際の処理
			System.out.println(e);// エラー内容を出力する
			e.printStackTrace();;// 原因の追跡を表示
			try {
				// 接続を閉じる
				close();
			} catch (Exception e1) {
				// 閉じる時にエラーが起きた際の処理
				System.out.println(e1);// エラー内容表示
				e1.printStackTrace();// 原因を追跡する
				setSocket(null);// ソケットをnull値で消す
				setDIS(null);// 受信用ストリームをnull値で消す
				setDOS(null);// 送信用ストリームをnull値で消す
			}
		}
	}
}
