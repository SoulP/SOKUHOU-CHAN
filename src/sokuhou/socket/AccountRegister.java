package sokuhou.socket;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import cipher.JCipher;
import cipher.JCipher.cipher;
import cipher.JDecrypt;
import data.Packet;
import data.User;
import exception.NullUserException;
import exception.UserException;
import io.JSocket;

public class AccountRegister extends JSocket{
	// インスタンス変数
	ResourceBundle	rb = sokuhou.MainSYS.lang.getResBundle(); // 言語

	// コンストラクタ
	public AccountRegister() throws IOException{
		super();
		user	= new User();
		id		= 1;
	}

	// コンストラクタ
	public AccountRegister(User user) throws IOException{
		super();
		this.user	= user;
		id			= 1;
	}

	// 実行
	public void run(){
			try {
				// 初期化
				rb = sokuhou.MainSYS.lang.getResBundle();

				// 例外処理
				if(user.name		== null || user.name.equals(""))		throw new NullUserException("名前がありません");
				if(user.email		== null || user.email.equals(""))		throw new NullUserException("メールアドレスがありません");
				if(user.password	== null || user.password.equals(""))	throw new NullUserException("パスワードがありません");
				if(user.birthday	== null || user.birthday.equals(""))	throw new NullUserException("誕生日がありません");
				if(!checkName(user.name))									throw new UserException("名前に特殊記号を使用することは、できません");
				if(!checkEmail(user.email))									throw new UserException("不正なメールアドレスです");
				if(!checkPassword(user.password))							throw new UserException("パスワードに空白と全角文字を使用することは、できません");
				if(!checkBirthday(user.birthday))							throw new UserException("不正な誕生日です (YYYY-MM-DD)");

				send(true);														// true送信
				while(!recvBoolean());											// 鯖の送信準備待ち
				if(!recvBoolean()){												// 鯖へのアクセス可否確認
					if(!socket.isClosed()) close();								// 接続が閉じられていない場合は、閉じる
					// エラーダイアログ表示
					JOptionPane.showMessageDialog(null, rb.getString("error.connect.failed"), rb.getString("error"), JOptionPane.ERROR_MESSAGE);
					return;
				}

				JDecrypt	dec		= new JDecrypt(cipher.RSA);					// 復号化
				dec.setKey(dec.getPrivateKey());								// 鍵 入力
				Thread		decTH	= new Thread(dec);							// スレッド

				Packet packet		= new Packet(this);							// パケット 作成
				packet.controll		= ctrl.WRITE;								// 操作 設定
				packet.dataTYPE		= type.USER;								// 操作するデータの種類 設定
				packet.publicKEY	= dec.getPublicKey();						// 公開鍵 入力
				packet.setID(id);

				// 送信
				send(packet);													// オブジェクト送信

				while(!recvBoolean());											// 鯖の送信準備待ち

				packet = (Packet) recvObject();									// オブジェクト受信

				dec.setBytes(packet.bytesKEY);									// バイト列 入力
				decTH.start();													// 復号化開始
				decTH.join();													// 終了待ち

				secretKEY			= JCipher.bytes2secretKey(dec.getBytes());	// バイト列を共通鍵に変換

				user.pack(secretKEY);											// 書庫作成
				packet				= new Packet(this);							// パケット 作成
				packet.user			= user;										// ユーザー 入力
				packet.controll		= ctrl.WRITE;								// 操作 設定
				packet.dataTYPE		= type.USER;								// 操作するデータの種類 設定

				send(packet);													// オブジェクト送信

				while(!recvBoolean());											// 鯖の送信準備待ち
				check = recvBoolean();											// 結果を受信

				// 結果失敗した場合、エラーダイアログを表示
				if(!check) JOptionPane.showMessageDialog(null, rb.getString(recvString()), rb.getString("error"), JOptionPane.ERROR_MESSAGE);

			} catch (Exception e) {
				// エラー表示
				System.out.println(e);
				e.printStackTrace();
			}
	}

}
