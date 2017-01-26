package io;

import java.util.Calendar;

public final class JCalendar{
	// インスタンス変数
	// 言語
	private static int lang = 0;

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
	private static int year, month, date, day, hour, minute, second = -1;

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
			getTime();
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

	// 日時取得
	public static void getTime(){
		try{
			// カレンダー
			Calendar calendar	= Calendar.getInstance();					// インスタンス取得
			year				= calendar.get(Calendar.YEAR);				// 年の値取得
			month				= calendar.get(Calendar.MONTH) + 1;			// 月の値取得
			date				= calendar.get(Calendar.DATE);				// 日の値取得
			day					= calendar.get(Calendar.DAY_OF_WEEK) - 1;	// 曜日の値取得
			day					= (day == 0)? 7 : day - 1;					// 曜日の値変更
			hour				= calendar.get(Calendar.HOUR_OF_DAY);		// 時の値取得
			minute				= calendar.get(Calendar.MINUTE);			// 分の値取得
			second				= calendar.get(Calendar.SECOND);			// 秒の値取得
		}catch (Exception e){
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
			year = month = date = day = hour = minute = second = -1;		// -1の値で初期化
		}
	}

	// 言語 出力
	public static int getLang(){
		return lang;
	}

	// 言語 入力
	public static void setLang(int lang){
		JCalendar.lang = lang;
	}

	// 表示
	public static void print(){
		if(lang == 0) output.JAPANESE.print();
		else output.ENGLISH.print();
	}

	// 表示
	public static void print(int lang){
		if(lang == 0) output.JAPANESE.print();
		else output.ENGLISH.print();
	}

	// 年 出力
	public static int getYEAR(){
		return year;
	}

	// 月 出力
	public static int getMONTH(){
		return month;
	}

	// 日 出力
	public static int getDATE(){
		return date;
	}

	// 曜日 出力
	public static int getDAY(){
		return day;
	}

	// 時 出力
	public static int getHOUR(){
		return hour;
	}

	// 分 出力
	public static int getMINUTE(){
		return minute;
	}

	// 秒 出力
	public static int getSECOND(){
		return second;
	}
}
