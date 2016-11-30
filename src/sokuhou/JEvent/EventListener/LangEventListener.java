package sokuhou.JEvent.EventListener;

import java.util.EventListener;

import sokuhou.JEvent.LangEvent;

public interface LangEventListener extends EventListener{
	public void updateLang(LangEvent evt);
}
