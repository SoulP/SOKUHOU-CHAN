package sokuhou.socket;

import cipher.JCipher;
import cipher.JDecrypt;
import cipher.JEncrypt;
import io.JSocket;
import sokuhou.MainSYS;

public class Recv extends JSocket{
	private String setRecv;// 操作用の文字列
	private String output;// 取得した情報の文字列
	private type dataType;// データの種類

	// コンストラクタ
	public Recv(){
		// 初期化
		super();
		setRecv = null;// null値で初期化
		output = null;// null値で初期化
		dataType = type.NULL;// 初期化
	}

	// 情報読込 文字列 入力
	public void setRecv(String setRecv){
		this.setRecv = setRecv;
	}

	// 情報の種類 入力
	public void setDataType(type dataType){
		this.dataType = dataType;
	}

	// 取得した情報 出力
	public String output(){
		return output;
	}

	// 実行
	public void run(){
		// 初期化
		JEncrypt enc = null;// null値で初期化
		JDecrypt dec = null;// null値で初期化
		byte[] buff = null;// null値で初期化

		check = false;

		try{
			// 値がない場合は、エラーとして発生させる
			if(getSecretKey() == null) throw new Exception("ERROR: key is null");
			if(getEmail() == null) throw new Exception("ERROR: email is null");

			enc = new JEncrypt(JCipher.cipher.AES, getSecretKey());
			dec = new JDecrypt(JCipher.cipher.AES, getSecretKey());

			open();// 接続を開く

// 01. CLがSVに情報受信を要求する(データ情報は共通鍵で暗号化し、接続情報の接続番号はログイン処理後の次の接続番号)
			setDataBytes(str2bytes(setRecv));
			// 暗号化
			enc.setBytes(getDataBytes());
			enc.start();

			// 接続情報
			setConnectionNO(nextConnect());
			createInfoBytes("" + nextConnect(), ctrl.READ, dataType);// 接続情報をバイト列に出力する

			enc.join();// 暗号化処理終了待ち

			buildBytes(enc.getBytes());

			// 送信
			send(getAllBytes());

// 10. CLがSVからTRUEと受けた場合、SVにTRUEを送る
// -10. CLがSVからFLASEと受けた場合、接続を閉じる
			// 受信
			boolean get = recvBoolean();
			if(get){
				sendBoolean(true);
			}else{
				if(!getSocket().isClosed()) close();
				return;
			}
			setConnectionNO(nextConnect());
			buff = recv("" + nextConnect());

// 12. CLがSVから受け取ったデータ情報を復号化する
			if(!getSocket().isClosed()) close();// 接続を閉じる
			dec.setBytes(buff);
			dec.start();
			dec.join();

// FIN. 出力
			output = bytes2str(dec.getBytes());
			check = true;

		} catch (Exception e){
		// エラーが起きた際の処理
			System.out.println(e);// エラー内容を出力する
			e.printStackTrace();// 原因の追跡を表示
			check = false;
		}finally{
			try {
				// 接続を閉じる
				if(!getSocket().isClosed()) close();
			} catch (Exception e) {
				// 閉じる時にエラーが起きた際の処理
				System.out.println(e);// エラー内容表示
				e.printStackTrace();// 原因を追跡する
				setSocket(null);// ソケットをnull値で消す
				setDIS(null);// 受信用ストリームをnull値で消す
				setDOS(null);// 送信用ストリームをnull値で消す
			}
			// 初期化
			clearInfoList();// null値で初期化
			sData = null;// null値で初期化
			enc = null;// null値で初期化
			dec = null;// null値で初期化
			buff = null;// null値で初期化
			MainSYS.socket = this;// 情報保持
		}
	}
}
