package sokuhou.socket;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import cipher.JCipher;
import cipher.JCipher.hash;
import exception.NullDataException;
import exception.NullUserException;
import io.JSocket;

public class Delete extends JSocket{
	// インスタンス変数
	private String setDelete;		// 操作用の文字列
	private type dataType;			// データの種類

	// コンストラクタ
	public Delete(SecretKey secretKEY) throws IOException{
		super();
		this.secretKEY	= secretKEY;
		setDelete		= null;
		dataType		= type.NULL;
		id				= 6;
	}

	// コンストラクタ
	public Delete(SecretKey secretKEY, String setDelete) throws IOException{
		super();
		this.secretKEY	= secretKEY;
		this.setDelete	= setDelete;
		dataType		= type.NULL;
		id				= 6;
	}

	// コンストラクタ
	public Delete(SecretKey secretKEY, String setDelete, type dataType) throws IOException{
		super();
		this.secretKEY	= secretKEY;
		this.setDelete	= setDelete;
		this.dataType	= dataType;
		id				= 6;
	}

	// 削除したい行 入力
	public void setDelete(String setDelete){
		this.setDelete = setDelete;
	}

	// 情報の種類 入力
	public void setDataType(type dataType){
		this.dataType = dataType;
	}

	// 実行
	public void run() throws Exception{
		if(user			== null)							throw new NullUserException("ユーザーがありません");
		if(user.email	== null || user.email.equals(""))	throw new NullUserException("ユーザーのメールアドレスがありません");
		if(secretKEY	== null)							throw new NullUserException("共通鍵がありません");
		if(setDelete	== null || setDelete.equals(""))	throw new NullDataException("削除したい行がありません");
		if(dataType		== null || dataType == type.NULL)	throw new NullDataException("データ種類が不明です");
		String tempSTR = user.email;
		tempSTR		+= user.password;
		tempSTR		+= user.code;
		tempSTR		+= JCipher.toHashCode(hash.SHA512, JCipher.secretKey2bytes(secretKEY));
		user.code	 = JCipher.toHashCode(hash.SHA512, tempSTR);
		Pattern pattern = Pattern.compile("\\p{Punct}");
	}
}
