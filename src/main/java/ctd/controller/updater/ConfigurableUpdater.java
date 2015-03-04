package ctd.controller.updater;

import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.controller.notifier.NotifierMessage;

public interface ConfigurableUpdater<T extends Configurable> {
	
	void create(T t) throws ControllerException;
	
	void update(T t) throws ControllerException;
	
	void setProperty(String id, String nm, Object v) throws ControllerException;
	
	void notifyMessage(NotifierMessage message) throws ControllerException;

}
