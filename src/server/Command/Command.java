package server.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Command extends Thread{
	private BufferedReader br;
	private boolean command;
	public Command(InputStream x){
		br = new BufferedReader(new InputStreamReader(x));
		command = false;
	}

	public boolean bool(){
		return command;
	}

	private void dbCommand(String str){

	}

	public void run(){
		command = true;
		String str;
		while(command){
			System.out.print("速報ちゃん ->");
			try {
				str = br.readLine();
				if(str.equals("exit")) command = false;
				if(str.substring(0, 3).equalsIgnoreCase("DB "))dbCommand(str.substring(3, str.length()));
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
}