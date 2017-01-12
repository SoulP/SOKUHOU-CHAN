package server.socket;

import java.util.List;

public class AccountRegister extends JSocket{
	// インスタンス変数
	private byte[] data;

	// コンストラクタ
	public AccountRegister(){
		data = null;
	}

	// コンストラクタ
	public AccountRegister(List<String> info){
		data = null;
		setInfo(info);
	}

	// コンストラクタ
	public AccountRegister(byte[] data){
		this.data = data;
	}

	// コンストラクタ
	public AccountRegister(List<String> info, byte[] data){
		this.data = data;
		setInfo(info);
	}

	// スレッド
	public void run(){

	}
}
