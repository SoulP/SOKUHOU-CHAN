package server;

import java.net.ServerSocket;
import java.net.Socket;

import server.Command.Command;
import server.JSocket.Service;

public class MainSYS {

	public static void main(String[] args) {
		final int port = 55324;
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
			System.out.println("速報ちゃんサーバ起動");
			Command command = new Command(System.in);
			command.start();
			while(command.bool()){
				Socket socket = server.accept();
				new Service(socket).start();
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}finally{
			try{
				if(!server.isClosed()) server.close();
			}catch (Exception e){
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

}