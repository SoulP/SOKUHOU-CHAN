package sokuhou.socket;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import cipher.JCipher;
import cipher.JDecrypt;
import cipher.JEncrypt;
import cipher.JCipher.cipher;
import io.JSocket;
import sokuhou.MainSYS;

public class Login extends JSocket{

	// コンストラクタ
	public Login(){
		super();
	}

	// CHAP認証
	private String CHAP(String password, int chap) throws UnsupportedEncodingException{
		BigInteger bigInt = new BigInteger(password.getBytes("UTF-8"));
		bigInt = bigInt.pow(chap);
		return JCipher.toHashCode(JCipher.hash.SHA512, bigInt.toString());
	}

	// 実行
	public void run(){
		// 初期化
		JEncrypt enc = null;// null値で初期化
		JDecrypt dec = null;// null値で初期化
		byte[] publicKEY = null;// null値で初期化
		byte[] buff = null;
		int chap = 0;// 0で初期化

		check = false;

		try{
// 01. CLが接続の準備をする(初期化など)
			// 値がない場合は、エラーとして発生させる
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");

			open();// 接続を開く

// 02. CLが公開鍵と秘密鍵を生成する
			// 公開鍵と秘密鍵を生成する
			enc = new JEncrypt();// 暗号化
			enc.generateRSA_KEY();// RSA用の公開鍵と秘密鍵を生成する
			dec = new JDecrypt(cipher.RSA, enc.getPrivateKey());// 復号化
			publicKEY = JCipher.publicKey2bytes(enc.getPublicKey()); // 公開鍵のバイト列

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
			buff = recv("9999");
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
			setSecretKey(JCipher.bytes2secretKey(dec.getBytes()));// 復号化したバイト列を秘密鍵に生成
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
			chap = recvInt();

// 25. CLが接続番号と接続番号用の鍵を使って、次の接続番号を生成する
			createInfoBytes("" + nextConnect(), ctrl.READ, type.USER);// 接続情報をバイト列に出力する

			// 暗号と復号の秘密鍵を設定する
			enc = new JEncrypt(cipher.AES, getSecretKey());// 暗号化
			dec = new JDecrypt(cipher.AES, getSecretKey());// 復号化

// 26. CLが復号化したCHAP認証用の乱数とパスワードを使って計算し、ハッシュ値に出力する
			String chapHash = CHAP(getPassword(), chap);

// 27. CLがメールアドレスのハッシュ値と、計算したパスワードのハッシュ値（と、ワンタイムパスワード）をデータ情報として秘密鍵で暗号化する
			sData = "$EMAIL:";
			sData += JCipher.toHashCode(JCipher.hash.SHA512, getEmail());
			sData += ";";
			sData += "$CHAP:";
			sData += chapHash;
			sData += ";";

			if(getOTP() != null){
				sData += "$OTP:";
				sData += getOTP();
				sData += ";";
			}

			// 暗号化
			setDataBytes(str2bytes(sData));
			enc.setBytes(getDataBytes());
			enc.start();
			enc.join();

			buildBytes(enc.getBytes());

// 28. CLが暗号化したデータ情報をSVに送信する(接続情報の接続番号は25.の次の接続番号)
			// 送信
			send(getAllBytes());

// FIN. 結果待ち
			// 受信
			check = recvBoolean();

		} catch (Exception e){
		// エラーが起きた際の処理
			System.out.println(e);// エラー内容を出力する
			e.printStackTrace();// 原因の追跡を表示
			check = false;
		}finally{
			try {
				// 接続を閉じる
				if(!getSocket().isClosed())close();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				// 閉じる時にエラーが起きた際の処理
				System.out.println(e);// エラー内容表示
				e.printStackTrace();// 原因を追跡する
				setSocket(null);// ソケットをnull値で消す
				setDIS(null);// 受信用ストリームをnull値で消す
				setDOS(null);// 送信用ストリームをnull値で消す
			}
			// 初期化
			clearUser();// null値で初期化
			clearInfoList();// null値で初期化
			sData = null;// null値で初期化
			enc = null;// null値で初期化
			dec = null;// null値で初期化
			publicKEY = null;// null値で初期化
			buff = null;// null値で初期化
			chap = 0;// 0で初期化
			MainSYS.socket = this;// 情報保持
		}
	}
}
