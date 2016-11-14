package sokuhou.JSocket;

import sokuhou.JCipher.JCipher;
import sokuhou.JCipher.JEncrypt;

public class Logout extends JSocket{

	// コンストラクタ
	public Logout(){
		super();
	}

	// 実行
	public void run(){
		// 初期化
		JEncrypt enc = null;// null値で初期化
		try{
			// 値がない場合は、エラーとして発生させる
			if(getSecretKey() == null) throw new Exception("ERROR: key is null");
			if(getEmail() == null) throw new Exception("ERROR: email is null");

			enc = new JEncrypt(JCipher.cipher.AES, getSecretKey());

			open();// 接続を開く

// 01. CLがSVにログアウトを要求する(データ情報は共通鍵で暗号化し、接続情報の接続番号はログイン処理後の次の接続番号)

			sData = "$LOGOUT:";
			sData += JCipher.toHashCode(JCipher.hash.SHA512, getEmail());
			sData += ";";

			setDataBytes(str2bytes(sData));

			// 暗号化
			enc.setBytes(getDataBytes());
			enc.start();

			// 接続情報
			setConnectionNO(nextConnect());
			createInfoBytes("" + nextConnect(), ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する

			enc.join();

			buildBytes(enc.getBytes());

			// 送信
			send(getAllBytes());

// FIN. 結果待ち
			// 受信
			check = recvBoolean();// boolean型の値を受信する true = ログアウト完了, false = ログアウト失敗

			close();// 接続を閉じる

		} catch (Exception e){
		// エラーが起きた際の処理
			System.out.println(e);// エラー内容を出力する
			e.printStackTrace();// 原因の追跡を表示
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
		}finally{
			// 初期化
			clearUser();// null値で初期化
			setSecretKey(null);// null値で初期化
			setConnectionNO(0);// 0で初期化
			setConnectionKEY(0);// 0で初期化
			clearInfoList();// null値で初期化
			sData = null;// null値で初期化
			enc = null;// null値で初期化
		}
	}
}
