package server.cipher;

import java.security.Key;

import javax.crypto.Cipher;

// 暗号化
public class JEncrypt extends JCipher{
	private cipher type;// 暗号化のアルゴリズム
	private Key key;// 鍵
	private byte[] bytes;// バイト列

	// コンストラクタ
	public JEncrypt(){
		// 初期化
		super();
		type = null;// null値で初期化
		key = null;// null値で初期化
		bytes = null;// null値で初期化
	}

	// コンストラクタ
	public JEncrypt(cipher type){
		// 初期化
		super();
		this.type = type;// typeからコピー
		key = null;// null値で初期化
		bytes = null;// null値で初期化
	}

	// コンストラクタ
	public JEncrypt(cipher type, Key key){
		// 初期化
		super();
		this.type = type;// typeからコピー
		this.key = key;// keyからコピー
		bytes = null;// null値で初期化
	}

	// コンストラクタ
	public JEncrypt(cipher type, Key key, byte[] bytes){
		// 初期化
		super();
		this.type = type;// typeからコピー
		this.key = key;// keyからコピー
		this.bytes = bytes;// bytesからコピー
	}

	// 暗号化のアルゴリズム 入力
	public void setType(cipher type){
		this.type = type;
	}

	// 暗号化のアルゴリズム 出力
	public cipher getType(){
		return type;
	}

	// 鍵 入力
	public void setKey(Key key){
		this.key = key;
	}

	// 鍵 出力
	public Key getKey(){
		return key;
	}

	// バイト列 入力 (通常のバイト列 入力)
	public void setBytes(byte[] bytes){
		this.bytes = bytes;
	}

	// バイト列 出力 (暗号化されたバイト列 出力)
	public byte[] getBytes(){
		return bytes;
	}

	// RSA 暗号化
	private void RSA(Key key, byte[] bytes){
		try {
			rsa.init(Cipher.ENCRYPT_MODE, key);
			this.bytes = rsa.doFinal(bytes);
		} catch (Exception e) {
			System.out.println(e);
			this.bytes = null;
		}
	}

	// AES 暗号化
	private void AES(Key key, byte[] bytes){
		try {
			aes.init(Cipher.ENCRYPT_MODE, key);
			setIV(aes.getIV());
			this.bytes = aes.doFinal(bytes);
		} catch (Exception e) {
			System.out.println(e);
			this.bytes = null;
		}
	}

	// 実行
	public void run(){
		if(type != null)if(type == cipher.RSA) RSA(key, bytes); else if(type == cipher.AES) AES(key, bytes);
	}
}
