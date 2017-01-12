package server.socket;

import java.net.Socket;

public class Service extends Thread{
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
			WatchDogTimer wdt = new WatchDogTimer(socket);
			wdt.start();
			
			clientIP = socket.getInetAddress().getHostAddress();// 送信元のIPアドレス取得
			clientHostName = socket.getInetAddress().getHostName();// 送信元のホスト名取得
			clientPort = socket.getPort();// 送信元のポート番号取得
			

		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
