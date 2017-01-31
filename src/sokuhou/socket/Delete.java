package sokuhou.socket;

import java.io.IOException;
import java.security.Key;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

import cipher.JCipher;
import cipher.JCipher.hash;
import data.Data;
import data.Packet;
import data.User;
import exception.NullDataException;
import exception.NullUserException;
import io.JSocket;

public class Delete extends JSocket{
	// インスタンス変数
	ResourceBundle	rb = sokuhou.MainSYS.lang.getResBundle();	// 言語
	private type dataType;										// データの種類

	// コンストラクタ
	public Delete(Key key) throws IOException{
		super();
		this.key	= key;
		data			= null;
		dataType		= type.NULL;
		id				= 6;
	}

	// コンストラクタ
	public Delete(Key key, Data data) throws IOException{
		super();
		this.key	= key;
		this.data	= data;
		dataType	= type.NULL;
		id			= 6;
	}

	// コンストラクタ
	public Delete(Key key, Data data, type dataType) throws IOException{
		super();
		this.key		= key;
		this.data		= data;
		this.dataType	= dataType;
		id				= 6;
	}

	// 削除したい行 入力
	public void setData(Data data){
		this.data = data;
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
			if(key			== null)							throw new NullUserException("共通鍵がありません");
			if(data			== null)							throw new NullDataException("削除したい行がありません");
			if(dataType		== null || dataType == type.NULL)	throw new NullDataException("データ種類が不明です");

			send(true);														// true送信
			while(!recvBoolean());											// 鯖の送信準備待ち
			if(!recvBoolean()){												// 鯖へのアクセス可否確認
				// エラーダイアログ表示
				JOptionPane.showMessageDialog(null, rb.getString("error.connect.failed"), rb.getString("error"), JOptionPane.ERROR_MESSAGE);
				if(!socket.isClosed()) close();								// 接断
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
			tempSTR			+= JCipher.toHashCode(hash.SHA512, JCipher.secretKey2bytes((SecretKey) key));

			tempUSER		 = new User();
			tempUSER.code	 = JCipher.toHashCode(hash.SHA512, tempSTR);

			data.pack(key);

			packet			 = new Packet(this);
			packet.user		 = tempUSER;
			packet.data		 = data;

			send(packet);
			while(!recvBoolean());
			// エラーダイアログ表示
			if(!(check = recvBoolean())) JOptionPane.showMessageDialog(null, rb.getString("error.connect.failed"), rb.getString("error"), JOptionPane.ERROR_MESSAGE);

			// 接断
			if(!socket.isClosed()) close();
		}catch (Exception e){

		}
	}
}
