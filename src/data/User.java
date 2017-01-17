package data;

import java.io.Serializable;

public abstract class User implements Serializable{
	// 一時的な変数
	public transient String name;// 名前
	public transient String email;// メールアドレス
	public transient String password;// パスワード
	public transient String birthday;// 誕生日
	public transient byte permission;// パーミッション
	protected transient byte[] time;// 日時 [0]=year [1]=month, [2]=date, [3]=day, [4]=hour, [5]=minute, [6]=second
	protected transient String ipAddress;// IPアドレス
	protected transient String hostName;// ホスト名
	protected transient int port;// ポート番号

	// 送受信用 バイト列には、暗号化したバイト列
	public byte[] bNAME;// 名前 AESの共通鍵で暗号化
	public byte[] bEMAIL;// メールアドレス AESの共通鍵で暗号化
	public byte[] bPASSWORD;// パスワード AESの共通鍵で暗号化
	public byte[] bBIRTHDAY;// 誕生日 AESの共通鍵で暗号化
	public byte[] iv;// AES復号用のIV RSAの公開鍵で暗号化
	public String otp;// ワンタイムパスワード 暗号化不要
	protected byte[] bTIME;// 日時 AESの共通鍵で暗号化

	public User(){

	}


}
