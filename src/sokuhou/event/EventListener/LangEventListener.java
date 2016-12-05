package sokuhou.event.EventListener;

import java.util.EventListener;

import sokuhou.event.LangEvent;

public interface LangEventListener extends EventListener{
	public void updateLang(LangEvent evt);
}
