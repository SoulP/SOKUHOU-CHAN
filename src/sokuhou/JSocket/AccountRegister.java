package sokuhou.JSocket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sokuhou.cipher.JCipher.cipher;
import sokuhou.cipher.JDecrypt;
import sokuhou.cipher.JEncrypt;

public class AccountRegister extends JSocket{
	private boolean check;// 登録処理確認

	// コンストラクタ
	public AccountRegister(){
		super();
		check = false;
	}

	// 登録処理確認 true = 登録完了, false = 登録失敗
	public boolean check(){
		return check;
	}

	// 登録処理
	public void run(){
		try{
// 01. CLが接続の準備をする(初期化など)
			// 値がない場合は、エラーとして発生させる
			if(getUserName() == null) throw new Exception("ERROR: user_name value is null");
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");
			if(getBirthDay() == null) throw new Exception ("ERROR: birth_day value is null");

			// 初期化
			rData = null;// バイト列のデータ(主に受信)

			// 接続を開く
			open();

// 02. CLが公開鍵と秘密鍵を生成する
			// 公開鍵と秘密鍵を生成する
			JEncrypt enc = new JEncrypt();// 暗号化
			enc.generateRSA_KEY();// RSA用の公開鍵と秘密鍵を生成する
			JDecrypt dec = new JDecrypt(cipher.RSA, enc.getPrivateKey());// 復号化
			byte[] publicKEY = enc.publicKey2bytes(enc.getPublicKey()); // 公開鍵のバイト列

// 03. CLがSVに接続を要求する(接続情報の接続番号は0000)
			// アカウントの登録を要求する
			createInfoBytes("0000", ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			setDataBytes(str2bytes("$REGISTER:USER;"));// 文字列をバイト列に出力する
			buildBytes();// 送信用バイト列に構築する
			send(getAllBytes());// 構築したバイト列を送信する

// 05. CLが応じられた結果を確認する
// 06. TRUEの場合は、CLの公開鍵をSVに送る(接続情報の接続番号は0000)
// -06. FALSEの場合は、CLは閉じる
			// サーバが応じるかどうか確認する true = OK, false = NG
			if(!recvBoolean()){// falseの場合、終了
				if(!getSocket().isClosed()) close();// 接続が閉じられていない場合は、閉じる
				return;
			}

			// クライアント(自分)の公開鍵を送信
			createInfoBytes("0000", ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する
			buildBytes(publicKEY);// 公開鍵のバイト列を使って、送信用バイト列に構築する

			// 送信
			send(getAllBytes());// 構築したバイト列を送信する

// 10. CLがSVから共通鍵を受け取り、CLの秘密鍵で復号化する
			// 受信
			byte[] buff = recv("0000");

			// 復号化
			dec.setBytes(buff);// データのバイト列を復号化に入力する
			dec.start();// 復号化開始

// 11. CLがSVにTRUEで応じる
			// 送信
			sendBoolean(true);

// 15. CLがSVから暗号化されたデータ情報を受け取り、CLの秘密鍵で復号化する
			// 受信
			buff = recv("0000");

			// 復号化
			dec.join();// 復号化処理終了待ち
			key = dec.bytes2secretKey(dec.getBytes());// 復号化したバイト列を秘密鍵に生成し、keyに保存する
			dec.setBytes(buff);// データのバイト列を復号化に入力する
			dec.start();// 復号化開始
			dec.join();// 復号化処理終了待ち

// 16. CLが復号化されたデータ情報を接続番号と接続番号用の鍵としてint型に変換する
			// 接続番号と接続番号用の乱数
			String connectKEY = bytes2str(dec.getBytes());// 復号化したバイト列を文字列に変換し、connectKEYに保存する
			setNextConnection(connectKEY);// 次の接続番号の設定

			// 暗号と復号の秘密鍵を設定する
			enc = new JEncrypt(cipher.AES, key);// 暗号化
			dec = new JDecrypt(cipher.AES, key);// 復号化

// 17. CLが接続番号と接続番号用の鍵を使って、次の接続番号を生成する
// 18. CLがユーザが入力した情報をデータ情報として作成し、共通鍵で暗号化する
			// ここからアカウント登録処理をする
			createInfoBytes("" + nextConnect(), ctrl.WRITE, type.USER);// 接続情報をバイト列に出力する

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
			setDataBytes(str2bytes(sData));// 文字列をバイト列に出力する
			sData = null;// 文字列をnull値で消す

			// 暗号化
			enc.setBytes(getDataBytes());// アカウント情報のバイト列を暗号化に入力する
			enc.start();// 暗号化開始
			enc.join();// 暗号化処理終了待ち

			buildBytes();// 送信用バイト列に構築する

// 19. CLがSVにユーザ情報を送る(接続情報の接続番号は、16.の次の接続番号)
			// 送信
			send(getAllBytes());// 構築したバイト列を送信する

// FIN. 結果待ち
			// 受信
			check = recvBoolean();// boolean型の値を受信する true = 登録完了, false = 登録失敗

			// 接続を閉じる
			close();

			// 値を消す
			setPassword(null);// パスワードをnull値で消す
			enc = null; // 暗号化をnull値で消す
			dec = null; // 復号化をnull値で消す
			pattern = null; // パターンをnull値で消す
			matcher = null; // 確認処理をnull値で消す

		} catch (Exception e){
		// エラーが起きた際の処理
			System.out.println(e);// エラー内容を出力する
			e.printStackTrace();;// 原因の追跡を表示
			try {
				// 接続を閉じる
				close();
			} catch (Exception e1) {
				// 閉じる時にエラーが起きた際の処理
				System.out.println(e1);// エラー内容表示
				e1.printStackTrace();// 原因を追跡する
				setSocket(null);// ソケットをnull値で消す
				setDIS(null);// 受信用ストリームをnull値で消す
				setDOS(null);// 送信用ストリームをnull値で消す
			}
		}
	}

}
