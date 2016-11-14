package sokuhou.JSocket;

import sokuhou.cipher.JCipher;
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
// 01. CLが接続の準備をする(初期化など)
			// 値がない場合は、エラーとして発生させる
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");

			// 初期化
			rData = null;// バイト列のデータ(主に受信)

			open(); // 接続を開く

// 02. CLが公開鍵と秘密鍵を生成する
			// 公開鍵と秘密鍵を生成する
			JEncrypt enc = new JEncrypt();// 暗号化
			enc.generateRSA_KEY();// RSA用の公開鍵と秘密鍵を生成する
			JDecrypt dec = new JDecrypt(cipher.RSA, enc.getPrivateKey());// 復号化
			byte[] publicKEY = JCipher.publicKey2bytes(enc.getPublicKey()); // 公開鍵のバイト列

// 03. CLがSVに接続を要求する(接続情報の接続番号は9999)
			// アカウントの登録を要求する
			createInfoBytes("9999", ctrl.READ, type.USER);// 接続情報をバイト列に出力する
			setDataBytes(str2bytes("$LOGIN:USER;"));// 文字列をバイト列に出力する
			buildBytes();// 送信用バイト列に構築する
			// 送信
			send(getAllBytes());// 構築したバイト列を送信する

// 05. CLが応じられた結果を確認する
// 06. TRUEの場合は、CLの公開鍵をSVに送る(接続情報の接続番号は9999)
// -06. FALSEの場合は、CLは閉じる
			// 受信
			// サーバが応じるかどうか確認する true = OK, false = NG
			if(!recvBoolean()){// falseの場合、終了
				if(!getSocket().isClosed()) close();// 接続が閉じられていない場合は、閉じる
				return;
			}

			// クライアント(自分)の公開鍵を送信
			createInfoBytes("9999", ctrl.READ, type.USER);// 接続情報をバイト列に出力する
			buildBytes(publicKEY);// 公開鍵のバイト列を使って、送信用バイト列に構築する

			// 送信
			send(getAllBytes());// 構築したバイト列を送信する

// 10. CLがSVから共通鍵を受け取り、CLの秘密鍵で復号化する
			// 受信
			byte[] buff = recv("9999");
			// 復号化
			dec.setBytes(buff);// データのバイト列を復号化に入力する
			dec.start();// 復号化開始

// 11. CLがSVにTRUEで応じる
			// 送信
			sendBoolean(true);

// 15. CLがSVから暗号化されたデータ情報を受け取り、CLの秘密鍵で復号化する
			// 受信
			buff = recv("9999");

			// 復号化
			dec.join();// 復号化処理終了待ち
			key = JCipher.bytes2secretKey(dec.getBytes());// 復号化したバイト列を秘密鍵に生成し、keyに保存する
			dec.setBytes(buff);// データのバイト列を復号化に入力する
			dec.start();// 復号化開始
			dec.join();// 復号化処理終了待ち

// 16. CLが復号化されたデータ情報を接続番号と接続番号用の鍵としてint型に変換する
			// 接続番号と接続番号用の乱数
			String connectKEY = bytes2str(dec.getBytes());// 復号化したバイト列を文字列に変換し、connectKEYに保存する
			setNextConnection(connectKEY);// 次の接続番号の設定

// 17. CLがSVにTRUEで応じる
			// 送信
			sendBoolean(true);

// 24. CLがSVからint型でCHAP認証用の乱数を受け取る
			// 受信
			int chap = recvInt();

// 25. CLが接続番号と接続番号用の鍵を使って、次の接続番号を生成する
			createInfoBytes("" + nextConnect(), ctrl.READ, type.USER);// 接続情報をバイト列に出力する

			// 暗号と復号の秘密鍵を設定する
			enc = new JEncrypt(cipher.AES, key);// 暗号化
			dec = new JDecrypt(cipher.AES, key);// 復号化

// 26. CLが復号化したCHAP認証用の乱数とパスワードを使って計算し、ハッシュ値に出力する
// 27. CLがメールアドレスのハッシュ値と、計算したパスワードのハッシュ値（と、ワンタイムパスワード）をデータ情報として秘密鍵で暗号化する
// 28. CLが暗号化したデータ情報をSVに送信する(接続情報の接続番号は25.の次の接続番号)
// FIN. 結果待ち


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
