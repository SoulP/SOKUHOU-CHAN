package data;

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;

import cipher.JCipher;
import cipher.JEncrypt;
import exception.NullUserException;
import exception.UserException;
import io.JCalendar;

public class User implements Serializable{
	// 一時的な変数
	public	transient String					name;		// 名前
	public	transient String					email;		// メールアドレス
	public	transient String					password;	// パスワード
	public	transient String					birthday;	// 誕生日
	public	transient byte						permission;	// パーミッション
			transient byte[]					bNAME;		// 名前
			transient byte[]					bEMAIL;		// メールアドレス
			transient byte[]					bPASSWORD;	// パスワード
			transient byte[]					bBIRTHDAY;	// 誕生日
			transient byte[]					time;		// 日時
			transient String					ipAddress;	// IPアドレス
			transient String					hostName;	// ホスト名
			transient int						port;		// ポート番号
			transient int						year,		// 年
												month,		// 月
												date,		// 日
												day,		// 曜日
												hour,		// 時
												minute,		// 分
												second;		// 秒
			transient ArrayList<ArrayList<?>>	packInfo;	// 書庫の詳細
			transient ArrayList<String>			packName;	// 書庫の詳細 変数名
			transient ArrayList<Integer>		packLength;	// 書庫の詳細 長さ
			transient ArrayList<String>			packHash;	// 書庫の詳細 変数の値のハッシュコード
			transient byte[]					packTemp;	// 一時的な書庫
			transient byte[]					tempBytes;	// 一時的なバイト列
			transient JCalendar					calendar;	// カレンダー

	// 送受信用
	public byte[] pack;		// 書庫 (AESの共通鍵で暗号化)
	public byte[] info;		// 書庫の詳細 (AESの共通鍵で暗号化)
	public String packHASH;	// 書庫のハッシュコード
	public String infoHASH;	// 書庫の詳細のハッシュコード
	public String otp;		// ワンタイムパスワード 暗号化不要

	// コンストラクタ
	public User(){
		// 初期化
		year = month = date = day = hour = minute = second = port = -1;
		packInfo   = new ArrayList<ArrayList<?>>();
		packName   = new ArrayList<String>();
		packLength = new ArrayList<Integer>();
		packHash   = new ArrayList<String>();
		calendar   = new JCalendar();
		time       = new byte[8];
	}

	// 書庫作成
	private void pack() throws UserException{
		// 書庫の情報作成
		bNAME		 = !isEmpty(name)?		Converter.string2bytes(name)		: null;
		bEMAIL		 = !isEmpty(email)?		Converter.string2bytes(email)		: null;
		bPASSWORD	 = !isEmpty(password)?	Converter.string2bytes(password)	: null;
		bBIRTHDAY	 = !isEmpty(birthday)?	Converter.string2bytes(birthday)	: null;
		calendar.time();
		year		 = calendar.getYEAR();
		month		 = calendar.getMONTH();
		date		 = calendar.getDATE();
		day			 = calendar.getDAY();
		hour		 = calendar.getHOUR();
		minute		 = calendar.getMINUTE();
		second		 = calendar.getSECOND();
		time[0]		 = (byte) ((year >>> 8) & 0xFF);
		time[1]		 = (byte)  (year        & 0xFF);
		time[2]		 = (byte)  (month       & 0x0F);
		time[3]		 = (byte)  (date        & 0xFF);
		time[4]		 = (byte)  (day         & 0x0F);
		time[5]		 = (byte)  (hour        & 0xFF);
		time[6]		 = (byte)  (minute      & 0xFF);
		time[7]		 = (byte)  (second      & 0xFF);
		int length	 = 0;

		if(!isEmpty(bNAME)){
			packName	.add("bNAME");
			packLength	.add(bNAME.length);
			packHash	.add(JCipher.toHashCode(JCipher.hash.SHA512, bNAME));
			length += bNAME.length;
		}

		if(!isEmpty(bEMAIL)){
			packName	.add("bEMAIL");
			packLength	.add(bEMAIL.length);
			packHash	.add(JCipher.toHashCode(JCipher.hash.SHA512, bEMAIL));
			length += bEMAIL.length;
		}

		if(!isEmpty(bPASSWORD)){
			packName	.add("bPASSWORD");
			packLength	.add(bPASSWORD.length);
			packHash	.add(JCipher.toHashCode(JCipher.hash.SHA512, bPASSWORD));
			length += bPASSWORD.length;
		}

		if(!isEmpty(bBIRTHDAY)){
			packName	.add("bBIRTHDAY");
			packLength	.add(bBIRTHDAY.length);
			packHash	.add(JCipher.toHashCode(JCipher.hash.SHA512, bBIRTHDAY));
			length += bBIRTHDAY.length;
		}

		if(length == 0) throw new NullUserException("書庫にするための情報が1つもありません");

		packName	.add("time");
		packLength	.add(time.length);
		packHash	.add(JCipher.toHashCode(JCipher.hash.SHA512, time));
		length += time.length;

		packInfo	.add(packName);
		packInfo	.add(packLength);
		packInfo	.add(packHash);

		// 書庫作成
		length	 = (length % 16 == 0)? length + 16 : length / 16 * 16 + 16;// 16バイトのブロック単位に調整
		packTemp = new byte[length];
		for(int i = 0; i < packTemp.length; i++) packTemp[i] = (byte) ((int) (Math.random() * 10000 % 0x7F) & 0xFF);// 乱数で埋める

		length = 0;

		if(!isEmpty(bNAME)){
			System.arraycopy(bNAME, 0, packTemp, length, bNAME.length);
			length += bNAME.length;
		}

		if(!isEmpty(bEMAIL)){
			System.arraycopy(bEMAIL, 0, packTemp, length, bEMAIL.length);
			length += bEMAIL.length;
		}

		if(!isEmpty(bPASSWORD)){
			System.arraycopy(bPASSWORD, 0, packTemp, length, bPASSWORD.length);
			length += bPASSWORD.length;
		}

		if(!isEmpty(bBIRTHDAY)){
			System.arraycopy(bBIRTHDAY, 0, packTemp, length, bBIRTHDAY.length);
			length += bBIRTHDAY.length;
		}

		System.arraycopy(time, 0, packTemp, length, time.length);

	}

	// 書庫作成 + 暗号化, 出力 IVのバイト列
	public byte[] pack(Key key) throws UserException{
		if(key == null) throw new NullUserException("共通鍵がありません");
		JEncrypt enc = new JEncrypt(JCipher.cipher.AES, key);
		pack();// 書庫作成
		tempBytes = Converter.object2bytes(packInfo);
		int length = tempBytes.length;
		length	 = (length % 16 == 0)? length + 16 : length / 16 * 16 + 16;// 16バイトのブロック単位に調整
		info = new byte[length + 16];
		for(int i = 0; i < info.length; i++) info[i] = (byte) ((int) (Math.random() * 10000 % 0x7F) & 0xFF);// 乱数で埋める
		System.arraycopy(tempBytes, 0, info, 0, tempBytes.length);

		// 最後の所に配列数の値を入れる
		info[info.length - 4] = (byte) (tempBytes.length >>> 24 & 0xFF);
		info[info.length - 3] = (byte) (tempBytes.length >>> 16 & 0xFF);
		info[info.length - 2] = (byte) (tempBytes.length >>> 8  & 0xFF);
		info[info.length - 1] = (byte) (tempBytes.length & 0xFF);

		tempBytes = new byte[info.length];
		System.arraycopy(info, 0, tempBytes, 0, info.length);

		try{
			enc.setBytes(tempBytes);
			enc.start();
			enc.join();
			info = enc.getBytes();
			tempBytes = enc.getIV();
			enc = new JEncrypt(JCipher.cipher.AES, key);
			enc.setIV(tempBytes);
			enc.setBytes(packTemp);
			enc.start();
			enc.join();
			pack = enc.getBytes();
			return tempBytes;
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	// 書庫解凍
	public void unpack() throws UserException{
		if(pack == null) throw new NullUserException("書庫がありません");
		if(info == null) throw new NullUserException("書庫の詳細情報がありません");
		if(packHASH == null || packHASH.equals("")) throw new NullUserException("書庫のハッシュコードがありません");
		if(infoHASH == null || infoHASH.equals("")) throw new NullUserException("書庫の詳細情報のハッシュコードがありません");
	}

	// 空の状態確認
	private boolean isEmpty(Object obj){
		return (obj == null)? true : false;
	}

	// 空の状態確認
	private boolean isEmpty(String str){
		return (str == null || str.equals(""))? true : false;
	}

	// 空の状態確認
	private boolean isEmpty(int num){
		return (num == -1)? true : false;
	}
}
