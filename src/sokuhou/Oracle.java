package sokuhou;

public abstract class Oracle {
	// 抽象クラス

	private String connect, user, password;

	// コンストラクタ
	public Oracle(){
		connect = "";
		user = "";
		password = "";
	}

	// コンストラクタ (文字列: 接続先)
	public Oracle(String connect){
		this.connect = connect;
	}
		// コンストラクタ (文字列: 接続先、文字列: ユーザー名)
	public Oracle(String connect, String user){
		this.connect = connect;
		this.user = user;
	}

	// コンストラクタ (文字列: 接続先、文字列: ユーザー名、文字列: パスワード)
	public Oracle(String connect, String user, String password){
		this.connect = connect;
		this.user = user;
		this.password = password;
	}

	// 入力: 接続先
	public void setConnect(String connect){
		this.connect = connect;
	}

	// 出力: 接続先
	public String getConnect(){
		return connect;
	}

	// 入力: ユーザー名
	public void setUser(String user){
		this.user = user;
	}

	// 出力: ユーザー名
	public String getUser(){
		return user;
	}

	// 入力: パスワード
	public void setPassword(String password){
		this.password = password;
	}

	// 出力: パスワード
	public String getPassword(){
		return password;
	}

}
