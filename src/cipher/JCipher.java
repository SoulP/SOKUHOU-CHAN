package cipher;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// 暗号化・復号化 抽象クラス
public abstract class JCipher implements Runnable{
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
	private byte[] iv;

	// コンストラクタ
	public JCipher(){
		// 初期化
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			rsa = Cipher.getInstance("RSA");// アルゴリズムのRSA
			aes = Cipher.getInstance("AES/CBC/NoPadding");// アルゴリズムのAES
			keygenRSA = KeyPairGenerator.getInstance("RSA");// アルゴリズムRSAの鍵生成
			keygenAES = KeyGenerator.getInstance("AES");// アルゴリズムAESの鍵生成
			keyfactory = KeyFactory.getInstance("RSA");// バイト列から鍵に変換など
			keygenRSA.initialize(2048, random);// 鍵長を2048ビットに初期化
			keygenAES.init(256, random);// 鍵長を256ビットに初期化
			privateKEY = null;// null値で初期化
			publicKEY = null;// null値で初期化
			secretKEY = null;// null値で初期化
			iv = null;
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

	// IV 入力
	public void setIV(byte[] iv){
		this.iv = iv;
	}

	// IV 出力
	public byte[] getIV(){
		return iv;
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

	// ハッシュコード生成
	public static String toHashCode(hash algorithmName, byte[] bytes){
		byte[] temp = new byte[bytes.length];
		System.arraycopy(bytes, 0, temp, 0, bytes.length);
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
			md.update(temp);
			sb = new StringBuilder();
			for(byte b : md.digest()){
				String hex = String.format("%02x", b);
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	// ブロックサイズ構築
	public static byte[] block(byte[] bytes, int size){
		int length = bytes.length;
		length = (length % size == 0)? length + size : length / size * size + size;		// 長さ調整
		length += (size < 4)? (int)(Math.ceil((4.0d / (double)size)) * 4) : size;		// 同上
		byte[] tempBYTES = new byte[length];											// バイト列作成
		// 乱数で埋める
		for(int i = 0; i < tempBYTES.length; i++) tempBYTES[i] = (byte) ((int) (Math.random() * 10000 % 0x7F) & 0xFF);
		System.arraycopy(bytes, 0, tempBYTES, 0, bytes.length);
		// 最後の所に配列数の値を入れる
		tempBYTES[tempBYTES.length - 4] = (byte) (bytes.length >>> 24 & 0xFF);			// 配列数 (int→byte変換)
		tempBYTES[tempBYTES.length - 3] = (byte) (bytes.length >>> 16 & 0xFF);			// 同上
		tempBYTES[tempBYTES.length - 2] = (byte) (bytes.length >>> 8  & 0xFF);			// 同上
		tempBYTES[tempBYTES.length - 1] = (byte) (bytes.length 		& 0xFF);			// 同上
		return tempBYTES;
	}

	// ブロックサイズ構築
	public static byte[] unblock(byte[] bytes){
		int length = 0;															// 初期設定
		length  = bytes[bytes.length - 4] << 24 & 0xFF000000;			// 書庫の詳細情報の配列数 (byte→int変換)
		length += bytes[bytes.length - 3] << 16 & 0x00FF0000;			// 同上
		length += bytes[bytes.length - 2] <<  8 & 0x0000FF00;			// 同上
		length += bytes[bytes.length - 1]       & 0x000000FF;			// 同上
		byte[] tempBYTES = new byte[length];
		System.arraycopy(bytes, 0, tempBYTES, 0, length);
		return tempBYTES;
	}
}