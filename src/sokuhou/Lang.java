package sokuhou;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {
	// インスタンス変数
	volatile private ResourceBundle rb;
	private final String RESOURCE_PROPERTIES = "resource/language/lang";

	public Lang(){
		rb = ResourceBundle.getBundle(RESOURCE_PROPERTIES);
	}

	public Lang(Locale locale){
		rb = ResourceBundle.getBundle(RESOURCE_PROPERTIES, locale);
	}

	public void setResBundle(ResourceBundle rb){
		this.rb = rb;
	}

	public ResourceBundle getResBundle(){
		return rb;
	}

	public void changeLocale(Locale locale){
		rb = ResourceBundle.getBundle(RESOURCE_PROPERTIES, locale);
	}
}
