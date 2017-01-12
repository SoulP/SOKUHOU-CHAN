package sokuhou.io;

import java.util.Calendar;

public class JCalendar extends Thread{
	// インスタンス変数
	// 言語
	private int lang;
	private boolean bool;

	// 曜日
	final static String[][] strDay = {
			{ "月曜日", "Monday"    },
			{ "火曜日", "Tuesday"   },
			{ "水曜日", "Wednesday" },
			{ "木曜日", "Thursday"  },
			{ "金曜日", "Friday"    },
			{ "土曜日", "Saturday"  },
			{ "日曜日", "Sunday"    }
	};

	// n月
	final static String[][] strMonth = {
			{ "1月",  "JAN" },
			{ "2月",  "FEB" },
			{ "3月",  "MAR" },
			{ "4月",  "APR" },
			{ "5月",  "MAY" },
			{ "6月",  "JUN" },
			{ "7月",  "JUL" },
			{ "8月",  "AUG" },
			{ "9月",  "SEP" },
			{ "10月", "OCT" },
			{ "11月", "NOV" },
			{ "12月", "DEC" }

	};

	// 日本語対応
	final static String[] formatJP = {	"年", "日", "時" , "分" , "秒"};

	// 日付と曜日と時間
	private static int year, month, date, day, hour, minute, second;

	// 出力
	enum output{
		JAPANESE(0),
		ENGLISH(1);
		private final int id;
		// プライベートコンストラクタ
		output(int id){
			this.id = id;
		}
		// 表示
		void print(){
			if(id == 0){
				// 日本語フォーマット
				System.out.print(year + formatJP[0] + strMonth[month - 1][id] + date + formatJP[1] +
						" (" + strDay[day][id] + ") " + hour + formatJP[2] + minute + formatJP[3] +
						second + formatJP[4] + " ");
			}else{
				// 英語フォーマット
				System.out.print(strMonth[month - 1][id] + " " + date + " (" + strDay[day][id] + "), " + year +
						" " + hour + "h " + minute + "m " + second + "s ");
			}
		}
	}

	// コンストラクタ
	public JCalendar(){
		year = month = date = day = hour = minute = second = 0;// 0の値で初期化
		lang = 0;// 0の値で初期化、デフォルトを日本語にする
		bool = true;
	}

	// コンストラクタ
	public JCalendar(int lang){
		year = month = date = day = hour = minute = second = 0;// 0の値で初期化
		this.lang = lang;// 値をコピー
		bool = true;
	}

	// スレッド
	public void run(){
		bool = true;
		while(bool){
			try{
				// カレンダー
				Calendar calendar = Calendar.getInstance();// インスタンス取得
				year   = calendar.get(Calendar.YEAR);// 年の値取得
				month  = calendar.get(Calendar.MONTH) + 1;// 月の値取得
				date   = calendar.get(Calendar.DATE);// 日の値取得
				day    = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 曜日の値取得
				day    = (day == 0)? 7 : day - 1;// 曜日の値変更
				hour   = calendar.get(Calendar.HOUR_OF_DAY);// 時の値取得
				minute = calendar.get(Calendar.MINUTE);// 分の値取得
				second = calendar.get(Calendar.SECOND);// 秒の値取得
			}catch (Exception e){
				// エラー起きた場合
				year = month = date = day = hour = minute = second = 0;// 0の値で初期化
				bool = false;
				System.out.println(e);// エラー表示
				e.printStackTrace();// エラー原因追跡表示
			}
		}
	}

	// 言語 出力
	public synchronized int getLang(){
		return lang;
	}

	// 言語 入力
	public synchronized void setLang(int lang){
		this.lang = lang;
	}

	// 表示
	public synchronized void print(){
		if(lang == 0) output.JAPANESE.print();
		else output.ENGLISH.print();
	}

	// 表示
	public synchronized void print(int lang){
		if(lang == 0) output.JAPANESE.print();
		else output.ENGLISH.print();
	}

	// 年 出力
	public synchronized int getYEAR(){
		return year;
	}

	// 月 出力
	public synchronized int getMONTH(){
		return month;
	}

	// 日 出力
	public synchronized int getDATE(){
		return date;
	}

	// 曜日 出力
	public synchronized int getDAY(){
		return day;
	}

	// 時 出力
	public synchronized int getHOUR(){
		return hour;
	}

	// 分 出力
	public synchronized int getMINUTE(){
		return minute;
	}

	// 秒 出力
	public synchronized int getSECOND(){
		return second;
	}

	// 安全停止
	public synchronized void stopSafety(){
		bool = false;
	}

}
