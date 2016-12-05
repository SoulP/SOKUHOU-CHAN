package server;

import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import server.command.Command;
import server.socket.Service;

public class MainSYS {

	public static void main(String[] args) {
		// メインシステム

		// デフォルトエンコードをUTF-8に変更する
		try {
			System.setProperty("file.encoding", "UTF-8");
			Field charset;
			charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		} catch (Exception e){
			e.printStackTrace();
		}

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
