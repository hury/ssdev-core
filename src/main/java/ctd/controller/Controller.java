package ctd.controller;

import java.util.List;
import ctd.controller.exception.ControllerException;
import ctd.controller.updater.ConfigurableUpdater;

public interface Controller<T extends Configurable> {
	public T get(String id) throws ControllerException;
	public void add(T t);
	public void reload(String id);
	public boolean isLoaded(String id);
	public void reloadAll();
	public void setLoader(ConfigurableLoader<T> loader);
	public ConfigurableLoader<T> getLoader();
	
	public void setUpdater(ConfigurableUpdater<T> updater);
	public ConfigurableUpdater<T> getUpdater();
	public List<T> getCachedList();
}
