package ctd.controller.updater;

import java.util.List;

import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;

public interface ConfigurableItemUpdater<T extends Configurable,I> extends ConfigurableUpdater<T> {
	
	void createItem(String id, I item) throws ControllerException;
	
	void updateItem(String id, I item) throws ControllerException;
	
	void removeItems(String id, List<Object> keys) throws ControllerException;
}
