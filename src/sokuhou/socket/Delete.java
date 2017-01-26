package sokuhou.socket;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

import cipher.JCipher;
import cipher.JCipher.hash;
import data.Packet;
import data.User;
import exception.NullDataException;
import exception.NullUserException;
import io.JSocket;

public class Delete extends JSocket{
	// インスタンス変数
	ResourceBundle	rb = sokuhou.MainSYS.lang.getResBundle();	// 言語
	private String setDelete;									// 操作用の文字列
	private type dataType;										// データの種類

	// コンストラクタ
	public Delete(SecretKey secretKEY) throws IOException{
		super();
		this.secretKEY	= secretKEY;
		setDelete		= null;
		dataType		= type.NULL;
		id				= 6;
	}

	// コンストラクタ
	public Delete(SecretKey secretKEY, String setDelete) throws IOException{
		super();
		this.secretKEY	= secretKEY;
		this.setDelete	= setDelete;
		dataType		= type.NULL;
		id				= 6;
	}

	// コンストラクタ
	public Delete(SecretKey secretKEY, String setDelete, type dataType) throws IOException{
		super();
		this.secretKEY	= secretKEY;
		this.setDelete	= setDelete;
		this.dataType	= dataType;
		id				= 6;
	}

	// 削除したい行 入力
	public void setDelete(String setDelete){
		this.setDelete = setDelete;
	}

	// 情報の種類 入力
	public void setDataType(type dataType){
		this.dataType = dataType;
	}

	// 実行
	public void run(){
		try{
			// 例外処理
			if(user			== null)							throw new NullUserException("ユーザーがありません");
			if(user.email	== null || user.email.equals(""))	throw new NullUserException("ユーザーのメールアドレスがありません");
			if(secretKEY	== null)							throw new NullUserException("共通鍵がありません");
			if(setDelete	== null || setDelete.equals(""))	throw new NullDataException("削除したい行がありません");
			if(dataType		== null || dataType == type.NULL)	throw new NullDataException("データ種類が不明です");

			send(true);														// true送信
			while(!recvBoolean());											// 鯖の送信準備待ち
			if(!recvBoolean()){												// 鯖へのアクセス可否確認
				if(!socket.isClosed()) close();								// 接続が閉じられていない場合は、閉じる
				// エラーダイアログ表示
				JOptionPane.showMessageDialog(null, rb.getString("error.connect.failed"), rb.getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			Packet packet	 = new Packet(this);
			User tempUSER	 = new User();
			tempUSER.code	 = JCipher.toHashCode(hash.SHA512, user.email);
			packet.user		 = tempUSER;
			packet.controll	 = ctrl.DELETE;
			packet.dataTYPE	 = dataType;

			send(packet);
			packet			 = (Packet) recvObject();

			String tempSTR	 = user.email;
			tempSTR			+= user.password;
			tempSTR			+= packet.user.code;
			tempSTR			+= JCipher.toHashCode(hash.SHA512, JCipher.secretKey2bytes(secretKEY));

			packet			 = new Packet(this);
			tempUSER		 = new User();
			tempUSER.code	 = JCipher.toHashCode(hash.SHA512, tempSTR);
			

		}catch (Exception e){

		}
	}
}
