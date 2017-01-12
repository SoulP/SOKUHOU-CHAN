package server.socket;

import java.net.Socket;

public class Service extends Thread{
	private final Socket socket;
	public String clientIP;// クライアントのIPアドレス


	public Service(final Socket socket){
		this.socket = socket;
	}

	public void run(){
		WatchDogTimer wdt = new WatchDogTimer(socket);
		wdt.start();

		try{

		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
