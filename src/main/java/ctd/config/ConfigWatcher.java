package ctd.config;

import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;

public class ConfigWatcher extends ConfigurableWatcher {

	public ConfigWatcher() {
		super(WatcherTopics.CONF);
	}


}
