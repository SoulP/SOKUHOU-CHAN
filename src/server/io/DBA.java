package server.io;

public class DBA extends Oracle{
	// Oracle DBA 11g

	// コンストラクタ
	public DBA(){
		super();
	}

	// コンストラクタ (文字列: 接続先)
	public DBA(String connect){
		super(connect);
	}

	// コンストラクタ (文字列: 接続先、文字列: ユーザー名)
	public DBA(String connect, String user){
		super(connect, user);
	}

	// コンストラクタ (文字列: 接続先、文字列: ユーザー名、文字列: パスワード)
	public DBA(String connect, String user, String password){
		super(connect, user, password);
	}

	// 実行処理
	public void run(){

	}

}
