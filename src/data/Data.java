package data;

import java.io.Serializable;
import java.security.Key;

import cipher.JCipher;
import cipher.JEncrypt;
import io.JPack;

public class Data implements JPack, Serializable{
	// 一時的な変数
	public	transient	volatile	String	table;		// 表
	public	transient	volatile	String	column;		// 列
	public	transient	volatile	String	row;		// 行

	// 送受信
	private				volatile	byte[]	bTABLE;		// 表
	private				volatile	byte[]	bCOLUMN;	// 列
	private				volatile	byte[]	bROW;		// 行
	private							byte[]	tableIV;	// IV
	private							byte[]	columnIV;	// IV
	private							byte[]	rowIV;		// IV

	// 書庫作成
	@Override
	public void pack(Key key) {
		try{
			// 例外処理

			// 暗号化 + 書庫作成
			JEncrypt	enc01	= new JEncrypt(JCipher.cipher.AES, key);
			JEncrypt	enc02	= new JEncrypt(JCipher.cipher.AES, key);
			JEncrypt	enc03	= new JEncrypt(JCipher.cipher.AES, key);
			Thread		encTH01	= new Thread(enc01);
			Thread		encTH02	= new Thread(enc02);
			Thread		encTH03	= new Thread(enc03);
						bTABLE	= JCipher.block(Converter.string2bytes(table), size);
						bCOLUMN	= JCipher.block(Converter.string2bytes(column), size);
						bROW	= JCipher.block(Converter.string2bytes(row), size);

			enc01.setBytes(bTABLE);
			encTH01.start();
			enc02.setBytes(bCOLUMN);
			encTH02.start();
			enc03.setBytes(bROW);
			encTH03.start();
			encTH01.join();
			bTABLE		= enc01.getBytes();
			tableIV		= enc01.getIV();
			encTH02.join();
			bCOLUMN		= enc02.getBytes();
			columnIV	= enc02.getIV();
			encTH03.join();
			bROW		= enc03.getBytes();
			rowIV		= enc03.getIV();

		}catch (Exception e){
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
		}

	}


	// 書庫解凍
	@Override
	public void unpack(Key key) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
