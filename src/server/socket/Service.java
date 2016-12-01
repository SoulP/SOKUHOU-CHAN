package server.socket;

import java.net.Socket;

public class Service extends Thread{
	private Socket socket;

	public Service(Socket socket){
		this.socket = socket;
	}
}
