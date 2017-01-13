package sokuhou.window;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WinPanel extends JPanel{
	public WinPanel(){
		setName("WinPanel");
		setBounds(0, 0, 640, 360);
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);

		JLabel label = new JLabel("ラベル");
		label.setBounds(100, 100, 40, 20);
		label.setVisible(true);
		JTextField text = new JTextField();
		text.setBounds(150, 100, 200, 20);
		text.setVisible(true);

		add(label);
		add(text);

		setVisible(false);
	}
}
