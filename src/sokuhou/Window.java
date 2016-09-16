package sokuhou;

import javax.swing.JFrame;

// メインフレーム
public class Window extends JFrame {

	// コンストラクタ
	public Window(){
		setTitle("速報ちゃん");
		setSize(640, 360);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
