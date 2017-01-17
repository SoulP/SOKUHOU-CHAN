package sokuhou.window;

import java.util.ResourceBundle;

import javax.swing.JLogin;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.EventListenerList;

import sokuhou.Lang;
import sokuhou.MainSYS;

public class WinLogin extends JLogin {
	// インスタンス変数
	volatile ResourceBundle rb;
	EventListenerList evList;
	
	public JLogin[] wLogin;
	@RequestMapping(value = "/", method = RequestMethod.POST)
	    public String postLogin(@RequestParam("userid") String pUserId,
	                             @RequestParam("password") String password,
	                             Locale locale,
	                             Model model) {

	        try{

	            logger.info("Login VikingEgg(LoginPOST)  The client locale is {}.", locale);


	            String userId = pUserId;
	            String pass = password;

	            String retForm = "";

	            // 入力エラーメッセージクリア
	            model.addAttribute("errorMessage","");

	            retForm = "/";


	            loginService.GetUserInfo(userId, pass);


	            String ErrMsg = loginService.UserIDCheck();

	            if (ErrMsg == ""){

	                // メニュー
	                retForm = loginService.getMenu();

}
