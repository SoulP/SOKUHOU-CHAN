package sokuhou.JSocket;

public class Login extends JSocket{
	private int nextConnect;// 接続順
	private boolean check;// 接続処理確認

	// コンストラクタ
	public Login(){
		super();
		check = false;
	}

	// 接続処理確認 true = 接続完了, false = 接続失敗
	public boolean check(){
		return check;
	}

	public void run(){
		try{
			// 値確認、問題あればエラーとして発生させる
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");


		} catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
