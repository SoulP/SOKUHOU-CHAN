package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exception.NullUserException;
import exception.UserException;

public class User implements Serializable{
	// 一時的な変数
	public transient String name;// 名前
	public transient String email;// メールアドレス
	public transient String password;// パスワード
	public transient String birthday;// 誕生日
	public transient byte permission;// パーミッション
	transient byte[] bNAME;// 名前
	transient byte[] bEMAIL;// メールアドレス
	transient byte[] bPASSWORD;// パスワード
	transient byte[] bBIRTHDAY;// 誕生日
	transient byte[] time;// 日時 [0]=year [1]=month, [2]=date, [3]=day, [4]=hour, [5]=minute, [6]=second
	transient String ipAddress;// IPアドレス
	transient String hostName;// ホスト名
	transient int port;// ポート番号
	transient List<List<?>> packInfo = new ArrayList<List<?>>();// 書庫の詳細
	transient List<String> packName = new ArrayList<String>();// 書庫の詳細 変数名
	transient List<Integer> packLength = new ArrayList<Integer>();// 書庫の詳細 長さ
	transient List<String> packHash = new ArrayList<String>();// 書庫の詳細 変数の値のハッシュコード
	transient byte[] tempBytes;// 一時的なバイト列

	// 送受信用
	public byte[] pack;// 書庫 (AESの共通鍵で暗号化)
	public byte[] info;// 書庫の詳細 (AESの共通鍵で暗号化)
	public String packHASH;// 書庫のハッシュコード
	public String infoHASH;// 書庫の詳細のハッシュコード

	public String otp;// ワンタイムパスワード 暗号化不要

	// コンストラクタ
	public User(){
	}

	public void pack(){

	}

	public void unpack() throws UserException{
		if(pack == null) throw new NullUserException("書庫がありません");
		if(info == null) throw new NullUserException("書庫の詳細情報がありません");

	}

}
