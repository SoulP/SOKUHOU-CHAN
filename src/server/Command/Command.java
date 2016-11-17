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

	public void run(){
		command = true;
		String str;
		while(command){
			System.out.print("sokuhou ->");
			try {
				str = br.readLine();
				if(str.equals("exit")) command = false;
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
}