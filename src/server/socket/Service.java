package server.socket;

import java.io.IOException;
import java.net.Socket;

import data.Packet;
import io.JCalendar;
import io.JSocket;

public class Service extends JSocket implements Runnable{
	public String	clientIP;		// クライアントのIPアドレス
	public String	clientHostName;	// クライアントのホスト名
	public int		clientPort;		// クライアントのポート番号


	public Service(final Socket socket) throws IOException{
		super(socket);
		clientIP		= null;
		clientHostName	= null;
		clientPort		= 0;
	}

	public void run(){
		try{
			if(!recvBoolean()) return;

			WatchDogTimer wdt	= new WatchDogTimer(socket);	// ウォッチドッグタイマ
			Thread wdtTH		= new Thread(wdt);				// スレッド
			wdtTH.start();										// 監視開始

			// 蔵 情報
			clientIP			= getSenderIPaddress();			// 送信元のIPアドレス取得
			clientHostName		= getSenderHostName();			// 送信元のホスト名取得
			clientPort			= getSenderPort();				// 送信元のポート番号取得

			// 日時
			JCalendar.print();									// 日時表示
			// 状態表示
			System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS");
			System.out.println();

// DB操作でブラックリスト取得する

boolean dbCHECK = true;// 一時的なテスト用 あとで消す

			try{
				wdt.success();									// 監視終了
				wdtTH.join();									// 終了待ち
				if(!dbCHECK){
					send(false);								// false送信

// DB操作でDBにアクセスの結果(失敗)と日時など、と原因を記録する

					// 日時表示
					JCalendar.print();
					// 状態表示
					System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS FAILED");
					System.out.println();
					return;
				}

				send(true);										// true送信
// DB操作でDBにアクセスの結果成功と日時など記録する

				// 日時表示
				JCalendar.print();
				// 状態表示
				System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS SUCCESS");
				System.out.println();

				// 受信
				packet = (Packet) recvObject();

				// 接続情報から基づいて判断する
				if(packet.getID() == 1){						// アカウント登録処理
					AccountRegister register = new AccountRegister(socket, packet, wdt);
					register.run();
				}


			}catch (Exception e){
				System.out.println(e);
				e.printStackTrace();
			}finally{
				wdt.stopSafety();								// 安全停止
			}


		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
