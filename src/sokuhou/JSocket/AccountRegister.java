package sokuhou.JSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sokuhou.cipher.JCipher.cipher;
import sokuhou.cipher.JDecrypt;
import sokuhou.cipher.JEncrypt;

public class AccountRegister extends JSocket{
	private int nextConnect;
	private boolean check;

	public AccountRegister(){
		super();
		check = false;
	}

	public boolean check(){
		return check;
	}

	public void run(){
		try{
			// 値確認、問題あればエラーとして発生させる
			if(getUserName() == null) throw new Exception("ERROR: user_name value is null");
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");
			if(getBirthDay() == null) throw new Exception ("ERROR: birth_day value is null");

			// 初期化
			byte[] rData = null;// バイト列のデータ(主に受信)
			nextConnect = 0;// 接続順の番号
			createSocket();// ソケット生成
			dos = new DataOutputStream(getSocket().getOutputStream());// 送信用のストリーム生成
			dis = new DataInputStream(getSocket().getInputStream());// 受信用のストリーム生成

			// 公開鍵と秘密鍵を生成する
			JEncrypt enc = new JEncrypt();// 暗号化
			enc.generateRSA_KEY();// RSA用の公開鍵と秘密鍵を生成する
			JDecrypt dec = new JDecrypt(cipher.RSA, enc.getPrivateKey());// 復号化
			byte[] publicKEY = enc.publicKey2bytes(enc.getPublicKey()); // 公開鍵のバイト列

			// アカウントの登録を要求する
			createInfoBytes("0000", "" + nextConnect++, ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			createDataBytes("$REGISTER:USER;");// 文字列をバイト列に出力する
			buildBytes();// 送信用バイト列に構築する
			dos.write(getAllBytes());// 構築したバイト列を送信する

			// サーバが応じるかどうか確認する true = OK, false = NG
			if(!dis.readBoolean()) return;// falseの場合、終了

			// クライアント(自分)の公開鍵を送信
			createInfoBytes("0000", "" + nextConnect++, ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			buildBytes(publicKEY);// 公開鍵のバイト列を使って、送信用バイト列に構築する

			dos.write(getAllBytes());// 構築したバイト列を送信する

			rData = new byte[dis.read(bufferData)];// 受信したバイト列をbufferDataに保存し、受信したバイト列の配列数を使ってrDataに値なしのバイト列を作成する
			clearBytes(rData);// バイト列を初期化する
			for(int i = 0; i < rData.length; i++)rData[i] = bufferData[i];// 全ての情報とデータをコピーする
			clearBytes(bufferData);// バイト列を初期化する
			List<String> info = getInfo(rData);// 接続情報用の文字列のリストを作成し、接続情報のバイト列から各情報をリストに追加する
			// 不正な接続番号もしくは、接続順の番号が正しくない場合は、エラーとして発生させる
			if(!info.get(0).equals("0000") || !info.get(1).equals("" + nextConnect++)) throw new Exception("ERROR: conncetion_no or nextConnection value can't use it. need reconnect");
			// データ情報の最後の部分に終了コードが無い、もしくは違う値である場合は、エラーとして発生させる
			if((rData[rData.length-4] | rData[rData.length-2]) != 0x00) throw new Exception("ERROR: data bytes can't find end-code ");
			if((rData[rData.length-3] | rData[rData.length-1]) != 0xFF) throw new Exception("ERROR: data bytes can't find end-code ");

			// バイト列からデータを取る
			byte[] buffData = new byte[rData.length-8];// データ用バイト列を作成する
			for(int i = 0; i < buffData.length; i++) buffData[i] = rData[i + 4];// 接続情報と終了コードを含めずに、データだけコピーする

			// 復号化
			dec.setBytes(buffData);// データのバイト列を復号化に入力する
			dec.start();// 復号化開始

			createInfoBytes("0000", "" + nextConnect++, ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			createDataBytes("$BOOL:OK;");// 文字列をバイト列に出力する
			buildBytes();// 送信用バイト列に構築する
			dos.write(getAllBytes());// 構築したバイト列を送信する

			rData = new byte[dis.read(bufferData)];// 受信したバイト列をbufferDataに保存し、受信したバイト列の配列数を使ってrDataに値なしのバイト列を作成する
			clearBytes(rData);// バイト列を初期化する
			for(int i = 0; i < rData.length; i++)rData[i] = bufferData[i];// 全ての情報とデータをコピーする
			clearBytes(bufferData);// バイト列を初期化する
			info = getInfo(rData);// 接続情報の文字列のリストを作成し、接続情報のバイト列から各情報をリストに追加する
			// 不正な接続番号もしくは、接続順の番号が正しくない場合は、エラーとして発生させる
			if(!info.get(0).equals("0000") || !info.get(1).equals("" + nextConnect++)) throw new Exception("ERROR: conncetion_no or nextConnection value can't use it. need reconnect");
			// データ情報の最後の部分に終了コードが無い、もしくは違う値である場合は、エラーとして発生させる
			if((rData[rData.length-4] | rData[rData.length-2]) != 0x00) throw new Exception("ERROR: data bytes can't find end-code ");
			if((rData[rData.length-3] | rData[rData.length-1]) != 0xFF) throw new Exception("ERROR: data bytes can't find end-code ");

			// バイト列からデータを取る
			buffData = new byte[rData.length-8];// データ用バイト列を作成する
			for(int i = 0; i < buffData.length; i++) buffData[i] = rData[i + 4];// 情報と終了コードを含めずに、データだけコピーする

			dec.join();// 復号化処理終了待ち
			key = dec.bytes2secretKey(dec.getBytes());// 復号化したバイト列を秘密鍵に生成し、keyに保存する
			// 復号化
			dec.setBytes(buffData);// データのバイト列を復号化に入力する
			dec.start();// 復号化開始
			dec.join();// 復号化処理終了待ち

			// 接続番号と接続番号用の乱数
			String connectKEY = bytes2str(dec.getBytes());// 復号化したバイト列を文字列に変換し、connectKEYに保存する
			int cNO = Integer.parseInt(connectKEY.substring(0,4));// 最初の4桁を接続番号としてcNOに保存する
			int cKEY = Integer.parseInt(connectKEY.substring(5, 8));// 次の4桁を接続番号用の乱数としてcKEYに保存する

			// 暗号と復号の秘密鍵を設定する
			enc = new JEncrypt(cipher.AES, key);// 暗号化
			dec = new JDecrypt(cipher.AES, key);// 復号化

			// ここからアカウント登録処理をする
			createInfoBytes("" + nextConnect(cKEY, cNO), "" + nextConnect++, ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する

			// 確認
			Pattern pattern = Pattern.compile("[\\s\\¥p{Punct}]");// ユーザ名用のパターン
			Matcher matcher = pattern.matcher(getUserName());// パターンを使って、ユーザ名を確認する
			// ユーザ名に使用できない特殊文字もしくは空白がある場合は、エラーとして発生させる
			if(matcher.find()) throw new Exception("ERROR: user_name can't use symbol and space");

			// ユーザ名を文字列に入れる
			sData = "$USER_NAME:";
			sData += getUserName() + ";";

			// 確認
			pattern = Pattern.compile("[\\s\\p{Punct}&&[^@_!%]]");// パスワード用のパターン
			matcher = pattern.matcher(getPassword());// パターンを使って、パスワードを確認する
			// パスワードに使用できない特殊文字(@, _, !, %は除く)もしくは空白がある場合は、エラーとして発生させる
			if(matcher.find()) throw new Exception("ERROR: password can't use \"$&'()*+,./:;<=>?[\\]^`{|}~ and space");
			// 確認
			pattern = Pattern.compile("^[^ -~｡-ﾟ]+$");// パスワード用のパターン
			matcher = pattern.matcher(getPassword());// パターンを使って、パスワードを確認する
			// パスワードに使用できない全角文字がある場合は、エラーとして発生させる
			if(matcher.find()) throw new Exception("ERROR: password can't use double-byte character");

			// パスワードを文字列に入れる
			sData += "$PASSWORD:";
			sData += getPassword() + ";";
			setPassword(null);// パスワードをnull値で消す

			// 確認
			pattern = Pattern.compile(".+@.+\\..++?");// メールアドレス用のパターン
			matcher = pattern.matcher(getEmail());// パターンを使って、メールアドレスを確認する
			// もし、メールアドレスではない文字列の場合は、エラーとして発生させる
			if(!matcher.find()) throw new Exception("ERROR: can't use email, value: " + getEmail());

			// メールアドレスを文字列に入れる
			sData += "$EMAIL:";
			sData += getEmail() + ";";

			// 確認
			pattern = Pattern.compile("\\d\\{4}\\-\\d\\{2}\\-\\d\\{2}+?");// 誕生日用のパターン
			matcher = pattern.matcher(getBirthDay());// パターンを使って、誕生日を確認する
			// もし、誕生日ではない文字列の場合は、エラーとして発生させる
			if(!matcher.find()) throw new Exception("ERROR: birth_day need input like YYYY-MM-DD, value: " + getBirthDay());

			// 誕生日を文字列に入れる
			sData += "$BIRTH_DAY:";
			sData += getBirthDay() + ";";

			// アカウント情報を送信する
			createDataBytes(sData);// 文字列をバイト列に出力する
			sData = null;// 文字列をnull値で消す

			// 暗号化
			enc.setBytes(getDataBytes());// アカウント情報のバイト列を暗号化に入力する
			enc.start();// 暗号化開始
			enc.join();// 暗号化処理終了待ち

			buildBytes();// 送信用バイト列に構築する

			dos.write(getAllBytes());// 構築したバイト列を送信する

			check = dis.readBoolean();// boolean型の値を受信する true = 登録完了, false = サーバ側にトラブルで登録失敗

			// 接続を閉じる
			dis.close();// 受信用ストリームを閉じる
			dos.close();// 送信用ストリームを閉じる
			setSocket(null);// ソケットをnull値で消す
		} catch (Exception e){
		// エラーが起きた際の処理
			System.out.println(e);// エラー内容を出力する
			e.getStackTrace();// 原因を追跡する
			setSocket(null);// ソケットをnull値で消す
			try {
				dis.close();// 受信用ストリームを閉じる
				dos.close();// 送信用ストリームを閉じる
			} catch (Exception e1) {
				// 閉じる時にエラーが起きた際の処理
				System.out.println(e1);// エラー内容を出力する
				e1.getStackTrace();// 原因を追跡する
				dis = null;// 受信用ストリームをnull値で消す
				dos = null;// 送信用ストリームをnull値で消す
			}
		}
	}

}
