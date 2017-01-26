package server.socket;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

import cipher.JCipher;
import cipher.JCipher.cipher;
import cipher.JEncrypt;
import data.Packet;
import exception.UserException;
import io.JCalendar;
import io.JSocket;

public class AccountRegister extends JSocket{
	// インスタンス変数
	public		String			clientIP;			// クライアントのIPアドレス
	public		String			clientHostName;		// クライアントのホスト名
	public		int				clientPort;			// クライアントのポート番号
	protected	WatchDogTimer	wdt;				// ウォッチドッグタイマ
	protected	PublicKey		clientPublicKey;	// クライアントの公開鍵
	private		int				id;					// オブジェクトID

	// コンストラクタ
	public AccountRegister(Socket socket, Packet packet, WatchDogTimer wdt) throws IOException{
		super(socket);
		this.packet = packet;
		this.wdt	= wdt;
		id			= 1;
	}

	// 実行
	public void run(){
		try {
			// 例外処理
			if(socket	== null) throw new UserException("ソケットがありません");
			if(packet	== null) throw new UserException("パケットがありません");
			if(wdt		== null) throw new UserException("ウォッチドッグタイマがありません");

			Thread wdtTH	= new Thread(wdt);
			JEncrypt enc	= new JEncrypt(cipher.RSA, packet.publicKEY);
			Thread encTH	= new Thread(enc);
			secretKEY		= enc.getSecretKey();
			enc.setBytes(JCipher.secretKey2bytes(secretKEY));
			encTH.start();
			packet			= new Packet(this);
			packet.controll	= ctrl.WRITE;
			packet.dataTYPE	= type.DATA;
			encTH.join();
			packet.bytesKEY	= enc.getBytes();
			send(packet);

			packet			= (Packet) recvObject();
			wdtTH.start();

			user = packet.user;
			user.unpack(secretKEY);

			// 蔵 情報
			clientIP		= getSenderIPaddress();	// 送信元のIPアドレス取得
			clientHostName	= getSenderHostName();	// 送信元のホスト名取得
			clientPort		= getSenderPort();		// 送信元のポート番号取得
			JCalendar.print();
			System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; AccountRegister: start register");
			System.out.println();

			// 例外処理
			if(user.name		== null || user.name.equals("")){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(user.email		== null || user.email.equals("")){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(user.password	== null || user.password.equals("")){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(user.birthday	== null || user.birthday.equals("")){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(!checkName(user.name)){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(!checkEmail(user.email)){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(!checkPassword(user.password)){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

			if(!checkBirthday(user.birthday)){
				wdt.success();
				wdtTH.join();
				send(false);
				send("");
				close();
				return;
			}

// データベース操作

			wdt.success();
			wdtTH.join();

check = true;// テスト用
String tempERROR = "test";// テスト用
			if(!check){
				send(false);
				send(tempERROR);
			}else send(true);
			close();
		} catch (Exception e) {
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
			try {
				close();
			} catch (IOException e1) {
				// エラー表示
				System.out.println(e1);
				e1.printStackTrace();
				socket	= null;
			}
		}
	}

	@Override
	public int getID() {
		// TODO 自動生成されたメソッド・スタブ
		return id;
	}
}
