package server.JCipher;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// 暗号化・復号化 抽象クラス
public abstract class JCipher extends Thread{
	public enum cipher {RSA, AES};// 暗号化・復号化のアルゴリズム
	public enum hash {MD2, MD5, SHA, SHA1, SHA256, SHA384, SHA512};// ハッシュコードのアルゴリズム
	protected Cipher rsa;// RSA
	protected Cipher aes;// AES
	private KeyPairGenerator keygenRSA;// RSAの公開鍵と秘密鍵生成
	private KeyGenerator keygenAES;// AESの共通鍵生成
	private SecretKey secretKEY;// 共通鍵
	private PrivateKey privateKEY;// 秘密鍵
	private PublicKey publicKEY;// 公開鍵
	private static KeyFactory keyfactory;// バイト列から鍵に変換など

	// コンストラクタ
	public JCipher(){
		// 初期化
		 try {
			rsa = Cipher.getInstance("RSA");// アルゴリズムのRSA
			aes = Cipher.getInstance("AES");// アルゴリズムのAES
			keygenRSA = KeyPairGenerator.getInstance("RSA");// アルゴリズムRSAの鍵生成
			keygenAES = KeyGenerator.getInstance("AES");// アルゴリズムAESの鍵生成
			keyfactory = KeyFactory.getInstance("RSA");// バイト列から鍵に変換など
			keygenRSA.initialize(2048);// 鍵長を2048ビットに初期化
			keygenAES.init(256);// 鍵長を256ビットに初期化
			privateKEY = null;// null値で初期化
			publicKEY = null;// null値で初期化
			secretKEY = null;// null値で初期化
			generateRSA_KEY();// 公開鍵、秘密鍵の生成
			generateAES_KEY();// 共通鍵の生成
		} catch (Exception e) {
			System.out.println(e);// エラー内容表示
			e.printStackTrace();// 原因追跡表示
		}
	}

	// RSAの鍵長 入力
	public void setRSA_KEY_SIZE(int size){
		keygenRSA.initialize(size);
	}

	// AESの鍵長 入力
	public void setAES_KEY_SIZE(int size){
		keygenAES.init(size);
	}

	// RSAの公開鍵、秘密鍵 生成
	public void generateRSA_KEY(){
		KeyPair rsaKey = keygenRSA.generateKeyPair();
		privateKEY = rsaKey.getPrivate();
		publicKEY = rsaKey.getPublic();
	}

	// 秘密鍵 出力
	public PrivateKey getPrivateKey(){
		return privateKEY;
	}

	// 公開鍵 出力
	public PublicKey getPublicKey(){
		return publicKEY;
	}

	// AESの共通鍵 生成
	public void generateAES_KEY(){
		secretKEY = keygenAES.generateKey();
	}

	// 共通鍵 出力
	public SecretKey getSecretKey(){
		return secretKEY;
	}

	// 秘密鍵 入力
	public void setPrivateKey(PrivateKey privateKEY){
		this.privateKEY = privateKEY;
	}

	// 公開鍵 入力
	public void setPublicKey(PublicKey publicKEY){
		this.publicKEY = publicKEY;
	}

	// 共通鍵 入力
	public void setSecretKey(SecretKey secretKEY){
		this.secretKEY = secretKEY;
	}

	// 秘密鍵からバイト列 変換
	public static byte[] privateKey2bytes(PrivateKey privateKEY){
		return privateKEY.getEncoded();
	}

	// 公開鍵からバイト列 変換
	public static byte[] publicKey2bytes(PublicKey publicKEY){
		return publicKEY.getEncoded();
	}

	// 共通鍵からバイト列 変換
	public static byte[] secretKey2bytes(SecretKey secretKEY){
		return secretKEY.getEncoded();
	}

	// バイト列から秘密鍵 変換
	public static PrivateKey bytes2privateKey(byte[] privateKeyBytes){
		try {
			return keyfactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// バイト列から公開鍵 変換
	public static PublicKey bytes2publicKey(byte[] publicKeyBytes){
		try {
			return keyfactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// バイト列から共通鍵 変換
	public static SecretKey bytes2secretKey(byte[] secretKeyBytes){
		return new SecretKeySpec(secretKeyBytes, "AES");
	}

	// ハッシュコード生成
	public static String toHashCode(hash algorithmName, String value){
		String algorithm = "";
		switch(algorithmName){
			case MD2: algorithm = "MD2"; break;
			case MD5: algorithm = "MD5"; break;
			case SHA1: algorithm = "SHA-1"; break;
			case SHA256: algorithm = "SHA-256"; break;
			case SHA384: algorithm = "SHA-384"; break;
			case SHA512: algorithm = "SHA-512"; break;
			default: algorithm = "SHA"; break;
		}

		MessageDigest md = null;
		StringBuilder sb = null;

		try {
			md = MessageDigest.getInstance(algorithm);
			md.update(value.getBytes("UTF-8"));
			sb = new StringBuilder();
			for(byte b : md.digest()){
				String hex = String.format("%02x", b);
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
}
