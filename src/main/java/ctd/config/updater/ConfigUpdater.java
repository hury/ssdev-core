package ctd.config.updater;

import ctd.config.Config;
import ctd.controller.exception.ControllerException;
import ctd.controller.updater.AbstractConfigurableUpdater;
import ctd.controller.watcher.WatcherTopics;

public class ConfigUpdater extends AbstractConfigurableUpdater<Config>{

	public ConfigUpdater() {
		super(WatcherTopics.CONF);
	}

	@Override
	public void create(Config t) throws ControllerException {
		
	}

	@Override
	protected void processUpdate(Config t) throws ControllerException {
		
	}

	@Override
	protected void processSetProperty(String id, String nm, Object v) throws ControllerException {
		
	}

}
