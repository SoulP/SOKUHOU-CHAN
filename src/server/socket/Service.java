package server.socket;

import java.net.Socket;

import server.io.JCalendar;

public class Service extends JSocket{
	private final Socket socket;
	public String clientIP;// クライアントのIPアドレス
	public String clientHostName;// クライアントのホスト名
	public int clientPort;// クライアントのポート番号


	public Service(final Socket socket){
		this.socket = socket;
		clientIP = null;
		clientHostName = null;
		clientPort = 0;
	}

	public void run(){
		try{
			if(!recvBoolean()) return;

			// カレンダー
			JCalendar calendar = new JCalendar();
			calendar.start();
			// ウォッチドッグタイマ
			WatchDogTimer wdt = new WatchDogTimer(socket);
			wdt.start();

			// クライアント情報
			clientIP = socket.getInetAddress().getHostAddress();// 送信元のIPアドレス取得
			clientHostName = socket.getInetAddress().getHostName();// 送信元のホスト名取得
			clientPort = socket.getPort();// 送信元のポート番号取得

			// 日時表示
			calendar.print();
			// 状態表示
			System.out.println("IPaddr: " + clientIP + " HostName: " + clientHostName + "Port: " + clientPort + "; ACCESS");
			System.out.println();

// DB操作でブラックリスト取得する

			boolean check = true;

			try{
				wdt.success();
				wdt.join();
				if(!check){
					// クライアントにfalse送信
					getDOS().writeBoolean(false);

// DB操作でDBにアクセスの結果(失敗)と日時など、と原因を記録する

					calendar.stopSafety();// 安全停止
					return;
				}
				getDOS().writeBoolean(true);
				// DB操作でDBにアクセスの結果成功と日時など記録する
				wdt.start();


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
