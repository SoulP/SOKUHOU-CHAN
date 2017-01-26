package server.socket;

import java.io.IOException;
import java.net.Socket;

import io.JSocket;

public class WatchDogTimer extends JSocket implements Runnable{
	private	int		interval;									// 間隔
	private	int		afterTime;									// 予測経過時間
	private	int		nowTime;									// 現在の時間
	private	boolean	rtm;										// 時間監視
	private	boolean	bool;										// 安全停止

	// コンストラクタ

	// コンストラクタ
	public WatchDogTimer(Socket socket) throws IOException{
		super(socket);
		interval	= 10000;									// 10秒の間隔(ミリ秒単位)
		rtm			= true;										// 時間監視をtrueに設定
	}

	// コンストラクタ
	public WatchDogTimer(Socket socket, int interval) throws IOException{
		// 初期設定
		super(socket);
		this.interval = interval;								// 間隔設定
		rtm = true;												// 時間監視をtrueに設定
	}

	public void run(){
		rtm = true;												// 時間監視をtrueに設定
		nowTime = afterTime = (int) System.currentTimeMillis();	// 現在の時間(ミリ秒単位)取得
		afterTime += interval;// 間隔を足す

		try{
			do{
				if(!bool) return;								// 安全停止
				if(nowTime >= afterTime){						// 現在の時間が予定以上過ぎた場合
					afterTime = nowTime;						// 現在の時間をコピー
					afterTime += interval;						// 間隔を足す
					send(false);								// false送信
				}
				nowTime = (int) System.currentTimeMillis();		// 現在の時間を取得
			}while(rtm);										// whileループ後判定 時間監視有効の間
			send(true);											// クライアントにtrue送信
		}catch (Exception e){
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
		}
	}

	// 間隔 出力
	public int getInterval(){
		return interval;
	}

	// 間隔 入力
	public void setInterval(int interval){
		this.interval = interval;
	}

	// 送信可能な状態をクライアントに知らせる
	public void success(){
		rtm = false;// 時間監視をfalseに設定
	}

	// 安全停止
	public void stopSafety(){
		bool = false;
	}

}
