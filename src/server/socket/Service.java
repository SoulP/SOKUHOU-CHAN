package server.socket;

import java.net.Socket;
import java.util.List;

import io.JCalendar;

public class Service extends JSocket{
	public String clientIP;// クライアントのIPアドレス
	public String clientHostName;// クライアントのホスト名
	public int clientPort;// クライアントのポート番号


	public Service(final Socket socket){
		setSocket(socket);
		clientIP = null;
		clientHostName = null;
		clientPort = 0;
	}

	public void run(){
		try{
			open();

			if(!recvBoolean()) return;

			// カレンダー
			JCalendar calendar = new JCalendar();
			calendar.start();
			// ウォッチドッグタイマ
			WatchDogTimer wdt = new WatchDogTimer(getSocket());
			wdt.start();// 監視開始

			// クライアント情報
			clientIP = getClientIPaddress();// 送信元のIPアドレス取得
			clientHostName = getClientHostName();// 送信元のホスト名取得
			clientPort = getClientPort();// 送信元のポート番号取得

			// 日時表示
			calendar.print();
			// 状態表示
			System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS");
			System.out.println();

// DB操作でブラックリスト取得する

boolean check = true;// 一時的なテスト用 あとで消す

			try{
				wdt.success();// 監視終了
				if(wdt.isAlive()) wdt.join();// 監視終了待ち
				if(!check){
					sendBoolean(false);// クライアントにfalse送信

// DB操作でDBにアクセスの結果(失敗)と日時など、と原因を記録する

					// 日時表示
					calendar.print();
					// 状態表示
					System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS FAILED");
					System.out.println();

					calendar.stopSafety();// 安全停止
					return;
				}
				sendBoolean(true);// クライアントにtrueを送信する
// DB操作でDBにアクセスの結果成功と日時など記録する

				// 日時表示
				calendar.print();
				// 状態表示
				System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS SUCCESS");
				System.out.println();

				wdt.start();// 監視開始

				// 受信
				byte[] data = recv();// 受信
				List<String> info = getInfo();// 接続情報取得

				// 接続情報から基づいて判断する
				if(// アカウント登録処理
					info.get(0).equals("0000")
					&&
					info.get(1).equals(ctrl.WRITE)
					&&
					info.get(2).equals(type.USER)
				){
					AccountRegister register = new AccountRegister(getInfo(), data);
					register.setSocket(getSocket());
					register.setDIS(getDIS());
					register.setDOS(getDOS());
					register.start();
					while(register.isAlive());
					if(register.check()){// アカウント登録成功した場合
						return;
					}else{// アカウント登録失敗した場合
						return;
					}
				}


			}catch (Exception e){
				System.out.println(e);
				e.printStackTrace();
			}finally{
				calendar.stopSafety();// 安全停止
				wdt.stopSafety();// 安全停止
			}


		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
