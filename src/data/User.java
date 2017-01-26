package data;

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;

import cipher.JCipher;
import cipher.JDecrypt;
import cipher.JEncrypt;
import exception.NullUserException;
import exception.UserException;
import exception.VerifyUserException;
import io.JCalendar;
import io.JPack;

public class User implements JPack, Serializable{
	// 一時的な変数
	public				transient	String					name;		// 名前
	public				transient	String					email;		// メールアドレス
	public				transient	String					password;	// パスワード
	public				transient	String					birthday;	// 誕生日
	public				transient	byte					permission;	// パーミッション
						transient	byte[]					bNAME;		// 名前
						transient	byte[]					bEMAIL;		// メールアドレス
						transient	byte[]					bPASSWORD;	// パスワード
						transient	byte[]					bBIRTHDAY;	// 誕生日
						transient	byte[]					time;		// 日時
						transient	String					ipAddress;	// IPアドレス
						transient	String					hostName;	// ホスト名
						transient	int						port;		// ポート番号
	public	volatile	transient	int						year,		// 年
															month,		// 月
															date,		// 日
															day,		// 曜日
															hour,		// 時
															minute,		// 分
															second;		// 秒
						transient	ArrayList<ArrayList<?>>	packInfo;	// 書庫の詳細
						transient	ArrayList<String>		packName;	// 書庫の詳細 変数名
						transient	ArrayList<Integer>		packLength;	// 書庫の詳細 長さ
						transient	ArrayList<String>		packHash;	// 書庫の詳細 変数の値のハッシュコード
			volatile	transient	byte[]					packTemp;	// 一時的な書庫
			volatile	transient	byte[]					tempBytes;	// 一時的なバイト列

	// 送受信用
	public	volatile				byte[]					pack;		// 書庫 (AESの共通鍵で暗号化)
	public	volatile				byte[]					info;		// 書庫の詳細 (AESの共通鍵で暗号化)
	public	volatile				byte[]					packIV;		// 書庫のIV
	public	volatile				byte[]					infoIV;		// 書庫の詳細情報のIV
	public							String					packHASH;	// 書庫のハッシュコード
	public							String					infoHASH;	// 書庫の詳細のハッシュコード
	public							String					otp;		// ワンタイムパスワード 暗号化不要
	public							String					code;		// 認証コード

	// コンストラクタ
	public User(){
		// 初期化
		year = month = date = day = hour = minute = second = port = -1;
		packInfo   = new ArrayList<ArrayList<?>>();
		packName   = new ArrayList<String>();
		packLength = new ArrayList<Integer>();
		packHash   = new ArrayList<String>();
		time       = new byte[8];
	}

	// 書庫作成
	private void pack() throws UserException{
		// 書庫の情報作成
		bNAME		 = !isEmpty(name)?		Converter.string2bytes(name)		: null;
		bEMAIL		 = !isEmpty(email)?		Converter.string2bytes(email)		: null;
		bPASSWORD	 = !isEmpty(password)?	Converter.string2bytes(password)	: null;
		bBIRTHDAY	 = !isEmpty(birthday)?	Converter.string2bytes(birthday)	: null;
		// 日時
		JCalendar.getTime();							// 日時取得
		year		 = JCalendar.getYEAR();				// 年を取得
		month		 = JCalendar.getMONTH();			// 月を取得
		date		 = JCalendar.getDATE();				// 日を取得
		day			 = JCalendar.getDAY();				// 曜日を取得
		hour		 = JCalendar.getHOUR();				// 時を取得
		minute		 = JCalendar.getMINUTE();			// 分を取得
		second		 = JCalendar.getSECOND();			// 秒を取得
		time[0]		 = (byte) ((year >>> 8) & 0xFF);	// 日時 (int→byte変換)
		time[1]		 = (byte)  (year        & 0xFF);	// 同上
		time[2]		 = (byte)  (month       & 0x0F);	// 同上
		time[3]		 = (byte)  (date        & 0xFF);	// 同上
		time[4]		 = (byte)  (day         & 0x0F);	// 同上
		time[5]		 = (byte)  (hour        & 0xFF);	// 同上
		time[6]		 = (byte)  (minute      & 0xFF);	// 同上
		time[7]		 = (byte)  (second      & 0xFF);	// 同上
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
		length	 = (length % 16 == 0)? length + 16 : length / 16 * 16 + 16; // 16バイトのブロック単位に調整
		packTemp = new byte[length];										// バイト列作成
		// 乱数で埋める
		for(int i = 0; i < packTemp.length; i++) packTemp[i] = (byte) ((int) (Math.random() * 10000 % 0x7F) & 0xFF);

		length = 0; // 初期設定
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

		System.arraycopy(time, 0, packTemp, length, time.length);// 配列コピー

	}

	// 書庫作成 + 暗号化, 出力 IVのバイト列
	@Override
	public void pack(Key key){
		try{
			// 例外処理
			if(key == null) throw new NullUserException("共通鍵がありません");

			pack();																	// 書庫作成
			tempBytes	= Converter.object2bytes(packInfo);							// 配列コピー
			infoHASH	= JCipher.toHashCode(JCipher.hash.SHA512, tempBytes);		// バイト列からハッシュコードに変換
			tempBytes	= JCipher.block(tempBytes, size);							// ブロック構築

			// ここから暗号化処理
			infoHASH		= JCipher.toHashCode(JCipher.hash.SHA512, tempBytes);	// ハッシュコード取得
			packHASH 		= JCipher.toHashCode(JCipher.hash.SHA512, packTemp);	// ハッシュコード取得
			JEncrypt enc1	= new JEncrypt(JCipher.cipher.AES, key, tempBytes);		// 暗号化
			JEncrypt enc2	= new JEncrypt(JCipher.cipher.AES, key, packTemp);		// 暗号化
			Thread encTH1	= new Thread(enc1);										// スレッド
			Thread encTH2	= new Thread(enc2);										// スレッド
			encTH1.start();															// 暗号化開始
			encTH2.start();															// 暗号化開始
			encTH1.join();															// 終了待ち
			encTH2.join();															// 終了待ち
			info			= enc1.getBytes();										// 暗号化したバイト列 出力
			infoIV			= enc1.getIV();											// IV取得
			pack			= enc2.getBytes();										// 暗号化したバイト列 出力
			packIV			= enc2.getIV();											// IV取得
		} catch (Exception e) {
			System.out.println(e);													// エラー表示
			e.printStackTrace();													// エラー原因追跡表示
		} finally {
			// 初期化
			packInfo		= null;
			packName		= null;
			packLength		= null;
			packHash		= null;
			packTemp		= null;
			tempBytes		= null;
		}
	}

	// 書庫解凍
	@SuppressWarnings("unchecked")
	private void unpack() throws UserException{
		// 例外処理
		if(packTemp		== null)													throw new NullUserException("復号化した書庫がありません");
		if(tempBytes	== null)													throw new NullUserException("復号化した書庫の詳細情報がありません");
		if(!packHASH.equals(JCipher.toHashCode(JCipher.hash.SHA512, packTemp)))		throw new VerifyUserException("ハッシュコード確認エラー: 書庫");
		if(!infoHASH.equals(JCipher.toHashCode(JCipher.hash.SHA512, tempBytes)))	throw new VerifyUserException("ハッシュコード確認エラー: 書庫の詳細情報");

		// 書庫解凍
		packInfo = (ArrayList<ArrayList<?>>) Converter.bytes2object(tempBytes);	// バイト列からArrayListに変換

		int length = 0;
		String hash = "";
		for(int i = 0; i < packInfo.get(0).size(); i++){
			if(packInfo.get(0).get(i) == null) continue;
			switch((String) packInfo.get(0).get(i)){
				case "bNAME"		:
					bNAME = new byte[(Integer) packInfo.get(1).get(i)];
					System.arraycopy(packTemp, length, bNAME, 0, bNAME.length);
					length += bNAME.length;
					hash = JCipher.toHashCode(JCipher.hash.SHA512, bNAME);
					if(!hash.equals(packInfo.get(2).get(i))) throw new VerifyUserException("ハッシュコード確認エラー: 名前");
					name = Converter.bytes2string(bNAME);
					break;
				case "bEMAIL"		:
					bEMAIL = new byte[(Integer) packInfo.get(1).get(i)];
					System.arraycopy(packTemp, length, bEMAIL, 0, bEMAIL.length);
					length += bEMAIL.length;
					hash = JCipher.toHashCode(JCipher.hash.SHA512, bEMAIL);
					if(!hash.equals(packInfo.get(2).get(i))) throw new VerifyUserException("ハッシュコード確認エラー: メールアドレス");
					email = Converter.bytes2string(bEMAIL);
					break;
				case "bPASSWORD"	:
					bPASSWORD = new byte[(Integer) packInfo.get(1).get(i)];
					System.arraycopy(packTemp, length, bPASSWORD, 0, bPASSWORD.length);
					length += bPASSWORD.length;
					hash = JCipher.toHashCode(JCipher.hash.SHA512, bPASSWORD);
					if(!hash.equals(packInfo.get(2).get(i))) throw new VerifyUserException("ハッシュコード確認エラー: パスワード");
					password = Converter.bytes2string(bPASSWORD);
					break;
				case "bBIRTHDAY"	:
					bBIRTHDAY = new byte[(Integer) packInfo.get(1).get(i)];
					System.arraycopy(packTemp, length, bBIRTHDAY, 0, bBIRTHDAY.length);
					length += bBIRTHDAY.length;
					hash = JCipher.toHashCode(JCipher.hash.SHA512, bBIRTHDAY);
					if(!hash.equals(packInfo.get(2).get(i))) throw new VerifyUserException("ハッシュコード確認エラー: 誕生日");
					birthday = Converter.bytes2string(bBIRTHDAY);
					break;
				case "time"			:
					time = new byte[(Integer) packInfo.get(1).get(i)];
					System.arraycopy(packTemp, length, time, 0, time.length);
					length += time.length;
					hash = JCipher.toHashCode(JCipher.hash.SHA512, time);
					if(!hash.equals(packInfo.get(2).get(i))) throw new VerifyUserException("ハッシュコード確認エラー: 日時");
					break;
			}

			// 日時 (byte→int変換)
			year 	 = time[0] << 8		& 0xFF00;
			year	+= time[1]			& 0x00FF;
			month	 = time[2]			& 0x000F;
			date	 = time[3]			& 0x00FF;
			day		 = time[4]			& 0x000F;
			hour	 = time[5]			& 0x00FF;
			minute	 = time[6]			& 0x00FF;
			second	 = time[7]			& 0x00FF;
		}
	}

	// 書庫解凍
	@Override
	public void unpack(Key key){
		try{
			// 例外処理
			if(key		== null)						throw new NullUserException("共通鍵がありません");
			if(pack		== null)						throw new NullUserException("書庫がありません");
			if(info		== null)						throw new NullUserException("書庫の詳細情報がありません");
			if(packHASH == null || packHASH.equals(""))	throw new NullUserException("書庫のハッシュコードがありません");
			if(infoHASH == null || infoHASH.equals(""))	throw new NullUserException("書庫の詳細情報のハッシュコードがありません");
			if(packIV	== null)						throw new NullUserException("書庫のIVがありません");
			if(infoIV	== null)						throw new NullUserException("書庫の詳細情報のIVがありません");
			// 復号化
			JDecrypt dec1 = new JDecrypt(JCipher.cipher.AES, key, pack);		// 復号化
			JDecrypt dec2 = new JDecrypt(JCipher.cipher.AES, key, info);		// 復号化
			dec1.setIV(packIV);													// IV 入力
			dec2.setIV(infoIV);													// IV 入力
			Thread decTH1 = new Thread(dec1);									// スレッド
			Thread decTH2 = new Thread(dec2);									// スレッド
			decTH1.start();														// 復号化開始
			decTH2.start();														// 復号化開始
			decTH1.join();														// 終了待ち
			decTH2.join();														// 終了待ち
			packTemp = dec1.getBytes();											// 復号化したバイト列 出力
			tempBytes = dec2.getBytes();										// 復号化したバイト列 出力
			int length = 0;														// 初期設定
			length  = tempBytes[tempBytes.length - 4] << 24 & 0xFF000000;		// 書庫の詳細情報の配列数 (byte→int変換)
			length += tempBytes[tempBytes.length - 3] << 16 & 0x00FF0000;		// 同上
			length += tempBytes[tempBytes.length - 2] <<  8 & 0x0000FF00;		// 同上
			length += tempBytes[tempBytes.length - 1]       & 0x000000FF;		// 同上
			byte[] temp = new byte[length];										// バイト列作成
			System.arraycopy(tempBytes, 0, temp, 0, length);					// 配列コピー
			tempBytes = temp;													// 参照値コピー
			unpack();															// 書庫解凍

		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		} finally {
			// 初期化
			packInfo	= null;
			packName	= null;
			packLength	= null;
			packHash	= null;
			packTemp	= null;
			tempBytes	= null;
		}
	}

	// 空の状態確認
	private boolean isEmpty(Object obj){
		return (obj == null)? true : false;
	}

	// 空の状態確認
	private boolean isEmpty(String str){
		return (str == null || str.equals(""))? true : false;
	}
}
