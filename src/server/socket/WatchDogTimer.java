package server.socket;

import java.net.Socket;

public class WatchDogTimer extends JSocket{
	private int interval;// 間隔
	private int afterTime;// 予測経過時間
	private int nowTime;// 現在の時間
	private boolean rtm;// 時間監視
	private boolean bool;// 安全停止

	// コンストラクタ
	public WatchDogTimer(){
		// 初期設定
		setSocket(null);// null値
		interval = 10000;// 10秒の間隔(ミリ秒単位)
		rtm = true;// 時間監視をtrueに設定
	}

	// コンストラクタ
	public WatchDogTimer(Socket socket){
		// 初期設定
		setSocket(socket);// ソケット 入力
		try {
			open();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			try {
				close();
			} catch (Exception e1) {
				System.out.println(e1);
				e1.printStackTrace();
				setDOS(null);// null値
				setDIS(null);// null値
			}
		}
		interval = 10000;// 10秒の間隔(ミリ秒単位)
		rtm = true;// 時間監視をtrueに設定
	}

	// コンストラクタ
	public WatchDogTimer(int interval){
		// 初期設定
		setSocket(null);// null値
		this.interval = interval;// 間隔設定
		rtm = true;// 時間監視をtrueに設定
	}

	// コンストラクタ
	public WatchDogTimer(Socket socket, int interval){
		// 初期設定
		setSocket(socket);// ソケット 入力
		try {
			open();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			try {
				close();
			} catch (Exception e1) {
				System.out.println(e1);
				e1.printStackTrace();
				setDOS(null);// null値
				setDIS(null);// null値
			}
		}
		this.interval = interval;// 間隔設定
		rtm = true;// 時間監視をtrueに設定
	}

	public void run(){
		rtm = true;// 時間監視をtrueに設定
		nowTime = afterTime = (int) System.currentTimeMillis();// 現在の時間(ミリ秒単位)取得
		afterTime += interval;// 間隔を足す

		try{
			do{
				if(!bool) return;// 安全停止
				if(nowTime >= afterTime){// 現在の時間が予定以上過ぎた場合
					afterTime = nowTime;// 現在の時間をコピー
					afterTime += interval;// 間隔を足す
					getDOS().writeBoolean(false);// クライアントにfalse送信
				}
				nowTime = (int) System.currentTimeMillis();// 現在の時間を取得
			}while(rtm);// whileループ後判定 時間監視有効の間
			getDOS().writeBoolean(true);// クライアントにtrue送信
		}catch (Exception e){
			// エラー表示
			System.out.println(e);// エラー内容の表示
			e.printStackTrace();// エラー原因追跡内容の表示
		}
	}

	// 間隔 出力
	public synchronized int getInterval(){
		return interval;
	}

	// 間隔 入力
	public synchronized void setInterval(int interval){
		this.interval = interval;
	}

	// ソケット 入力
	public synchronized void setSocket(Socket socket){
		super.setSocket(socket);
		try {
			open();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			try {
				close();
			} catch (Exception e1) {
				System.out.println(e1);
				e1.printStackTrace();
				setDOS(null);// null値
				setDIS(null);// null値
			}
		}
	}

	// 送信可能な状態をクライアントに知らせる
	public synchronized void success(){
		rtm = false;// 時間監視をfalseに設定
	}

	// 安全停止
	public synchronized void stopSafety(){
		bool = false;
	}

}
