package data;

import java.io.Serializable;
import java.security.Key;
import java.util.regex.Pattern;

import cipher.JCipher;
import cipher.JDecrypt;
import cipher.JEncrypt;
import exception.DataException;
import exception.NullDataException;
import io.JPack;

public class Data implements JPack, Serializable{
	// 一時的な変数
	public	transient	volatile			String	table;			// 表
	public	transient	volatile			String	column;			// 列
	public	transient	volatile			String	row;			// 行
	private	transient				final	String	NULL = "NULL";	// 初期値の文字列

																	// 命名規則
	private	transient	static		final	Pattern	NAMING_RULE	= Pattern.compile("^([\\$_#]|\\p{Alpha}|\\p{InHiragana}|\\p{InKatakana}|\\p{InCJKUnifiedIdeographs})+([\\$_#]|\\p{Alnum}|\\p{InHiragana}|\\p{InKatakana}|\\p{InCJKUnifiedIdeographs}){0,14}$");
																	// 値規則
	private	transient	static		final	Pattern	VALUE_RULE	= Pattern.compile("^(\\p{Alnum}|\\p{InHiragana}|\\p{InKatakana}|\\p{InCJKUnifiedIdeographs})+$");

	// 送受信
	private				volatile			byte[]	bTABLE;			// 表
	private				volatile			byte[]	bCOLUMN;		// 列
	private				volatile			byte[]	bROW;			// 行
	private									byte[]	tableIV;		// IV
	private									byte[]	columnIV;		// IV
	private									byte[]	rowIV;			// IV

	// 書庫作成
	@Override
	public void pack(Key key) {
		try{
			// 例外処理
			if(key		== null)							throw new NullDataException("鍵がありません");
			if(table	== null)							throw new NullDataException("表がありません");
			if(column	== null)							throw new NullDataException("列がありません");
			if(row		== null)							row	= NULL;
			if(!NAMING_RULE	.matcher(table)		.find())	throw new DataException("表明の命名規則に反してます");
			if(!NAMING_RULE	.matcher(column)	.find())	throw new DataException("列名の命名規則に反してます");
			if(!VALUE_RULE	.matcher(row)		.find())	row = NULL;

			// 暗号化 + 書庫作成
			bTABLE				= JCipher.block(Converter.string2bytes(table), size);	// 文字列をブロック単位のバイト列に変換
			bCOLUMN				= JCipher.block(Converter.string2bytes(column), size);	// 文字列をブロック単位のバイト列に変換
			bROW				= JCipher.block(Converter.string2bytes(row), size);		// 文字列をブロック単位のバイト列に変換
			JEncrypt	enc01	= new JEncrypt(JCipher.cipher.AES, key, bTABLE);		// 暗号化
			JEncrypt	enc02	= new JEncrypt(JCipher.cipher.AES, key, bCOLUMN);		// 暗号化
			JEncrypt	enc03	= new JEncrypt(JCipher.cipher.AES, key, bROW);			// 暗号化
			Thread		encTH01	= new Thread(enc01);									// スレッド
			Thread		encTH02	= new Thread(enc02);									// スレッド
			Thread		encTH03	= new Thread(enc03);									// スレッド
			encTH01.start();															// 暗号化開始
			encTH02.start();															// 暗号化開始
			encTH03.start();															// 暗号化開始
			encTH01.join();																// 終了待ち
			bTABLE		= enc01.getBytes();												// 暗号化したバイト列 出力
			tableIV		= enc01.getIV();												// IV 出力
			encTH02.join();																// 終了待ち
			bCOLUMN		= enc02.getBytes();												// 暗号化したバイト列 出力
			columnIV	= enc02.getIV();												// IV 出力
			encTH03.join();																// 終了待ち
			bROW		= enc03.getBytes();												// 暗号化したバイト列 出力
			rowIV		= enc03.getIV();												// IV 出力
		}catch (Exception e){
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
		}
	}


	// 書庫解凍
	@Override
	public void unpack(Key key) {
		try{
			// 例外処理
			if(key		== null)	throw new NullDataException("鍵がありません");
			if(bTABLE	== null)	throw new NullDataException("表のバイト列がありません");
			if(bCOLUMN	== null)	throw new NullDataException("列のバイト列がありません");
			if(bROW		== null)	throw new NullDataException("行のバイト列がありません");
			if(tableIV	== null)	throw new NullDataException("表のIVがありません");
			if(columnIV	== null)	throw new NullDataException("列のIVがありません");
			if(rowIV	== null)	throw new NullDataException("行のIVがありません");

			// 復号化 + 書庫解凍
			JDecrypt	dec01	= new JDecrypt(JCipher.cipher.AES, key, bTABLE, tableIV);	// 復号化
			JDecrypt	dec02	= new JDecrypt(JCipher.cipher.AES, key, bCOLUMN, columnIV);	// 復号化
			JDecrypt	dec03	= new JDecrypt(JCipher.cipher.AES, key, bROW, rowIV);		// 復号化
			Thread		decTH01	= new Thread(dec01);										// スレッド
			Thread		decTH02	= new Thread(dec02);										// スレッド
			Thread		decTH03	= new Thread(dec03);										// スレッド
			decTH01.start();																// 復号化開始
			decTH02.start();																// 復号化開始
			decTH03.start();																// 復号化開始
			decTH01.join();																	// 終了待ち
			table	= Converter.bytes2string(JCipher.unblock(dec01.getBytes()));			// 復号化したバイト列を文字列に変換
			decTH02.join();																	// 終了待ち
			column	= Converter.bytes2string(JCipher.unblock(dec02.getBytes()));			// 復号化したバイト列を文字列に変換
			decTH03.join();																	// 終了待ち
			row		= Converter.bytes2string(JCipher.unblock(dec03.getBytes()));			// 復号化したバイト列を文字列に変換
		}catch (Exception e){
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
