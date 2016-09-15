package sokuhou;

import javax.swing.JFrame;

public class Window extends JFrame {
	public Window(){
		setTitle("速報ちゃん");
		setSize(640, 360);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
